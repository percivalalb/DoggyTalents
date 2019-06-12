package doggytalents.entity;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import com.google.common.base.Optional;

import doggytalents.DoggyTalents;
import doggytalents.ModBlocks;
import doggytalents.ModItems;
import doggytalents.ModSerializers;
import doggytalents.ModTalents;
import doggytalents.api.DoggyTalentsAPI;
import doggytalents.api.inferface.IDogInteractItem;
import doggytalents.api.inferface.Talent;
import doggytalents.entity.ai.DogLocationManager;
import doggytalents.entity.ai.EntityAIBegDog;
import doggytalents.entity.ai.EntityAIDogFeed;
import doggytalents.entity.ai.EntityAIDogWander;
import doggytalents.entity.ai.EntityAIFetch;
import doggytalents.entity.ai.EntityAIFetchReturn;
import doggytalents.entity.ai.EntityAIFollowOwnerDog;
import doggytalents.entity.ai.EntityAIOwnerHurtByTargetDog;
import doggytalents.entity.ai.EntityAIOwnerHurtTargetDog;
import doggytalents.entity.ai.EntityAIOwnerTool;
import doggytalents.entity.ai.EntityAIShepherdDog;
import doggytalents.entity.ai.EntityBerserkerMode;
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
import doggytalents.lib.GuiNames;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAISit;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ITeleporter;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ProPercivalalb
 */
public class EntityDog extends EntityTameable {

