package doggytalents.common.entity;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.base.Function;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import doggytalents.DoggyAccessories;
import doggytalents.DoggyBlocks;
import doggytalents.DoggyEntityTypes;
import doggytalents.DoggyItems;
import doggytalents.DoggySerializers;
import doggytalents.DoggyTags;
import doggytalents.DoggyTalents2;
import doggytalents.api.enu.WetSource;
import doggytalents.api.feature.DataKey;
import doggytalents.api.feature.DogLevel;
import doggytalents.api.feature.DogLevel.Type;
import doggytalents.api.feature.EnumGender;
import doggytalents.api.feature.EnumMode;
import doggytalents.api.feature.FoodHandler;
import doggytalents.api.inferface.AbstractDogEntity;
import doggytalents.api.inferface.IDogAlteration;
import doggytalents.api.inferface.IDogFoodHandler;
import doggytalents.api.inferface.IDogItem;
import doggytalents.api.inferface.IThrowableItem;
import doggytalents.api.registry.Accessory;
import doggytalents.api.registry.AccessoryInstance;
import doggytalents.api.registry.AccessoryType;
import doggytalents.api.registry.Talent;
import doggytalents.client.screen.DogInfoScreen;
import doggytalents.common.config.ConfigValues;
import doggytalents.common.entity.ai.BerserkerModeGoal;
import doggytalents.common.entity.ai.DogBegGoal;
import doggytalents.common.entity.ai.DogFollowOwnerGoal;
import doggytalents.common.entity.ai.DogWanderGoal;
import doggytalents.common.entity.ai.FetchGoal;
import doggytalents.common.entity.ai.FindWaterGoal;
import doggytalents.common.entity.ai.OwnerHurtByTargetGoal;
import doggytalents.common.entity.ai.OwnerHurtTargetGoal;
import doggytalents.common.entity.serializers.DimensionDependantArg;
import doggytalents.common.entity.stats.StatsTracker;
import doggytalents.common.storage.DogLocationStorage;
import doggytalents.common.storage.DogRespawnStorage;
import doggytalents.common.util.BackwardsComp;
import doggytalents.common.util.Cache;
import doggytalents.common.util.NBTUtil;
import doggytalents.common.util.WorldUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.ai.goal.BreedGoal;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.SitGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.monster.AbstractSkeletonEntity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.GhastEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.IDyeableArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.datasync.EntityDataManager.DataEntry;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.RegistryObject;

public class DogEntity extends AbstractDogEntity {

    // Use Cache.make to ensure static fields are not initialised too early (before Serializers have been registered)
    private static final Cache<DataParameter<List<AccessoryInstance>>> ACCESSORIES =  Cache.make(() -> (DataParameter<List<AccessoryInstance>>) EntityDataManager.createKey(DogEntity.class, DoggySerializers.ACCESSORY_SERIALIZER.get().getSerializer()));
    private static final Cache<DataParameter<Map<Talent, Integer>>> TALENTS =  Cache.make(() -> (DataParameter<Map<Talent, Integer>>) EntityDataManager.createKey(DogEntity.class, DoggySerializers.TALENT_LEVEL_SERIALIZER.get().getSerializer()));
    private static final DataParameter<Optional<ITextComponent>> LAST_KNOWN_NAME = EntityDataManager.createKey(DogEntity.class, DataSerializers.OPTIONAL_TEXT_COMPONENT);
    private static final DataParameter<Byte> DOG_FLAGS = EntityDataManager.createKey(DogEntity.class, DataSerializers.BYTE);
    private static final Cache<DataParameter<EnumGender>> GENDER = Cache.make(() -> (DataParameter<EnumGender>) EntityDataManager.createKey(DogEntity.class,  DoggySerializers.GENDER_SERIALIZER.get().getSerializer()));
    private static final Cache<DataParameter<EnumMode>> MODE = Cache.make(() -> (DataParameter<EnumMode>) EntityDataManager.createKey(DogEntity.class, DoggySerializers.MODE_SERIALIZER.get().getSerializer()));
    private static final DataParameter<Float> HUNGER_INT = EntityDataManager.createKey(DogEntity.class, DataSerializers.FLOAT);
    private static final DataParameter<String> CUSTOM_SKIN = EntityDataManager.createKey(DogEntity.class, DataSerializers.STRING);
    private static final Cache<DataParameter<DogLevel>> DOG_LEVEL = Cache.make(() -> (DataParameter<DogLevel>) EntityDataManager.createKey(DogEntity.class, DoggySerializers.DOG_LEVEL_SERIALIZER.get().getSerializer()));
    private static final DataParameter<Byte> SIZE = EntityDataManager.createKey(DogEntity.class, DataSerializers.BYTE);
    private static final DataParameter<ItemStack> BONE_VARIANT = EntityDataManager.createKey(DogEntity.class, DataSerializers.ITEMSTACK);
    private static final Cache<DataParameter<DimensionDependantArg<Optional<BlockPos>>>> DOG_BED_LOCATION = Cache.make(() -> (DataParameter<DimensionDependantArg<Optional<BlockPos>>>) EntityDataManager.createKey(DogEntity.class, DoggySerializers.BED_LOC_SERIALIZER.get().getSerializer()));
    private static final Cache<DataParameter<DimensionDependantArg<Optional<BlockPos>>>> DOG_BOWL_LOCATION = Cache.make(() -> (DataParameter<DimensionDependantArg<Optional<BlockPos>>>) EntityDataManager.createKey(DogEntity.class, DoggySerializers.BED_LOC_SERIALIZER.get().getSerializer()));


    // Cached values
    private final Cache<Integer> spendablePoints = Cache.make(this::getSpendablePointsInternal);
    private final List<IDogAlteration> alterations = Lists.newArrayList();

    public final StatsTracker statsTracker = new StatsTracker();
    public final Map<Integer, Object> objects = Maps.newHashMap();

    private int hungerTick;
    private int prevHungerTick;
    private int healingTick;
    private int prevHealingTick;

    private float headRotationCourse;
    private float headRotationCourseOld;
    private WetSource wetSource;
    private boolean isShaking;
    private float timeWolfIsShaking;
    private float prevTimeWolfIsShaking;

    protected boolean dogJumping;
    protected float jumpPower;

    public DogEntity(EntityType<? extends DogEntity> type, World worldIn) {
        super(type, worldIn);
        this.setTamed(false);
        this.setGender(EnumGender.random(this.getRNG()));
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(ACCESSORIES.get(), Lists.newArrayList());
        this.dataManager.register(TALENTS.get(), Maps.newHashMap());
        this.dataManager.register(LAST_KNOWN_NAME, Optional.empty());
        this.dataManager.register(DOG_FLAGS, (byte) 0);
        this.dataManager.register(GENDER.get(), EnumGender.UNISEX);
        this.dataManager.register(MODE.get(), EnumMode.DOCILE);
        this.dataManager.register(HUNGER_INT, 60F);
        this.dataManager.register(CUSTOM_SKIN, "");
        this.dataManager.register(DOG_LEVEL.get(), new DogLevel(0, 0));
        this.dataManager.register(SIZE, (byte) 3);
        this.dataManager.register(BONE_VARIANT, ItemStack.EMPTY);
        this.dataManager.register(DOG_BED_LOCATION.get(), new DimensionDependantArg<>(() -> DataSerializers.OPTIONAL_BLOCK_POS));
        this.dataManager.register(DOG_BOWL_LOCATION.get(), new DimensionDependantArg<>(() -> DataSerializers.OPTIONAL_BLOCK_POS));
    }

