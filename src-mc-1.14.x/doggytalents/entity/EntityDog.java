package doggytalents.entity;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import javax.annotation.Nullable;

import doggytalents.DoggyTalentsMod;
import doggytalents.ModBlocks;
import doggytalents.ModItems;
import doggytalents.ModSerializers;
import doggytalents.ModTags;
import doggytalents.ModTalents;
import doggytalents.api.DoggyTalentsAPI;
import doggytalents.api.inferface.IDogInteractItem;
import doggytalents.api.inferface.Talent;
import doggytalents.entity.ai.DogLocationManager;
import doggytalents.entity.ai.EntityAIBegDog;
import doggytalents.entity.ai.EntityAIDogFeed;
import doggytalents.entity.ai.EntityAIDogWander;
import doggytalents.entity.ai.EntityAIExtinguishFire;
import doggytalents.entity.ai.EntityAIFetch;
import doggytalents.entity.ai.EntityAIFetchReturn;
import doggytalents.entity.ai.EntityAIFollowOwnerDog;
import doggytalents.entity.ai.EntityAIHurtByTargetDog;
import doggytalents.entity.ai.EntityAIOwnerHurtByTargetDog;
import doggytalents.entity.ai.EntityAIOwnerHurtTargetDog;
import doggytalents.entity.ai.EntityAIShepherdDog;
import doggytalents.entity.ai.EntityAIBerserkerMode;
import doggytalents.entity.features.CoordFeature;
import doggytalents.entity.features.DogFeature;
import doggytalents.entity.features.DogGenderFeature;
import doggytalents.entity.features.LevelFeature;
import doggytalents.entity.features.ModeFeature;
import doggytalents.entity.features.ModeFeature.EnumMode;
import doggytalents.entity.features.TalentFeature;
import doggytalents.helper.DogUtil;
import doggytalents.helper.TalentHelper;
import doggytalents.inventory.InventoryTreatBag;
import doggytalents.item.ItemChewStick;
import doggytalents.item.ItemFancyCollar;
import doggytalents.lib.Constants;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.goal.BreedGoal;
import net.minecraft.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.SitGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.GhastEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.ServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.storage.loot.LootTables;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class EntityDog extends TameableEntity implements INamedContainerProvider {
	
	private static final DataParameter<Float> 					DATA_HEALTH_ID 	= EntityDataManager.createKey(EntityDog.class, DataSerializers.FLOAT);
	private static final DataParameter<Boolean> 				BEGGING 		= EntityDataManager.createKey(EntityDog.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Byte> 					DOG_TEXTURE 	= EntityDataManager.createKey(EntityDog.class, DataSerializers.BYTE);
	private static final DataParameter<Integer>					COLLAR_COLOUR 	= EntityDataManager.createKey(EntityDog.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> 				LEVEL 			= EntityDataManager.createKey(EntityDog.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> 				LEVEL_DIRE 		= EntityDataManager.createKey(EntityDog.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> 				MODE_PARAM 		= EntityDataManager.createKey(EntityDog.class, DataSerializers.VARINT);
	private static final DataParameter<Map<Talent, Integer>>	TALENTS_PARAM 	= EntityDataManager.createKey(EntityDog.class, ModSerializers.TALENT_LEVEL_SERIALIZER);
	private static final DataParameter<Integer> 				HUNGER 			= EntityDataManager.createKey(EntityDog.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> 				BONE 			= EntityDataManager.createKey(EntityDog.class, DataSerializers.VARINT);
	private static final DataParameter<Boolean> 				FRIENDLY_FIRE 	= EntityDataManager.createKey(EntityDog.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> 				OBEY_OTHERS 	= EntityDataManager.createKey(EntityDog.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Integer> 				CAPE 			= EntityDataManager.createKey(EntityDog.class, DataSerializers.VARINT);
	private static final DataParameter<Boolean> 				SUNGLASSES 		= EntityDataManager.createKey(EntityDog.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> 				RADAR_COLLAR 	= EntityDataManager.createKey(EntityDog.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Optional<BlockPos>> 		BOWL_POS 		= EntityDataManager.createKey(EntityDog.class, DataSerializers.OPTIONAL_BLOCK_POS);
	private static final DataParameter<Optional<BlockPos>> 		BED_POS 		= EntityDataManager.createKey(EntityDog.class, DataSerializers.OPTIONAL_BLOCK_POS);
	private static final DataParameter<Integer> 				SIZE 			= EntityDataManager.createKey(EntityDog.class, DataSerializers.VARINT);
	private static final DataParameter<String> 					GENDER_PARAM 	= EntityDataManager.createKey(EntityDog.class, DataSerializers.STRING);
	private static final DataParameter<Optional<ITextComponent>> LAST_KNOWN_NAME = EntityDataManager.createKey(EntityDog.class, DataSerializers.OPTIONAL_TEXT_COMPONENT);
	
	@Nullable
	public DogLocationManager locationManager;
	
	public TalentFeature TALENTS;
	public LevelFeature LEVELS;
	public ModeFeature MODE;
	public CoordFeature COORDS;
	public DogGenderFeature GENDER;
	public DogStats STATS;
	private List<DogFeature> FEATURES;
	
	public Map<String, Object> objects;
	
	private float headRotationCourse;
	private float headRotationCourseOld;
	public boolean isWet;
	private boolean isShaking;
	private float timeWolfIsShaking;
	private float prevTimeWolfIsShaking;
	
	//Timers
    private float timeWolfIsHappy;
    private float prevTimeWolfIsHappy;
    private boolean isWolfHappy;
    public boolean hiyaMaster;
    private int hungerTick;
    private int prevHungerTick;
    private int healingTick;
    private int prevHealingTick;
    private int regenerationTick;
    private int prevRegenerationTick;
    private int foodBowlCheck;
    private int reversionTime;
    
    private int generalTick;
    
	public EntityDog(EntityType<EntityDog> type, World worldIn) {
		super(type, worldIn);
		this.TALENTS = new TalentFeature(this);
		this.LEVELS = new LevelFeature(this);
		this.MODE = new ModeFeature(this);
		this.COORDS = new CoordFeature(this);
		this.GENDER = new DogGenderFeature(this);
		this.STATS = new DogStats(this);
		this.FEATURES = Arrays.asList(TALENTS, LEVELS, MODE, COORDS, STATS);
		if(worldIn instanceof ServerWorld)
			this.locationManager = DogLocationManager.getHandler((ServerWorld)this.getEntityWorld());
		this.objects = new HashMap<String, Object>();
		this.setTamed(false);
		
		TalentHelper.onClassCreation(this);
	}
	
	@Override
	protected void registerGoals() {
		this.field_70911_d = new SitGoal(this);
		this.goalSelector.addGoal(1, new SwimGoal(this));
		this.goalSelector.addGoal(1, new EntityAIExtinguishFire(this, 1.15D, 16));
		this.goalSelector.addGoal(2, this.field_70911_d);
		this.goalSelector.addGoal(3, new EntityAIFetchReturn(this, 1.0D));
		this.goalSelector.addGoal(4, new EntityAIDogWander(this, 1.0D));
		 //TODO this.tasks.addGoal(4, new EntityAIPatrolArea(this));
		this.goalSelector.addGoal(5, new LeapAtTargetGoal(this, 0.4F));
		this.goalSelector.addGoal(6, new MeleeAttackGoal(this, 1.0D, true));
		this.goalSelector.addGoal(7, new EntityAIShepherdDog(this, 1.0D, 8F, entity -> !(entity instanceof EntityDog)));
		this.goalSelector.addGoal(8, new EntityAIFetch(this, 1.0D, 32));
		this.goalSelector.addGoal(10, new EntityAIFollowOwnerDog(this, 1.0D, 10.0F, 2.0F));
		this.goalSelector.addGoal(11, new BreedGoal(this, 1.0D));
		this.goalSelector.addGoal(13, new EntityAIBegDog(this, 8.0F));
		this.goalSelector.addGoal(14, new EntityAIDogFeed(this, 1.0D, 20.0F));
		this.goalSelector.addGoal(15, new LookAtGoal(this, PlayerEntity.class, 8.0F));
		this.goalSelector.addGoal(15, new LookRandomlyGoal(this));
		
		this.targetSelector.addGoal(1, new EntityAIOwnerHurtByTargetDog(this));
		this.targetSelector.addGoal(2, new EntityAIOwnerHurtTargetDog(this));
		this.targetSelector.addGoal(3, new EntityAIHurtByTargetDog(this).func_220794_a());
		this.targetSelector.addGoal(4, new EntityAIBerserkerMode<>(this, MobEntity.class, false));
	}
	
	@Override
	protected void registerData() {
		super.registerData();
		this.dataManager.register(DATA_HEALTH_ID, this.getHealth());
		this.dataManager.register(BEGGING, Boolean.valueOf(false));
		this.dataManager.register(DOG_TEXTURE, (byte)0);
        this.dataManager.register(COLLAR_COLOUR, -2);
        this.dataManager.register(TALENTS_PARAM, Collections.emptyMap());
        this.dataManager.register(HUNGER, Integer.valueOf(60));
        this.dataManager.register(OBEY_OTHERS, Boolean.valueOf(false));
        this.dataManager.register(FRIENDLY_FIRE, Boolean.valueOf(false));
        this.dataManager.register(BONE, -1);
        this.dataManager.register(RADAR_COLLAR, Boolean.valueOf(false));
        this.dataManager.register(MODE_PARAM, Integer.valueOf(0));
        this.dataManager.register(LEVEL, Integer.valueOf(0));
        this.dataManager.register(LEVEL_DIRE, Integer.valueOf(0));
        this.dataManager.register(BOWL_POS, Optional.empty());
        this.dataManager.register(BED_POS, Optional.empty());
        this.dataManager.register(CAPE, -2);
        this.dataManager.register(SUNGLASSES, false);
        this.dataManager.register(SIZE, Integer.valueOf(3));
        this.dataManager.register(GENDER_PARAM, this.getRandom().nextInt(2) == 0 ? "male" : "female");
        this.dataManager.register(LAST_KNOWN_NAME, Optional.empty());  
	}
	
	@Override
	public void notifyDataManagerChange(DataParameter<?> key) {
		super.notifyDataManagerChange(key);
		
	}
	
	@Override
	public float func_213355_cm() {
		if(this.isChild()) {
			return 0.5F;
		} else {
			switch(this.getDogSize()) { 
				case 1:
		            return 0.5F;
		        case 2:
		        	return 0.7F;
		        case 3:
		        	return 1.0F;
		        case 4:
		        	return 1.3F;
		        case 5:
		        	return 1.6F;
		        default:
		        	return 1.0F;
			}
		}
	}
	
	@Override
	protected void updateAITasks() {
		super.updateAITasks();
		this.dataManager.set(DATA_HEALTH_ID, this.getHealth());
	}
	
	@Override
	protected void registerAttributes() {
		super.registerAttributes();
		this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue((double)0.3F);
		this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(this.isTamed() ? 20.0D : 8.0D);
		this.getAttributes().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(2.0D);
	}
	
	@Override
    protected SoundEvent getAmbientSound() {
        if(this.getDogHunger() <= Constants.LOW_HUNGER && Constants.DOG_WHINE_WHEN_HUNGER_LOW) {
            return SoundEvents.ENTITY_WOLF_WHINE;
        } else if(this.rand.nextInt(3) == 0) {
            return this.isTamed() && this.dataManager.get(DATA_HEALTH_ID) < this.getMaxHealth() / 2 ? SoundEvents.ENTITY_WOLF_WHINE : SoundEvents.ENTITY_WOLF_PANT;
        } else {
            return SoundEvents.ENTITY_WOLF_AMBIENT;
        }
    }
	
	@Override
	protected void playStepSound(BlockPos pos, BlockState blockIn) {
		this.playSound(SoundEvents.ENTITY_WOLF_STEP, 0.15F, 1.0F);
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
	
	@Override
	public void writeAdditional(CompoundNBT compound) {
		super.writeAdditional(compound);
		this.FEATURES.stream().forEach(f -> f.writeAdditional(compound));

        compound.putInt("doggyTex", this.getTameSkin());
        compound.putInt("collarColour", this.getCollarData());
        compound.putInt("dogHunger", this.getDogHunger());
        compound.putBoolean("willObey", this.willObeyOthers());
        compound.putBoolean("friendlyFire", this.canFriendlyFire());
        compound.putBoolean("radioCollar", this.hasRadarCollar());
        compound.putBoolean("sunglasses", this.hasSunglasses());
        compound.putInt("capeData", this.getCapeData());
        compound.putInt("dogSize", this.getDogSize());
        if(this.getGender().equals("male") || this.getGender().equals("female")) compound.putString("dogGender", this.getGender());
        compound.putBoolean("hasBone", this.hasBone());
        if(this.hasBone()) compound.putInt("boneVariant", this.getBoneVariant());
        if(this.dataManager.get(LAST_KNOWN_NAME).isPresent()) compound.putString("lastKnownOwnerName", ITextComponent.Serializer.toJson(this.dataManager.get(LAST_KNOWN_NAME).get()));
        
        TalentHelper.writeAdditional(this, compound);
	}

	@Override
	public void readAdditional(CompoundNBT compound) {
		super.readAdditional(compound);
		this.FEATURES.stream().forEach(f -> f.readAdditional(compound));
		
        this.setTameSkin(compound.getInt("doggyTex"));
        if (compound.contains("collarColour", 99)) this.setCollarData(compound.getInt("collarColour"));
        this.setDogHunger(compound.getInt("dogHunger"));
        this.setWillObeyOthers(compound.getBoolean("willObey"));
        this.setFriendlyFire(compound.getBoolean("friendlyFire"));
        this.hasRadarCollar(compound.getBoolean("radioCollar"));
        this.setHasSunglasses(compound.getBoolean("sunglasses"));
        if(compound.contains("capeData", 99)) this.setCapeData(compound.getInt("capeData"));
        if(compound.contains("dogSize", 99)) this.setDogSize(compound.getInt("dogSize"));
        
        if(compound.contains("dogGender", 8)) this.setGender(compound.getString("dogGender"));
        else if(Constants.DOG_GENDER) this.setGender(this.rand.nextInt(2) == 0 ? "male" : "female");
        if(compound.getBoolean("hasBone")) this.setBoneVariant(compound.getInt("boneVariant"));
        if(compound.contains("lastKnownOwnerName", 8)) this.dataManager.set(LAST_KNOWN_NAME, Optional.of(ITextComponent.Serializer.fromJson(compound.getString("lastKnownOwnerName"))));
        
        TalentHelper.readAdditional(this, compound);
        
        //Backwards Compatibility
        if (compound.contains("dogName"))
            this.setCustomName(new StringTextComponent(compound.getString("dogName")));
	}
	
	@Override
    public void livingTick() {
        super.livingTick();
        
        if(!this.world.isRemote && this.isWet && !this.isShaking && !this.hasPath() && this.onGround) {
			this.isShaking = true;
			this.timeWolfIsShaking = 0.0F;
			this.prevTimeWolfIsShaking = 0.0F;
			this.world.setEntityState(this, (byte)8);
		}
        
        if (Constants.IS_HUNGER_ON) {
            this.prevHungerTick = this.hungerTick;

            if (!this.isBeingRidden() && !this.isSitting() /** && !this.mode.isMode(EnumMode.WANDERING) && !this.level.isDireDog() || worldObj.getWorldInfo().getWorldTime() % 2L == 0L **/)
                this.hungerTick += 1;

            this.hungerTick += TalentHelper.hungerTick(this, this.hungerTick - this.prevHungerTick);

            if (this.hungerTick > 400) {
                this.setDogHunger(this.getDogHunger() - 1);
                this.hungerTick -= 400;
            }
        }

        if (Constants.DOGS_IMMORTAL) {
            this.prevRegenerationTick = this.regenerationTick;

            if (this.isSitting()) {
                this.regenerationTick += 1;
                this.regenerationTick += TalentHelper.regenerationTick(this, this.regenerationTick - this.prevRegenerationTick);
            } else if (!this.isSitting())
                this.regenerationTick = 0;

            if (this.regenerationTick >= 2400 && this.isIncapacicated()) {
                this.setHealth(2);
                this.setDogHunger(1);
            } else if (this.regenerationTick >= 2400 && !this.isIncapacicated()) {
                if (this.regenerationTick >= 4400 && this.getDogHunger() < 60) {
                    this.setDogHunger(this.getDogHunger() + 1);
                    this.world.setEntityState(this, (byte) 7);
                    this.regenerationTick = 2400;
                }
            }
        }

        if (this.getHealth() != Constants.LOW_HEATH_LEVEL) {
            this.prevHealingTick = this.healingTick;
            this.healingTick += this.nourishment();

            if (this.healingTick >= 6000) {
                if (this.getHealth() < this.getMaxHealth())
                    this.setHealth(this.getHealth() + 1);

                this.healingTick = 0;
            }
        }

        if(this.getHealth() <= 0 && this.isImmortal()) {
            this.deathTime = 0;
            this.setHealth(1);
        }

        if(this.world.isRemote && this.LEVELS.isDireDog() && Constants.DIRE_PARTICLES) {
            for (int i = 0; i < 2; i++) {
            	double width = this.getSize(this.getPose()).width;
            	double height = this.getSize(this.getPose()).height;
                this.world.addParticle(ParticleTypes.PORTAL, this.posX + (this.rand.nextDouble() - 0.5D) * (double) width, (this.posY + rand.nextDouble() * (double) height) - 0.25D, posZ + (rand.nextDouble() - 0.5D) * (double) width, (this.rand.nextDouble() - 0.5D) * 2D, -this.rand.nextDouble(), (this.rand.nextDouble() - 0.5D) * 2D);
            }
        }
        
        if (this.reversionTime > 0)
            this.reversionTime -= 1;

        //Remove dog from players head if sneaking
        Entity entityRidden = this.getRidingEntity();

        if (entityRidden instanceof PlayerEntity)
            if (entityRidden.isSneaking())
                this.stopRiding();

        //Check if dog bowl still exists every 50t/2.5s, if not remove
        if(this.foodBowlCheck++ > 50 && this.COORDS.hasBowlPos()) {
            if(this.world.isBlockLoaded(this.COORDS.getBowlPos()))
                if(this.world.getBlockState(this.COORDS.getBowlPos()).getBlock() != ModBlocks.FOOD_BOWL)
                    this.COORDS.resetBowlPosition();

            this.foodBowlCheck = 0;
        }
        
        TalentHelper.livingTick(this);
    }

    @Override
    public void tick() {
        super.tick();

        this.headRotationCourseOld = this.headRotationCourse;
		if (this.isBegging()) {
			this.headRotationCourse += (1.0F - this.headRotationCourse) * 0.4F;
		} 
		else {
			this.headRotationCourse += (0.0F - this.headRotationCourse) * 0.4F;
		}

		if(this.isInWaterRainOrBubbleColumn()) {
			this.isWet = true;
			this.isShaking = false;
			this.timeWolfIsShaking = 0.0F;
			this.prevTimeWolfIsShaking = 0.0F;
		} else if((this.isWet || this.isShaking) && this.isShaking) {
			if(this.timeWolfIsShaking == 0.0F) {
				this.playSound(SoundEvents.ENTITY_WOLF_SHAKE, this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
			}

			this.prevTimeWolfIsShaking = this.timeWolfIsShaking;
			this.timeWolfIsShaking += 0.05F;
			if (this.prevTimeWolfIsShaking >= 2.0F) {
				this.isWet = false;
				this.isShaking = false;
				this.prevTimeWolfIsShaking = 0.0F;
				this.timeWolfIsShaking = 0.0F;
				
				this.onFinishShaking();
			}

			if(this.timeWolfIsShaking > 0.4F) {
				float f = (float)this.getBoundingBox().minY;
				int i = (int)(MathHelper.sin((this.timeWolfIsShaking - 0.4F) * (float)Math.PI) * 7.0F);
				Vec3d vec3d = this.getMotion();
				
				for(int j = 0; j < i; ++j) {
					float f1 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.getWidth() * 0.5F;
					float f2 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.getWidth() * 0.5F;
					this.world.addParticle(ParticleTypes.SPLASH, this.posX + (double)f1, (double)(f + 0.8F), this.posZ + (double)f2, vec3d.x, vec3d.y, vec3d.z);
				}
			}
		}
        
        if(this.rand.nextInt(200) == 0)
            this.hiyaMaster = true;

        if(((this.isBegging()) || (this.hiyaMaster)) && (!this.isWolfHappy)) {
            this.isWolfHappy = true;
            this.timeWolfIsHappy = 0.0F;
            this.prevTimeWolfIsHappy = 0.0F;
        } else
            this.hiyaMaster = false;

        if(this.isWolfHappy) {
            if(this.timeWolfIsHappy % 1.0F == 0.0F)
                this.playSound(SoundEvents.ENTITY_WOLF_PANT, this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
            this.prevTimeWolfIsHappy = this.timeWolfIsHappy;
            this.timeWolfIsHappy += 0.05F;
            if (this.prevTimeWolfIsHappy >= 8.0F) {
                this.isWolfHappy = false;
                this.prevTimeWolfIsHappy = 0.0F;
                this.timeWolfIsHappy = 0.0F;
            }
        }
        
        if(this.isTamed()) {
            PlayerEntity player = (PlayerEntity) this.getOwner();

            if(player != null) {
                float distanceToOwner = player.getDistance(this);

                if(distanceToOwner <= 2F && this.hasBone()) {
                    if(!this.world.isRemote) {
                        ItemStack fetchItem = ItemStack.EMPTY;

                        switch (this.getBoneVariant()) {
                            case 0:
                                fetchItem = new ItemStack(ModItems.THROW_BONE_WET);
                                break;
                            case 1:
                                fetchItem = new ItemStack(ModItems.THROW_STICK_WET);
                                break;
                        }

                        this.entityDropItem(fetchItem, 0.0F);
                    }

                    this.setNoFetchItem();
                }
            }
        }
        
        this.generalTick--;
        if(this.generalTick < 0) {
        	if(!this.world.isRemote) {
		        if(this.isAlive()) { //Prevent the data from being added when the entity dies
		            this.locationManager.update(this);
		        } else {
		            this.locationManager.remove(this);
		        }
		        
	        	if(this.getOwner() != null)
	        		this.dataManager.set(LAST_KNOWN_NAME, Optional.ofNullable(this.getOwner().getDisplayName()));
        	}
        	
	        this.generalTick = 40;
        }
        
        TalentHelper.tick(this);
    }
	
	@Override
    public boolean processInteract(PlayerEntity player, Hand hand) {
        ItemStack stack = player.getHeldItem(hand);
        
        ActionResult<ItemStack> result = TalentHelper.interactWithPlayer(this, player, stack);
        switch(result.getType()) {
        case SUCCESS:
        	return true;
        case FAIL:
        	return false;
        case PASS:
			break;
        }
        
        if(stack.getItem() == ModItems.OWNER_CHANGE && player.abilities.isCreativeMode && !this.isOwner(player)) {
        	if(!this.world.isRemote) {
	        	this.setTamed(true);
	            this.navigator.clearPath();
	            this.setAttackTarget((LivingEntity) null);
	            this.field_70911_d.setSitting(true);
	            this.setOwnerId(player.getUniqueID());
	            this.playTameEffect(true);
	            this.world.setEntityState(this, (byte) 7);
        	}
        	return true;
        }
        
        if(this.isTamed()) {
            if(!stack.isEmpty()) {
                int foodValue = this.foodValue(stack);
                
                if(foodValue != 0 && this.getDogHunger() < Constants.HUNGER_POINTS && this.canInteract(player) && !this.isIncapacicated()) {
                    if (!player.abilities.isCreativeMode)
                        stack.shrink(1);

                    this.setDogHunger(this.getDogHunger() + foodValue);
                    if (stack.getItem() == ModItems.CHEW_STICK)
                        ((ItemChewStick)ModItems.CHEW_STICK).addChewStickEffects(this);

                    return true;
                }
                else if(stack.getItem() == ModItems.DOGGY_CHARM && player.abilities.isCreativeMode) {
                	if(!this.world.isRemote) {
                		EntityDog babySpawn = this.createChild(this);
                        if(babySpawn != null) {
                           babySpawn.setGrowingAge(-Constants.TIME_TO_MATURE);
                           babySpawn.setTamed(true);
                           if(Constants.PUPS_GET_PARENT_LEVELS) {
                               babySpawn.LEVELS.setLevel(Math.min(this.LEVELS.getLevel(), 20));
                           }
                           
                           babySpawn.setLocationAndAngles(this.posX, this.posY, this.posZ, 0.0F, 0.0F);
                           this.world.addEntity(babySpawn);

                           if(!player.abilities.isCreativeMode) {
                        	   stack.shrink(1);
                           }
                        }
                     }

                	return true;
                }
            	/*else if(stack.getItem() == Items.BONE && this.canInteract(player)) {
            		this.startRiding(player);
            		
            		if(this.field_70911_d != null)
            			this.field_70911_d.setSitting(true);
            		
                    return true;
                }*/
                //TODO else if(stack.getItem() == Items.BIRCH_DOOR && this.canInteract(player)) {
                //	this.patrolOutline.add(this.getPosition());
                //}
                //else if(stack.getItem() == Items.OAK_DOOR && this.canInteract(player)) {
                //	this.patrolOutline.clear();
                //}
                else if(stack.getItem() == Items.STICK && this.canInteract(player)) {
                	
                	if(this.isIncapacicated()) {
                		if(!this.world.isRemote)
                			player.sendMessage(new TranslationTextComponent("dog.mode.incapacitated.help", this.getDisplayName()));
                	} else {
                		if(this.world.isRemote) {
                			DoggyTalentsMod.PROXY.openDoggyInfo(this);
                		}
                		
                		//TODO
	                	//if(player instanceof ServerPlayerEntity && !(player instanceof FakePlayer)) {
	        	        //	ServerPlayerEntity entityPlayerMP = (ServerPlayerEntity) player;
	        	
	        	        //	NetworkHooks.openGui(entityPlayerMP, this, buf -> buf.writeInt(this.getEntityId()));
	        	        //}
                	}
                	
                    return true;
                } else if(stack.getItem() == ModItems.RADIO_COLLAR && this.canInteract(player) && !this.hasRadarCollar() && !this.isIncapacicated()) {
                    this.hasRadarCollar(true);

                    if(!player.abilities.isCreativeMode)
                        stack.shrink(1);
                    return true;
                } else if(stack.getItem() == ModItems.WOOL_COLLAR && this.canInteract(player) && !this.hasCollar() && !this.isIncapacicated()) {
                    int colour = -1;

                    if(stack.hasTag() && stack.getTag().contains("collar_colour", 99))
                        colour = stack.getTag().getInt("collar_colour");

                    this.setCollarData(colour);

                    if(!player.abilities.isCreativeMode)
                        stack.shrink(1);
                    return true;
                } else if(stack.getItem() instanceof ItemFancyCollar && this.canInteract(player) && !this.hasCollar() && !this.isIncapacicated()) {
                    this.setCollarData(-3 - ((ItemFancyCollar)stack.getItem()).type.ordinal());

                    if(!player.abilities.isCreativeMode)
                        stack.shrink(1);
                    return true;
                } else if(stack.getItem() == ModItems.CAPE && this.canInteract(player) && !this.hasCape() && !this.isIncapacicated()) {
                    this.setFancyCape();
                    if(!player.abilities.isCreativeMode)
                        stack.shrink(1);
                    return true;
                } else if(stack.getItem() == ModItems.LEATHER_JACKET && this.canInteract(player) && !this.hasCape() && !this.isIncapacicated()) {
                    this.setLeatherJacket();
                    if(!player.abilities.isCreativeMode)
                        stack.shrink(1);
                    return true;
                } else if(stack.getItem() == ModItems.CAPE_COLOURED && this.canInteract(player) && !this.hasCape() && !this.isIncapacicated()) {
                    int colour = -1;

                    if(stack.hasTag() && stack.getTag().contains("cape_colour", 99))
                        colour = stack.getTag().getInt("cape_colour");

                    this.setCapeData(colour);

                    if(!player.abilities.isCreativeMode)
                        stack.shrink(1);
                    return true;
                } else if(stack.getItem() == ModItems.SUNGLASSES && this.canInteract(player) && !this.hasSunglasses() && !this.isIncapacicated()) {
                    this.setHasSunglasses(true);
                    if(!player.abilities.isCreativeMode)
                        stack.shrink(1);
                    return true;
                } else if(stack.getItem() instanceof IDogInteractItem && this.canInteract(player) && !this.isIncapacicated()) {
                	IDogInteractItem treat = (IDogInteractItem) stack.getItem();
                	ActionResult<ItemStack> treatResult = treat.onItemRightClick(stack, this, this.world, player);
                  
                	switch(treatResult.getType()) {
                    case SUCCESS:
                    	return true;
                    case FAIL:
                    	return false;
                    case PASS:
            			break;
                    }
                	
                } else if(stack.getItem() == ModItems.COLLAR_SHEARS && this.canInteract(player)) {
                    if(!this.world.isRemote) {
                        if(this.hasCollar() || this.hasSunglasses() || this.hasCape()) {
                            this.reversionTime = 40;
                            if(this.hasCollarColoured()) {
                                ItemStack collarDrop = new ItemStack(ModItems.WOOL_COLLAR, 1);
                                if(this.isCollarColoured()) {
                                    collarDrop.setTag(new CompoundNBT());
                                    collarDrop.getTag().putInt("collar_colour", this.getCollarData());
                                }
                                this.entityDropItem(collarDrop, 1);
                                this.setNoCollar();
                            }

                            if(this.hasFancyCollar()) {
                            	Item drop = ModItems.MULTICOLOURED_COLLAR;
                            	if(this.getCollarData() == -3)
                            		drop = ModItems.CREATIVE_COLLAR;
                            	else if(this.getCollarData() == -4)
                            		drop = ModItems.SPOTTED_COLLAR;
                            	
                                this.entityDropItem(drop, 1);
                                this.setNoCollar();
                            }

                            if(this.hasFancyCape()) {
                                this.entityDropItem(new ItemStack(ModItems.CAPE, 1), 1);
                                this.setNoCape();
                            }

                            if(this.hasCapeColoured()) {
                                ItemStack capeDrop = new ItemStack(ModItems.CAPE_COLOURED, 1);
                                if (this.isCapeColoured()) {
                                    capeDrop.setTag(new CompoundNBT());
                                    capeDrop.getTag().putInt("cape_colour", this.getCapeData());
                                }
                                this.entityDropItem(capeDrop, 1);
                                this.setNoCape();
                            }

                            if(this.hasLeatherJacket()) {
                                this.entityDropItem(new ItemStack(ModItems.LEATHER_JACKET, 1), 1);
                                this.setNoCape();
                            }

                            if(this.hasSunglasses()) {
                                this.entityDropItem(new ItemStack(ModItems.SUNGLASSES, 1), 1);
                                this.setHasSunglasses(false);
                            }
                        } else if(this.reversionTime < 1) {
                            this.setTamed(false);
                            this.navigator.clearPath();
                            this.field_70911_d.setSitting(false);
                            this.setHealth(8);
                            this.TALENTS.resetTalents();
                            this.setOwnerId(null);
                            this.dataManager.set(LAST_KNOWN_NAME, Optional.empty());
                            this.setWillObeyOthers(false);
                            this.MODE.setMode(EnumMode.DOCILE);
                            if(this.hasRadarCollar())
                                this.entityDropItem(ModItems.RADIO_COLLAR);
                            this.hasRadarCollar(false);
                            this.reversionTime = 40;
                        }
                    }

                    return true;
                } else if(stack.getItem() == Blocks.CAKE.asItem() && this.canInteract(player) && this.isIncapacicated()) {
                    if (!player.abilities.isCreativeMode)
                        stack.shrink(1);

                    if(!this.world.isRemote) {
                        this.field_70911_d.setSitting(true);
                        this.setHealth(this.getMaxHealth());
                        this.setDogHunger(Constants.HUNGER_POINTS);
                        this.regenerationTick = 0;
                        this.setAttackTarget((LivingEntity) null);
                        this.playTameEffect(true);
                        this.world.setEntityState(this, (byte) 7);
                    }

                    return true;
                } else if(stack.getItem().isIn(net.minecraftforge.common.Tags.Items.DYES) && this.canInteract(player) && this.hasCollarColoured()) { //TODO Add Plants compatibility

                	if(!this.world.isRemote) {
	                    int[] aint = new int[3];
	                    int maxCompSum = 0;
	                    int count = 1; //The number of different sources of colour
	    
	                    DyeColor colour = DyeColor.getColor(stack);
	                    if(colour == null) {
	                    	return false;
	                    }
	                    
	                    float[] afloat = colour.getColorComponentValues();
	                    int l1 = (int)(afloat[0] * 255.0F);
	                    int i2 = (int)(afloat[1] * 255.0F);
	                    int j2 = (int)(afloat[2] * 255.0F);
	                    maxCompSum += Math.max(l1, Math.max(i2, j2));
	                    aint[0] += l1;
	                    aint[1] += i2;
	                    aint[2] += j2;
	                    
	                    if(this.isCollarColoured()) {
	                    	int l = this.getCollarData();
	                    	float f = (float)(l >> 16 & 255) / 255.0F;
	                    	float f1 = (float)(l >> 8 & 255) / 255.0F;
	                    	float f2 = (float)(l & 255) / 255.0F;
	                    	maxCompSum = (int)((float)maxCompSum + Math.max(f, Math.max(f1, f2)) * 255.0F);
	                    	aint[0] = (int) ((float) aint[0] + f * 255.0F);
	                    	aint[1] = (int) ((float) aint[1] + f1 * 255.0F);
	                    	aint[2] = (int) ((float) aint[2] + f2 * 255.0F);
	                    	count++;
	                    }
	                    
	                    
	                    int i1 = aint[0] / count;
	                    int j1 = aint[1] / count;
	                    int k1 = aint[2] / count;
	                    float f3 = (float) maxCompSum / (float) count;
	                    float f4 = (float) Math.max(i1, Math.max(j1, k1));
	                    i1 = (int)((float) i1 * f3 / f4);
	                    j1 = (int)((float) j1 * f3 / f4);
	                    k1 = (int)((float) k1 * f3 / f4);
	                    int k2 = (i1 << 8) + j1;
	                    k2 = (k2 << 8) + k1;
	                    this.setCollarData(k2);
                	}
                	
                    return true;
                } else if(stack.getItem() == ModItems.TREAT_BAG && this.getDogHunger() < Constants.HUNGER_POINTS && this.canInteract(player)) {

                    InventoryTreatBag treatBag = new InventoryTreatBag(player.inventory, player.inventory.currentItem, stack);
                    treatBag.openInventory(player);

                    int slotIndex = DogUtil.getFirstSlotWithFood(this, treatBag);
                    if (slotIndex >= 0)
                        DogUtil.feedDog(this, treatBag, slotIndex);

                    treatBag.closeInventory(player);
                    return true;
                }
            }

            if(!this.isBreedingItem(stack) && this.canInteract(player)) {
                if(!this.world.isRemote) {
                    this.field_70911_d.setSitting(!this.isSitting());
                    this.isJumping = false;
                    this.navigator.clearPath();
                    this.setAttackTarget((LivingEntity) null);
                }
                return true;
            }
        } else if(stack.getItem() == ModItems.COLLAR_SHEARS && this.reversionTime < 1) {
            if(!this.world.isRemote) {
                this.locationManager.remove(this);
                this.remove();
                WolfEntity wolf = EntityType.WOLF.create(this.world);
                wolf.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
                this.world.addEntity(wolf);
            }
            return true;
        } else if(stack.getItem() == Items.BONE || stack.getItem() == ModItems.TRAINING_TREAT) {
        	if(!player.abilities.isCreativeMode)
        		stack.shrink(1);

        	if(!this.world.isRemote) {
                if(stack.getItem() == ModItems.TRAINING_TREAT || this.rand.nextInt(3) == 0) {
                    this.setTamed(true);
                    this.navigator.clearPath();
                    this.setAttackTarget((LivingEntity) null);
                    this.field_70911_d.setSitting(true);
                    this.setHealth(20.0F);
                    this.setOwnerId(player.getUniqueID());
                    this.playTameEffect(true);
                    this.world.setEntityState(this, (byte) 7);
                } else {
                    this.playTameEffect(false);
                    this.world.setEntityState(this, (byte) 6);
                }
            }

            return true;
        }

        return super.processInteract(player, hand);
    }
	
	@Override
    public EntityDog createChild(AgeableEntity entityAgeable) {
        EntityDog entitydog = (EntityDog)this.getType().create(this.world);
        UUID uuid = this.getOwnerId();

        if (uuid != null) {
            entitydog.setOwnerId(uuid);
            entitydog.setTamed(true);
        }

        entitydog.setGrowingAge(-Constants.TIME_TO_MATURE);

        if(Constants.PUPS_GET_PARENT_LEVELS && entityAgeable instanceof EntityDog) {
            int combinedLevel = this.LEVELS.getLevel() + ((EntityDog)entityAgeable).LEVELS.getLevel();
            combinedLevel /= 2;
            combinedLevel = Math.min(combinedLevel, 20);
            entitydog.LEVELS.setLevel(combinedLevel);
        }

        return entitydog;
    }
	
	@Override
	public boolean isBreedingItem(ItemStack stack) {
		return stack.getItem().isIn(ModTags.getTag(ModTags.BREEDING_ITEMS));
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
    public boolean getAlwaysRenderNameTagForRender() {
        return this.hasCustomName();
    }
	
	// Changes visibility to public
	@Override
	public void playTameEffect(boolean successful) {
		super.playTameEffect(successful);
	}
	
	// Talent Hooks
	@Override
    public void fall(float distance, float damageMultiplier) {
        if (!TalentHelper.isImmuneToFalls(this))
            super.fall(distance - TalentHelper.fallProtection(this), damageMultiplier);
    }
	
	@Override
    public boolean attackEntityFrom(DamageSource damageSource, float damage) {
        if (this.isInvulnerableTo(damageSource))
            return false;
        else {
            Entity entity = damageSource.getTrueSource();
            //Friendly fire
            if (!this.canFriendlyFire() && entity instanceof PlayerEntity && (this.willObeyOthers() || this.isOwner((PlayerEntity) entity)))
                return false;

            if (!TalentHelper.attackEntityFrom(this, damageSource, damage))
                return false;

            if (this.field_70911_d != null)
                this.field_70911_d.setSitting(false);

            if (entity != null && !(entity instanceof PlayerEntity) && !(entity instanceof ArrowEntity))
                damage = (damage + 1.0F) / 2.0F;

            return super.attackEntityFrom(damageSource, damage);
        }
    }
	
	@Override
    public boolean attackEntityAsMob(Entity entityIn) {
        if (!TalentHelper.shouldDamageMob(this, entityIn))
            return false;

        int damage = 4 + (MathHelper.floor(this.effectiveLevel()) + 1) / 2;
        damage = TalentHelper.attackEntityAsMob(this, entityIn, damage);

        if (entityIn instanceof ZombieEntity)
            ((ZombieEntity)entityIn).setAttackTarget(this);

        //TODO  (float)((int)this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getValue()
        boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), (float)damage);//(float)((int)this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getValue()));
   		if (flag) {
   			this.applyEnchantments(this, entityIn);
   		}

   		return flag;
    }
	
	@Override
   	public void setTamed(boolean tamed) {
   		super.setTamed(tamed);
   		this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(tamed ? 20.0D : 8.0D);
   		this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.0D);
   	}
	
	@Override
   	@OnlyIn(Dist.CLIENT)
   	public void handleStatusUpdate(byte id) {
		if(id == 8) {
   			this.isShaking = true;
   			this.timeWolfIsShaking = 0.0F;
   			this.prevTimeWolfIsShaking = 0.0F;
   		} else {
   			super.handleStatusUpdate(id);
   		}
   	}
	
	@Override
	public void onDeath(DamageSource cause) {
        if(!this.world.isRemote && this.world.getGameRules().getBoolean("showDeathMessages") && !this.isImmortal() && this.getOwner() instanceof ServerPlayerEntity) {
            DoggyTalentsMod.LOGGER.debug("From onDeath");
            this.locationManager.remove(this);
            this.getOwner().sendMessage(this.getCombatTracker().getDeathMessage());
        }
    }
	
	@Override
    public boolean isPotionApplicable(EffectInstance potioneffectIn) {
        if(this.isIncapacicated())
            return false;

        if(!TalentHelper.isPostionApplicable(this, potioneffectIn))
            return false;

        return super.isPotionApplicable(potioneffectIn);
    }
	
	@Override
    public void setFire(int amount) {
        if(TalentHelper.setFire(this, amount))
            super.setFire(amount);
    }
	
	@Override
    protected int decreaseAirSupply(int air) {
    	int level = this.TALENTS.getLevel(ModTalents.SWIMMER_DOG);
    	return level > 0 && this.rand.nextInt(level + 1) > 0 ? air : super.decreaseAirSupply(air);
    }
	
	@Override
    public boolean canBreatheUnderwater() {
        return TalentHelper.canBreatheUnderwater(this);
    }

    @Override
    protected boolean canTriggerWalking() {
        return TalentHelper.canTriggerWalking(this);
    }
    
    @Override
    public boolean shouldAttackEntity(LivingEntity target, LivingEntity owner) {
        if(TalentHelper.canAttackEntity(this, target))
            return true;

        if (!(target instanceof CreeperEntity) && !(target instanceof GhastEntity)) {
            if (target instanceof EntityDog) {
                EntityDog entitydog = (EntityDog) target;

                if (entitydog.isTamed() && entitydog.getOwner() == owner)
                    return false;
            } else if (target instanceof WolfEntity) {
            	WolfEntity entitywolf = (WolfEntity) target;

                if(entitywolf.isTamed() && entitywolf.getOwner() == owner)
                    return false;
            }

            if (target instanceof PlayerEntity && owner instanceof PlayerEntity && !((PlayerEntity)owner).canAttackPlayer((PlayerEntity)target)) {
            	return false;
            } else if (target instanceof AbstractHorseEntity && ((AbstractHorseEntity)target).isTame()) {
            	return false;
            } else {
            	return !(target instanceof CatEntity) || !((CatEntity)target).isTamed();
            }
        }
        
        return false;
    }


    @Override
    public boolean canAttack(EntityType<?> cls) {
        if(TalentHelper.canAttack(this, cls))
            return true;

        return super.canAttack(cls);
    }
    
    @Override
    public Entity changeDimension(DimensionType dimType) {
    	Entity entity = super.changeDimension(dimType);
    	if(entity instanceof EntityDog) {
    		EntityDog dog = (EntityDog)entity;
    		
    		if(!this.world.isRemote) {
	    		dog.locationManager.update(dog);
	    		this.locationManager.remove(this);
    		}
    	} else if(entity != null) {
    		DoggyTalentsMod.LOGGER.warn("Dog tried to change dimension but now isn't a dog?");
    	}
    	
    	return entity;
    }
    
    @Override
    public void onAddedToWorld() { 
    	super.onAddedToWorld();
    	if(!this.world.isRemote)
    		this.locationManager.update(this);
    }

    @Override
    public void onRemovedFromWorld() {
    	super.onRemovedFromWorld();
    	if(!this.world.isRemote && !this.isAlive())
    		this.locationManager.remove(this);
    }
    
    @Override
    protected float getJumpUpwardsMotion() {
        return 0.42F;
    }
    
    //TODO
    /**
    @Override
    public boolean canDespawn() {
    	return false;
    }**/
    
    @Override
    protected float getWaterSlowDown() {
        return 0.8F;
    }
    
    protected void onFinishShaking() {
        if(!this.world.isRemote) {
            int lvlFisherDog = this.TALENTS.getLevel(ModTalents.FISHER_DOG);
            int lvlHellHound = this.TALENTS.getLevel(ModTalents.HELL_HOUND);

            if (this.rand.nextInt(15) < lvlFisherDog * 2)
                this.entityDropItem(this.rand.nextInt(15) < lvlHellHound * 2 ? Items.COOKED_COD : Items.COD);
        }
    }
    
 	@Override
   	public boolean canMateWith(AnimalEntity otherAnimal) {
   		if(otherAnimal == this) {
   			return false;
   		} else if(!this.isTamed()) {
   			return false;
   		} else if(!(otherAnimal instanceof EntityDog)) {
   			return false;
   		} else {
   			EntityDog entitydog = (EntityDog)otherAnimal;
   			if(!entitydog.isTamed()) {
   				return false;
   			} else if(entitydog.isSitting()) {
   				return false;
   			} else if(this.getGender().equals(entitydog.getGender())) {
   				return false;
   			} else {
   				return this.isInLove() && entitydog.isInLove();
   			}
   		}
   	}
	
 	@OnlyIn(Dist.CLIENT)
   	public boolean isDogWet() {
		return this.isWet;
	}

   	@OnlyIn(Dist.CLIENT)
   	public float getShadingWhileWet(float p_70915_1_) {
		return 0.75F + (this.prevTimeWolfIsShaking + (this.timeWolfIsShaking - this.prevTimeWolfIsShaking) * p_70915_1_) / 2.0F * 0.25F;
   	}

   	@OnlyIn(Dist.CLIENT)
   	public float getShakeAngle(float partialTick, float offset) {
	   float f = (this.prevTimeWolfIsShaking + (this.timeWolfIsShaking - this.prevTimeWolfIsShaking) * partialTick + offset) / 1.8F;
	   if (f < 0.0F) {
		   f = 0.0F;
	   } else if (f > 1.0F) {
		   f = 1.0F;
	   }

	   return MathHelper.sin(f * (float)Math.PI) * MathHelper.sin(f * (float)Math.PI * 11.0F) * 0.15F * (float)Math.PI;
   	}

   	@OnlyIn(Dist.CLIENT)
   	public float getInterestedAngle(float p_70917_1_) {
	   return (this.headRotationCourseOld + (this.headRotationCourse - this.headRotationCourseOld) * p_70917_1_) * 0.15F * (float)Math.PI;
   	}
   	
	public float getWagAngle(float partialTick, float offset) {
        float f = (this.prevTimeWolfIsHappy + (this.timeWolfIsHappy - this.prevTimeWolfIsHappy) * partialTick + offset) / 2.0F;
        if (f < 0.0F) f = 0.0F;
        else if (f > 2.0F) f %= 2.0F;
        return MathHelper.sin(f * (float) Math.PI * 11.0F) * 0.3F * (float) Math.PI;
    }
	
	@OnlyIn(Dist.CLIENT)
   	public float getTailRotation() {
   		return this.isTamed() ? (0.55F - (this.getMaxHealth() - this.dataManager.get(DATA_HEALTH_ID)) / this.getMaxHealth() * 20.0F * 0.02F) * (float)Math.PI : ((float)Math.PI / 5F);
   	}

	//TODO
	/**
   	@Override
   	public float getEyeHeight() {
   		return this.height * 0.8F;
   	}**/

   	@Override
   	public int getVerticalFaceSpeed() {
   		return this.isSitting() ? 20 : super.getVerticalFaceSpeed();
   	}
	
	public boolean isImmortal() {
		return this.isTamed() && Constants.DOGS_IMMORTAL || this.LEVELS.isDireDog();
	}
	
	public boolean isIncapacicated() {
		return this.isImmortal() && this.getHealth() <= Constants.LOW_HEATH_LEVEL;
	}
	
	public double effectiveLevel() {
		return (this.LEVELS.getLevel() + this.LEVELS.getDireLevel()) / 10.0D;
	}
	
	public double getHealthRelative() {
        return getHealth() / (double) getMaxHealth();
    }
	
	public boolean canWander() {
		return this.isTamed() && this.MODE.isMode(EnumMode.WANDERING) && this.COORDS.hasBowlPos() && this.COORDS.getBowlPos().distanceSq(this.getPosition()) < 256.0D;
	}
	
	public boolean canInteract(PlayerEntity player) {
        return this.isOwner(player) || this.willObeyOthers();
    }
	
	public int foodValue(ItemStack stack) {
        if (stack.isEmpty())
            return 0;

        int foodValue = 0;

        Item item = stack.getItem();

        if (stack.getItem() != Items.ROTTEN_FLESH && item.isFood()) {
            if (item.getFood().isMeat())
                foodValue = 40;
        } else if (stack.getItem() == ModItems.CHEW_STICK) {
            return 10;
        }

        foodValue = TalentHelper.changeFoodValue(this, stack, foodValue);

        return foodValue;
    }
	
	public int nourishment() {
        int amount = 0;

        if (this.getDogHunger() > 0) {
            amount = 40 + 4 * (MathHelper.floor(this.effectiveLevel()) + 1);

            if (isSitting() && this.TALENTS.getLevel(ModTalents.QUICK_HEALER) == 5) {
                amount += 20 + 2 * (MathHelper.floor(this.effectiveLevel()) + 1);
            }

            if (!this.isSitting()) {
                amount *= 5 + this.TALENTS.getLevel(ModTalents.QUICK_HEALER);
                amount /= 10;
            }
        }

        return amount;
    }
	
	public void mountTo(LivingEntity entityLiving) {
        entityLiving.rotationYaw = this.rotationYaw;
        entityLiving.rotationPitch = this.rotationPitch;

        if(!this.world.isRemote)
            entityLiving.startRiding(this);
    }
	
    public int points() {
        return this.isCreativeCollar() ? 1000 : this.LEVELS.getLevel() + this.LEVELS.getDireLevel() + (this.LEVELS.isDireDog() ? 15 : 0) + (this.getGrowingAge() < 0 ? 0 : 15);
    }

    public int spendablePoints() {
        return this.points() - this.usedPoints();
    }

    public int usedPoints() {
        return TalentHelper.getUsedPoints(this);
    }

    public int deductive(int id) {
    	if(id >= 1 && id <= 5)
    		return new int[] {1,3,6,10,15}[id - 1];
    	
    	return 0;
    }
	
    public ITextComponent getOwnersName() {
    	if(this.getOwner() != null) {
			return this.getOwner().getDisplayName();
		} else if(this.dataManager.get(LAST_KNOWN_NAME).isPresent()) {
			return this.dataManager.get(LAST_KNOWN_NAME).get();
		} else if(this.getOwnerId() != null) {
			return new TranslationTextComponent("entity.doggytalents.dog.unknown_owner");
		} else {
			return new TranslationTextComponent("entity.doggytalents.dog.untamed");
		}
    }
    
	public void setBegging(boolean flag) {
		this.dataManager.set(BEGGING, flag);
	}
	
	public boolean isBegging() {
		return this.dataManager.get(BEGGING);
	}
    
	public int getTameSkin() {
   	 	return this.dataManager.get(DOG_TEXTURE);
    }
	
    public void setTameSkin(int index) {
   		this.dataManager.set(DOG_TEXTURE, (byte)index);
    }
    
    public void setWillObeyOthers(boolean flag) {
    	this.dataManager.set(OBEY_OTHERS, flag);
    }
    
    public boolean willObeyOthers() {
    	return this.dataManager.get(OBEY_OTHERS);
    }
    
    public void setFriendlyFire(boolean flag) {
    	this.dataManager.set(FRIENDLY_FIRE, flag);
    }
    
    public boolean canFriendlyFire() {
    	return this.dataManager.get(FRIENDLY_FIRE);
    }
    
    public int getDogHunger() {
		return ((Integer)this.dataManager.get(HUNGER)).intValue();
	}
    
    public void setDogHunger(int par1) {
    	this.dataManager.set(HUNGER, Math.min(Constants.HUNGER_POINTS, Math.max(0, par1)));
    }
    
    public void hasRadarCollar(boolean flag) {
    	this.dataManager.set(RADAR_COLLAR, Boolean.valueOf(flag));
    }
    
    public boolean hasRadarCollar() {
    	return ((Boolean)this.dataManager.get(RADAR_COLLAR)).booleanValue();
    }
    
    public void setNoFetchItem() {
    	this.dataManager.set(BONE, -1);
    }
      
	public void setBoneVariant(int value) {
    	this.dataManager.set(BONE, value);
	}
    
    public int getBoneVariant() {
    	return this.dataManager.get(BONE);
    }
    
    public boolean hasBone() {
    	return this.getBoneVariant() >= 0;
    }
    
    public void setHasSunglasses(boolean hasSunglasses) {
    	this.dataManager.set(SUNGLASSES, hasSunglasses);
    }
    
    public boolean hasSunglasses() {
    	return ((Boolean)this.dataManager.get(SUNGLASSES)).booleanValue();
    }
    
    public int getCollarData() {
    	return this.dataManager.get(COLLAR_COLOUR);
    }
    
    public void setCollarData(int value) {
    	this.dataManager.set(COLLAR_COLOUR, value);
    }
    
    public int getCapeData() {
    	return this.dataManager.get(CAPE);
    }
    
    public void setCapeData(int value) {
    	this.dataManager.set(CAPE, value);
    }
    
	public void setDogSize(int value) {
    	this.dataManager.set(SIZE, Math.min(5, Math.max(1, value)));
    }
    
	public int getDogSize() {
    	return this.dataManager.get(SIZE);
    }
    
	public void setGender(String data) {
		this.dataManager.set(GENDER_PARAM, data);
	}

	public String getGender() {
		return this.dataManager.get(GENDER_PARAM);
	}
    
	public void setLevel(int level) {
    	this.dataManager.set(LEVEL, level);	
	}

	public int getLevel() {
		return this.dataManager.get(LEVEL);
	}

	public void setDireLevel(int level) {
		this.dataManager.set(LEVEL_DIRE, level);
	}

	public int getDireLevel() {
		return this.dataManager.get(LEVEL_DIRE);
	}

	public void setModeId(int mode) {
		this.dataManager.set(MODE_PARAM, Math.min(mode, EnumMode.values().length - 1));
	}

	public int getModeId() {
		return this.dataManager.get(MODE_PARAM);
	}

	public void setTalentMap(Map<Talent, Integer> data) {
		this.dataManager.set(TALENTS_PARAM, data);
	}
	
	public Map<Talent, Integer> getTalentMap() {
		return this.dataManager.get(TALENTS_PARAM);
	}
	
	public boolean hasBedPos() {
		return this.dataManager.get(BED_POS).isPresent();
	}

	public boolean hasBowlPos() {
		return this.dataManager.get(BOWL_POS).isPresent();
	}

	public BlockPos getBedPos() {
		return this.dataManager.get(BED_POS).orElse(this.world.getSpawnPoint());
	}
	
	public BlockPos getBowlPos() {
		return this.dataManager.get(BOWL_POS).orElse(this.getPosition());
	}

	public void resetBedPosition() {
		this.dataManager.set(BED_POS, Optional.empty());
	}

	public void resetBowlPosition() {
		this.dataManager.set(BOWL_POS, Optional.empty());
	}

	public void setBedPos(BlockPos pos) {
		this.dataManager.set(BED_POS, Optional.of(pos));
	}

	public void setBowlPos(BlockPos pos) {
		this.dataManager.set(BOWL_POS, Optional.of(pos));
	}
    

    public void setNoCollar() {
        this.setCollarData(-2);
    }

    public boolean hasCollar() {
        return this.getCollarData() != -2;
    }

    public boolean hasCollarColoured() {
        return this.getCollarData() >= -1;
    }

    public boolean isCollarColoured() {
        return this.getCollarData() > -1;
    }

    public void setHasCollar() {
        this.setCollarData(-1);
    }

    public boolean hasFancyCollar() {
        return this.getCollarData() < -2;
    }

    public int getFancyCollarIndex() {
        return -3 - this.getCollarData();
    }

    public boolean isCreativeCollar() {
        return this.getCollarData() == -3;
    }

    public float[] getCollar() {
        return DogUtil.rgbIntToFloatArray(this.getCollarData());
    }

    public boolean hasCape() {
        return this.getCapeData() != -2;
    }

    public boolean hasCapeColoured() {
        return this.getCapeData() >= -1;
    }

    public boolean hasFancyCape() {
        return this.getCapeData() == -3;
    }

    public boolean hasLeatherJacket() {
        return this.getCapeData() == -4;
    }

    public boolean isCapeColoured() {
        return this.getCapeData() > -1;
    }

    public void setFancyCape() {
        this.setCapeData(-3);
    }

    public void setLeatherJacket() {
        this.setCapeData(-4);
    }

    public void setCapeColoured() {
        this.setCapeData(-1);
    }

    public void setNoCape() {
        this.setCapeData(-2);
    }

    public float[] getCapeColour() {
        return DogUtil.rgbIntToFloatArray(this.getCapeData());
    }
    
    public Random getRandom() {
    	return this.rand;
    }

	@Override
	public Container createMenu(int windowId, PlayerInventory inventory, PlayerEntity player) {
		return new Container(null, windowId) { //TODO
			@Override
			public boolean canInteractWith(PlayerEntity playerIn) {
				return true;
			}
		};
	}
	
	
	protected boolean dogJumping;
	protected float jumpPower;
	
	public boolean isDogJumping() {
		return this.dogJumping;
	}

	public void setDogJumping(boolean jumping) {
		this.dogJumping = jumping;
	}
	
	public double getDogJumpStrength() {
		float verticalVelocity = 0.42F + 0.06F * this.TALENTS.getLevel(ModTalents.WOLF_MOUNT);
		if(this.TALENTS.getLevel(ModTalents.WOLF_MOUNT) == 5) verticalVelocity += 0.04F;
        return verticalVelocity;
	}
	
	@Override
    public Entity getControllingPassenger() {
        return this.getPassengers().isEmpty() ? null : (Entity) this.getPassengers().get(0);
    }
	
	@Override
	public boolean canBeSteered() {
		return this.getControllingPassenger() instanceof LivingEntity;
	}
	
	@Override
	public boolean canBePushed() {
		return !this.isBeingRidden();
	}
	
	@Override
	public void updatePassenger(Entity passenger) {
		super.updatePassenger(passenger);
		if(passenger instanceof LivingEntity) {
			LivingEntity entityliving = (LivingEntity)passenger;
			this.renderYawOffset = entityliving.renderYawOffset;
		}
	}
	
	@Override
    public double getYOffset() {
        return this.getRidingEntity() instanceof PlayerEntity ? 0.5D : 0.0D;
    }
    
    @Override
    public boolean canBeRiddenInWater(Entity rider) {
        if (!TalentHelper.shouldDismountInWater(this, rider))
            return false;

        return true;
    }
    
    @Override
    public boolean canRiderInteract() {
        return true;
    }
	
	// 0 - 100 input
	public void setJumpPower(int jumpPowerIn) {
		if(this.TALENTS.getLevel(ModTalents.WOLF_MOUNT) > 0) {
			this.jumpPower = 1.0F;
		}
	}

	public boolean canJump() {
		return this.TALENTS.getLevel(ModTalents.WOLF_MOUNT) > 0;
	}
	
	@Override
	public void travel(Vec3d travelVec) {
		if(this.isAlive()) {
			if(this.isBeingRidden() && this.canBeSteered() && this.TALENTS.getLevel(ModTalents.WOLF_MOUNT) > 0) {
				LivingEntity livingentity = (LivingEntity)this.getControllingPassenger();
				this.rotationYaw = livingentity.rotationYaw;
	            this.prevRotationYaw = this.rotationYaw;
	            this.rotationPitch = livingentity.rotationPitch * 0.5F;
	            this.setRotation(this.rotationYaw, this.rotationPitch);
	            this.renderYawOffset = this.rotationYaw;
	            this.rotationYawHead = this.renderYawOffset;
	            float f = livingentity.moveStrafing * 0.7F;
	            float f1 = livingentity.moveForward;
	            if (f1 <= 0.0F) {
	               f1 *= 0.5F;
	            }	
	            
	            if (this.jumpPower > 0.0F && !this.isDogJumping() && this.onGround) {
	            	double d0 = this.getDogJumpStrength() * (double)this.jumpPower;
	            	double d1;
	            	if (this.isPotionActive(Effects.JUMP_BOOST)) {
	            		d1 = d0 + (double)((float)(this.getActivePotionEffect(Effects.JUMP_BOOST).getAmplifier() + 1) * 0.1F);
	            	} else {
	            		d1 = d0;
	            	}

	            	Vec3d vec3d = this.getMotion();
	            	this.setMotion(vec3d.x, d1, vec3d.z);
	            	this.setDogJumping(true);
	            	this.isAirBorne = true;
	            	if (f1 > 0.0F) {
	            		float f2 = MathHelper.sin(this.rotationYaw * ((float)Math.PI / 180F));
	            		float f3 = MathHelper.cos(this.rotationYaw * ((float)Math.PI / 180F));
	            		this.setMotion(this.getMotion().add((double)(-0.4F * f2 * this.jumpPower), 0.0D, (double)(0.4F * f3 * this.jumpPower)));
	            		//this.playJumpSound();
	            	}

	            	this.jumpPower = 0.0F;
	            }

	            this.jumpMovementFactor = this.getAIMoveSpeed() * 0.1F;
	            if (this.canPassengerSteer()) {
	            	this.setAIMoveSpeed((float)this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getValue() * 0.5F);
	            	super.travel(new Vec3d((double)f, travelVec.y, (double)f1));
	            } else if (livingentity instanceof PlayerEntity) {
	            	this.setMotion(Vec3d.ZERO);
	            }

	            if(this.onGround) {
	            	this.jumpPower = 0.0F;
		        	this.setDogJumping(false);
	            }

	            this.prevLimbSwingAmount = this.limbSwingAmount;
	            double d2 = this.posX - this.prevPosX;
	            double d3 = this.posZ - this.prevPosZ;
	            float f4 = MathHelper.sqrt(d2 * d2 + d3 * d3) * 4.0F;
	            if (f4 > 1.0F) {
	               f4 = 1.0F;
	            }

	            this.limbSwingAmount += (f4 - this.limbSwingAmount) * 0.4F;
	            this.limbSwing += this.limbSwingAmount;
	         } else {
	        	 this.jumpMovementFactor = 0.02F;
	        	 super.travel(travelVec);
	         }
		}
	}
}