	private static final DataParameter<Float> 					DATA_HEALTH_ID 	= EntityDataManager.createKey(EntityDog.class, DataSerializers.FLOAT);
	private static final DataParameter<Boolean> 				BEGGING 		= EntityDataManager.createKey(EntityDog.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Byte> 					DOG_TEXTURE 	= EntityDataManager.createKey(EntityDog.class, DataSerializers.BYTE);
	private static final DataParameter<Integer>					COLLAR_COLOUR 	= EntityDataManager.createKey(EntityDog.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> 				LEVEL 			= EntityDataManager.createKey(EntityDog.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> 				LEVEL_DIRE 		= EntityDataManager.createKey(EntityDog.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> 				MODE_PARAM 		= EntityDataManager.createKey(EntityDog.class, DataSerializers.VARINT);
	private static final DataParameter<Map<Talent, Integer>> 	TALENTS_PARAM 	= EntityDataManager.createKey(EntityDog.class, ModSerializers.TALENT_LEVEL_SERIALIZER);
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
	private static final DataParameter<Optional<ITextComponent>>LAST_KNOWN_NAME = EntityDataManager.createKey(EntityDog.class, ModSerializers.OPTIONAL_TEXT_COMPONENT_SERIALIZER);
	
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
    
    private float timeWolfIsHappy;
    private float prevTimeWolfIsHappy;
    private boolean isWolfHappy;
    public boolean hiyaMaster;
    private int reversionTime;
    private int hungerTick;
    private int prevHungerTick;
    private int healingTick;
    private int prevHealingTick;
    private int regenerationTick;
    private int prevRegenerationTick;
    private int foodBowlCheck;
    private int generalTick;

    //TODO public List<BlockPos> patrolOutline;

    public EntityDog(World world) {
        super(world);
        this.TALENTS = new TalentFeature(this);
		this.LEVELS = new LevelFeature(this);
		this.MODE = new ModeFeature(this);
		this.COORDS = new CoordFeature(this);
		this.GENDER = new DogGenderFeature(this);
		this.STATS = new DogStats(this);
		this.FEATURES = Arrays.asList(TALENTS, LEVELS, MODE, COORDS, STATS);
        this.locationManager = DogLocationManager.getHandler(this.getEntityWorld());
        this.objects = new HashMap<String, Object>();
    	this.setSize(0.6F, 0.85F);
		this.setTamed(false);
        
        TalentHelper.onClassCreation(this);
    }

    @Override
    protected void initEntityAI() {
    	this.aiSit = new EntityAISit(this);
		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(2, this.aiSit);
		this.tasks.addTask(3, new EntityAIFetchReturn(this, 1.0D));
		this.tasks.addTask(4, new EntityAIDogWander(this, 1.0D));
		 //TODO this.tasks.addTask(4, new EntityAIPatrolArea(this));
		this.tasks.addTask(5, new EntityAILeapAtTarget(this, 0.4F));
		this.tasks.addTask(6, new EntityAIAttackMelee(this, 1.0D, true));
		this.tasks.addTask(7, new EntityAIShepherdDog(this, 1.0D, 8F, entity -> !(entity instanceof EntityDog)));
		this.tasks.addTask(8, new EntityAIFetch(this, 1.0D, 32));
		this.tasks.addTask(9, new EntityAIOwnerTool(this, 1.0D, 1.0F, 5F));
		this.tasks.addTask(10, new EntityAIFollowOwnerDog(this, 1.0D, 10.0F, 2.0F));
		this.tasks.addTask(11, new EntityAIMate(this, 1.0D));
		this.tasks.addTask(13, new EntityAIBegDog(this, 8.0F));
		this.tasks.addTask(14, new EntityAIDogFeed(this, 1.0D, 20.0F));
		this.tasks.addTask(15, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(15, new EntityAILookIdle(this));
		
		this.targetTasks.addTask(1, new EntityAIOwnerHurtByTargetDog(this));
		this.targetTasks.addTask(2, new EntityAIOwnerHurtTargetDog(this));
		this.targetTasks.addTask(3, new EntityAIHurtByTarget(this, true));
		this.targetTasks.addTask(4, new EntityBerserkerMode<>(this, EntityMob.class, false));
    }
    
    @Override
	protected void entityInit() {
		super.entityInit();
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
        this.dataManager.register(BOWL_POS, Optional.absent());
        this.dataManager.register(BED_POS, Optional.absent());
        this.dataManager.register(CAPE, -2);
        this.dataManager.register(SUNGLASSES, false);
        this.dataManager.register(SIZE, Integer.valueOf(3));
        this.dataManager.register(GENDER_PARAM, this.getRandom().nextInt(2) == 0 ? "male" : "female");
        this.dataManager.register(LAST_KNOWN_NAME, Optional.absent());
	}
    
    @Override
	public void notifyDataManagerChange(DataParameter<?> key) {
		super.notifyDataManagerChange(key);
		if(SIZE.equals(key)) {
			 switch(this.getDogSize()) {
		        case 1:
		            this.setScale(0.5F);
		            break;
		        case 2:
		            this.setScale(0.7F);
		            break;
		        case 3:
		            this.setScale(1.0F);
		            break;
		        case 4:
		            this.setScale(1.3F);
		            break;
		        case 5:
		            this.setScale(1.6F);
		            break;
		    }
		}
	}
    
    @Override
	protected void updateAITasks() {
		super.updateAITasks();
		this.dataManager.set(DATA_HEALTH_ID, this.getHealth());
	}

    @Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue((double)0.3F);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(this.isTamed() ? 20.0D : 8.0D);
		this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(2.0D);
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
	protected void playStepSound(BlockPos pos, Block blockIn) {
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
    protected ResourceLocation getLootTable() {
        return null; //TODO DOG Loot
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        this.FEATURES.stream().forEach(f -> f.writeAdditional(compound));
        
        compound.setInteger("doggyTex", this.getTameSkin());
        compound.setInteger("collarColour", this.getCollarData());
        compound.setInteger("dogHunger", this.getDogHunger());
        compound.setBoolean("willObey", this.willObeyOthers());
        compound.setBoolean("friendlyFire", this.canFriendlyFire());
        compound.setBoolean("radioCollar", this.hasRadarCollar());
        compound.setBoolean("sunglasses", this.hasSunglasses());
        compound.setInteger("capeData", this.getCapeData());
        compound.setInteger("dogSize", this.getDogSize());
        if(this.getGender().equals("male") || this.getGender().equals("female")) compound.setString("dogGender", this.getGender());
        compound.setBoolean("hasBone", this.hasBone());
        if(this.hasBone()) compound.setInteger("boneVariant", this.getBoneVariant());
        if(this.dataManager.get(LAST_KNOWN_NAME).isPresent()) compound.setString("lastKnownOwnerName", ITextComponent.Serializer.componentToJson(this.dataManager.get(LAST_KNOWN_NAME).get()));
        
        TalentHelper.writeAdditional(this, compound);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.FEATURES.stream().forEach(f -> f.readAdditional(compound));
        
        this.setTameSkin(compound.getInteger("doggyTex"));
        if (compound.hasKey("collarColour", 99)) this.setCollarData(compound.getInteger("collarColour"));
        this.setDogHunger(compound.getInteger("dogHunger"));
        this.setWillObeyOthers(compound.getBoolean("willObey"));
        this.setFriendlyFire(compound.getBoolean("friendlyFire"));
        this.hasRadarCollar(compound.getBoolean("radioCollar"));
        this.setHasSunglasses(compound.getBoolean("sunglasses"));
        if(compound.hasKey("capeData", 99)) this.setCapeData(compound.getInteger("capeData"));
        if(compound.hasKey("dogSize", 99)) this.setDogSize(compound.getInteger("dogSize"));
        
        if(compound.hasKey("dogGender", 8)) this.setGender(compound.getString("dogGender"));
        else if(Constants.DOG_GENDER) this.setGender(this.rand.nextInt(2) == 0 ? "male" : "female");
        if(compound.getBoolean("hasBone")) this.setBoneVariant(compound.getInteger("boneVariant"));
        if(compound.hasKey("lastKnownOwnerName", 8)) this.dataManager.set(LAST_KNOWN_NAME, Optional.of(ITextComponent.Serializer.jsonToComponent(compound.getString("lastKnownOwnerName"))));

        TalentHelper.readAdditional(this, compound);

        //Backwards Compatibility
        if (compound.hasKey("dogName"))
            this.setCustomNameTag(compound.getString("dogName"));
    }

    public EntityAISit getSitAI() {
        return this.aiSit;
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        
        if(!this.world.isRemote && this.isWet && !this.isShaking && !this.hasPath() && this.onGround) {
			this.isShaking = true;
			this.timeWolfIsShaking = 0.0F;
			this.prevTimeWolfIsShaking = 0.0F;
			this.world.setEntityState(this, (byte)8);
		}
        
        if(Constants.IS_HUNGER_ON) {
            this.prevHungerTick = this.hungerTick;

            if(!this.isBeingRidden() && !this.isSitting() /** && !this.mode.isMode(EnumMode.WANDERING) && !this.level.isDireDog() || worldObj.getWorldInfo().getWorldTime() % 2L == 0L **/)
                this.hungerTick += 1;

            this.hungerTick += TalentHelper.hungerTick(this, this.hungerTick - this.prevHungerTick);

            if(this.hungerTick > 400) {
                this.setDogHunger(this.getDogHunger() - 1);
                this.hungerTick -= 400;
            }
        }

        if(Constants.DOGS_IMMORTAL) {
            this.prevRegenerationTick = this.regenerationTick;

            if(this.isSitting()) {
                this.regenerationTick += 1;
                this.regenerationTick += TalentHelper.regenerationTick(this, this.regenerationTick - this.prevRegenerationTick);
            } else if(!this.isSitting())
                this.regenerationTick = 0;

            if(this.regenerationTick >= 2400 && this.isIncapacicated()) {
                this.setHealth(2);
                this.setDogHunger(1);
            } else if(this.regenerationTick >= 2400 && !this.isIncapacicated()) {
                if(this.regenerationTick >= 4400 && this.getDogHunger() < 60) {
                    this.setDogHunger(this.getDogHunger() + 1);
                    this.world.setEntityState(this, (byte) 7);
                    this.regenerationTick = 2400;
                }
            }
        }

        if(this.getHealth() != Constants.LOW_HEATH_LEVEL) {
            this.prevHealingTick = this.healingTick;
            this.healingTick += this.nourishment();

            if(this.healingTick >= 6000) {
                if(this.getHealth() < this.getMaxHealth())
                    this.setHealth(this.getHealth() + 1);

                this.healingTick = 0;
            }
        }

        if(this.getHealth() <= 0 && this.isImmortal()) {
            this.deathTime = 0;
            this.setHealth(1);
        }

        if(this.LEVELS.isDireDog() && Constants.DIRE_PARTICLES)
            for(int i = 0; i < 2; i++)
                this.world.spawnParticle(EnumParticleTypes.PORTAL, this.posX + (this.rand.nextDouble() - 0.5D) * (double) this.width, (this.posY + rand.nextDouble() * (double) height) - 0.25D, posZ + (rand.nextDouble() - 0.5D) * (double) this.width, (this.rand.nextDouble() - 0.5D) * 2D, -this.rand.nextDouble(), (this.rand.nextDouble() - 0.5D) * 2D);

        if(this.reversionTime > 0)
            this.reversionTime -= 1;

        //Remove dog from players head if sneaking
        Entity entityRidden = this.getRidingEntity();

        if (entityRidden instanceof EntityPlayer)
            if (entityRidden.isSneaking())
                this.dismountRidingEntity();

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
    public void onUpdate() {
        super.onUpdate();

        this.headRotationCourseOld = this.headRotationCourse;
		if (this.isBegging()) {
			this.headRotationCourse += (1.0F - this.headRotationCourse) * 0.4F;
		} 
		else {
			this.headRotationCourse += (0.0F - this.headRotationCourse) * 0.4F;
		}

		if(this.isWet()) {
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
			if(this.prevTimeWolfIsShaking >= 2.0F) {
				this.isWet = false;
				this.isShaking = false;
				this.prevTimeWolfIsShaking = 0.0F;
				this.timeWolfIsShaking = 0.0F;
				
				this.onFinishShaking();
			}

			if (this.timeWolfIsShaking > 0.4F) {
				float f = (float)this.getEntityBoundingBox().minY;
				int i = (int)(MathHelper.sin((this.timeWolfIsShaking - 0.4F) * (float)Math.PI) * 7.0F);

				for(int j = 0; j < i; ++j) {
					float f1 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width * 0.5F;
					float f2 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width * 0.5F;
					this.world.spawnParticle(EnumParticleTypes.WATER_SPLASH, this.posX + (double)f1, (double)(f + 0.8F), this.posZ + (double)f2, this.motionX, this.motionY, this.motionZ);
				}
			}
		}
        
        if (this.rand.nextInt(200) == 0)
            this.hiyaMaster = true;

        if (((this.isBegging()) || (this.hiyaMaster)) && (!this.isWolfHappy)) {
            this.isWolfHappy = true;
            this.timeWolfIsHappy = 0.0F;
            this.prevTimeWolfIsHappy = 0.0F;
        } else
            this.hiyaMaster = false;

        if (this.isWolfHappy) {
            if (this.timeWolfIsHappy % 1.0F == 0.0F)
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
            EntityPlayer player = (EntityPlayer) this.getOwner();

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
		        if(this.isEntityAlive()) { //Prevent the data from being added when the entity dies
		            if(Constants.DEBUG_MODE) DoggyTalents.LOGGER.debug("Update/Add Request From Living");
		            this.locationManager.update(this);
		        } else {
		            if(Constants.DEBUG_MODE) DoggyTalents.LOGGER.debug("Remove Request From Living");
		            this.locationManager.remove(this);
		        }
		        
		        
		        if(this.getOwner() != null)
	        		this.dataManager.set(LAST_KNOWN_NAME, Optional.fromNullable(this.getOwner().getDisplayName()));
        	}
        	
        	
	        this.generalTick = 40;
        }

        TalentHelper.tick(this);
    }

    public boolean isControllingPassengerPlayer() {
        return this.getControllingPassenger() instanceof EntityPlayer;
    }

    @Override
    public void fall(float distance, float damageMultiplier) {
        if (!TalentHelper.isImmuneToFalls(this))
            super.fall(distance - TalentHelper.fallProtection(this), damageMultiplier);
    }

    @Override
    public boolean attackEntityFrom(DamageSource damageSource, float damage) {
        if (this.isEntityInvulnerable(damageSource))
            return false;
        else {
            Entity entity = damageSource.getTrueSource();
            //Friendly fire
            if (!this.canFriendlyFire() && entity instanceof EntityPlayer && (this.willObeyOthers() || this.isOwner((EntityPlayer) entity)))
                return false;

            if (!TalentHelper.attackEntityFrom(this, damageSource, damage))
                return false;

            if (this.aiSit != null)
                this.aiSit.setSitting(false);

            if (entity != null && !(entity instanceof EntityPlayer) && !(entity instanceof EntityArrow))
                damage = (damage + 1.0F) / 2.0F;

            return super.attackEntityFrom(damageSource, damage);
        }
    }

    @Override
    public boolean attackEntityAsMob(Entity entity) {
        if (!TalentHelper.shouldDamageMob(this, entity))
            return false;

        int damage = 4 + (MathHelper.floor(this.effectiveLevel()) + 1) / 2;
        damage = TalentHelper.attackEntityAsMob(this, entity, damage);

        if (entity instanceof EntityZombie)
            ((EntityZombie) entity).setAttackTarget(this);

        return entity.attackEntityFrom(DamageSource.causeMobDamage(this), (float) damage);
    }

    @Override
   	public void setTamed(boolean tamed) {
   		super.setTamed(tamed);
   		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(tamed ? 20.0D : 8.0D);
   		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.0D);
   	}
	
	@Override
   	@SideOnly(Side.CLIENT)
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
    public boolean processInteract(EntityPlayer player, EnumHand hand) {
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
        
        if(stack.getItem() == ModItems.OWNER_CHANGE && player.capabilities.isCreativeMode && !this.isOwner(player)) {
        	if(!this.world.isRemote) {
	        	this.setTamed(true);
	            this.navigator.clearPath();
	            this.setAttackTarget((EntityLivingBase) null);
	            this.aiSit.setSitting(true);
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
                    if (!player.capabilities.isCreativeMode)
                        stack.shrink(1);

                    this.setDogHunger(this.getDogHunger() + foodValue);
                    if (stack.getItem() == ModItems.CHEW_STICK)
                        ((ItemChewStick)ModItems.CHEW_STICK).addChewStickEffects(this);

                    return true;
                }
                else if(stack.getItem() == ModItems.DOGGY_CHARM && player.capabilities.isCreativeMode) {
                	if(!this.world.isRemote) {
                		EntityDog babySpawn = this.createChild(this);
                        if(babySpawn != null) {
                           babySpawn.setGrowingAge(-24000 * (Constants.TEN_DAY_PUPS ? 10 : 1));
                           babySpawn.setTamed(true);
                           if(Constants.PUPS_GET_PARENT_LEVELS) {
                               babySpawn.LEVELS.setLevel(Math.min(this.LEVELS.getLevel(), 20));
                           }
                           
                           babySpawn.setLocationAndAngles(this.posX, this.posY, this.posZ, 0.0F, 0.0F);
                           this.world.spawnEntity(babySpawn);

                           if(!player.capabilities.isCreativeMode) {
                        	   stack.shrink(1);
                           }
                        }
                     }

                	return true;
                }
            	/*else if(stack.getItem() == Items.BONE && this.canInteract(player)) {
            		this.startRiding(player);
            		
            		if(this.aiSit != null)
            			this.aiSit.setSitting(true);
            		
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
                			player.sendMessage(new TextComponentTranslation("dog.mode.incapacitated.help", this.getDisplayName()));
                	} else {
                		 player.openGui(DoggyTalents.INSTANCE, GuiNames.GUI_ID_DOGGY, this.world, this.getEntityId(), MathHelper.floor(this.posY), MathHelper.floor(this.posZ));
                	}
                	
                    return true;
                } else if(stack.getItem() == ModItems.RADIO_COLLAR && this.canInteract(player) && !this.hasRadarCollar() && !this.isIncapacicated()) {
                    this.hasRadarCollar(true);

                    if(!player.capabilities.isCreativeMode)
                        stack.shrink(1);
                    return true;
                } else if(stack.getItem() == ModItems.WOOL_COLLAR && this.canInteract(player) && !this.hasCollar() && !this.isIncapacicated()) {
                    int colour = -1;

                    if(stack.hasTagCompound() && stack.getTagCompound().hasKey("collar_colour", 99))
                        colour = stack.getTagCompound().getInteger("collar_colour");

                    this.setCollarData(colour);

                    if(!player.capabilities.isCreativeMode)
                        stack.shrink(1);
                    return true;
                } else if(stack.getItem() instanceof ItemFancyCollar && this.canInteract(player) && !this.hasCollar() && !this.isIncapacicated()) {
                    this.setCollarData(-3 - ((ItemFancyCollar)stack.getItem()).type.ordinal());

                    if(!player.capabilities.isCreativeMode)
                        stack.shrink(1);
                    return true;
                } else if(stack.getItem() == ModItems.CAPE && this.canInteract(player) && !this.hasCape() && !this.isIncapacicated()) {
                    this.setFancyCape();
                    if(!player.capabilities.isCreativeMode)
                        stack.shrink(1);
                    return true;
                } else if(stack.getItem() == ModItems.LEATHER_JACKET && this.canInteract(player) && !this.hasCape() && !this.isIncapacicated()) {
                    this.setLeatherJacket();
                    if(!player.capabilities.isCreativeMode)
                        stack.shrink(1);
                    return true;
                } else if(stack.getItem() == ModItems.CAPE_COLOURED && this.canInteract(player) && !this.hasCape() && !this.isIncapacicated()) {
                    int colour = -1;

                    if(stack.hasTagCompound() && stack.getTagCompound().hasKey("cape_colour", 99))
                        colour = stack.getTagCompound().getInteger("cape_colour");

                    this.setCapeData(colour);

                    if (!player.capabilities.isCreativeMode)
                        stack.shrink(1);
                    return true;
                } else if(stack.getItem() == ModItems.SUNGLASSES && this.canInteract(player) && !this.hasSunglasses() && !this.isIncapacicated()) {
                    this.setHasSunglasses(true);
                    if (!player.capabilities.isCreativeMode)
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
                                    collarDrop.setTagCompound(new NBTTagCompound());
                                    collarDrop.getTagCompound().setInteger("collar_colour", this.getCollarData());
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
                            	
                                this.dropItem(drop, 1);
                                this.setNoCollar();
                            }

                            if(this.hasFancyCape()) {
                                this.entityDropItem(new ItemStack(ModItems.CAPE, 1), 1);
                                this.setNoCape();
                            }

                            if(this.hasCapeColoured()) {
                                ItemStack capeDrop = new ItemStack(ModItems.CAPE_COLOURED, 1);
                                if (this.isCapeColoured()) {
                                    capeDrop.setTagCompound(new NBTTagCompound());
                                    capeDrop.getTagCompound().setInteger("cape_colour", this.getCapeData());
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
                            this.aiSit.setSitting(false);
                            this.setHealth(8);
                            this.TALENTS.resetTalents();
                            this.setOwnerId(null);
                            this.dataManager.set(LAST_KNOWN_NAME, Optional.absent());
                            this.setWillObeyOthers(false);
                            this.MODE.setMode(EnumMode.DOCILE);
                            if(this.hasRadarCollar())
                                this.dropItem(ModItems.RADIO_COLLAR, 1);
                            this.hasRadarCollar(false);
                            this.reversionTime = 40;
                        }
                    }

                    return true;
                } else if(stack.getItem() == Items.CAKE && this.canInteract(player) && this.isIncapacicated()) {
                    if (!player.capabilities.isCreativeMode)
                        stack.shrink(1);

                    if(!this.world.isRemote) {
                        this.aiSit.setSitting(true);
                        this.setHealth(this.getMaxHealth());
                        this.setDogHunger(Constants.HUNGER_POINTS);
                        this.regenerationTick = 0;
                        this.setAttackTarget((EntityLivingBase) null);
                        this.playTameEffect(true);
                        this.world.setEntityState(this, (byte) 7);
                    }

                    return true;
                } else if(stack.getItem() == Items.DYE && this.canInteract(player) && this.hasCollarColoured()) { //TODO Add Plants compatibility

                	if(!this.world.isRemote) {
	                    int[] aint = new int[3];
	                    int maxCompSum = 0;
	                    int count = 1; //The number of different sources of colour
	    
	                    EnumDyeColor colour = EnumDyeColor.byDyeDamage(stack.getMetadata());
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

                    InventoryTreatBag treatBag = new InventoryTreatBag(player, player.inventory.currentItem, stack);
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
                    this.aiSit.setSitting(!this.isSitting());
                    this.isJumping = false;
                    this.navigator.clearPath();
                    this.setAttackTarget((EntityLivingBase) null);
                }
                return true;
            }
        } else if(stack.getItem() == ModItems.COLLAR_SHEARS && this.reversionTime < 1) {
            if(!this.world.isRemote) {
                this.locationManager.remove(this);
                this.setDead();
                EntityWolf wolf = new EntityWolf(this.world);
                wolf.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
                this.world.spawnEntity(wolf);
            }
            return true;
        } else if(stack.getItem() == Items.BONE || stack.getItem() == ModItems.TRAINING_TREAT) {
        	if(!player.capabilities.isCreativeMode)
        		stack.shrink(1);

        	if(!this.world.isRemote) {
        		if(stack.getItem() == ModItems.TRAINING_TREAT || this.rand.nextInt(3) == 0) {
                    this.setTamed(true);
                    this.navigator.clearPath();
                    this.setAttackTarget((EntityLivingBase) null);
                    this.aiSit.setSitting(true);
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
    public EntityDog createChild(EntityAgeable entityAgeable) {
        EntityDog entitydog = new EntityDog(this.world);
        UUID uuid = this.getOwnerId();

        if (uuid != null) {
            entitydog.setOwnerId(uuid);
            entitydog.setTamed(true);
        }

        entitydog.setGrowingAge(-24000 * (Constants.TEN_DAY_PUPS ? 10 : 1));

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
		return DoggyTalentsAPI.BREED_WHITELIST.containsItem(stack);
	}
    
    @Override
    @SideOnly(Side.CLIENT)
    public boolean getAlwaysRenderNameTagForRender() {
        return this.hasCustomName();
    }
    
    @Override
    protected boolean isMovementBlocked() {
        return this.isPlayerSleeping() || super.isMovementBlocked(); //this.getRidingEntity() != null || this.riddenByEntity instanceof EntityPlayer || super.isMovementBlocked();
    }

    @Override
    public boolean isPotionApplicable(PotionEffect potionEffect) {
        if (this.isIncapacicated())
            return false;

        if (!TalentHelper.isPostionApplicable(this, potionEffect))
            return false;

        return true;
    }

    @Override
    public void setFire(int amount) {
        if (TalentHelper.setFire(this, amount))
            super.setFire(amount);
    }
    
    @Override
    public boolean isPlayerSleeping() {
        return false;
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
    public void playTameEffect(boolean successful) {
        super.playTameEffect(successful);
    }

    @Override
    public boolean shouldAttackEntity(EntityLivingBase target, EntityLivingBase owner) {
        if (TalentHelper.canAttackEntity(this, target))
            return true;

        if (!(target instanceof EntityCreeper) && !(target instanceof EntityGhast)) {
            if (target instanceof EntityDog) {
                EntityDog entitydog = (EntityDog) target;

                if (entitydog.isTamed() && entitydog.getOwner() == owner)
                    return false;
            } else if (target instanceof EntityWolf) {
                EntityWolf entitywolf = (EntityWolf) target;

                if (entitywolf.isTamed() && entitywolf.getOwner() == owner)
                    return false;
            }

            if (target instanceof EntityPlayer && owner instanceof EntityPlayer && !((EntityPlayer) owner).canAttackPlayer((EntityPlayer) target))
                return false;
            else if (target == owner)
                return false;
            else
                return !(target instanceof AbstractHorse) || !((AbstractHorse) target).isTame();
        }

        return false;
    }

    @Override
    public boolean canAttackClass(Class<? extends EntityLivingBase> cls) {
        if (TalentHelper.canAttackClass(this, cls))
            return true;

        return super.canAttackClass(cls);
    }
    
    @Override
    public Entity changeDimension(int dimId, ITeleporter teleporter) {
    	Entity entity = super.changeDimension(dimId, teleporter);
    	if(entity instanceof EntityDog) {
    		EntityDog dog = (EntityDog)entity;
    		
    		if(!this.world.isRemote) {
	    		dog.locationManager.update(dog);
	    		this.locationManager.remove(this);
    		}
    	} else if(entity != null) {
    		DoggyTalents.LOGGER.warn("Dog tried to change dimension but now isn't a dog?");
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
    	if(!this.world.isRemote && !this.isEntityAlive())
    		this.locationManager.remove(this);
    }
    

    @Override
    public void onDeath(DamageSource cause) {
        if(!this.world.isRemote && this.world.getGameRules().getBoolean("showDeathMessages") && !this.isImmortal() && this.getOwner() instanceof EntityPlayerMP) {
            this.getOwner().sendMessage(this.getCombatTracker().getDeathMessage());
        }
        
        if(!this.world.isRemote && !this.isImmortal()) {
        	 this.locationManager.remove(this);
        }
    }
    
    @Override
    protected float getJumpUpwardsMotion() {
        return 0.42F;
    }
    
    @Override
    public boolean canDespawn() {
    	return false;
    }
    
    @Override
    protected float getWaterSlowDown() {
        return 0.8F;
    }

    protected void onFinishShaking() {
        if(!this.world.isRemote) {
            int lvlFisherDog = this.TALENTS.getLevel(ModTalents.FISHER_DOG);
            int lvlHellHound = this.TALENTS.getLevel(ModTalents.HELL_HOUND);

            if (this.rand.nextInt(15) < lvlFisherDog * 2)
                this.dropItem(this.rand.nextInt(15) < lvlHellHound * 2 ? Items.COOKED_FISH : Items.FISH, 1);
        }
    }
    
    @Override
   	public boolean canMateWith(EntityAnimal otherAnimal) {
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
    
    @SideOnly(Side.CLIENT)
   	public boolean isDogWet() {
		return this.isWet;
	}

    @SideOnly(Side.CLIENT)
   	public float getShadingWhileWet(float p_70915_1_) {
		return 0.75F + (this.prevTimeWolfIsShaking + (this.timeWolfIsShaking - this.prevTimeWolfIsShaking) * p_70915_1_) / 2.0F * 0.25F;
   	}

   	@SideOnly(Side.CLIENT)
   	public float getShakeAngle(float partialTick, float offset) {
	   float f = (this.prevTimeWolfIsShaking + (this.timeWolfIsShaking - this.prevTimeWolfIsShaking) * partialTick + offset) / 1.8F;
	   if (f < 0.0F) {
		   f = 0.0F;
	   } else if (f > 1.0F) {
		   f = 1.0F;
	   }

	   return MathHelper.sin(f * (float)Math.PI) * MathHelper.sin(f * (float)Math.PI * 11.0F) * 0.15F * (float)Math.PI;
   	}

   	@SideOnly(Side.CLIENT)
   	public float getInterestedAngle(float p_70917_1_) {
	   return (this.headRotationCourseOld + (this.headRotationCourse - this.headRotationCourseOld) * p_70917_1_) * 0.15F * (float)Math.PI;
   	}
   	
	public float getWagAngle(float partialTick, float offset) {
        float f = (this.prevTimeWolfIsHappy + (this.timeWolfIsHappy - this.prevTimeWolfIsHappy) * partialTick + offset) / 2.0F;
        if (f < 0.0F) f = 0.0F;
        else if (f > 2.0F) f %= 2.0F;
        return MathHelper.sin(f * (float) Math.PI * 11.0F) * 0.3F * (float) Math.PI;
    }
	
	@SideOnly(Side.CLIENT)
   	public float getTailRotation() {
   		return this.isTamed() ? (0.55F - (this.getMaxHealth() - this.dataManager.get(DATA_HEALTH_ID)) / this.getMaxHealth() * 20.0F * 0.02F) * (float)Math.PI : ((float)Math.PI / 5F);
   	}

   	@Override
   	public float getEyeHeight() {
   		return this.height * 0.8F;
   	}

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
		return this.isTamed() && this.MODE.isMode(EnumMode.WANDERING) && this.COORDS.hasBowlPos() && this.getDistanceSq(this.COORDS.getBowlPos()) < 256.0D;
	}
	
	public boolean canInteract(EntityPlayer player) {
        return this.isOwner(player) || this.willObeyOthers();
    }
	
	public int foodValue(ItemStack stack) {
        if (stack.isEmpty())
            return 0;

        int foodValue = 0;

        Item item = stack.getItem();

        if (stack.getItem() != Items.ROTTEN_FLESH && item instanceof ItemFood) {
            ItemFood itemfood = (ItemFood)item;

            if (itemfood.isWolfsFavoriteMeat())
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
	
	public void mountTo(EntityLivingBase entityLiving) {
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
			return new TextComponentTranslation("entity.doggytalents.dog.unknown_owner");
		} else {
			return new TextComponentTranslation("entity.doggytalents.dog.untamed");
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
		return this.dataManager.get(BED_POS).or(this.world.getSpawnPoint());
	}
	
	public BlockPos getBowlPos() {
		return this.dataManager.get(BOWL_POS).or(this.getPosition());
	}

	public void resetBedPosition() {
		this.dataManager.set(BED_POS, Optional.absent());
	}

	public void resetBowlPosition() {
		this.dataManager.set(BOWL_POS, Optional.absent());
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
		return this.getControllingPassenger() instanceof EntityLivingBase;
	}
	
	@Override
	public boolean canBePushed() {
		return !this.isBeingRidden();
	}
	
	@Override
	public void updatePassenger(Entity passenger) {
		super.updatePassenger(passenger);
		if(passenger instanceof EntityLiving) {
			EntityLiving entityliving = (EntityLiving)passenger;
			this.renderYawOffset = entityliving.renderYawOffset;
		}
	}
	
	@Override
    public double getYOffset() {
        return this.getRidingEntity() instanceof EntityPlayer ? 0.5D : 0.0D;
    }
    
    @Override
    public boolean shouldDismountInWater(Entity rider) {
        if(!TalentHelper.shouldDismountInWater(this, rider))
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
	public void travel(float strafe, float vertical, float forward) {
		if(this.isBeingRidden() && this.canBeSteered() && this.TALENTS.getLevel(ModTalents.WOLF_MOUNT) > 0) {
			EntityLivingBase entitylivingbase = (EntityLivingBase)this.getControllingPassenger();
			this.rotationYaw = entitylivingbase.rotationYaw;
			this.prevRotationYaw = this.rotationYaw;
	        this.rotationPitch = entitylivingbase.rotationPitch * 0.5F;
	        this.setRotation(this.rotationYaw, this.rotationPitch);
	        this.renderYawOffset = this.rotationYaw;
	        this.rotationYawHead = this.renderYawOffset;
	        strafe = entitylivingbase.moveStrafing * 0.7F;
	        forward = entitylivingbase.moveForward;
	        if (forward <= 0.0F) {
	        	forward *= 0.5F;
	        }
	        
	        this.stepHeight = 1.0F;

	        if(this.jumpPower > 0.0F && !this.isDogJumping() && this.onGround) {
	        	this.motionY = this.getDogJumpStrength() * (double)this.jumpPower;
	            if(this.isPotionActive(MobEffects.JUMP_BOOST)) {
	            	this.motionY += (double)((float)(this.getActivePotionEffect(MobEffects.JUMP_BOOST).getAmplifier() + 1) * 0.1F);
	            }

	            this.setDogJumping(true);
	            this.isAirBorne = true;
	            if(forward > 0.0F) {
	            	float f = MathHelper.sin(this.rotationYaw * ((float)Math.PI / 180F));
	            	float f1 = MathHelper.cos(this.rotationYaw * ((float)Math.PI / 180F));
	            	this.motionX += (double)(-0.4F * f * this.jumpPower);
	            	this.motionZ += (double)(0.4F * f1 * this.jumpPower);
	            }

	            this.jumpPower = 0.0F;
	        }
	        else if(this.jumpPower > 0.0F && this.isInWater() && !this.isDogJumping()) {
	        	this.motionY = this.getDogJumpStrength() * 0.4F;
	        	this.setDogJumping(true);
	        	  this.jumpPower = 0.0F;
	        }

	        this.jumpMovementFactor = this.getAIMoveSpeed() * 0.3F;
	        if(this.canPassengerSteer()) {
	        	this.setAIMoveSpeed((float)this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue() * 0.5F);
	        	super.travel(strafe, vertical, forward);
	        } else if(entitylivingbase instanceof EntityPlayer) {
	        	this.motionX = 0.0D;
	        	this.motionY = 0.0D;
	        	this.motionZ = 0.0D;
	        }

	        if(this.onGround || this.isInWater()) {
	        	this.jumpPower = 0.0F;
	        	this.setDogJumping(false);
	        }

	        this.prevLimbSwingAmount = this.limbSwingAmount;
	        double d1 = this.posX - this.prevPosX;
	        double d0 = this.posZ - this.prevPosZ;
	        float f2 = MathHelper.sqrt(d1 * d1 + d0 * d0) * 4.0F;
	        if(f2 > 1.0F) {
	        	f2 = 1.0F;
	        }

	        this.limbSwingAmount += (f2 - this.limbSwingAmount) * 0.4F;
	        this.limbSwing += this.limbSwingAmount;
		} else {
			this.stepHeight = 0.6F;
			this.jumpMovementFactor = 0.02F;
			super.travel(strafe, vertical, forward);
		}
	}
}