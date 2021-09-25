package doggytalents.common.entity;

import java.util.ArrayList;
import java.util.HashMap;
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

import doggytalents.DoggyAccessories;
import doggytalents.DoggyAttributes;
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
import doggytalents.api.feature.InteractHandler;
import doggytalents.api.inferface.AbstractDogEntity;
import doggytalents.api.inferface.IDogAlteration;
import doggytalents.api.inferface.IDogFoodHandler;
import doggytalents.api.inferface.IThrowableItem;
import doggytalents.api.registry.Accessory;
import doggytalents.api.registry.AccessoryInstance;
import doggytalents.api.registry.AccessoryType;
import doggytalents.api.registry.Talent;
import doggytalents.api.registry.TalentInstance;
import doggytalents.client.screen.DogInfoScreen;
import doggytalents.common.config.ConfigValues;
import doggytalents.common.entity.ai.BerserkerModeGoal;
import doggytalents.common.entity.ai.BreedGoal;
import doggytalents.common.entity.ai.DogBegGoal;
import doggytalents.common.entity.ai.DogFollowOwnerGoal;
import doggytalents.common.entity.ai.DogWanderGoal;
import doggytalents.common.entity.ai.FetchGoal;
import doggytalents.common.entity.ai.FindWaterGoal;
import doggytalents.common.entity.ai.GuardModeGoal;
import doggytalents.common.entity.ai.MoveToBlockGoal;
import doggytalents.common.entity.ai.OwnerHurtByTargetGoal;
import doggytalents.common.entity.ai.OwnerHurtTargetGoal;
import doggytalents.common.entity.serializers.DimensionDependantArg;
import doggytalents.common.entity.stats.StatsTracker;
import doggytalents.common.storage.DogLocationStorage;
import doggytalents.common.storage.DogRespawnStorage;
import doggytalents.common.util.*;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
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
import net.minecraft.server.management.PreYggdrasilConverter;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.ITeleporter;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.registries.ForgeRegistries;

public class DogEntity extends AbstractDogEntity {

    private static final DataParameter<Optional<ITextComponent>> LAST_KNOWN_NAME = EntityDataManager.defineId(DogEntity.class, DataSerializers.OPTIONAL_COMPONENT);
    private static final DataParameter<Byte> DOG_FLAGS = EntityDataManager.defineId(DogEntity.class, DataSerializers.BYTE);

    private static final DataParameter<Float> HUNGER_INT = EntityDataManager.defineId(DogEntity.class, DataSerializers.FLOAT);
    private static final DataParameter<String> CUSTOM_SKIN = EntityDataManager.defineId(DogEntity.class, DataSerializers.STRING);

    private static final DataParameter<Byte> SIZE = EntityDataManager.defineId(DogEntity.class, DataSerializers.BYTE);
    private static final DataParameter<ItemStack> BONE_VARIANT = EntityDataManager.defineId(DogEntity.class, DataSerializers.ITEM_STACK);

    // Use Cache.make to ensure static fields are not initialised too early (before Serializers have been registered)
    private static final Cache<DataParameter<List<AccessoryInstance>>> ACCESSORIES =  Cache.make(() -> (DataParameter<List<AccessoryInstance>>) EntityDataManager.defineId(DogEntity.class, DoggySerializers.ACCESSORY_SERIALIZER.get().getSerializer()));
    private static final Cache<DataParameter<List<TalentInstance>>> TALENTS = Cache.make(() -> (DataParameter<List<TalentInstance>>) EntityDataManager.defineId(DogEntity.class, DoggySerializers.TALENT_SERIALIZER.get().getSerializer()));
    private static final Cache<DataParameter<DogLevel>> DOG_LEVEL = Cache.make(() -> (DataParameter<DogLevel>) EntityDataManager.defineId(DogEntity.class, DoggySerializers.DOG_LEVEL_SERIALIZER.get().getSerializer()));
    private static final Cache<DataParameter<EnumGender>> GENDER = Cache.make(() -> (DataParameter<EnumGender>) EntityDataManager.defineId(DogEntity.class,  DoggySerializers.GENDER_SERIALIZER.get().getSerializer()));
    private static final Cache<DataParameter<EnumMode>> MODE = Cache.make(() -> (DataParameter<EnumMode>) EntityDataManager.defineId(DogEntity.class, DoggySerializers.MODE_SERIALIZER.get().getSerializer()));
    private static final Cache<DataParameter<DimensionDependantArg<Optional<BlockPos>>>> DOG_BED_LOCATION = Cache.make(() -> (DataParameter<DimensionDependantArg<Optional<BlockPos>>>) EntityDataManager.defineId(DogEntity.class, DoggySerializers.BED_LOC_SERIALIZER.get().getSerializer()));
    private static final Cache<DataParameter<DimensionDependantArg<Optional<BlockPos>>>> DOG_BOWL_LOCATION = Cache.make(() -> (DataParameter<DimensionDependantArg<Optional<BlockPos>>>) EntityDataManager.defineId(DogEntity.class, DoggySerializers.BED_LOC_SERIALIZER.get().getSerializer()));

    public static final void initDataParameters() {
        ACCESSORIES.get();
        TALENTS.get();
        DOG_LEVEL.get();
        GENDER.get();
        MODE.get();
        DOG_BED_LOCATION.get();
        DOG_BOWL_LOCATION.get();
    }

    // Cached values
    private final Cache<Integer> spendablePoints = Cache.make(this::getSpendablePointsInternal);
    private final List<IDogAlteration> alterations = new ArrayList<>(4);
    private final List<IDogFoodHandler> foodHandlers = new ArrayList<>(4);
    public final Map<Integer, Object> objects = new HashMap<>();

    public final StatsTracker statsTracker = new StatsTracker();

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

    protected BlockPos targetBlock;