    @Override
    protected void registerGoals() {
        this.sitGoal = new SitGoal(this);
        this.goalSelector.addGoal(1, new SwimGoal(this));
        this.goalSelector.addGoal(1, new FindWaterGoal(this));
        //this.goalSelector.addGoal(1, new PatrolAreaGoal(this));
        this.goalSelector.addGoal(2, this.sitGoal);
        //this.goalSelector.addGoal(3, new WolfEntity.AvoidEntityGoal(this, LlamaEntity.class, 24.0F, 1.5D, 1.5D));
        this.goalSelector.addGoal(4, new LeapAtTargetGoal(this, 0.4F));
        this.goalSelector.addGoal(5, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(5, new DogWanderGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new FetchGoal(this, 1.0D, 32.0F));
        this.goalSelector.addGoal(6, new DogFollowOwnerGoal(this, 1.0D, 10.0F, 2.0F));
        this.goalSelector.addGoal(7, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(8, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(9, new DogBegGoal(this, 8.0F));
        this.goalSelector.addGoal(10, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(10, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(3, (new HurtByTargetGoal(this)).setCallsForHelp());
        //this.targetSelector.addGoal(4, new NonTamedTargetGoal<>(this, AnimalEntity.class, false, TARGET_ENTITIES));
        //this.targetSelector.addGoal(4, new NonTamedTargetGoal<>(this, TurtleEntity.class, false, TurtleEntity.TARGET_DRY_BABY));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, AbstractSkeletonEntity.class, false));
        this.targetSelector.addGoal(6, new BerserkerModeGoal<>(this, MonsterEntity.class, false));
    }

    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3F);
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(this.isTamed() ? 20.0D : 8.0D);

        this.getAttributes().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(2.0D);
        this.getAttributes().registerAttribute(JUMP_STRENGTH).setBaseValue(0.42F);
        this.getAttributes().registerAttribute(CRIT_CHANCE).setBaseValue(0.42F);
        this.getAttributes().registerAttribute(CRIT_BONUS).setBaseValue(1F);
    }

    @Override
    public void playStepSound(BlockPos pos, BlockState blockIn) {
        this.playSound(SoundEvents.ENTITY_WOLF_STEP, 0.15F, 1.0F);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        if (this.rand.nextInt(3) == 0) {
            return this.isTamed() && this.getHealth() < 10.0F ? SoundEvents.ENTITY_WOLF_WHINE : SoundEvents.ENTITY_WOLF_PANT;
        } else {
            return SoundEvents.ENTITY_WOLF_AMBIENT;
        }
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.ENTITY_WOLF_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_WOLF_DEATH;
    }

    @Override
    public float getSoundVolume() {
        return 0.4F;
    }

    public boolean isDogWet() {
        return this.wetSource != null;
    }

    @OnlyIn(Dist.CLIENT)
    public float getShadingWhileWet(float partialTicks) {
        return 0.75F + MathHelper.lerp(partialTicks, this.prevTimeWolfIsShaking, this.timeWolfIsShaking) / 2.0F * 0.25F;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public float getShakeAngle(float partialTicks, float p_70923_2_) {
        float f = (MathHelper.lerp(partialTicks, this.prevTimeWolfIsShaking, this.timeWolfIsShaking) + p_70923_2_) / 1.8F;
        if (f < 0.0F) {
            f = 0.0F;
        } else if (f > 1.0F) {
            f = 1.0F;
        }

        return MathHelper.sin(f * (float)Math.PI) * MathHelper.sin(f * (float)Math.PI * 11.0F) * 0.15F * (float)Math.PI;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public float getInterestedAngle(float partialTicks) {
        return MathHelper.lerp(partialTicks, this.headRotationCourseOld, this.headRotationCourse) * 0.15F * (float)Math.PI;
    }

    @Override
    public void handleStatusUpdate(byte id) {
        if (id == doggytalents.common.lib.Constants.EntityState.WOLF_STOP_SHAKING) {
           this.isShaking = true;
           this.timeWolfIsShaking = 0.0F;
           this.prevTimeWolfIsShaking = 0.0F;
        } else {
            super.handleStatusUpdate(id);
        }
    }

    public float getTailRotation() {
        return this.isTamed() ? (0.55F - (this.getMaxHealth() - this.getHealth()) * 0.02F) * (float)Math.PI : ((float)Math.PI / 5F);
    }

    @Override
    public float getWagAngle(float limbSwing, float limbSwingAmount, float partialTickTime) {
        return MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
    }

    @Override
    protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) {
        return sizeIn.height * 0.8F;
    }

    @Override
    public int getVerticalFaceSpeed() {
        return this.isSitting() ? 20 : super.getVerticalFaceSpeed();
    }

    @Override
    public double getYOffset() {
        return this.getRidingEntity() instanceof PlayerEntity ? 0.5D : 0.0D;
    }

    @Override
    public void tick() {
        super.tick();

        if (this.isAlive()) {
            this.headRotationCourseOld = this.headRotationCourse;
            if (this.isBegging()) {
                this.headRotationCourse += (1.0F - this.headRotationCourse) * 0.4F;
            } else {
                this.headRotationCourse += (0.0F - this.headRotationCourse) * 0.4F;
            }

            boolean inWater = this.isInWater() ;
            boolean inRain = this.isInRain();
            boolean inBubbleColumn = this.isInBubbleColumn();

            if (inWater || inRain || inBubbleColumn) {
                this.isShaking = false;
                this.timeWolfIsShaking = 0.0F;
                this.prevTimeWolfIsShaking = 0.0F;

                if (this.wetSource == null) {
                    this.wetSource = WetSource.of(inWater, inBubbleColumn, inRain);
                }
            } else if ((this.wetSource != null || this.isShaking) && this.isShaking) {
                if (this.timeWolfIsShaking == 0.0F) {
                    this.playSound(SoundEvents.ENTITY_WOLF_SHAKE, this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
                }

                this.prevTimeWolfIsShaking = this.timeWolfIsShaking;
                this.timeWolfIsShaking += 0.05F;
                if (this.prevTimeWolfIsShaking >= 2.0F) {

                    //TODO check if only called server side
                    if (this.wetSource != null) {
                        for (IDogAlteration alter : this.alterations) {
                            alter.onShakingDry(this, this.wetSource);
                        }
                    }

                    this.wetSource = null;
                    this.isShaking = false;
                    this.prevTimeWolfIsShaking = 0.0F;
                    this.timeWolfIsShaking = 0.0F;
                }

                if (this.timeWolfIsShaking > 0.4F) {
                    float f = (float)this.getPosY();
                    int i = (int)(MathHelper.sin((this.timeWolfIsShaking - 0.4F) * (float)Math.PI) * 7.0F);
                    Vec3d vec3d = this.getMotion();

                    for(int j = 0; j < i; ++j) {
                        float f1 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.getWidth() * 0.5F;
                        float f2 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.getWidth() * 0.5F;
                        this.world.addParticle(ParticleTypes.SPLASH, this.getPosX() + f1, f + 0.8F, this.getPosZ() + f2, vec3d.x, vec3d.y, vec3d.z);
                    }
                }
            }

            // On server side
            if (!this.world.isRemote) {

                // Every 2 seconds
                if(this.ticksExisted % 40 == 0) {
                    DogLocationStorage.get(this.world).getOrCreateData(this).update(this);

                    if(this.getOwner() != null) {
                        this.setOwnersName(this.getOwner().getName());
                    }
                }
            }
        }

        this.alterations.forEach((alter) -> alter.tick(this));
    }

    @Override
    public void livingTick() {
        super.livingTick();
        if (!this.world.isRemote && this.wetSource != null && !this.isShaking && !this.hasPath() && this.onGround) {
            this.isShaking = true;
            this.timeWolfIsShaking = 0.0F;
            this.prevTimeWolfIsShaking = 0.0F;
            this.world.setEntityState(this, doggytalents.common.lib.Constants.EntityState.WOLF_STOP_SHAKING);
        }

        if (!this.world.isRemote) {
            if(!ConfigValues.DISABLE_HUNGER) {
                this.prevHungerTick = this.hungerTick;

                if (!this.isBeingRidden() && !this.isSitting()) {
                    this.hungerTick += 1;
                }

                for (IDogAlteration alter : this.alterations) {
                    ActionResult<Integer> result = alter.hungerTick(this, this.hungerTick - this.prevHungerTick);

                    if (result.getType().isSuccess()) {
                        this.hungerTick = result.getResult() + this.prevHungerTick;
                    }
                }

                if (this.hungerTick > 400) {
                    this.setDogHunger(this.getDogHunger() - 1);
                    this.hungerTick -= 400;
                }
            }

            this.prevHealingTick = this.healingTick;
            this.healingTick += 8;

            if (this.isSitting()) {
                this.healingTick += 4;
            }

            for (IDogAlteration alter : this.alterations) {
                ActionResult<Integer> result = alter.healingTick(this, this.healingTick - this.prevHealingTick);

                if (result.getType().isSuccess()) {
                    this.healingTick = result.getResult() + this.prevHealingTick;
                }
            }

            if (this.healingTick >= 6000) {
                if (this.getHealth() < this.getMaxHealth()) {
                    DoggyTalents2.LOGGER.debug("Heal:");
                    this.heal(1);
                }

                this.healingTick = 0;
            }
        }

        this.alterations.forEach((alter) -> alter.livingTick(this));
    }

    private static final Item[] HELMETS = new Item[] {Items.IRON_HELMET, Items.DIAMOND_HELMET, Items.GOLDEN_HELMET, Items.LEATHER_HELMET, Items.CHAINMAIL_HELMET, Items.TURTLE_HELMET};
    private static final List<RegistryObject<? extends Accessory>> HELMET_ACCESSORIES = Lists.newArrayList(DoggyAccessories.IRON_HELMET, DoggyAccessories.DIAMOND_HELMET, DoggyAccessories.GOLDEN_HELMET, DoggyAccessories.LEATHER_HELMET, DoggyAccessories.CHAINMAIL_HELMET, DoggyAccessories.TURTLE_HELMET);

    @Override
    public boolean processInteract(PlayerEntity player, Hand hand) {

        ItemStack stack = player.getHeldItem(hand);

        if (this.isTamed()) {
            if(stack.getItem() == Items.STICK && this.canInteract(player)) {

                if(this.world.isRemote) {
                    DogInfoScreen.open(this);
                }

                return true;
            }

            if (!stack.isEmpty()) {
                for (int i = 0; i < HELMETS.length; i++) {
                    if (stack.getItem().equals(HELMETS[i])) {
                        AccessoryInstance inst;
                        if (HELMETS[i] == Items.LEATHER_HELMET) {
                            inst = DoggyAccessories.LEATHER_HELMET.get().create(((IDyeableArmorItem) Items.LEATHER_HELMET).getColor(stack));
                        } else {
                            inst = HELMET_ACCESSORIES.get(i).get().getDefault();
                        }
                        if (this.addAccessory(inst)) {
                            this.consumeItemFromStack(player, stack);
                            return true;
                        }
                    }
                }
            }
        } else { // Not tamed
            if(stack.getItem() == Items.BONE || stack.getItem() == DoggyItems.TRAINING_TREAT.get()) {

                if(!this.world.isRemote) {
                    this.consumeItemFromStack(player, stack);

                    if(stack.getItem() == DoggyItems.TRAINING_TREAT.get() || this.rand.nextInt(3) == 0) {
                        this.setTamedBy(player);
                        this.navigator.clearPath();
                        this.setAttackTarget((LivingEntity) null);
                        this.sitGoal.setSitting(true);
                        this.setHealth(20.0F);
                        this.world.setEntityState(this, doggytalents.common.lib.Constants.EntityState.WOLF_HEARTS);
                    } else {
                        this.world.setEntityState(this, doggytalents.common.lib.Constants.EntityState.WOLF_SMOKE);
                    }
                }

                return true;
            }
        }

        Optional<IDogFoodHandler> foodHandler = FoodHandler.getMatch(this, stack, player);

        if (foodHandler.isPresent()) {
            return foodHandler.get().consume(this, stack, player);
        }

        if (stack.getItem() instanceof IDogItem) {
            IDogItem item = (IDogItem) stack.getItem();
            ActionResultType result = item.processInteract(this, this.world, player, hand);
            if (result.isSuccessOrConsume()) {
                return true;
            } else if (result == ActionResultType.FAIL) {
                return false;
            }
        }

        for (IDogAlteration alter : this.alterations) {
            ActionResultType result = alter.processInteract(this, this.world, player, hand);
            if (result.isSuccessOrConsume()) {
                return true;
            } else if (result == ActionResultType.FAIL) {
                return false;
            }
        }

        boolean flag = super.processInteract(player, hand);
        if (!flag || this.isChild()) {
            if (!this.world.isRemote) {
                this.sitGoal.setSitting(!this.isSitting());

            }
            return true;
        }

        return flag;
    }

    @Override
    public boolean canBeRiddenInWater(Entity rider) {
        for (IDogAlteration alter : this.alterations) {
            ActionResultType result = alter.canBeRiddenInWater(this, rider);

            if (result.isSuccess()) {
                return true;
            } else if (result == ActionResultType.FAIL) {
                return false;
            }
        }

        return super.canBeRiddenInWater(rider);
    }

    @Override
    public boolean canTrample(BlockState state, BlockPos pos, float fallDistance) {
        for (IDogAlteration alter : this.alterations) {
            ActionResultType result = alter.canTrample(this, state, pos, fallDistance);

            if (result.isSuccess()) {
                return true;
            } else if (result == ActionResultType.FAIL) {
                return false;
            }
        }

        return super.canTrample(state, pos, fallDistance);
    }

    @Override
    public boolean onLivingFall(float distance, float damageMultiplier) {
        for (IDogAlteration alter : this.alterations) {
            ActionResultType result = alter.onLivingFall(this, distance, damageMultiplier);

            if (result.isSuccess()) {
                return true;
            } else if (result == ActionResultType.FAIL) {
                return false;
            }
        }

        return super.onLivingFall(distance, damageMultiplier);
    }

    // TODO
    @Override
    public int getMaxFallHeight() {
        return super.getMaxFallHeight();
    }

    @Override
    protected int calculateFallDamage(float distance, float damageMultiplier) {
        EffectInstance effectInst = this.getActivePotionEffect(Effects.JUMP_BOOST);
        float f = effectInst == null ? 0.0F : effectInst.getAmplifier() + 1;
        distance -= f;

        for (IDogAlteration alter : this.alterations) {
            ActionResult<Float> result = alter.calculateFallDistance(this, distance);

            if (result.getType().isSuccess()) {
                distance = result.getResult();
                break;
            }
        }

        return MathHelper.ceil((distance - 3.0F - f) * damageMultiplier);
    }

    @Override
    public boolean canBreatheUnderwater() {
        for (IDogAlteration alter : this.alterations) {
            ActionResultType result = alter.canBreatheUnderwater(this);

            if (result.isSuccess()) {
                return true;
            } else if (result == ActionResultType.FAIL) {
                return false;
            }
        }

        return super.canBreatheUnderwater();
    }

    @Override
    protected int decreaseAirSupply(int air) {
        for (IDogAlteration alter : this.alterations) {
            ActionResult<Integer> result = alter.decreaseAirSupply(this, air);

            if (result.getType().isSuccess()) {
                return result.getResult();
            }
        }

        return super.decreaseAirSupply(air);
    }

    @Override
    protected int determineNextAir(int currentAir) {
        currentAir += 4;
        for (IDogAlteration alter : this.alterations) {
            ActionResult<Integer> result = alter.determineNextAir(this, currentAir);

            if (result.getType().isSuccess()) {
                currentAir = result.getResult();
                break;
            }
        }

        return Math.min(currentAir, this.getMaxAir());
    }

    @Override
    public boolean canAttack(LivingEntity target) {
        if (this.isMode(EnumMode.DOCILE)) {
            return false;
        }

        for (IDogAlteration alter : this.alterations) {
            ActionResultType result = alter.canAttack(this, target);

            if (result.isSuccess()) {
                return true;
            } else if (result == ActionResultType.FAIL) {
                return false;
            }
        }

        return super.canAttack(target);
    }

    @Override
    public boolean canAttack(EntityType<?> entityType) {
        if (this.isMode(EnumMode.DOCILE)) {
            return false;
        }

        for (IDogAlteration alter : this.alterations) {
            ActionResultType result = alter.canAttack(this, entityType);

            if (result.isSuccess()) {
                return true;
            } else if (result == ActionResultType.FAIL) {
                return false;
            }
        }

        return super.canAttack(entityType);
    }

    @Override
    public boolean shouldAttackEntity(LivingEntity target, LivingEntity owner) {
        if (this.isMode(EnumMode.DOCILE)) {
            return false;
        }

        //TODO make wolves not able to attack dogs
        for (IDogAlteration alter : this.alterations) {
            ActionResultType result = alter.shouldAttackEntity(this, target, owner);

            if (result.isSuccess()) {
                return true;
            } else if (result == ActionResultType.FAIL) {
                return false;
            }
        }

        if (!(target instanceof CreeperEntity) && !(target instanceof GhastEntity)) {
            if (target instanceof WolfEntity) {
                WolfEntity wolfentity = (WolfEntity)target;
                return !wolfentity.isTamed() || wolfentity.getOwner() != owner;
            } else if (target instanceof DogEntity) {
                DogEntity dogEntity = (DogEntity)target;
                return !dogEntity.isTamed() || dogEntity.getOwner() != owner;
             } else if (target instanceof PlayerEntity && owner instanceof PlayerEntity && !((PlayerEntity)owner).canAttackPlayer((PlayerEntity)target)) {
                 return false;
            } else if (target instanceof AbstractHorseEntity && ((AbstractHorseEntity)target).isTame()) {
                return false;
            } else {
                return !(target instanceof TameableEntity) || !((TameableEntity)target).isTamed();
            }
         } else {
             return false;
         }
    }

    // TODO
    //@Override
//    public boolean canAttack(LivingEntity livingentityIn, EntityPredicate predicateIn) {
//        return predicateIn.canTarget(this, livingentityIn);
//     }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        for (IDogAlteration alter : this.alterations) {
            ActionResult<Float> result = alter.attackEntityFrom(this, source, amount);

            // TODO
            if (result.getType() == ActionResultType.FAIL) {
                return false;
            } else {
                amount = result.getResult();
            }
        }

        if (this.isInvulnerableTo(source)) {
            return false;
        } else {
            Entity entity = source.getTrueSource();
            if (this.sitGoal != null) {
               this.sitGoal.setSitting(false);
            }

            if (entity != null && !(entity instanceof PlayerEntity) && !(entity instanceof AbstractArrowEntity)) {
                amount = (amount + 1.0F) / 2.0F;
            }

            return super.attackEntityFrom(source, amount);
        }
    }

    @Override
    public boolean attackEntityAsMob(Entity target) {
        for (IDogAlteration alter : this.alterations) {
            ActionResultType result = alter.attackEntityAsMob(this, target);

            if (result.isSuccess()) {
                return true;
            } else if (result == ActionResultType.FAIL) {
                return false;
            }
        }

        IAttributeInstance attackDamageInst = this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);

        Set<AttributeModifier> critModifiers = null;

        if (this.getAttribute(CRIT_CHANCE).getValue() > this.getRNG().nextDouble()) {
            critModifiers = this.getAttribute(CRIT_BONUS).func_225505_c_();
            critModifiers.forEach(attackDamageInst::applyModifier);
        }

        int damage = ((int) attackDamageInst.getValue());
        if (critModifiers != null) {
            critModifiers.forEach(attackDamageInst::removeModifier);
        }

        boolean flag = target.attackEntityFrom(DamageSource.causeMobDamage(this), damage);
        if (flag) {
            this.applyEnchantments(this, target);
            this.statsTracker.increaseDamageDealt(damage);

            if (critModifiers != null) {
                // TODO Might want to make into a packet
                DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> Minecraft.getInstance().particles.addParticleEmitter(this, ParticleTypes.CRIT));
            }
        }

        return flag;
    }

    @Override
    public void onKillEntity(LivingEntity entityLivingIn) {
        super.onKillEntity(entityLivingIn);
        this.statsTracker.incrementKillCount(entityLivingIn);
    }

    @Override
    public boolean canBlockDamageSource(DamageSource source) {
        for (IDogAlteration alter : this.alterations) {
            ActionResultType result = alter.canBlockDamageSource(this, source);

            if (result.isSuccess()) {
                return true;
            } else if (result == ActionResultType.FAIL) {
                return false;
            }
        }

        return super.canBlockDamageSource(source);
    }

    @Override
    public void setFire(int second) {
        for (IDogAlteration alter : this.alterations) {
            ActionResult<Integer> result = alter.setFire(this, second);

            if (result.getType().isSuccess()) {
                second = result.getResult();
            }
        }

        super.setFire(second);
    }

    @Override
    public boolean isImmuneToFire() {
        for (IDogAlteration alter : this.alterations) {
            ActionResultType result = alter.isImmuneToFire(this);

            if (result.isSuccess()) {
                return true;
            } else if (result == ActionResultType.FAIL) {
                return false;
            }
        }

        return super.isImmuneToFire();
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        for (IDogAlteration alter : this.alterations) {
            ActionResultType result = alter.isInvulnerableTo(this, source);

            if (result.isSuccess()) {
                return true;
            } else if (result == ActionResultType.FAIL) {
                return false;
            }
        }

        return super.isInvulnerableTo(source);
    }

    @Override
    public boolean isInvulnerable() {
        for (IDogAlteration alter : this.alterations) {
            ActionResultType result = alter.isInvulnerable(this);

            if (result.isSuccess()) {
                return true;
            } else if (result == ActionResultType.FAIL) {
                return false;
            }
        }

        return super.isInvulnerable();
    }

    @Override
    public boolean isPotionApplicable(EffectInstance effectIn) {
        for (IDogAlteration alter : this.alterations) {
            ActionResultType result = alter.isPotionApplicable(this, effectIn);

            if (result.isSuccess()) {
                return true;
            } else if (result == ActionResultType.FAIL) {
                return false;
            }
        }

        return super.isPotionApplicable(effectIn);
    }

    @Override
    public void setUniqueId(UUID uniqueIdIn) {

        // If the UUID is changed remove old one and add new one
        UUID oldUniqueId = this.getUniqueID();

        if (uniqueIdIn.equals(oldUniqueId)) {
            return; // No change do nothing
        }

        super.setUniqueId(uniqueIdIn);

        if (this.world != null && !this.world.isRemote) {
            DogLocationStorage.get(this.world).remove(oldUniqueId);
            DogLocationStorage.get(this.world).getOrCreateData(this).update(this);
        }
    }

    @Override
    public void setTamedBy(PlayerEntity player) {
        super.setTamedBy(player);
        // When tamed by player cache their display name
        this.setOwnersName(player.getName());
    }

    @Override
    public void setTamed(boolean tamed) {
        super.setTamed(tamed);
        if (tamed) {
           this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0D);
           this.setHealth(20.0F);
        } else {
           this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(8.0D);
        }

        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.0D);
     }

    @Override
    public void setOwnerId(@Nullable UUID uuid) {
        super.setOwnerId(uuid);

        if (uuid == null) {
            this.setOwnersName((ITextComponent) null);
        }
    }

    @Override // blockAttackFromPlayer
    public boolean hitByEntity(Entity entityIn) {
        if (entityIn instanceof PlayerEntity && !this.canPlayersAttack()) {
            return true;
        }

        for (IDogAlteration alter : this.alterations) {
            ActionResultType result = alter.hitByEntity(this, entityIn);

            if (result.isSuccess()) {
                return true;
            } else if (result == ActionResultType.FAIL) {
                return false;
            }
        }

        return false;
    }

    @Override
    public ItemStack getPickedResult(RayTraceResult target) {
        return new ItemStack(DoggyItems.DOGGY_CHARM.get());
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return stack.getItem().isIn(DoggyTags.BREEDING_ITEMS);
    }

    @Override
    public boolean canMateWith(AnimalEntity otherAnimal) {
        if (otherAnimal == this) {
            return false;
        } else if (!this.isTamed()) {
            return false;
        } else if (!(otherAnimal instanceof DogEntity)) {
            return false;
        } else {
            DogEntity entitydog = (DogEntity) otherAnimal;
            if (!entitydog.isTamed()) {
                return false;
            } else if(entitydog.isSitting()) {
                return false;
            } else if(ConfigValues.DOG_GENDER && !this.getGender().canMateWith(entitydog.getGender())) {
                return false;
            } else {
                return this.isInLove() && entitydog.isInLove();
            }
        }
    }

    @Override
    public AgeableEntity createChild(AgeableEntity ageable) {
        DogEntity entitydog = DoggyEntityTypes.DOG.get().create(this.world);
        UUID uuid = this.getOwnerId();

        if(uuid != null) {
            entitydog.setOwnerId(uuid);
            entitydog.setTamed(true);
        }

        if(ageable instanceof DogEntity && ConfigValues.PUPS_GET_PARENT_LEVELS) {
            entitydog.setLevel(this.getLevel().combine(((DogEntity) ageable).getLevel()));
        }

        return entitydog;
    }

    @Override
    public boolean getAlwaysRenderNameTagForRender() {
        return (ConfigValues.ALWAYS_SHOW_DOG_NAME && this.hasCustomName()) || super.getAlwaysRenderNameTagForRender();
    }

    @Override
    public float getRenderScale() {
        if(this.isChild()) {
            return 0.5F;
        } else {
            return this.getDogSize() * 0.3F + 0.1F;
        }
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        for (IDogAlteration alter : this.alterations) {
            LazyOptional<T> result = alter.getCapability(this, cap, side);

            if (result != null) {
                return result;
            }
        }

        return super.getCapability(cap, side);
    }

    private boolean changingDimension = false;

    @Override
    public Entity changeDimension(DimensionType dimType) {
        this.changingDimension = true;
        Entity transportedEntity = super.changeDimension(dimType);
        if (transportedEntity instanceof DogEntity) {
            DogLocationStorage.get(this.world).getOrCreateData(this).update((DogEntity) transportedEntity);
        }
        return transportedEntity;
    }

    @Override
    public void onAddedToWorld() {
        super.onAddedToWorld(); // When the entity is added to tracking list#
        if (this.world != null && !this.world.isRemote) {
            //DogLocationData locationData = DogLocationStorage.get(this.world).getOrCreateData(this);
            //locationData.update(this);
        }
    }

    @Override
    public void onRemovedFromWorld() {
        super.onRemovedFromWorld(); // When the entity is removed from tracking list
    }

    /**
     * When the entity is brought back to life
     */
    @Override
    public void revive() {
        super.revive();
    }

    @Override
    protected void onDeathUpdate() {
        if (this.deathTime == 19) { // 1 second after death
            if (this.world != null && !this.world.isRemote) {
//                DogRespawnStorage.get(this.world).putData(this);
//                DoggyTalents.LOGGER.debug("Saved dog as they died {}", this);
//
//                DogLocationStorage.get(this.world).remove(this);
//                DoggyTalents.LOGGER.debug("Removed dog location as they were removed from the world {}", this);
            }
        }

        super.onDeathUpdate();
    }

    @Override
    public void onDeath(DamageSource cause) {

        this.wetSource = null;
        this.isShaking = false;
        this.prevTimeWolfIsShaking = 0.0F;
        this.timeWolfIsShaking = 0.0F;

        if (this.world != null && !this.world.isRemote) {
            DogRespawnStorage.get(this.world).putData(this);
            DoggyTalents2.LOGGER.debug("Saved dog as they died {}", this);

            DogLocationStorage.get(this.world).remove(this);
            DoggyTalents2.LOGGER.debug("Removed dog location as they were removed from the world {}", this);
        }

        this.alterations.forEach((alter) -> alter.onDeath(this, cause));
        super.onDeath(cause);
    }

    @Override
    public void dropInventory() {
        super.dropInventory();

        this.alterations.forEach((alter) -> alter.dropInventory(this));
    }

    /**
     * When the entity is removed
     */
    @Override
    public void remove(boolean keepData) {
        super.remove(keepData);

        if (!keepData) {
            this.alterations.forEach((alter) -> alter.invalidateCapabilities(this));
        }

        if (this.world != null && !this.world.isRemote) {

            // Dog did not die, just changed dimension
            if (this.changingDimension) {
//                DogRespawnStorage.get(this.world).putData(this);
//                DoggyTalents.LOGGER.debug("Saved dog as they died {}", this);
//
//                DogLocationStorage.get(this.world).remove(this);
//                DoggyTalents.LOGGER.debug("Removed dog location as they were removed from the world {}", this);
            }
        }
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);

        NBTUtil.putTalentMap(compound, "talent_level_list", this.getTalentMap());

        ListNBT accessoryList = new ListNBT();
        List<AccessoryInstance> accessories = this.getAccessories();

        for (int i = 0; i < accessories.size(); i++) {

            CompoundNBT accessoryTag = new CompoundNBT();
            accessories.get(i).writeInstance(accessoryTag);
            accessoryList.add(accessoryTag);
        }

        compound.put("accessories", accessoryList);

        this.alterations.forEach((alter) -> alter.write(this, compound));

        compound.putString("mode", this.getMode().getSaveName());
        compound.putString("dogGender", this.getGender().getSaveName());
        compound.putFloat("dogHunger", this.getDogHunger());
        this.getOwnersName().ifPresent((comp) -> {
            NBTUtil.putTextComponent(compound, "lastKnownOwnerName", comp);
        });

        compound.putString("customSkinHash", this.getSkinHash());
        compound.putBoolean("willObey", this.willObeyOthers());
        compound.putBoolean("friendlyFire", this.canPlayersAttack());
        compound.putInt("dogSize", this.getDogSize());
        compound.putInt("level_normal", this.getLevel().getLevel(Type.NORMAL));
        compound.putInt("level_dire", this.getLevel().getLevel(Type.DIRE));
        NBTUtil.writeItemStack(compound, "fetchItem", this.getBoneVariant());

        DimensionDependantArg<Optional<BlockPos>> bedsData = this.dataManager.get(DOG_BED_LOCATION.get());

        if (!bedsData.isEmpty()) {
            ListNBT bedsList = new ListNBT();

            for (Entry<DimensionType, Optional<BlockPos>> entry : bedsData.entrySet()) {
                CompoundNBT bedNBT = new CompoundNBT();
                NBTUtil.putResourceLocation(bedNBT, "dim", entry.getKey().getRegistryName());
                NBTUtil.putBlockPos(bedNBT, "pos", entry.getValue());
                bedsList.add(bedNBT);
            }

            compound.put("beds", bedsList);
        }

        DimensionDependantArg<Optional<BlockPos>> bowlsData = this.dataManager.get(DOG_BOWL_LOCATION.get());

        if (!bowlsData.isEmpty()) {
            ListNBT bowlsList = new ListNBT();

            for (Entry<DimensionType, Optional<BlockPos>> entry : bowlsData.entrySet()) {
                CompoundNBT bowlsNBT = new CompoundNBT();
                NBTUtil.putResourceLocation(bowlsNBT, "dim", entry.getKey().getRegistryName());
                NBTUtil.putBlockPos(bowlsNBT, "pos", entry.getValue());
                bowlsList.add(bowlsNBT);
            }

            compound.put("bowls", bowlsList);
        }

        this.statsTracker.writeAdditional(compound);
    }


    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);

        Map<Talent, Integer> talentMap = this.getTalentMap();
        talentMap.clear();

        if (compound.contains("talent_level_list", Constants.NBT.TAG_LIST)) {
            talentMap.putAll(NBTUtil.getTalentMap(compound, "talent_level_list"));
        } else {
            // Try to read old talent format if new one doesn't exist
            BackwardsComp.readTalentMapping(compound, talentMap);
        }

        this.markDataParameterDirty(TALENTS.get(), false); // Mark dirty so data is synced to client

        List<AccessoryInstance> accessories = this.getAccessories();
        accessories.clear();

        if (compound.contains("accessories", Constants.NBT.TAG_LIST)) {
            ListNBT accessoryList = compound.getList("accessories", Constants.NBT.TAG_COMPOUND);

            for (int i = 0; i < accessoryList.size(); i++) {
                // Add directly so that nothing is lost, if number allowed on changes
                AccessoryInstance.readInstance(accessoryList.getCompound(i)).ifPresent(accessories::add);
            }
        } else {
            // Try to read old accessories from their individual format
            BackwardsComp.readAccessories(compound, accessories);
        }

        this.markDataParameterDirty(ACCESSORIES.get(), false); // Mark dirty so data is synced to client

        // Does what notifyDataManagerChange would have done but this way only does it once
        this.recalculateAlterationsCache();
        this.spendablePoints.markForRefresh();
        for (Entry<Talent, Integer> entry : this.getTalentMap().entrySet()) {
            entry.getKey().init(this);
        }

        this.alterations.forEach((alter) -> alter.read(this, compound));

        this.setGender(EnumGender.bySaveName(compound.getString("dogGender")));

        if (compound.contains("mode", Constants.NBT.TAG_STRING)) {
            this.setMode(EnumMode.bySaveName(compound.getString("mode")));
        } else {
            // Read old mode id
            BackwardsComp.readMode(compound, this::setMode);
        }

        if (compound.contains("customSkinHash", Constants.NBT.TAG_STRING)) {
            this.setSkinHash(compound.getString("customSkinHash"));
        } else {
            BackwardsComp.readDogTexture(compound, this::setSkinHash);
        }

        if (compound.contains("fetchItem", Constants.NBT.TAG_COMPOUND)) {
            this.setBoneVariant(NBTUtil.readItemStack(compound, "fetchItem"));
        } else {
            BackwardsComp.readHasBone(compound, this::setBoneVariant);
        }

        this.setHungerDirectly(compound.getFloat("dogHunger"));
        this.setOwnersName(NBTUtil.getTextComponent(compound, "lastKnownOwnerName"));
        this.setWillObeyOthers(compound.getBoolean("willObey"));
        this.setCanPlayersAttack(compound.getBoolean("friendlyFire"));
        if (compound.contains("dogSize", Constants.NBT.TAG_ANY_NUMERIC)) {
            this.setDogSize(compound.getInt("dogSize"));
        }

        if (compound.contains("level_normal", Constants.NBT.TAG_ANY_NUMERIC)) {
            this.getLevel().setLevel(Type.NORMAL, compound.getInt("level_normal"));
            this.markDataParameterDirty(DOG_LEVEL.get());
        }

        if (compound.contains("level_dire", Constants.NBT.TAG_ANY_NUMERIC)) {
            this.getLevel().setLevel(Type.DIRE, compound.getInt("level_dire"));
            this.markDataParameterDirty(DOG_LEVEL.get());
        }

        DimensionDependantArg<Optional<BlockPos>> bedsData = this.dataManager.get(DOG_BED_LOCATION.get()).copyEmpty();

        if (compound.contains("beds", Constants.NBT.TAG_LIST)) {
            ListNBT bedsList = compound.getList("beds", Constants.NBT.TAG_COMPOUND);

            for (int i = 0; i < bedsList.size(); i++) {
                CompoundNBT bedNBT = bedsList.getCompound(i);
                ResourceLocation loc = NBTUtil.getResourceLocation(bedNBT, "dim");
                Optional<DimensionType> type = Registry.DIMENSION_TYPE.getValue(loc);
                if (type.isPresent()) {
                    Optional<BlockPos> pos = NBTUtil.getBlockPos(bedNBT, "pos");
                    bedsData.put(type.get(), pos);
                } else {
                    DoggyTalents2.LOGGER.warn("Failed loading from NBT. Could not find dimension {}", loc);
                }
            }
        } else {
            BackwardsComp.readBedLocations(compound, bedsData);
        }

        this.dataManager.set(DOG_BED_LOCATION.get(), bedsData);

        DimensionDependantArg<Optional<BlockPos>> bowlsData = this.dataManager.get(DOG_BOWL_LOCATION.get()).copyEmpty();

        if (compound.contains("bowls", Constants.NBT.TAG_LIST)) {
            ListNBT bowlsList = compound.getList("bowls", Constants.NBT.TAG_COMPOUND);

            for (int i = 0; i < bowlsList.size(); i++) {
                CompoundNBT bowlsNBT = bowlsList.getCompound(i);
                ResourceLocation loc = NBTUtil.getResourceLocation(bowlsNBT, "dim");
                Optional<DimensionType> type = Registry.DIMENSION_TYPE.getValue(loc);
                if (type.isPresent()) {
                    Optional<BlockPos> pos = NBTUtil.getBlockPos(bowlsNBT, "pos");
                    bowlsData.put(type.get(), pos);
                } else {
                    DoggyTalents2.LOGGER.warn("Failed loading from NBT. Could not find dimension {}", loc);
                }
            }
        } else {
            BackwardsComp.readBowlLocations(compound, bowlsData);
        }

        this.dataManager.set(DOG_BOWL_LOCATION.get(), bowlsData);

        this.statsTracker.readAdditional(compound);
    }

    @Override
    public void notifyDataManagerChange(DataParameter<?> key) {
        super.notifyDataManagerChange(key);
        if (TALENTS.get().equals(key) || ACCESSORIES.get().equals(key)) {
            this.recalculateAlterationsCache();
        }

        if (TALENTS.get().equals(key)) {
            for (Entry<Talent, Integer> entry : this.getTalentMap().entrySet()) {
                entry.getKey().init(this);
            }

            this.spendablePoints.markForRefresh();
        }

        if (DOG_LEVEL.get().equals(key)) {
            this.spendablePoints.markForRefresh();
        }

        if (ACCESSORIES.get().equals(key)) {
            // If client sort accessories
            if (this.world.isRemote) {
                // Does not recall this notifyDataManagerChange as list object is
                // still the same, maybe in future MC versions this will change so need to watch out
                this.getAccessories().sort(AccessoryInstance.RENDER_SORTER);
            }
        }

        if(SIZE.equals(key)) {
            this.recalculateSize();
        }
    }

    public void recalculateAlterationsCache() {
        this.alterations.clear();

        this.getAccessories().forEach((inst) -> {
            if (inst instanceof IDogAlteration) {
                this.alterations.add((IDogAlteration) inst);
            }
        });

        for (Entry<Talent, Integer> entry : this.getTalentMap().entrySet()) {
            this.alterations.add(entry.getKey());
        }

        DoggyTalents2.LOGGER.debug("Recalculate alterations, size {}", this.alterations.size());
    }

    /**
     * If the entity can make changes to the dog
     * @param livingEntity The entity
     */
    @Override
    public boolean canInteract(LivingEntity livingEntity) {
        return this.willObeyOthers() || this.isOwner(livingEntity);
    }

    @Override
    public List<AccessoryInstance> getAccessories() {
        return this.dataManager.get(ACCESSORIES.get());
    }

    @Override
    public boolean addAccessory(@Nonnull AccessoryInstance accessoryInst) {
        List<AccessoryInstance> accessories = this.getAccessories();
        AccessoryType type = accessoryInst.getAccessory().getType();

        // Gets accessories of the same type
        List<AccessoryInstance> filtered = accessories.stream().filter((inst) -> {
            return type == inst.getAccessory().getType();
        }).collect(Collectors.toList());

//        while (!filtered.isEmpty() && filtered.size() >= type.numberToPutOn()) {
//            accessories.remove(filtered.get(0));
//            filtered.remove(0);
//        }

        if (filtered.size() >= type.numberToPutOn()) {
            return false;
        }

        accessories.add(accessoryInst);

        this.markDataParameterDirty(ACCESSORIES.get());

        return true;
    }

    @Override
    public List<AccessoryInstance> removeAccessories() {
        List<AccessoryInstance> removed = Lists.newArrayList(this.getAccessories());
        this.getAccessories().clear();
        this.markDataParameterDirty(ACCESSORIES.get());
        return removed;
    }

    public Optional<AccessoryInstance> getAccessory(AccessoryType typeIn) {
        List<AccessoryInstance> accessories = this.getAccessories();

        for (AccessoryInstance inst : accessories) {
            if (inst.getAccessory().getType() == typeIn) {
                return Optional.of(inst);
            }
        }

        return Optional.empty();
    }

    public Optional<AccessoryInstance> getAccessory(Accessory typeIn) {
        List<AccessoryInstance> accessories = this.getAccessories();

        for (AccessoryInstance inst : accessories) {
            if (inst.getAccessory() == typeIn) {
                return Optional.of(inst);
            }
        }

        return Optional.empty();
    }

    public Optional<ITextComponent> getOwnersName() {
        return this.dataManager.get(LAST_KNOWN_NAME);
    }

    public void setOwnersName(@Nullable ITextComponent comp) {
        this.setOwnersName(Optional.ofNullable(comp));
    }

    public void setOwnersName(Optional<ITextComponent> collar) {
        this.dataManager.set(LAST_KNOWN_NAME, collar);
    }

    public EnumGender getGender() {
        return this.dataManager.get(GENDER.get());
    }

    public void setGender(EnumGender collar) {
        this.dataManager.set(GENDER.get(), collar);
    }

    @Override
    public EnumMode getMode() {
        return this.dataManager.get(MODE.get());
    }

    public boolean isMode(EnumMode... modes) {
        EnumMode mode = this.getMode();
        for (EnumMode test : modes) {
            if (mode == test) {
                return true;
            }
        }

        return false;
    }

    public void setMode(EnumMode collar) {
        this.dataManager.set(MODE.get(), collar);
    }

    public Optional<BlockPos> getBedPos() {
        return this.getBedPos(this.world.getDimension().getType());
    }

    public Optional<BlockPos> getBedPos(DimensionType dim) {
        return this.dataManager.get(DOG_BED_LOCATION.get()).getOrDefault(dim, Optional.empty());
    }

    public void setBedPos(@Nullable BlockPos pos) {
        this.setBedPos(this.world.getDimension().getType(), pos);
    }

    public void setBedPos(DimensionType dim, @Nullable BlockPos pos) {
        this.setBedPos(dim, WorldUtil.toImmutable(pos));
    }

    public void setBedPos(DimensionType dim, Optional<BlockPos> pos) {
        this.dataManager.set(DOG_BED_LOCATION.get(), this.dataManager.get(DOG_BED_LOCATION.get()).copy().set(dim, pos));
    }

    public Optional<BlockPos> getBowlPos() {
        return this.getBowlPos(this.world.getDimension().getType());
    }

    public Optional<BlockPos> getBowlPos(DimensionType dim) {
        return this.dataManager.get(DOG_BOWL_LOCATION.get()).getOrDefault(dim, Optional.empty());
    }

    public void setBowlPos(@Nullable BlockPos pos) {
        this.setBowlPos(this.world.getDimension().getType(), pos);
    }

    public void setBowlPos(DimensionType dim, @Nullable BlockPos pos) {
        this.setBowlPos(dim, WorldUtil.toImmutable(pos));
    }

    public void setBowlPos(DimensionType dim, Optional<BlockPos> pos) {
        this.dataManager.set(DOG_BOWL_LOCATION.get(), this.dataManager.get(DOG_BOWL_LOCATION.get()).copy().set(dim, pos));
    }

    @Override
    public float getMaxHunger() {
        float maxHunger = ConfigValues.DEFAULT_MAX_HUNGER;

        for (IDogAlteration alter : this.alterations) {
            ActionResult<Float> result = alter.getMaxHunger(this, maxHunger);

            if (result.getType().isSuccess()) {
                maxHunger = result.getResult();
            }
        }

        return maxHunger;
    }

    @Override
    public float getDogHunger() {
        return this.dataManager.get(HUNGER_INT);
    }

    @Override
    public void addHunger(float add) {
        this.setDogHunger(this.getDogHunger() + add);
    }

    @Override
    public void setDogHunger(float hunger) {
        float diff = hunger - this.getDogHunger();

        for (IDogAlteration alter : this.alterations) {
            ActionResult<Float> result = alter.setDogHunger(this, hunger, diff);

            if (result.getType().isSuccess()) {
                hunger = result.getResult();
                diff = hunger - this.getDogHunger();
            }
        }

        this.setHungerDirectly(MathHelper.clamp(hunger, 0, this.getMaxHunger()));
    }

    private void setHungerDirectly(float hunger) {
        this.dataManager.set(HUNGER_INT, hunger);
    }

    public boolean hasCustomSkin() {
        return !Strings.isNullOrEmpty(this.getSkinHash());
    }

    public String getSkinHash() {
        return this.dataManager.get(CUSTOM_SKIN);
    }

    public void setSkinHash(String hash) {
        if (hash == null) {
            hash = "";
        }
        this.dataManager.set(CUSTOM_SKIN, hash);
    }

    @Override
    public DogLevel getLevel() {
        return this.dataManager.get(DOG_LEVEL.get());
    }

    public void setLevel(DogLevel level) {
        this.dataManager.set(DOG_LEVEL.get(), level);
    }

    @Override
    public void increaseLevel(DogLevel.Type typeIn) {
        this.getLevel().incrementLevel(typeIn);
        this.markDataParameterDirty(DOG_LEVEL.get());
    }

    @Override
    public void setDogSize(int value) {
        this.dataManager.set(SIZE, (byte)Math.min(5, Math.max(1, value)));
    }

    @Override
    public int getDogSize() {
        return this.dataManager.get(SIZE);
    }

    public void setBoneVariant(ItemStack stack) {
        this.dataManager.set(BONE_VARIANT, stack);
    }

    public ItemStack getBoneVariant() {
        return this.dataManager.get(BONE_VARIANT);
    }

    @Nullable
    public IThrowableItem getThrowableItem() {
        Item item = this.dataManager.get(BONE_VARIANT).getItem();
        return item instanceof IThrowableItem ? (IThrowableItem) item : null;
    }

    public boolean hasBone() {
        return !this.getBoneVariant().isEmpty();
    }

    private boolean getDogFlag(int bit) {
        return (this.dataManager.get(DOG_FLAGS) & bit) != 0;
    }

    private void setDogFlag(int bits, boolean flag) {
        byte c = this.dataManager.get(DOG_FLAGS);
        this.dataManager.set(DOG_FLAGS, (byte)(flag ? c | bits : c & ~bits));
    }

    public void setBegging(boolean begging) {
        this.setDogFlag(1, begging);
    }

    public boolean isBegging() {
        return this.getDogFlag(1);
    }

    public void setWillObeyOthers(boolean obeyOthers) {
        this.setDogFlag(2, obeyOthers);
    }

    public boolean willObeyOthers() {
        return this.getDogFlag(2);
    }

    public void setCanPlayersAttack(boolean flag) {
        this.setDogFlag(4, flag);
    }

    public boolean canPlayersAttack() {
        return this.getDogFlag(4);
    }

    public void set8Flag(boolean collar) {
        this.setDogFlag(8, collar);
    }

    public boolean get8Flag() {
        return this.getDogFlag(8);
    }

    public void setHasSunglasses(boolean sunglasses) {
        this.setDogFlag(16, sunglasses);
    }

    public boolean hasSunglasses() {
        return this.getDogFlag(16);
    }

    public void setLyingDown(boolean lying) {
        this.setDogFlag(32, lying);
    }

    public boolean isLyingDown() {
        return this.getDogFlag(32);
    }

    public void set64Flag(boolean lying) {
        this.setDogFlag(64, lying);
    }

    public boolean get64Flag() {
        return this.getDogFlag(64);
    }

    public Map<Talent, Integer> getTalentMap() {
        return this.dataManager.get(TALENTS.get());
    }

    public void setTalentMap(Map<Talent, Integer> map) {
        this.dataManager.set(TALENTS.get(), map);
    }

    public void setTalentLevel(Talent talent, int level) {
        Map<Talent, Integer> map = this.getTalentMap();
        boolean existed = map.containsKey(talent);
        if (level > 0) {
            map.put(talent, level);

            if (!existed) {
                talent.init(this);
            }

            talent.set(this, level);

        } else {
            int preLevel = map.getOrDefault(talent, 0);
            map.remove(talent);
            talent.removed(this, preLevel);
        }

        this.markDataParameterDirty(TALENTS.get());
        DoggyTalents2.LOGGER.debug("Set talent {} to level {}", talent.getRegistryName(), level);
    }


    public <T> void markDataParameterDirty(DataParameter<T> key) {
        this.markDataParameterDirty(key, true);
    }

    public <T> void markDataParameterDirty(DataParameter<T> key, boolean notify) {
        if (notify) {
            this.notifyDataManagerChange(key);
        }

        // Force the entry to update
        DataEntry<T> dataentry = this.dataManager.getEntry(key);
        dataentry.setDirty(true);
        this.dataManager.dirty = true;
    }

    @Override
    public void markAccessoriesDirty() {
        this.markDataParameterDirty(ACCESSORIES.get());
    }

    @Override
    public int getLevel(Talent talentIn) {
        Map<Talent, Integer> map = this.getTalentMap();
        return map.getOrDefault(talentIn, 0);
    }

    @Override
    public <T> void setData(DataKey<T> key, T value) {
        if (key.isFinal() && this.hasData(key)) {
            throw new RuntimeException("Key is final but was tried to be set again.");
        }
        this.objects.put(key.getIndex(), value);
    }

    /**
     * Tries to put the object in the map, does nothing if the key already exists
     */
    @Override
    public <T> void setDataIfEmpty(DataKey<T> key, T value) {
        if (!this.hasData(key)) {
            this.objects.put(key.getIndex(), value);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getData(DataKey<T> key) {
        return (T) this.objects.get(key.getIndex());
    }

    @Override
    public <T> T getDataOrGet(DataKey<T> key, Supplier<T> other) {
        if (this.hasData(key)) {
            return this.getData(key);
        }
        return other.get();
    }

    @Override
    public <T> T getDataOrDefault(DataKey<T> key, T other) {
        if (this.hasData(key)) {
            return this.getData(key);
        }
        return other;
    }

    @Override
    public <T> boolean hasData(DataKey<T> key) {
        return this.objects.containsKey(key.getIndex());
    }

    @Override
    public void untame() {
        this.setTamed(false);
        this.navigator.clearPath();
        this.sitGoal.setSitting(false);
        this.setHealth(8);

        this.getTalentMap().clear();
        this.markDataParameterDirty(TALENTS.get());

        this.setOwnerId(null);
        this.setWillObeyOthers(false);
        this.setMode(EnumMode.DOCILE);

        DoggyTalents2.LOGGER.debug("Untamed dog");
    }

    public boolean canSpendPoints(int amount) {
        return this.getSpendablePoints() >= amount || this.getAccessory(DoggyAccessories.GOLDEN_COLLAR.get()).isPresent();
    }

    // When this method is changed the cache may need to be updated at certain points
    private final int getSpendablePointsInternal() {
        int totalPoints = 15 + this.getLevel().getLevel(Type.NORMAL) + this.getLevel().getLevel(Type.DIRE);
        for (Entry<Talent, Integer> entry : this.getTalentMap().entrySet()) {
            totalPoints -= entry.getKey().getCummulativeCost(entry.getValue());
        }
        return totalPoints;
    }

    public int getSpendablePoints() {
        return this.spendablePoints.get();
    }

    @Override
    public boolean canRiderInteract() {
        return true;
    }

    @Override
    public Entity getControllingPassenger() {
        // Gets the first passenger which is the controlling passenger
        return this.getPassengers().isEmpty() ? null : (Entity) this.getPassengers().get(0);
    }

    @Override
    public boolean canBeSteered() {
        return this.getControllingPassenger() instanceof LivingEntity;
    }

    //TODO
    @Override
    public boolean canBeCollidedWith() {
        return super.canBeCollidedWith();
    }

    @Override
    public boolean canBePushed() {
        return !this.isBeingRidden() && super.canBePushed();
    }

    @Override
    public boolean canPassengerSteer() {
        // Super calls canBeSteered so controlling passenger can be guaranteed to be LivingEntity
        return super.canPassengerSteer() && this.canInteract((LivingEntity) this.getControllingPassenger());
    }

    public boolean isDogJumping() {
        return this.dogJumping;
    }

    public void setDogJumping(boolean jumping) {
        this.dogJumping = jumping;
    }

//    public double getDogJumpStrength() {
//        float verticalVelocity = 0.42F + 0.06F * this.TALENTS.getLevel(ModTalents.WOLF_MOUNT);
//        if(this.TALENTS.getLevel(ModTalents.WOLF_MOUNT) == 5) verticalVelocity += 0.04F;
//        return verticalVelocity;
//    }

    // 0 - 100 input
    public void setJumpPower(int jumpPowerIn) {
       // if(this.TALENTS.getLevel(ModTalents.WOLF_MOUNT) > 0) {
            this.jumpPower = 1.0F;
       // }
    }

    public boolean canJump() {
        return true;
        //TODO return this.TALENTS.getLevel(ModTalents.WOLF_MOUNT) > 0;
    }

    @Override
    public void travel(Vec3d positionIn) {
        if(this.isAlive()) {
            if(this.isBeingRidden() && this.canBeSteered()) {
                LivingEntity livingentity = (LivingEntity) this.getControllingPassenger();

                // Face the dog in the direction of the controlling passenger
                this.rotationYaw = livingentity.rotationYaw;
                this.prevRotationYaw = this.rotationYaw;
                this.rotationPitch = livingentity.rotationPitch * 0.5F;
                this.setRotation(this.rotationYaw, this.rotationPitch);
                this.renderYawOffset = this.rotationYaw;
                this.rotationYawHead = this.renderYawOffset;

                this.stepHeight = 1.0F;

                float straf = livingentity.moveStrafing * 0.7F;
                float foward = livingentity.moveForward;

                // If moving backwards half the speed
                if (foward <= 0.0F) {
                   foward *= 0.5F;
                }

                if (this.jumpPower > 0.0F && !this.isDogJumping() && this.onGround) {

                    // Calculate jump value based of jump strength, power this jump and jump boosts
                    double jumpValue = this.getAttribute(JUMP_STRENGTH).getValue() * this.getJumpFactor() * this.jumpPower; //TODO do we want getJumpFactor?
                    if (this.isPotionActive(Effects.JUMP_BOOST)) {
                        jumpValue += (this.getActivePotionEffect(Effects.JUMP_BOOST).getAmplifier() + 1) * 0.1F;
                    }

                    // Apply jump
                    Vec3d vec3d = this.getMotion();
                    this.setMotion(vec3d.x, jumpValue, vec3d.z);
                    this.setDogJumping(true);
                    this.isAirBorne = true;

                    // If moving forward, propel further in the direction
                    if (foward > 0.0F) {
                        final float amount = 0.4F; // TODO Allow people to change this value
                        float compX = MathHelper.sin(this.rotationYaw * ((float)Math.PI / 180F));
                        float compZ = MathHelper.cos(this.rotationYaw * ((float)Math.PI / 180F));
                        this.setMotion(this.getMotion().add(-amount * compX * this.jumpPower, 0.0D, amount * compZ * this.jumpPower));
                        //this.playJumpSound();
                    }

                    // Mark as unable jump until reset
                    this.jumpPower = 0.0F;
                }

                this.jumpMovementFactor = this.getAIMoveSpeed() * 0.1F;
                if (this.canPassengerSteer()) {
                    // Set the move speed and move the dog in the direction of the controlling entity
                    this.setAIMoveSpeed((float)this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getValue() * 0.5F);
                    super.travel(new Vec3d(straf, positionIn.y, foward));
                    this.newPosRotationIncrements = 0;
                } else if (livingentity instanceof PlayerEntity) {
                    // A player is riding and can not control then
                    this.setMotion(Vec3d.ZERO);
                }

                // Once the entity reaches the ground again allow it to jump again
                if(this.onGround) {
                    this.jumpPower = 0.0F;
                    this.setDogJumping(false);
                }

                //
                this.prevLimbSwingAmount = this.limbSwingAmount;
                double changeX = this.getPosX() - this.prevPosX;
                double changeY = this.getPosZ() - this.prevPosZ;
                float f4 = MathHelper.sqrt(changeX * changeX + changeY * changeY) * 4.0F;

                if (f4 > 1.0F) {
                   f4 = 1.0F;
                }

                this.limbSwingAmount += (f4 - this.limbSwingAmount) * 0.4F;
                this.limbSwing += this.limbSwingAmount;
             } else {
                 this.stepHeight = 0.5F; // Default
                 this.jumpMovementFactor = 0.02F; // Default
                 super.travel(positionIn);
             }

            this.addMovementStat(this.getPosX() - this.prevPosX, this.getPosY() - this.prevPosY, this.getPosZ() - this.prevPosZ);
        }
    }

    public void addMovementStat(double xD, double yD, double zD) {
        if(this.isBeingRidden()) {
            int j = Math.round(MathHelper.sqrt(xD * xD + zD * zD) * 100.0F);
            this.statsTracker.increaseDistanceRidden(j);
        }
        if(!this.isPassenger()) {
            if(this.areEyesInFluid(FluidTags.WATER, true)) {
                int j = Math.round(MathHelper.sqrt(xD * xD + yD * yD + zD * zD) * 100.0F);
                if (j > 0) {
                    this.statsTracker.increaseDistanceOnWater(j);
                }
            } else if(this.isInWater()) {
                int k = Math.round(MathHelper.sqrt(xD * xD + zD * zD) * 100.0F);
                if (k > 0) {
                    this.statsTracker.increaseDistanceInWater(k);
                }
            } else if(this.onGround) {
                int l = Math.round(MathHelper.sqrt(xD * xD + zD * zD) * 100.0F);
                if(l > 0) {
                    if (this.isSprinting()) {
                        this.statsTracker.increaseDistanceSprint(l);
                    } else if(this.isCrouching()) {
                        this.statsTracker.increaseDistanceSneaking(l);
                    } else {
                        this.statsTracker.increaseDistanceWalk(l);
                    }
                }
            } else { // Time in air
                int j1 = Math.round(MathHelper.sqrt(xD * xD + zD * zD) * 100.0F);
                //this.STATS.increaseDistanceInWater(k);
            }


        }
    }

    @Override
    public TranslationTextComponent getTranslationKey(Function<EnumGender, String> function) {
        return new TranslationTextComponent(function.apply(ConfigValues.DOG_GENDER ? this.getGender() : EnumGender.UNISEX));
    }

    @Override
    public boolean isLying() {
        LivingEntity owner = this.getOwner();
        boolean ownerSleeping = owner != null && owner.isSleeping();
        if (ownerSleeping) {
            return true;
        }

        Block blockBelow = this.world.getBlockState(this.getPosition().down()).getBlock();
        boolean onBed = blockBelow == DoggyBlocks.DOG_BED.get() || BlockTags.BEDS.contains(blockBelow);
        if (onBed) {
            return true;
        }

        return false;
    }
}