    public DogEntity(EntityType<? extends DogEntity> type, World worldIn) {
        super(type, worldIn);
        this.setTame(false);
        this.setGender(EnumGender.random(this.getRandom()));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ACCESSORIES.get(), new ArrayList<>(4));
        this.entityData.define(TALENTS.get(), new ArrayList<>(4));
        this.entityData.define(LAST_KNOWN_NAME, Optional.empty());
        this.entityData.define(DOG_FLAGS, (byte) 0);
        this.entityData.define(GENDER.get(), EnumGender.UNISEX);
        this.entityData.define(MODE.get(), EnumMode.DOCILE);
        this.entityData.define(HUNGER_INT, 60F);
        this.entityData.define(CUSTOM_SKIN, "");
        this.entityData.define(DOG_LEVEL.get(), new DogLevel(0, 0));
        this.entityData.define(SIZE, (byte) 3);
        this.entityData.define(BONE_VARIANT, ItemStack.EMPTY);
        this.entityData.define(DOG_BED_LOCATION.get(), new DimensionDependantArg<>(() -> DataSerializers.OPTIONAL_BLOCK_POS));
        this.entityData.define(DOG_BOWL_LOCATION.get(), new DimensionDependantArg<>(() -> DataSerializers.OPTIONAL_BLOCK_POS));
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new SwimGoal(this));
        this.goalSelector.addGoal(1, new FindWaterGoal(this));
        //this.goalSelector.addGoal(1, new PatrolAreaGoal(this));
        this.goalSelector.addGoal(2, new SitGoal(this));
        //this.goalSelector.addGoal(3, new WolfEntity.AvoidEntityGoal(this, LlamaEntity.class, 24.0F, 1.5D, 1.5D));
        this.goalSelector.addGoal(4, new LeapAtTargetGoal(this, 0.4F));
        this.goalSelector.addGoal(5, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(5, new MoveToBlockGoal(this));
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
        this.targetSelector.addGoal(3, (new HurtByTargetGoal(this)).setAlertOthers());
        //this.targetSelector.addGoal(4, new NonTamedTargetGoal<>(this, AnimalEntity.class, false, TARGET_ENTITIES));
        //this.targetSelector.addGoal(4, new NonTamedTargetGoal<>(this, TurtleEntity.class, false, TurtleEntity.TARGET_DRY_BABY));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, AbstractSkeletonEntity.class, false));
        this.targetSelector.addGoal(6, new BerserkerModeGoal<>(this, MonsterEntity.class, false));
        this.targetSelector.addGoal(6, new GuardModeGoal(this, false));
    }

    @Override
    public void playStepSound(BlockPos pos, BlockState blockIn) {
        this.playSound(SoundEvents.WOLF_STEP, 0.15F, 1.0F);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        if (this.random.nextInt(3) == 0) {
            return this.isTame() && this.getHealth() < 10.0F ? SoundEvents.WOLF_WHINE : SoundEvents.WOLF_PANT;
        } else {
            return SoundEvents.WOLF_AMBIENT;
        }
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.WOLF_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.WOLF_DEATH;
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
        return Math.min(0.5F + MathHelper.lerp(partialTicks, this.prevTimeWolfIsShaking, this.timeWolfIsShaking) / 2.0F * 0.5F, 1.0F);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public float getShakeAngle(float partialTicks, float offset) {
        float f = (MathHelper.lerp(partialTicks, this.prevTimeWolfIsShaking, this.timeWolfIsShaking) + offset) / 1.8F;
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
    public void handleEntityEvent(byte id) {
        if (id == doggytalents.common.lib.Constants.EntityState.WOLF_START_SHAKING) {
            this.startShaking();
        } else if (id == doggytalents.common.lib.Constants.EntityState.WOLF_INTERUPT_SHAKING) {
            this.finishShaking();
        } else {
            super.handleEntityEvent(id);
        }
    }

    public float getTailRotation() {
        return this.isTame() ? (0.55F - (this.getMaxHealth() - this.getHealth()) * 0.02F) * (float)Math.PI : ((float)Math.PI / 5F);
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
    public Vector3d getLeashOffset() {
        return new Vector3d(0.0D, 0.6F * this.getEyeHeight(), this.getBbWidth() * 0.4F);
    }

    @Override
    public int getMaxHeadXRot() {
        return this.isInSittingPose() ? 20 : super.getMaxHeadXRot();
    }

    @Override
    public double getMyRidingOffset() {
        return this.getVehicle() instanceof PlayerEntity ? 0.5D : 0.0D;
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

            boolean inWater = this.isInWater();
            // If inWater is false then isInRain is true in the or statement
            boolean inRain = inWater ? false : this.isInWaterOrRain();
            boolean inBubbleColumn = this.isInBubbleColumn();

            if (inWater || inRain || inBubbleColumn) {
                if (this.wetSource == null) {
                    this.wetSource = WetSource.of(inWater, inBubbleColumn, inRain);
                }
                if (this.isShaking && !this.level.isClientSide) {
                    this.finishShaking();
                    this.level.broadcastEntityEvent(this, doggytalents.common.lib.Constants.EntityState.WOLF_INTERUPT_SHAKING);
                }
            } else if ((this.wetSource != null || this.isShaking) && this.isShaking) {
                if (this.timeWolfIsShaking == 0.0F) {
                    this.playSound(SoundEvents.WOLF_SHAKE, this.getSoundVolume(), (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
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
                    this.finishShaking();
                }

                if (this.timeWolfIsShaking > 0.4F) {
                    float f = (float)this.getY();
                    int i = (int)(MathHelper.sin((this.timeWolfIsShaking - 0.4F) * (float)Math.PI) * 7.0F);
                    Vector3d vec3d = this.getDeltaMovement();

                    for (int j = 0; j < i; ++j) {
                        float f1 = (this.random.nextFloat() * 2.0F - 1.0F) * this.getBbWidth() * 0.5F;
                        float f2 = (this.random.nextFloat() * 2.0F - 1.0F) * this.getBbWidth() * 0.5F;
                        this.level.addParticle(ParticleTypes.SPLASH, this.getX() + f1, f + 0.8F, this.getZ() + f2, vec3d.x, vec3d.y, vec3d.z);
                    }
                }
            }

            // On server side
            if (!this.level.isClientSide) {

                // Every 2 seconds
                if (this.tickCount % 40 == 0) {
                    DogLocationStorage.get(this.level).getOrCreateData(this).update(this);

                    if (this.getOwner() != null) {
                        this.setOwnersName(this.getOwner().getName());
                    }
                }
            }
        }

        this.alterations.forEach((alter) -> alter.tick(this));
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level.isClientSide && this.wetSource != null && !this.isShaking && !this.isPathFinding() && this.isOnGround()) {
            this.startShaking();
            this.level.broadcastEntityEvent(this, doggytalents.common.lib.Constants.EntityState.WOLF_START_SHAKING);
        }

        if (!this.level.isClientSide) {
            if (!ConfigValues.DISABLE_HUNGER) {
                this.prevHungerTick = this.hungerTick;

                if (!this.isVehicle() && !this.isInSittingPose()) {
                    this.hungerTick += 1;
                }

                for (IDogAlteration alter : this.alterations) {
                    ActionResult<Integer> result = alter.hungerTick(this, this.hungerTick - this.prevHungerTick);

                    if (result.getResult().shouldSwing()) {
                        this.hungerTick = result.getObject() + this.prevHungerTick;
                    }
                }

                if (this.hungerTick > 400) {
                    this.setDogHunger(this.getDogHunger() - 1);
                    this.hungerTick -= 400;
                }
            }

            this.prevHealingTick = this.healingTick;
            this.healingTick += 8;

            if (this.isInSittingPose()) {
                this.healingTick += 4;
            }

            for (IDogAlteration alter : this.alterations) {
                ActionResult<Integer> result = alter.healingTick(this, this.healingTick - this.prevHealingTick);

                if (result.getResult().shouldSwing()) {
                    this.healingTick = result.getObject() + this.prevHealingTick;
                }
            }

            if (this.healingTick >= 6000) {
                if (this.getHealth() < this.getMaxHealth()) {
                    this.heal(1);
                }

                this.healingTick = 0;
            }
        }

        if(ConfigValues.DIRE_PARTICLES && this.level.isClientSide && this.getLevel().isDireDog()) {
            for (int i = 0; i < 2; i++) {
                this.level.addParticle(ParticleTypes.PORTAL, this.getRandomX(0.5D), this.getRandomY() - 0.25D, this.getRandomZ(0.5D), (this.random.nextDouble() - 0.5D) * 2D, -this.random.nextDouble(), (this.random.nextDouble() - 0.5D) * 2D);
            }
        }

        // Check if dog bowl still exists every 50t/2.5s, if not remove
        if (this.tickCount % 50 == 0) {
            RegistryKey<World> dimKey = this.level.dimension();
            Optional<BlockPos> bowlPos = this.getBowlPos(dimKey);

            // If the dog has a food bowl in this dimension then check if it is still there
            // Only check if the chunk it is in is loaded
            if (bowlPos.isPresent() && this.level.hasChunkAt(bowlPos.get()) && !this.level.getBlockState(bowlPos.get()).is(DoggyBlocks.FOOD_BOWL.get())) {
                this.setBowlPos(dimKey, Optional.empty());
            }
        }

        this.alterations.forEach((alter) -> alter.livingTick(this));
    }

    @Override
    public ActionResultType mobInteract(PlayerEntity player, Hand hand) {

        ItemStack stack = player.getItemInHand(hand);

        if (this.isTame()) {
            if (stack.getItem() == Items.STICK && this.canInteract(player)) {

                if (this.level.isClientSide) {
                    DogInfoScreen.open(this);
                }

                return ActionResultType.SUCCESS;
            }
        } else { // Not tamed
            if (stack.getItem() == Items.BONE || stack.getItem() == DoggyItems.TRAINING_TREAT.get()) {

                if (!this.level.isClientSide) {
                    this.usePlayerItem(player, stack);

                    if (stack.getItem() == DoggyItems.TRAINING_TREAT.get() || this.random.nextInt(3) == 0) {
                        this.tame(player);
                        this.navigation.stop();
                        this.setTarget((LivingEntity) null);
                        this.setOrderedToSit(true);
                        this.setHealth(20.0F);
                        this.level.broadcastEntityEvent(this, doggytalents.common.lib.Constants.EntityState.WOLF_HEARTS);
                    } else {
                        this.level.broadcastEntityEvent(this, doggytalents.common.lib.Constants.EntityState.WOLF_SMOKE);
                    }
                }

                return ActionResultType.SUCCESS;
            }
        }

        Optional<IDogFoodHandler> foodHandler = FoodHandler.getMatch(this, stack, player);

        if (foodHandler.isPresent()) {
            return foodHandler.get().consume(this, stack, player);
        }

        ActionResultType interactResult = InteractHandler.getMatch(this, stack, player, hand);

        if (interactResult != ActionResultType.PASS) {
            return interactResult;
        }

        for (IDogAlteration alter : this.alterations) {
            ActionResultType result = alter.processInteract(this, this.level, player, hand);
            if (result != ActionResultType.PASS) {
                return result;
            }
        }

        ActionResultType actionresulttype = super.mobInteract(player, hand);
        if ((!actionresulttype.consumesAction() || this.isBaby()) && this.canInteract(player)) {
            this.setOrderedToSit(!this.isOrderedToSit());
            this.jumping = false;
            this.navigation.stop();
            this.setTarget(null);
            return ActionResultType.SUCCESS;
        }

        return actionresulttype;
    }

    @Override
    public boolean canBeRiddenInWater(Entity rider) {
        for (IDogAlteration alter : this.alterations) {
            ActionResultType result = alter.canBeRiddenInWater(this, rider);

            if (result.shouldSwing()) {
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

            if (result.shouldSwing()) {
                return true;
            } else if (result == ActionResultType.FAIL) {
                return false;
            }
        }

        return super.canTrample(state, pos, fallDistance);
    }

    @Override
    public boolean causeFallDamage(float distance, float damageMultiplier) {
        for (IDogAlteration alter : this.alterations) {
            ActionResultType result = alter.onLivingFall(this, distance, damageMultiplier);

            if (result.shouldSwing()) {
                return true;
            } else if (result == ActionResultType.FAIL) {
                return false;
            }
        }

        return super.causeFallDamage(distance, damageMultiplier);
    }

    // TODO
    @Override
    public int getMaxFallDistance() {
        return super.getMaxFallDistance();
    }

    @Override
    protected int calculateFallDamage(float distance, float damageMultiplier) {
        EffectInstance effectInst = this.getEffect(Effects.JUMP);
        float f = effectInst == null ? 0.0F : effectInst.getAmplifier() + 1;
        distance -= f;

        for (IDogAlteration alter : this.alterations) {
            ActionResult<Float> result = alter.calculateFallDistance(this, distance);

            if (result.getResult().shouldSwing()) {
                distance = result.getObject();
                break;
            }
        }

        return MathHelper.ceil((distance - 3.0F - f) * damageMultiplier);
    }

    @Override
    public boolean canBreatheUnderwater() {
        for (IDogAlteration alter : this.alterations) {
            ActionResultType result = alter.canBreatheUnderwater(this);

            if (result.shouldSwing()) {
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

            if (result.getResult().shouldSwing()) {
                return result.getObject();
            }
        }

        return super.decreaseAirSupply(air);
    }

    @Override
    protected int increaseAirSupply(int currentAir) {
        currentAir += 4;
        for (IDogAlteration alter : this.alterations) {
            ActionResult<Integer> result = alter.determineNextAir(this, currentAir);

            if (result.getResult().shouldSwing()) {
                currentAir = result.getObject();
                break;
            }
        }

        return Math.min(currentAir, this.getMaxAirSupply());
    }

    @Override
    public boolean canAttack(LivingEntity target) {
        if (this.isMode(EnumMode.DOCILE)) {
            return false;
        }

        for (IDogAlteration alter : this.alterations) {
            ActionResultType result = alter.canAttack(this, target);

            if (result.shouldSwing()) {
                return true;
            } else if (result == ActionResultType.FAIL) {
                return false;
            }
        }

        return super.canAttack(target);
    }

    @Override
    public boolean canAttackType(EntityType<?> entityType) {
        if (this.isMode(EnumMode.DOCILE)) {
            return false;
        }

        for (IDogAlteration alter : this.alterations) {
            ActionResultType result = alter.canAttack(this, entityType);

            if (result.shouldSwing()) {
                return true;
            } else if (result == ActionResultType.FAIL) {
                return false;
            }
        }

        return super.canAttackType(entityType);
    }

    @Override
    public boolean wantsToAttack(LivingEntity target, LivingEntity owner) {
        if (this.isMode(EnumMode.DOCILE)) {
            return false;
        }

        //TODO make wolves not able to attack dogs
        for (IDogAlteration alter : this.alterations) {
            ActionResultType result = alter.shouldAttackEntity(this, target, owner);

            if (result.shouldSwing()) {
                return true;
            } else if (result == ActionResultType.FAIL) {
                return false;
            }
        }

        if (target instanceof CreeperEntity || target instanceof GhastEntity) {
            return false;
        }

        if (target instanceof WolfEntity) {
            WolfEntity wolfentity = (WolfEntity)target;
            return !wolfentity.isTame() || wolfentity.getOwner() != owner;
        } else if (target instanceof DogEntity) {
            DogEntity dogEntity = (DogEntity)target;
            return !dogEntity.isTame() || dogEntity.getOwner() != owner;
         } else if (target instanceof PlayerEntity && owner instanceof PlayerEntity && !((PlayerEntity)owner).canHarmPlayer((PlayerEntity)target)) {
             return false;
        } else if (target instanceof AbstractHorseEntity && ((AbstractHorseEntity)target).isTamed()) {
            return false;
        } else {
            return !(target instanceof TameableEntity) || !((TameableEntity)target).isTame();
        }
    }

    // TODO
    //@Override
//    public boolean canAttack(LivingEntity livingentityIn, EntityPredicate predicateIn) {
//        return predicateIn.canTarget(this, livingentityIn);
//     }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        for (IDogAlteration alter : this.alterations) {
            ActionResult<Float> result = alter.attackEntityFrom(this, source, amount);

            // TODO
            if (result.getResult() == ActionResultType.FAIL) {
                return false;
            } else {
                amount = result.getObject();
            }
        }

        if (this.isInvulnerableTo(source)) {
            return false;
        } else {
            Entity entity = source.getEntity();
            // Must be checked here too as hitByEntity only applies to when the dog is
            // directly hit not indirect damage like sweeping effect etc
            if (entity instanceof PlayerEntity && !this.canPlayersAttack()) {
                return false;
            }

            this.setOrderedToSit(false);

            if (entity != null && !(entity instanceof PlayerEntity) && !(entity instanceof AbstractArrowEntity)) {
                amount = (amount + 1.0F) / 2.0F;
            }

            return super.hurt(source, amount);
        }
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        for (IDogAlteration alter : this.alterations) {
            ActionResultType result = alter.attackEntityAsMob(this, target);

            if (result.shouldSwing()) {
                return true;
            } else if (result == ActionResultType.FAIL) {
                return false;
            }
        }

        ModifiableAttributeInstance attackDamageInst = this.getAttribute(Attributes.ATTACK_DAMAGE);

        Set<AttributeModifier> critModifiers = null;

        if (this.getAttribute(DoggyAttributes.CRIT_CHANCE.get()).getValue() > this.getRandom().nextDouble()) {
            critModifiers = this.getAttribute(DoggyAttributes.CRIT_BONUS.get()).getModifiers();
            critModifiers.forEach(attackDamageInst::addTransientModifier);
        }

        int damage = ((int) attackDamageInst.getValue());
        if (critModifiers != null) {
            critModifiers.forEach(attackDamageInst::removeModifier);
        }

        boolean flag = target.hurt(DamageSource.mobAttack(this), damage);
        if (flag) {
            this.doEnchantDamageEffects(this, target);
            this.statsTracker.increaseDamageDealt(damage);

            if (critModifiers != null) {
                // TODO Might want to make into a packet
                DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> Minecraft.getInstance().particleEngine.createTrackingEmitter(target, ParticleTypes.CRIT));
            }
        }

        return flag;
    }

    @Override
    public void awardKillScore(Entity killed, int scoreValue, DamageSource damageSource) {
        super.awardKillScore(killed, scoreValue, damageSource);
        this.statsTracker.incrementKillCount(killed);
    }

    @Override
    public boolean isDamageSourceBlocked(DamageSource source) {
        for (IDogAlteration alter : this.alterations) {
            ActionResultType result = alter.canBlockDamageSource(this, source);

            if (result.shouldSwing()) {
                return true;
            } else if (result == ActionResultType.FAIL) {
                return false;
            }
        }

        return super.isDamageSourceBlocked(source);
    }

    @Override
    public void setSecondsOnFire(int second) {
        for (IDogAlteration alter : this.alterations) {
            ActionResult<Integer> result = alter.setFire(this, second);

            if (result.getResult().shouldSwing()) {
                second = result.getObject();
            }
        }

        super.setSecondsOnFire(second);
    }

    @Override
    public boolean fireImmune() {
        for (IDogAlteration alter : this.alterations) {
            ActionResultType result = alter.isImmuneToFire(this);

            if (result.shouldSwing()) {
                return true;
            } else if (result == ActionResultType.FAIL) {
                return false;
            }
        }

        return super.fireImmune();
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        for (IDogAlteration alter : this.alterations) {
            ActionResultType result = alter.isInvulnerableTo(this, source);

            if (result.shouldSwing()) {
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

            if (result.shouldSwing()) {
                return true;
            } else if (result == ActionResultType.FAIL) {
                return false;
            }
        }

        return super.isInvulnerable();
    }

    @Override
    public boolean canBeAffected(EffectInstance effectIn) {
        for (IDogAlteration alter : this.alterations) {
            ActionResultType result = alter.isPotionApplicable(this, effectIn);

            if (result.shouldSwing()) {
                return true;
            } else if (result == ActionResultType.FAIL) {
                return false;
            }
        }

        return super.canBeAffected(effectIn);
    }

    @Override
    public void setUUID(UUID uniqueIdIn) {

        // If the UUID is changed remove old one and add new one
        UUID oldUniqueId = this.getUUID();

        if (uniqueIdIn.equals(oldUniqueId)) {
            return; // No change do nothing
        }

        super.setUUID(uniqueIdIn);

        if (this.level != null && !this.level.isClientSide) {
            DogLocationStorage.get(this.level).remove(oldUniqueId);
            DogLocationStorage.get(this.level).getOrCreateData(this).update(this);
        }
    }

    @Override
    public void tame(PlayerEntity player) {
        super.tame(player);
        // When tamed by player cache their display name
        this.setOwnersName(player.getName());
    }

    @Override
    public void setTame(boolean tamed) {
        super.setTame(tamed);
        if (tamed) {
           this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(20.0D);
           this.setHealth(20.0F);
        } else {
           this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(8.0D);
        }

        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(4.0D);
     }

    @Override
    public void setOwnerUUID(@Nullable UUID uuid) {
        super.setOwnerUUID(uuid);

        if (uuid == null) {
            this.setOwnersName((ITextComponent) null);
        }
    }

    @Override // blockAttackFromPlayer
    public boolean skipAttackInteraction(Entity entityIn) {
        if (entityIn instanceof PlayerEntity && !this.canPlayersAttack()) {
            return true;
        }

        for (IDogAlteration alter : this.alterations) {
            ActionResultType result = alter.hitByEntity(this, entityIn);

            if (result.shouldSwing()) {
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
    public boolean isFood(ItemStack stack) {
        return stack.getItem().is(DoggyTags.BREEDING_ITEMS);
    }

    @Override
    public boolean canMate(AnimalEntity otherAnimal) {
        if (otherAnimal == this) {
            return false;
        } else if (!this.isTame()) {
            return false;
        } else if (!(otherAnimal instanceof DogEntity)) {
            return false;
        } else {
            DogEntity entitydog = (DogEntity) otherAnimal;
            if (!entitydog.isTame()) {
                return false;
            } else if (entitydog.isInSittingPose()) {
                return false;
            } else if (ConfigValues.DOG_GENDER && !this.getGender().canMateWith(entitydog.getGender())) {
                return false;
            } else {
                return this.isInLove() && entitydog.isInLove();
            }
        }
    }

    @Override
    public AgeableEntity getBreedOffspring(ServerWorld worldIn, AgeableEntity partner) {
        DogEntity child = DoggyEntityTypes.DOG.get().create(worldIn);
        UUID uuid = this.getOwnerUUID();

        if (uuid != null) {
            child.setOwnerUUID(uuid);
            child.setTame(true);
        }

        if (partner instanceof DogEntity && ConfigValues.PUPS_GET_PARENT_LEVELS) {
            child.setLevel(this.getLevel().combine(((DogEntity) partner).getLevel()));
        }

        return child;
    }

    @Override
    public boolean shouldShowName() {
        return (ConfigValues.ALWAYS_SHOW_DOG_NAME && this.hasCustomName()) || super.shouldShowName();
    }

    @Override
    public float getScale() {
        if (this.isBaby()) {
            return 0.5F;
        } else {
            return this.getDogSize() * 0.3F + 0.1F;
        }
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        // Any mod that tries to access capabilities from entity size/entity
        // creation event will crash here because of the order java inits the
        // classes fields and so will not have been initialised and are
        // accessed during the classes super() call.
        // Since this.alterations will be empty anyway as we have not read
        // NBT data at this point just avoid silent error
        // DoggyTalents#295, DoggyTalents#296
        if (this.alterations == null) {
            return super.getCapability(cap, side);
        }

        for (IDogAlteration alter : this.alterations) {
            LazyOptional<T> result = alter.getCapability(this, cap, side);

            if (result != null) {
                return result;
            }
        }

        return super.getCapability(cap, side);
    }

    @Override
    public Entity changeDimension(ServerWorld worldIn, ITeleporter teleporter) {
        Entity transportedEntity = super.changeDimension(worldIn, teleporter);
        if (transportedEntity instanceof DogEntity) {
            DogLocationStorage.get(this.level).getOrCreateData(this).update((DogEntity) transportedEntity);
        }
        return transportedEntity;
    }

    @Override
    public void onAddedToWorld() {
        super.onAddedToWorld(); // When the entity is added to tracking list
        if (this.level != null && !this.level.isClientSide) {
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
    protected void tickDeath() {
        if (this.deathTime == 19) { // 1 second after death
            if (this.level != null && !this.level.isClientSide) {
//                DogRespawnStorage.get(this.world).putData(this);
//                DoggyTalents.LOGGER.debug("Saved dog as they died {}", this);
//
//                DogLocationStorage.get(this.world).remove(this);
//                DoggyTalents.LOGGER.debug("Removed dog location as they were removed from the world {}", this);
            }
        }

        super.tickDeath();
    }

    private void startShaking() {
        this.isShaking = true;
        this.timeWolfIsShaking = 0.0F;
        this.prevTimeWolfIsShaking = 0.0F;
    }

    private void finishShaking() {
        this.isShaking = false;
        this.timeWolfIsShaking = 0.0F;
        this.prevTimeWolfIsShaking = 0.0F;
    }

    @Override
    public void die(DamageSource cause) {
        this.wetSource = null;
        this.finishShaking();

        this.alterations.forEach((alter) -> alter.onDeath(this, cause));
        super.die(cause);

        // Save inventory after onDeath is called so that pack puppy inventory
        // can be dropped and not saved
        if (this.level != null && !this.level.isClientSide) {
            DogRespawnStorage.get(this.level).putData(this);
            DogLocationStorage.get(this.level).remove(this);
        }
    }

    @Override
    public void dropEquipment() {
        super.dropEquipment();

        this.alterations.forEach((alter) -> alter.dropInventory(this));
    }

    /**
     * When the entity is removed
     */
    @Override
    public void remove(boolean keepData) {
        super.remove(keepData);
    }

    @Override
    protected void invalidateCaps() {
        super.invalidateCaps();
        this.alterations.forEach((alter) -> alter.invalidateCapabilities(this));
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT compound) {
        super.addAdditionalSaveData(compound);

        ListNBT talentList = new ListNBT();
        List<TalentInstance> talents = this.getTalentMap();

        for (int i = 0; i < talents.size(); i++) {
            CompoundNBT talentTag = new CompoundNBT();
            talents.get(i).writeInstance(this, talentTag);
            talentList.add(talentTag);
        }

        compound.put("talents", talentList);

        ListNBT accessoryList = new ListNBT();
        List<AccessoryInstance> accessories = this.getAccessories();

        for (int i = 0; i < accessories.size(); i++) {
            CompoundNBT accessoryTag = new CompoundNBT();
            accessories.get(i).writeInstance(accessoryTag);
            accessoryList.add(accessoryTag);
        }

        compound.put("accessories", accessoryList);

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

        DimensionDependantArg<Optional<BlockPos>> bedsData = this.entityData.get(DOG_BED_LOCATION.get());

        if (!bedsData.isEmpty()) {
            ListNBT bedsList = new ListNBT();

            for (Entry<RegistryKey<World>, Optional<BlockPos>> entry : bedsData.entrySet()) {
                CompoundNBT bedNBT = new CompoundNBT();
                NBTUtil.putResourceLocation(bedNBT, "dim", entry.getKey().location());
                NBTUtil.putBlockPos(bedNBT, "pos", entry.getValue());
                bedsList.add(bedNBT);
            }

            compound.put("beds", bedsList);
        }

        DimensionDependantArg<Optional<BlockPos>> bowlsData = this.entityData.get(DOG_BOWL_LOCATION.get());

        if (!bowlsData.isEmpty()) {
            ListNBT bowlsList = new ListNBT();

            for (Entry<RegistryKey<World>, Optional<BlockPos>> entry : bowlsData.entrySet()) {
                CompoundNBT bowlsNBT = new CompoundNBT();
                NBTUtil.putResourceLocation(bowlsNBT, "dim", entry.getKey().location());
                NBTUtil.putBlockPos(bowlsNBT, "pos", entry.getValue());
                bowlsList.add(bowlsNBT);
            }

            compound.put("bowls", bowlsList);
        }

        this.statsTracker.writeAdditional(compound);

        this.alterations.forEach((alter) -> alter.onWrite(this, compound));
    }

    @Override
    public void load(CompoundNBT compound) {

        // DataFix uuid entries and attribute ids
        try {
            if (NBTUtil.hasOldUniqueId(compound, "UUID")) {
                UUID entityUUID = NBTUtil.getOldUniqueId(compound, "UUID");

                compound.putUUID("UUID", entityUUID);
                NBTUtil.removeOldUniqueId(compound, "UUID");
            }

            if (compound.contains("OwnerUUID", Constants.NBT.TAG_STRING)) {
                UUID ownerUUID = UUID.fromString(compound.getString("OwnerUUID"));

                compound.putUUID("Owner", ownerUUID);
                compound.remove("OwnerUUID");
            } else if (compound.contains("Owner", Constants.NBT.TAG_STRING)) {
                UUID ownerUUID = PreYggdrasilConverter.convertMobOwnerIfNecessary(this.getServer(), compound.getString("Owner"));

                compound.putUUID("Owner", ownerUUID);
            }

            if (NBTUtil.hasOldUniqueId(compound, "LoveCause")) {
                UUID entityUUID = NBTUtil.getOldUniqueId(compound, "LoveCause");

                compound.putUUID("LoveCause", entityUUID);
                NBTUtil.removeOldUniqueId(compound, "LoveCause");
            }
        } catch (Exception e) {
            DoggyTalents2.LOGGER.error("Failed to data fix UUIDs: " + e.getMessage());
        }

        try {
            if (compound.contains("Attributes", Constants.NBT.TAG_LIST)) {
                ListNBT attributeList = compound.getList("Attributes", Constants.NBT.TAG_COMPOUND);
                for (int i = 0; i < attributeList.size(); i++) {
                    CompoundNBT attributeData = attributeList.getCompound(i);
                    String namePrev = attributeData.getString("Name");
                    Object name = namePrev;

                    switch (namePrev) {
                    case "forge.swimSpeed": name = ForgeMod.SWIM_SPEED; break;
                    case "forge.nameTagDistance": name = ForgeMod.NAMETAG_DISTANCE; break;
                    case "forge.entity_gravity": name = ForgeMod.ENTITY_GRAVITY; break;
                    case "forge.reachDistance": name = ForgeMod.REACH_DISTANCE; break;
                    case "generic.maxHealth": name = Attributes.MAX_HEALTH; break;
                    case "generic.knockbackResistance": name = Attributes.KNOCKBACK_RESISTANCE; break;
                    case "generic.movementSpeed": name = Attributes.MOVEMENT_SPEED; break;
                    case "generic.armor": name = Attributes.ARMOR; break;
                    case "generic.armorToughness": name = Attributes.ARMOR_TOUGHNESS; break;
                    case "generic.followRange": name = Attributes.FOLLOW_RANGE; break;
                    case "generic.attackKnockback": name = Attributes.ATTACK_KNOCKBACK; break;
                    case "generic.attackDamage": name = Attributes.ATTACK_DAMAGE; break;
                    case "generic.jumpStrength": name = DoggyAttributes.JUMP_POWER; break;
                    case "generic.critChance": name = DoggyAttributes.CRIT_CHANCE; break;
                    case "generic.critBonus": name = DoggyAttributes.CRIT_BONUS; break;
                    }

                    ResourceLocation attributeRL = Util.getRegistryId(name);

                    if (attributeRL != null && ForgeRegistries.ATTRIBUTES.containsKey(attributeRL)) {
                        attributeData.putString("Name", attributeRL.toString());
                        ListNBT modifierList = attributeData.getList("Modifiers", Constants.NBT.TAG_COMPOUND);
                        for (int j = 0; j < modifierList.size(); j++) {
                            CompoundNBT modifierData = modifierList.getCompound(j);
                            if (NBTUtil.hasOldUniqueId(modifierData, "UUID")) {
                                UUID entityUUID = NBTUtil.getOldUniqueId(modifierData, "UUID");

                                modifierData.putUUID("UUID", entityUUID);
                                NBTUtil.removeOldUniqueId(modifierData, "UUID");
                            }
                        }
                    } else {
                        DoggyTalents2.LOGGER.warn("Failed to data fix '" + namePrev + "'");
                    }
                }
            }
        } catch (Exception e) {
            DoggyTalents2.LOGGER.error("Failed to data fix attribute IDs: " + e.getMessage());
        }

        super.load(compound);
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT compound) {
        super.readAdditionalSaveData(compound);

        List<TalentInstance> talentMap = this.getTalentMap();
        talentMap.clear();

        if (compound.contains("talents", Constants.NBT.TAG_LIST)) {
            ListNBT talentList = compound.getList("talents", Constants.NBT.TAG_COMPOUND);

            for (int i = 0; i < talentList.size(); i++) {
                // Add directly so that nothing is lost, if number allowed on changes
                TalentInstance.readInstance(this, talentList.getCompound(i)).ifPresent(talentMap::add);
            }
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

        try {
            for (IDogAlteration inst : this.alterations) {
                inst.init(this);
            }
        } catch (Exception e) {
            DoggyTalents2.LOGGER.error("Failed to init alteration: " + e.getMessage());
            e.printStackTrace();
		}

        try {
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
        } catch (Exception e) {
            DoggyTalents2.LOGGER.error("Failed to load levels: " + e.getMessage());
            e.printStackTrace();
        }

        try {
            if (compound.contains("level_normal", Constants.NBT.TAG_ANY_NUMERIC)) {
                this.getLevel().setLevel(Type.NORMAL, compound.getInt("level_normal"));
                this.markDataParameterDirty(DOG_LEVEL.get());
            }

            if (compound.contains("level_dire", Constants.NBT.TAG_ANY_NUMERIC)) {
                this.getLevel().setLevel(Type.DIRE, compound.getInt("level_dire"));
                this.markDataParameterDirty(DOG_LEVEL.get());
            }
        } catch (Exception e) {
            DoggyTalents2.LOGGER.error("Failed to load levels: " + e.getMessage());
            e.printStackTrace();
        }

        DimensionDependantArg<Optional<BlockPos>> bedsData = this.entityData.get(DOG_BED_LOCATION.get()).copyEmpty();

        try {
            if (compound.contains("beds", Constants.NBT.TAG_LIST)) {
                ListNBT bedsList = compound.getList("beds", Constants.NBT.TAG_COMPOUND);

                for (int i = 0; i < bedsList.size(); i++) {
                    CompoundNBT bedNBT = bedsList.getCompound(i);
                    ResourceLocation loc = NBTUtil.getResourceLocation(bedNBT, "dim");
                    RegistryKey<World> type = RegistryKey.create(Registry.DIMENSION_REGISTRY, loc);
                    Optional<BlockPos> pos = NBTUtil.getBlockPos(bedNBT, "pos");
                    bedsData.put(type, pos);
                }
            } else {
                BackwardsComp.readBedLocations(compound, bedsData);
            }
        } catch (Exception e) {
            DoggyTalents2.LOGGER.error("Failed to load beds: " + e.getMessage());
            e.printStackTrace();
        }

        this.entityData.set(DOG_BED_LOCATION.get(), bedsData);

        DimensionDependantArg<Optional<BlockPos>> bowlsData = this.entityData.get(DOG_BOWL_LOCATION.get()).copyEmpty();

        try {
            if (compound.contains("bowls", Constants.NBT.TAG_LIST)) {
                ListNBT bowlsList = compound.getList("bowls", Constants.NBT.TAG_COMPOUND);

                for (int i = 0; i < bowlsList.size(); i++) {
                    CompoundNBT bowlsNBT = bowlsList.getCompound(i);
                    ResourceLocation loc = NBTUtil.getResourceLocation(bowlsNBT, "dim");
                    RegistryKey<World> type = RegistryKey.create(Registry.DIMENSION_REGISTRY, loc);
                    Optional<BlockPos> pos = NBTUtil.getBlockPos(bowlsNBT, "pos");
                    bowlsData.put(type, pos);
                }
            } else {
                BackwardsComp.readBowlLocations(compound, bowlsData);
            }
        } catch (Exception e) {
            DoggyTalents2.LOGGER.error("Failed to load bowls: " + e.getMessage());
            e.printStackTrace();
        }

        this.entityData.set(DOG_BOWL_LOCATION.get(), bowlsData);

        try {
            this.statsTracker.readAdditional(compound);
        } catch (Exception e) {
            DoggyTalents2.LOGGER.error("Failed to load stats tracker: " + e.getMessage());
            e.printStackTrace();
        }
        this.alterations.forEach((alter) -> {
            try {
                alter.onRead(this, compound);
            } catch (Exception e) {
                DoggyTalents2.LOGGER.error("Failed to load alteration: " + e.getMessage());
                e.printStackTrace();
            }
        });

    }

    @Override
    public void onSyncedDataUpdated(DataParameter<?> key) {
        super.onSyncedDataUpdated(key);
        if (TALENTS.get().equals(key) || ACCESSORIES.get().equals(key)) {
            this.recalculateAlterationsCache();

            for (IDogAlteration inst : this.alterations) {
                inst.init(this);
            }
        }

        if (TALENTS.get().equals(key)) {
            this.spendablePoints.markForRefresh();
        }

        if (DOG_LEVEL.get().equals(key)) {
            this.spendablePoints.markForRefresh();
        }

        if (ACCESSORIES.get().equals(key)) {
            // If client sort accessories
            if (this.level.isClientSide) {
                // Does not recall this notifyDataManagerChange as list object is
                // still the same, maybe in future MC versions this will change so need to watch out
                this.getAccessories().sort(AccessoryInstance.RENDER_SORTER);
            }
        }

        if (SIZE.equals(key)) {
            this.refreshDimensions();
        }
    }

    public void recalculateAlterationsCache() {
        this.alterations.clear();
        this.foodHandlers.clear();

        for (AccessoryInstance inst : this.getAccessories()) {
            if (inst instanceof IDogAlteration) {
                this.alterations.add((IDogAlteration) inst);
            }

            if (inst instanceof IDogFoodHandler) {
                this.foodHandlers.add((IDogFoodHandler) inst);
            }
        };

        List<TalentInstance> talents = this.getTalentMap();
        this.alterations.addAll(talents);
        for (TalentInstance inst : talents) {
            if (inst instanceof IDogFoodHandler) {
                this.foodHandlers.add((IDogFoodHandler) inst);
            }
        }
    }

    /**
     * If the entity can make changes to the dog
     * @param livingEntity The entity
     */
    @Override
    public boolean canInteract(LivingEntity livingEntity) {
        return this.willObeyOthers() || this.isOwnedBy(livingEntity);
    }

    @Override
    public List<AccessoryInstance> getAccessories() {
        return this.entityData.get(ACCESSORIES.get());
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
        List<AccessoryInstance> removed = new ArrayList<>(this.getAccessories());

        for (AccessoryInstance inst : removed) {
            if (inst instanceof IDogAlteration) {
                ((IDogAlteration) inst).remove(this);
            }
        }

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
        return this.entityData.get(LAST_KNOWN_NAME);
    }

    public void setOwnersName(@Nullable ITextComponent comp) {
        this.setOwnersName(Optional.ofNullable(comp));
    }

    public void setOwnersName(Optional<ITextComponent> collar) {
        this.entityData.set(LAST_KNOWN_NAME, collar);
    }

    public EnumGender getGender() {
        return this.entityData.get(GENDER.get());
    }

    public void setGender(EnumGender collar) {
        this.entityData.set(GENDER.get(), collar);
    }

    @Override
    public EnumMode getMode() {
        return this.entityData.get(MODE.get());
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
        this.entityData.set(MODE.get(), collar);
    }

    public Optional<BlockPos> getBedPos() {
        return this.getBedPos(this.level.dimension());
    }

    public Optional<BlockPos> getBedPos(RegistryKey<World> registryKey) {
        return this.entityData.get(DOG_BED_LOCATION.get()).getOrDefault(registryKey, Optional.empty());
    }

    public void setBedPos(@Nullable BlockPos pos) {
        this.setBedPos(this.level.dimension(), pos);
    }

    public void setBedPos(RegistryKey<World> registryKey, @Nullable BlockPos pos) {
        this.setBedPos(registryKey, WorldUtil.toImmutable(pos));
    }

    public void setBedPos(RegistryKey<World> registryKey, Optional<BlockPos> pos) {
        this.entityData.set(DOG_BED_LOCATION.get(), this.entityData.get(DOG_BED_LOCATION.get()).copy().set(registryKey, pos));
    }

    public Optional<BlockPos> getBowlPos() {
        return this.getBowlPos(this.level.dimension());
    }

    public Optional<BlockPos> getBowlPos(RegistryKey<World> registryKey) {
        return this.entityData.get(DOG_BOWL_LOCATION.get()).getOrDefault(registryKey, Optional.empty());
    }

    public void setBowlPos(@Nullable BlockPos pos) {
        this.setBowlPos(this.level.dimension(), pos);
    }

    public void setBowlPos(RegistryKey<World> registryKey, @Nullable BlockPos pos) {
        this.setBowlPos(registryKey, WorldUtil.toImmutable(pos));
    }

    public void setBowlPos(RegistryKey<World> registryKey, Optional<BlockPos> pos) {
        this.entityData.set(DOG_BOWL_LOCATION.get(), this.entityData.get(DOG_BOWL_LOCATION.get()).copy().set(registryKey, pos));
    }

    @Override
    public float getMaxHunger() {
        float maxHunger = ConfigValues.DEFAULT_MAX_HUNGER;

        for (IDogAlteration alter : this.alterations) {
            ActionResult<Float> result = alter.getMaxHunger(this, maxHunger);

            if (result.getResult().shouldSwing()) {
                maxHunger = result.getObject();
            }
        }

        return maxHunger;
    }

    @Override
    public float getDogHunger() {
        return this.entityData.get(HUNGER_INT);
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

            if (result.getResult().shouldSwing()) {
                hunger = result.getObject();
                diff = hunger - this.getDogHunger();
            }
        }

        this.setHungerDirectly(MathHelper.clamp(hunger, 0, this.getMaxHunger()));
    }

    private void setHungerDirectly(float hunger) {
        this.entityData.set(HUNGER_INT, hunger);
    }

    public boolean hasCustomSkin() {
        return !Strings.isNullOrEmpty(this.getSkinHash());
    }

    public String getSkinHash() {
        return this.entityData.get(CUSTOM_SKIN);
    }

    public void setSkinHash(String hash) {
        if (hash == null) {
            hash = "";
        }
        this.entityData.set(CUSTOM_SKIN, hash);
    }

    @Override
    public DogLevel getLevel() {
        return this.entityData.get(DOG_LEVEL.get());
    }

    public void setLevel(DogLevel level) {
        this.entityData.set(DOG_LEVEL.get(), level);
    }

    @Override
    public void increaseLevel(DogLevel.Type typeIn) {
        this.getLevel().incrementLevel(typeIn);
        this.markDataParameterDirty(DOG_LEVEL.get());
    }

    @Override
    public void setDogSize(int value) {
        this.entityData.set(SIZE, (byte)Math.min(5, Math.max(1, value)));
    }

    @Override
    public int getDogSize() {
        return this.entityData.get(SIZE);
    }

    public void setBoneVariant(ItemStack stack) {
        this.entityData.set(BONE_VARIANT, stack);
    }

    public ItemStack getBoneVariant() {
        return this.entityData.get(BONE_VARIANT);
    }

    @Nullable
    public IThrowableItem getThrowableItem() {
        Item item = this.entityData.get(BONE_VARIANT).getItem();
        return item instanceof IThrowableItem ? (IThrowableItem) item : null;
    }

    public boolean hasBone() {
        return !this.getBoneVariant().isEmpty();
    }

    private boolean getDogFlag(int bit) {
        return (this.entityData.get(DOG_FLAGS) & bit) != 0;
    }

    private void setDogFlag(int bits, boolean flag) {
        byte c = this.entityData.get(DOG_FLAGS);
        this.entityData.set(DOG_FLAGS, (byte)(flag ? c | bits : c & ~bits));
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

    public List<TalentInstance> getTalentMap() {
        return this.entityData.get(TALENTS.get());
    }

    public void setTalentMap(List<TalentInstance> map) {
        this.entityData.set(TALENTS.get(), map);
    }

    public ActionResultType setTalentLevel(Talent talent, int level) {
        if (0 > level || level > talent.getMaxLevel()) {
            return ActionResultType.FAIL;
        }

        List<TalentInstance> activeTalents = this.getTalentMap();

        TalentInstance inst = null;
        for (TalentInstance activeInst : activeTalents) {
            if (activeInst.of(talent)) {
                inst = activeInst;
                break;
            }
        }

        if (inst == null) {
            if (level == 0) {
                return ActionResultType.PASS;
            }

            inst = talent.getDefault(level);
            activeTalents.add(inst);
            inst.init(this);
        } else {
            int previousLevel = inst.level();
            if (previousLevel == level) {
                return ActionResultType.PASS;
            }

            inst.setLevel(level);
            inst.set(this, previousLevel);

            if (level == 0) {
                activeTalents.remove(inst);
            }
        }

        this.markDataParameterDirty(TALENTS.get());
        return ActionResultType.SUCCESS;
    }


    public <T> void markDataParameterDirty(DataParameter<T> key) {
        this.markDataParameterDirty(key, true);
    }

    public <T> void markDataParameterDirty(DataParameter<T> key, boolean notify) {
        if (notify) {
            this.onSyncedDataUpdated(key);
        }

        // Force the entry to update
        DataEntry<T> dataentry = this.entityData.getItem(key);
        dataentry.setDirty(true);
        this.entityData.isDirty = true;
    }

    @Override
    public void markAccessoriesDirty() {
        this.markDataParameterDirty(ACCESSORIES.get());
    }

    @Override
    public Optional<TalentInstance> getTalent(Talent talentIn) {
        List<TalentInstance> activeTalents = this.getTalentMap();

        for (TalentInstance activeInst : activeTalents) {
            if (activeInst.of(talentIn)) {
                return Optional.of(activeInst);
            }
        }

        return Optional.empty();
    }

    @Override
    public int getLevel(Talent talentIn) {
        return this.getTalent(talentIn).map(TalentInstance::level).orElse(0);
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
        this.setTame(false);
        this.navigation.stop();
        this.setOrderedToSit(false);
        this.setHealth(8);

        this.getTalentMap().clear();
        this.markDataParameterDirty(TALENTS.get());

        this.setOwnerUUID(null);
        this.setWillObeyOthers(false);
        this.setMode(EnumMode.DOCILE);
    }

    public boolean canSpendPoints(int amount) {
        return this.getSpendablePoints() >= amount || this.getAccessory(DoggyAccessories.GOLDEN_COLLAR.get()).isPresent();
    }

    // When this method is changed the cache may need to be updated at certain points
    private final int getSpendablePointsInternal() {
        int totalPoints = 15 + this.getLevel().getLevel(Type.NORMAL) + this.getLevel().getLevel(Type.DIRE);
        for (TalentInstance entry : this.getTalentMap()) {
            totalPoints -= entry.getTalent().getCummulativeCost(entry.level());
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
    public boolean canBeControlledByRider() {
        return this.getControllingPassenger() instanceof LivingEntity;
    }

    //TODO
    @Override
    public boolean isPickable() {
        return super.isPickable();
    }

    @Override
    public boolean isPushable() {
        return !this.isVehicle() && super.isPushable();
    }

    @Override
    public boolean isControlledByLocalInstance() {
        // Super calls canBeSteered so controlling passenger can be guaranteed to be LivingEntity
        return super.isControlledByLocalInstance() && this.canInteract((LivingEntity) this.getControllingPassenger());
    }

    public boolean isDogJumping() {
        return this.dogJumping;
    }

    public void setDogJumping(boolean jumping) {
        this.dogJumping = jumping;
    }

//    public double getDogJumpStrength() {
//        float verticalVelocity = 0.42F + 0.06F * this.TALENTS.getLevel(ModTalents.WOLF_MOUNT);
//        if (this.TALENTS.getLevel(ModTalents.WOLF_MOUNT) == 5) verticalVelocity += 0.04F;
//        return verticalVelocity;
//    }

    // 0 - 100 input
    public void setJumpPower(int jumpPowerIn) {
       // if (this.TALENTS.getLevel(ModTalents.WOLF_MOUNT) > 0) {
            this.jumpPower = 1.0F;
       // }
    }

    public boolean canJump() {
        return true;
        //TODO return this.TALENTS.getLevel(ModTalents.WOLF_MOUNT) > 0;
    }

    @Override
    public void travel(Vector3d positionIn) {
        if (this.isAlive()) {
            if (this.isVehicle() && this.canBeControlledByRider()) {
                LivingEntity livingentity = (LivingEntity) this.getControllingPassenger();

                // Face the dog in the direction of the controlling passenger
                this.yRot = livingentity.yRot;
                this.yRotO = this.yRot;
                this.xRot = livingentity.xRot * 0.5F;
                this.setRot(this.yRot, this.xRot);
                this.yBodyRot = this.yRot;
                this.yHeadRot = this.yBodyRot;

                this.maxUpStep = 1.0F;

                float straf = livingentity.xxa * 0.7F;
                float foward = livingentity.zza;

                // If moving backwards half the speed
                if (foward <= 0.0F) {
                   foward *= 0.5F;
                }

                if (this.jumpPower > 0.0F && !this.isDogJumping() && this.isOnGround()) {

                    // Calculate jump value based of jump strength, power this jump and jump boosts
                    double jumpValue = this.getAttribute(DoggyAttributes.JUMP_POWER.get()).getValue() * this.getBlockJumpFactor() * this.jumpPower; //TODO do we want getJumpFactor?
                    if (this.hasEffect(Effects.JUMP)) {
                        jumpValue += (this.getEffect(Effects.JUMP).getAmplifier() + 1) * 0.1F;
                    }

                    // Apply jump
                    Vector3d vec3d = this.getDeltaMovement();
                    this.setDeltaMovement(vec3d.x, jumpValue, vec3d.z);
                    this.setDogJumping(true);
                    this.hasImpulse = true;

                    // If moving forward, propel further in the direction
                    if (foward > 0.0F) {
                        final float amount = 0.4F; // TODO Allow people to change this value
                        float compX = MathHelper.sin(this.yRot * ((float)Math.PI / 180F));
                        float compZ = MathHelper.cos(this.yRot * ((float)Math.PI / 180F));
                        this.setDeltaMovement(this.getDeltaMovement().add(-amount * compX * this.jumpPower, 0.0D, amount * compZ * this.jumpPower));
                        //this.playJumpSound();
                    }

                    // Mark as unable jump until reset
                    this.jumpPower = 0.0F;
                }

                this.flyingSpeed = this.getSpeed() * 0.1F;
                if (this.isControlledByLocalInstance()) {
                    // Set the move speed and move the dog in the direction of the controlling entity
                    this.setSpeed((float)this.getAttribute(Attributes.MOVEMENT_SPEED).getValue() * 0.5F);
                    super.travel(new Vector3d(straf, positionIn.y, foward));
                    this.lerpSteps = 0;
                } else if (livingentity instanceof PlayerEntity) {
                    // A player is riding and can not control then
                    this.setDeltaMovement(Vector3d.ZERO);
                }

                // Once the entity reaches the ground again allow it to jump again
                if (this.isOnGround()) {
                    this.jumpPower = 0.0F;
                    this.setDogJumping(false);
                }

                //
                this.animationSpeedOld = this.animationSpeed;
                double changeX = this.getX() - this.xo;
                double changeY = this.getZ() - this.zo;
                float f4 = MathHelper.sqrt(changeX * changeX + changeY * changeY) * 4.0F;

                if (f4 > 1.0F) {
                   f4 = 1.0F;
                }

                this.animationSpeed += (f4 - this.animationSpeed) * 0.4F;
                this.animationPosition += this.animationSpeed;
             } else {
                 this.maxUpStep = 0.5F; // Default
                 this.flyingSpeed = 0.02F; // Default
                 super.travel(positionIn);
             }

            this.addMovementStat(this.getX() - this.xo, this.getY() - this.yo, this.getZ() - this.zo);
        }
    }

    public void addMovementStat(double xD, double yD, double zD) {
        if (this.isVehicle()) {
            int j = Math.round(MathHelper.sqrt(xD * xD + zD * zD) * 100.0F);
            this.statsTracker.increaseDistanceRidden(j);
        }
        if (!this.isPassenger()) {
            if (this.isEyeInFluid(FluidTags.WATER)) {
                int j = Math.round(MathHelper.sqrt(xD * xD + yD * yD + zD * zD) * 100.0F);
                if (j > 0) {
                    this.statsTracker.increaseDistanceOnWater(j);
                }
            } else if (this.isInWater()) {
                int k = Math.round(MathHelper.sqrt(xD * xD + zD * zD) * 100.0F);
                if (k > 0) {
                    this.statsTracker.increaseDistanceInWater(k);
                }
            } else if (this.isOnGround()) {
                int l = Math.round(MathHelper.sqrt(xD * xD + zD * zD) * 100.0F);
                if (l > 0) {
                    if (this.isSprinting()) {
                        this.statsTracker.increaseDistanceSprint(l);
                    } else if (this.isCrouching()) {
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

        Block blockBelow = this.level.getBlockState(this.blockPosition().below()).getBlock();
        boolean onBed = blockBelow == DoggyBlocks.DOG_BED.get() || BlockTags.BEDS.contains(blockBelow);
        if (onBed) {
            return true;
        }

        return false;
    }

    @Override
    public List<IDogFoodHandler> getFoodHandlers() {
        return this.foodHandlers;
    }

    public void setTargetBlock(BlockPos pos) {
        this.targetBlock = pos;
    }

    public BlockPos getTargetBlock() {
        return this.targetBlock;
    }

    public void teleportToOwner() {
        EntityUtil.tryToTeleportNearEntity(this, this.getNavigation(), this.getOwner(), 4);
    }
}
