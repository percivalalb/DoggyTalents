package doggytalents.entity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;

import doggytalents.DoggyTalentsMod;
import doggytalents.ModItems;
import doggytalents.api.DoggyTalentsAPI;
import doggytalents.api.IDogTreat;
import doggytalents.api.IDogTreat.EnumFeedBack;
import doggytalents.entity.ModeUtil.EnumMode;
import doggytalents.entity.ai.EntityAIDogBeg;
import doggytalents.entity.ai.EntityAIFetchBone;
import doggytalents.entity.ai.EntityAIFollowOwner;
import doggytalents.entity.ai.EntityAIModeAttackTarget;
import doggytalents.entity.ai.EntityAIOwnerHurtByTarget;
import doggytalents.entity.ai.EntityAIOwnerHurtTarget;
import doggytalents.entity.ai.EntityAIShepherdDog;
import doggytalents.helper.ChatHelper;
import doggytalents.lib.Constants;
import doggytalents.lib.Reference;
import doggytalents.proxy.CommonProxy;
import jline.internal.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
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
import net.minecraft.entity.ai.EntityAITargetNonTamed;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ProPercivalalb
 */
public class EntityDog extends EntityTameable {
	
	public static final DataParameter<Float> DATA_HEALTH_ID = EntityDataManager.<Float>createKey(EntityDog.class, DataSerializers.FLOAT);
	public static final DataParameter<Boolean> BEGGING = EntityDataManager.<Boolean>createKey(EntityDog.class, DataSerializers.BOOLEAN);
	public static final DataParameter<Byte> DOG_TEXTURE = EntityDataManager.<Byte>createKey(EntityDog.class, DataSerializers.BYTE);
	public static final DataParameter<Integer> LEVEL = EntityDataManager.<Integer>createKey(EntityDog.class, DataSerializers.VARINT);
	public static final DataParameter<Integer> LEVEL_DIRE = EntityDataManager.<Integer>createKey(EntityDog.class, DataSerializers.VARINT);
	public static final DataParameter<Integer> MODE = EntityDataManager.<Integer>createKey(EntityDog.class, DataSerializers.VARINT);
	public static final DataParameter<String> DOG_NAME = EntityDataManager.<String>createKey(EntityDog.class, DataSerializers.STRING);
	public static final DataParameter<String> TALENTS = EntityDataManager.<String>createKey(EntityDog.class, DataSerializers.STRING);
	public static final DataParameter<Integer> HUNGER = EntityDataManager.<Integer>createKey(EntityDog.class, DataSerializers.VARINT);
	public static final DataParameter<Boolean> OBEY_OTHERS = EntityDataManager.<Boolean>createKey(EntityDog.class, DataSerializers.BOOLEAN);
	public static final DataParameter<Boolean> RADAR_COLLAR = EntityDataManager.<Boolean>createKey(EntityDog.class, DataSerializers.BOOLEAN);
	public static final DataParameter<Optional<BlockPos>> BOWL_POS = EntityDataManager.<Optional<BlockPos>>createKey(EntityDog.class, DataSerializers.OPTIONAL_BLOCK_POS);
	public static final DataParameter<Optional<BlockPos>> BED_POS = EntityDataManager.<Optional<BlockPos>>createKey(EntityDog.class, DataSerializers.OPTIONAL_BLOCK_POS);
	
	
    /**
    this.dataWatcher.addObject(20, new Byte((byte)0)); //Dog Texture
    this.dataWatcher.addObject(21, new String("")); //Dog Name
    this.dataWatcher.addObject(22, new String("")); //Talent Data
    this.dataWatcher.addObject(23, new Integer(60)); //Dog Hunger
    this.dataWatcher.addObject(24, new String("0:0")); //Level Data
    this.dataWatcher.addObject(25, new Integer(0)); //Radio Collar
    this.dataWatcher.addObject(26, new Integer(0)); //Obey Others
    this.dataWatcher.addObject(27, new Integer(0)); //Dog Mode
    this.dataWatcher.addObject(28, "-1:-1:-1:-1:-1:-1"); //Dog Mode
    **/
    /** Float used to smooth the rotation of the wolf head */
    private float headRotationCourse;
    private float headRotationCourseOld;
    /** true is the wolf is wet else false */
    private boolean isWet;
    /** True if the wolf is shaking else False */
    public boolean isShaking;
    /** This time increases while wolf is shaking and emitting water particles. */
    private float timeWolfIsShaking;
    private float prevTimeWolfIsShaking;
    
   // private boolean isWet;
    //public boolean isShaking;
    private int hungerTick;
   	private int prevHungerTick;
    private int healingTick;
    private int prevHealingTick;
    private int regenerationTick;
    private int prevRegenerationTick;
    private float timeWolfIsHappy;
    private float prevTimeWolfIsHappy;
    private boolean isWolfHappy;
    public boolean hiyaMaster;
    private int reversionTime;
    private boolean hasBone;
    public EntityAIFetchBone aiFetchBone;
    public TalentUtil talents;
    public LevelUtil levels;
    public ModeUtil mode;
    public CoordUtil coords;
    public Map<String, Object> objects;

    public EntityDog(World word) {
        super(word);
        this.objects = new HashMap<String, Object>();
        this.setSize(0.6F, 0.85F);
        
        TalentHelper.onClassCreation(this);
    }
    
    @Override
    protected void initEntityAI() {
        this.aiSit = new EntityAISit(this);
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, this.aiSit);
        this.tasks.addTask(3, new EntityAILeapAtTarget(this, 0.4F));
        this.tasks.addTask(4, new EntityAIAttackMelee(this, 1.0D, true));
        this.tasks.addTask(5, new EntityAIFollowOwner(this, 1.0D, 10.0F, 2.0F));
        this.tasks.addTask(6, this.aiFetchBone = new EntityAIFetchBone(this, 1.0D, 0.5F, 20.0F));
        this.tasks.addTask(7, new EntityAIMate(this, 1.0D));
        this.tasks.addTask(8, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(9, new EntityAIDogBeg(this, 8.0F));
        this.tasks.addTask(10, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(10, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIOwnerHurtByTarget(this));
        this.targetTasks.addTask(2, new EntityAIOwnerHurtTarget(this));
        this.targetTasks.addTask(3, new EntityAIModeAttackTarget(this));
        this.targetTasks.addTask(4, new EntityAIHurtByTarget(this, true, new Class[0]));
        this.targetTasks.addTask(5, new EntityAITargetNonTamed(this, EntityAnimal.class, false, new Predicate<Entity>()
        {
        	@Override
            public boolean apply(@Nullable Entity p_apply_1_)
            {
                return p_apply_1_ instanceof EntitySheep || p_apply_1_ instanceof EntityRabbit;
            }
        }));
        this.targetTasks.addTask(6, new EntityAIShepherdDog(this, EntityAnimal.class, 0, false));
        this.setTamed(false);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.30000001192092896D);
        this.updateEntityAttributes();
    }
    
    public void updateEntityAttributes() {
    	if (this.isTamed())
            this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0D + (this.effectiveLevel() + 1.0D));
        else
            this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(8.0D);
    }
    
    @Override
    public String getName() {
    	String name = this.getDogName();
    	if(name != "")
    		return name;
    	return super.getName();
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public boolean getAlwaysRenderNameTagForRender() {
        return true;
    }

    @Override
    protected void updateAITasks() {
        this.dataManager.set(DATA_HEALTH_ID, Float.valueOf(this.getHealth()));
    }
    
    @Override
    protected void entityInit() {
        super.entityInit();
        this.talents = new TalentUtil(this);
        this.levels = new LevelUtil(this);
        this.mode = new ModeUtil(this);
        this.coords = new CoordUtil(this);
        
        this.dataManager.register(DATA_HEALTH_ID, Float.valueOf(this.getHealth()));
        this.dataManager.register(BEGGING, Boolean.valueOf(false));
        this.dataManager.register(DOG_TEXTURE, Byte.valueOf((byte)0));
        this.dataManager.register(DOG_NAME, "");
        this.dataManager.register(TALENTS, "");
        this.dataManager.register(HUNGER, Integer.valueOf(60));
        this.dataManager.register(OBEY_OTHERS, Boolean.valueOf(false));
        this.dataManager.register(RADAR_COLLAR, Boolean.valueOf(false));
        this.dataManager.register(MODE, Integer.valueOf(0));
        this.dataManager.register(LEVEL, Integer.valueOf(0));
        this.dataManager.register(LEVEL_DIRE, Integer.valueOf(0));
        this.dataManager.register(BOWL_POS, Optional.absent());
        this.dataManager.register(BED_POS, Optional.absent());
        /**
        
        this.dataWatcher.addObject(20, new Byte((byte)0)); //Dog Texture
        this.dataWatcher.addObject(21, new String("")); //Dog Name
        this.dataWatcher.addObject(22, new String("")); //Talent Data
        this.dataWatcher.addObject(23, new Integer(60)); //Dog Hunger
        this.dataWatcher.addObject(24, new String("0:0")); //Level Data
        this.dataWatcher.addObject(25, new Integer(0)); //Radio Collar
        this.dataWatcher.addObject(26, new Integer(0)); //Obey Others
        this.dataWatcher.addObject(27, new Integer(0)); //Dog Mode
        this.dataWatcher.addObject(28, "-1:-1:-1:-1:-1:-1"); //Dog Mode**/
    }

    @Override
    protected void playStepSound(BlockPos pos, Block blockIn) {
        this.playSound(SoundEvents.ENTITY_WOLF_STEP, 0.15F, 1.0F);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setString("version", Reference.MOD_VERSION);
        
        tagCompound.setInteger("doggyTex", this.getTameSkin());
        tagCompound.setString("dogName", this.getDogName());
        tagCompound.setInteger("dogHunger", this.getDogHunger());
        tagCompound.setBoolean("willObey", this.willObeyOthers());
        tagCompound.setBoolean("radioCollar", this.hasRadarCollar());
        
        this.talents.writeTalentsToNBT(tagCompound);
        this.levels.writeTalentsToNBT(tagCompound);
        this.mode.writeToNBT(tagCompound);
        this.coords.writeToNBT(tagCompound);
        TalentHelper.writeToNBT(this, tagCompound);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tagCompound) {
        super.readEntityFromNBT(tagCompound);

        String lastVersion = tagCompound.getString("version");
        this.setTameSkin(tagCompound.getInteger("doggyTex"));
        this.setDogName(tagCompound.getString("dogName"));
        this.setDogHunger(tagCompound.getInteger("dogHunger"));
        this.setWillObeyOthers(tagCompound.getBoolean("willObey"));
        this.hasRadarCollar(tagCompound.getBoolean("radioCollar"));
        
        this.talents.readTalentsFromNBT(tagCompound);
        this.levels.readTalentsFromNBT(tagCompound);
        this.mode.readFromNBT(tagCompound);
        this.coords.readFromNBT(tagCompound);
        TalentHelper.readFromNBT(this, tagCompound);
    }
    
    @Override
    protected SoundEvent getAmbientSound() {
    	SoundEvent sound = TalentHelper.getLivingSound(this);
    	if(sound != null)
    		return sound;
        return (this.rand.nextInt(3) == 0 ? (this.isTamed() && ((Float)this.dataManager.get(DATA_HEALTH_ID)).floatValue() < this.getMaxHealth() / 2 ? SoundEvents.ENTITY_WOLF_WHINE : SoundEvents.ENTITY_WOLF_PANT) : SoundEvents.ENTITY_WOLF_AMBIENT);
    }

    @Override
    protected SoundEvent getHurtSound() {
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

    @Nullable
    protected ResourceLocation getLootTable() {
        return LootTableList.ENTITIES_WOLF; //TODO DOG Loot
    }
    
    public EntityAISit getSitAI() {
    	return this.aiSit;
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();

        if(!this.worldObj.isRemote && this.isWet && !this.isShaking && !this.hasPath() && this.onGround) {
            this.isShaking = true;
            this.timeWolfIsShaking = 0.0F;
            this.prevTimeWolfIsShaking = 0.0F;
            this.worldObj.setEntityState(this, (byte)8);
        }
        
        if(Constants.IS_HUNGER_ON) {
        	this.prevHungerTick = this.hungerTick;
        	
	        if(!this.isBeingRidden() && !this.isSitting() /** && !this.mode.isMode(EnumMode.WANDERING) && !this.level.isDireDog() || worldObj.getWorldInfo().getWorldTime() % 2L == 0L **/)
	        	this.hungerTick += 1;
	        
	        this.hungerTick += TalentHelper.onHungerTick(this, this.hungerTick - this.prevHungerTick);
	        
	        if (this.hungerTick > 400) {
	            this.setDogHunger(this.getDogHunger() - 1);
	            this.hungerTick -= 400;
	        }
        }
        
        if(Constants.DOGS_IMMORTAL) {
        	this.prevRegenerationTick = this.regenerationTick;
        	
	        if(this.isSitting()) {
	        	this.regenerationTick += 1;
	        	this.regenerationTick += TalentHelper.onRegenerationTick(this, this.regenerationTick - this.prevRegenerationTick);
	        }
	        else if(!this.isSitting())
	        	this.regenerationTick = 0;
	        
	        if(this.regenerationTick >= 2400 && this.isIncapacicated()) {
	            this.setHealth(2);
	            this.setDogHunger(1);
	        }
	        else if(this.regenerationTick >= 2400 && !this.isIncapacicated()) {
		        if(this.regenerationTick >= 4400 && this.getDogHunger() < 60) {
		        	this.setDogHunger(this.getDogHunger() + 1);
		            this.worldObj.setEntityState(this, (byte)7);
		            this.regenerationTick = 2400;
		        }
	        }
    	}
        
        if(this.getHealth() != 1) {
	        this.prevHealingTick = this.healingTick;
	        this.healingTick += this.nourishment();
	        
	        if (this.healingTick >= 6000) {
	            if (this.getHealth() < this.getMaxHealth())
	            	this.setHealth(this.getHealth() + 1);
	            
	            this.healingTick = 0;
	        }
        }
        
        if (this.getHealth() <= 0 && this.isImmortal()) {
            this.deathTime = 0;
            this.setHealth(1);
        }
        
        if(this.getDogHunger() == 0 && this.worldObj.getWorldInfo().getWorldTime() % 100L == 0L && this.getHealth() > 1) {
            this.attackEntityFrom(DamageSource.generic, 1);
            //this.fleeingTick = 0;
        }
        
        if (this.levels.isDireDog() && Constants.DIRE_PARTICLES) {
            for (int i = 0; i < 2; i++) {
                worldObj.spawnParticle(EnumParticleTypes.PORTAL, this.posX + (this.rand.nextDouble() - 0.5D) * (double)this.width, (this.posY + rand.nextDouble() * (double)height) - 0.25D, posZ + (rand.nextDouble() - 0.5D) * (double)this.width, (this.rand.nextDouble() - 0.5D) * 2D, -this.rand.nextDouble(), (this.rand.nextDouble() - 0.5D) * 2D);
            }
        }
        
        if(this.reversionTime > 0)
        	this.reversionTime -= 1;
        
        //Remove dog from players head if sneaking
        if(this.getRidingEntity() instanceof EntityPlayer)
        	if(this.getRidingEntity().isSneaking())
        		this.dismountRidingEntity();
        
        TalentHelper.onLivingUpdate(this);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.headRotationCourseOld = this.headRotationCourse;

        if (this.isBegging())
            this.headRotationCourse += (1.0F - this.headRotationCourse) * 0.4F;
        else
            this.headRotationCourse += (0.0F - this.headRotationCourse) * 0.4F;

        if (this.isWet()) {
            this.isWet = true;
            this.isShaking = false;
            this.timeWolfIsShaking = 0.0F;
            this.prevTimeWolfIsShaking = 0.0F;
        }
        else if((this.isWet || this.isShaking) && this.isShaking) {
        	
        	if (this.timeWolfIsShaking == 0.0F)
                this.playSound(SoundEvents.ENTITY_WOLF_SHAKE, this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);

        	this.prevTimeWolfIsShaking = this.timeWolfIsShaking;
            this.timeWolfIsShaking += 0.05F;

            if (this.prevTimeWolfIsShaking >= 2.0F) {
            	if(this.rand.nextInt(15) < this.talents.getLevel("fisherdog") * 2) {
                    if(this.rand.nextInt(15) < this.talents.getLevel("hellhound") * 2) {
                    	if(!this.worldObj.isRemote) {
                    		dropItem(Items.COOKED_FISH, 1);
                    	}
                    }
                    else {
                    	if(!this.worldObj.isRemote) {
                    		dropItem(Items.FISH, 1);
                    	}
                    }
                }
            	
            	 this.isWet = false;
                 this.isShaking = false;
                 this.prevTimeWolfIsShaking = 0.0F;
                 this.timeWolfIsShaking = 0.0F;
            }

            if (this.timeWolfIsShaking > 0.4F) {
                float f = (float)this.getEntityBoundingBox().minY;
                int i = (int)(MathHelper.sin((this.timeWolfIsShaking - 0.4F) * (float)Math.PI) * 7.0F);

                for (int j = 0; j < i; ++j)
                {
                    float f1 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width * 0.5F;
                    float f2 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width * 0.5F;
                    this.worldObj.spawnParticle(EnumParticleTypes.WATER_SPLASH, this.posX + (double)f1, (double)(f + 0.8F), this.posZ + (double)f2, this.motionX, this.motionY, this.motionZ, new int[0]);
                }
            }
        }
        
        if(this.rand.nextInt(200) == 0) {
        	this.hiyaMaster = true;
        }
        
        if (((this.isBegging()) || (this.hiyaMaster)) && (!this.isWolfHappy))
        {
        	this.isWolfHappy = true;
          	this.timeWolfIsHappy = 0.0F;
          	this.prevTimeWolfIsHappy = 0.0F;
        }
        else  {
        	hiyaMaster = false;
        }
        if (this.isWolfHappy)
        {
        	if (this.timeWolfIsHappy % 1.0F == 0.0F)
        	{
        		this.playSound(SoundEvents.ENTITY_WOLF_PANT, this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
        	}
        	this.prevTimeWolfIsHappy = this.timeWolfIsHappy;
        	this.timeWolfIsHappy += 0.05F;
        	if (this.prevTimeWolfIsHappy >= 8.0F)
        	{
        		this.isWolfHappy = false;
        		this.prevTimeWolfIsHappy = 0.0F;
        		this.timeWolfIsHappy = 0.0F;
        	}
        }
        
        if(this.isTamed()) {
    		EntityPlayer player = (EntityPlayer)this.getOwner();
    		
    		if(player != null) {
    			float distanceToOwner = player.getDistanceToEntity(this);

                if (distanceToOwner <= 2F && this.hasBone()) {
                	if(!this.worldObj.isRemote) {
                		this.entityDropItem(new ItemStack(ModItems.throwBone, 1, 1), 0.0F);
                	}
                	
                    this.setHasBone(false);
                }
    		}
    	}
        
        TalentHelper.onUpdate(this);
    }
    
    @Nullable
    @Override
    public Entity getControllingPassenger() {
        return this.getPassengers().isEmpty() ? null : (Entity)this.getPassengers().get(0);
    }
    
    public boolean isControllingPassengerPlayer() {
        return this.getControllingPassenger() instanceof EntityPlayer;
    }
    
    @Override
    public void moveEntityWithHeading(float strafe, float forward) {
        if(this.isControllingPassengerPlayer()) {
        	 EntityLivingBase entitylivingbase = (EntityLivingBase)this.getControllingPassenger();
             this.rotationYaw = entitylivingbase.rotationYaw;
             this.prevRotationYaw = this.rotationYaw;
             this.rotationPitch = entitylivingbase.rotationPitch * 0.5F;
             this.setRotation(this.rotationYaw, this.rotationPitch);
             this.renderYawOffset = this.rotationYaw;
             this.rotationYawHead = this.renderYawOffset;
             strafe = entitylivingbase.moveStrafing * 0.75F;
             forward = entitylivingbase.moveForward;

            if (forward <= 0.0F)
                forward *= 0.5F;

            if (this.onGround) {
                if (forward > 0.0F) {
                    float f2 = MathHelper.sin(this.rotationYaw * (float)Math.PI / 180.0F);
                    float f3 = MathHelper.cos(this.rotationYaw * (float)Math.PI / 180.0F);
                    this.motionX += (double)(-0.4F * f2 * 0.05F); // May change
                    this.motionZ += (double)(0.4F * f3 * 0.05F);
                }
            }

            this.stepHeight = 1.0F;
            this.jumpMovementFactor = this.getAIMoveSpeed() * 0.4F;

            if (this.canPassengerSteer())
            {
            	this.setAIMoveSpeed(this.getAIMoveSpeed() * 0.4F);
                super.moveEntityWithHeading(strafe, forward);
            }
           	else if (entitylivingbase instanceof EntityPlayer)
         	{
           		this.motionX = 0.0D;
           		this.motionY = 0.0D;
           		this.motionZ = 0.0D;
         	}

            this.prevLimbSwingAmount = this.limbSwingAmount;
            double d0 = this.posX - this.prevPosX;
            double d1 = this.posZ - this.prevPosZ;
            float f4 = MathHelper.sqrt_double(d0 * d0 + d1 * d1) * 4.0F;

            if (f4 > 1.0F)
                f4 = 1.0F;

            this.limbSwingAmount += (f4 - this.limbSwingAmount) * 0.4F;
            this.limbSwing += this.limbSwingAmount;
        }
        else {
            this.stepHeight = 0.5F;
            this.jumpMovementFactor = 0.02F;
            super.moveEntityWithHeading(strafe, forward);
        }
    }
    
    @Override
    public float getAIMoveSpeed() {
    	double speed = 0.30000001192092896D;
    	
    	speed += TalentHelper.addToMoveSpeed(this);
    	
    	if((!(this.getAttackTarget() instanceof EntityDog) && !(this.getAttackTarget() instanceof EntityPlayer)) || this.isControllingPassengerPlayer())
    		if (this.levels.isDireDog())
    			speed += 0.05D;
    	
        return (float)speed;
    }

    @SideOnly(Side.CLIENT)
    public boolean isDogWet() {
        return this.isWet;
    }


    /**
     * Used when calculating the amount of shading to apply while the wolf is wet.
     */
    @SideOnly(Side.CLIENT)
    public float getShadingWhileWet(float p_70915_1_)
    {
        return 0.75F + (this.prevTimeWolfIsShaking + (this.timeWolfIsShaking - this.prevTimeWolfIsShaking) * p_70915_1_) / 2.0F * 0.25F;
    }

    @SideOnly(Side.CLIENT)
    public float getShakeAngle(float p_70923_1_, float p_70923_2_)
    {
        float f = (this.prevTimeWolfIsShaking + (this.timeWolfIsShaking - this.prevTimeWolfIsShaking) * p_70923_1_ + p_70923_2_) / 1.8F;

        if (f < 0.0F)
        {
            f = 0.0F;
        }
        else if (f > 1.0F)
        {
            f = 1.0F;
        }

        return MathHelper.sin(f * (float)Math.PI) * MathHelper.sin(f * (float)Math.PI * 11.0F) * 0.15F * (float)Math.PI;
    }
    
    public boolean isImmortal() {
        return this.isTamed() && Constants.DOGS_IMMORTAL || this.levels.isDireDog();
    }
    
    @Override
    public float getEyeHeight() {
        return this.height * 0.8F;
    }

    @SideOnly(Side.CLIENT)
    public float getInterestedAngle(float p_70917_1_) {
        return (this.headRotationCourseOld + (this.headRotationCourse - this.headRotationCourseOld) * p_70917_1_) * 0.15F * (float)Math.PI;
    }

    @Override
    public int getVerticalFaceSpeed() {
        return this.isSitting() ? 20 : super.getVerticalFaceSpeed();
    }

    @Override
    public void fall(float distance, float damageMultiplier) {
        float[] ret = net.minecraftforge.common.ForgeHooks.onLivingFall(this, distance, damageMultiplier);
        if (ret == null) return;
        distance = ret[0]; damageMultiplier = ret[1];
        
        if(this.isBeingRidden())
        	 for (Entity entity : this.getPassengers())
                 entity.fall(distance, damageMultiplier);
        
        PotionEffect potioneffect = this.getActivePotionEffect(MobEffects.JUMP_BOOST);
        float f2 = potioneffect != null ? (float)(potioneffect.getAmplifier() + 1) : 0.0F;
        int i = MathHelper.ceiling_float_int(((distance - 3.0F - f2) - TalentHelper.fallProtection(this)) * damageMultiplier);

        if (i > 0 && !TalentHelper.isImmuneToFalls(this)) {
        	this.playSound(this.getFallSound(i), 1.0F, 1.0F);
            this.attackEntityFrom(DamageSource.fall, (float)i);
            int j = MathHelper.floor_double(this.posX);
            int k = MathHelper.floor_double(this.posY - 0.20000000298023224D);
            int l = MathHelper.floor_double(this.posZ);
            IBlockState iblockstate = this.worldObj.getBlockState(new BlockPos(j, k, l));

            if(iblockstate.getMaterial() != Material.AIR) {
                SoundType soundtype = iblockstate.getBlock().getSoundType(iblockstate, worldObj, new BlockPos(j, k, l), this);
                this.playSound(soundtype.getFallSound(), soundtype.getVolume() * 0.5F, soundtype.getPitch() * 0.75F);
            }
        }
    }

    @Override
    public boolean attackEntityFrom(DamageSource damageSource, float damage) {
        if (this.isEntityInvulnerable(damageSource))
            return false;
        else {
        	if(!TalentHelper.attackEntityFrom(this, damageSource, damage))
        		return false;
        	
            Entity entity = damageSource.getEntity();
            if (this.aiSit != null)
            	this.aiSit.setSitting(false);

            if (entity != null && !(entity instanceof EntityPlayer) && !(entity instanceof EntityArrow))
                damage = (damage + 1.0F) / 2.0F;

            return super.attackEntityFrom(damageSource, damage);
        }
    }

    @Override
    public boolean attackEntityAsMob(Entity entity) {
    	if(!TalentHelper.shouldDamageMob(this, entity))
    		return false;
    	
    	int damage = 4 + (MathHelper.floor_double(this.effectiveLevel()) + 1) / 2;
        damage = TalentHelper.attackEntityAsMob(this, entity, damage);
        
        if (entity instanceof EntityZombie)
            ((EntityZombie)entity).setAttackTarget(this);
        
        return entity.attackEntityFrom(DamageSource.causeMobDamage(this), (float)damage);
    }

    @Override
    public void setTamed(boolean p_70903_1_) {
        super.setTamed(p_70903_1_);

        if (p_70903_1_)
            this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0D);
        else
            this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(8.0D);
    }

    public void mountTo(EntityLivingBase entityLiving) {
        entityLiving.rotationYaw = this.rotationYaw;
        entityLiving.rotationPitch = this.rotationPitch;

        if(!this.worldObj.isRemote)
            entityLiving.startRiding(this);
    }
    
    @Override
    public boolean processInteract(EntityPlayer player, EnumHand hand, @Nullable ItemStack stack) {

        if(TalentHelper.interactWithPlayer(this, player))
        	return true;
        
        if (this.isTamed()) {
            if (stack != null) {
            	int foodValue = this.foodValue(stack);
            	
            	if(foodValue != 0 && this.getDogHunger() < 120 && this.canInteract(player) && !this.isIncapacicated()) {
            		 if(!player.capabilities.isCreativeMode && --stack.stackSize <= 0)
                         player.inventory.setInventorySlotContents(player.inventory.currentItem, (ItemStack)null);
            		
                    this.setDogHunger(this.getDogHunger() + foodValue);
                    return true;
                }
            	else if(stack.getItem() == Items.BONE && this.canInteract(player)) {
            		//if (!this.worldObj.isRemote) {
                       // if(this.isRiding())
                        //	this.dismountEntity(player);
                      	//else
                        	 this.startRiding(player);
                        	 if(this.aiSit != null)
                        		 this.aiSit.setSitting(true);
                    //}
                    return true;
                }
            	else if(stack.getItem() == Items.STICK && this.canInteract(player) && !this.isIncapacicated()) {
            		player.openGui(DoggyTalentsMod.instance, CommonProxy.GUI_ID_DOGGY, this.worldObj, this.getEntityId(), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ));
                 	return true;
                }
                else if(stack.getItem() == ModItems.radioCollar && this.canInteract(player) && !this.hasRadarCollar()&& !this.isIncapacicated()) {
                	if(!player.capabilities.isCreativeMode && --stack.stackSize <= 0)
                         player.inventory.setInventorySlotContents(player.inventory.currentItem, (ItemStack)null);
                 	this.hasRadarCollar(true);
                 	return true;
                }
                else if(stack.getItem() instanceof IDogTreat && this.canInteract(player) && !this.isIncapacicated()) {
                 	IDogTreat treat = (IDogTreat)stack.getItem();
                 	EnumFeedBack type = treat.canGiveToDog(player, this, this.levels.getLevel(), this.levels.getDireLevel());
                 	treat.giveTreat(type, player, stack, this);
                 	return true;
                }
                else if(stack.getItem() == ModItems.collarShears && this.isOwner(player)) {
                	if(!this.worldObj.isRemote) {
                		this.setTamed(false);
                	    this.navigator.clearPathEntity();
                        this.setSitting(false);
                        this.setHealth(8);
                        this.talents.resetTalents();
                        this.setOwnerId(null);
                        this.setWillObeyOthers(false);
                        this.mode.setMode(EnumMode.DOCILE);
                        if(this.hasRadarCollar())
                        	this.dropItem(ModItems.radioCollar, 1);
                        this.hasRadarCollar(false);
                        this.reversionTime = 40;
                     }

                	return true;
                }
                else if(stack.getItem() == Items.CAKE && this.canInteract(player) && this.isIncapacicated()) {
                	if(!player.capabilities.isCreativeMode && --stack.stackSize <= 0)
                        player.inventory.setInventorySlotContents(player.inventory.currentItem, (ItemStack)null);
                 	
                    if(!this.worldObj.isRemote) {
                        this.aiSit.setSitting(true);
                        this.setHealth(this.getMaxHealth());
                        this.setDogHunger(120);
                        this.regenerationTick = 0;
                        this.setAttackTarget((EntityLivingBase)null);
                        this.playTameEffect(true);
                        this.worldObj.setEntityState(this, (byte)7);
                    }

                    return true;
                }
            }

            if(!this.worldObj.isRemote && !this.isBreedingItem(stack) && this.canInteract(player)) {
                this.aiSit.setSitting(!this.isSitting());
                this.isJumping = false;
                this.navigator.clearPathEntity();
                this.setAttackTarget((EntityLivingBase)null);
            }
        }
        else if(stack != null && stack.getItem() == ModItems.collarShears && this.reversionTime < 1 && !this.worldObj.isRemote) {
            this.setDead();
            EntityWolf wolf = new EntityWolf(this.worldObj);
            wolf.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
            this.worldObj.spawnEntityInWorld(wolf);
            return true;
        }
        else if(stack != null && stack.getItem() == Items.BONE) {
        	if(!player.capabilities.isCreativeMode && --stack.stackSize <= 0)
                player.inventory.setInventorySlotContents(player.inventory.currentItem, (ItemStack)null);

            if(!this.worldObj.isRemote) {
                if(this.rand.nextInt(3) == 0) {
                    this.setTamed(true);
                    this.navigator.clearPathEntity();
                    this.setAttackTarget((EntityLivingBase)null);
                    this.aiSit.setSitting(true);
                    this.setHealth(20.0F);
                    this.setOwnerId(player.getUniqueID());
                    this.playTameEffect(true);
                    this.worldObj.setEntityState(this, (byte)7);
                }
                else {
                    this.playTameEffect(false);
                    this.worldObj.setEntityState(this, (byte)6);
                }
            }

            return true;
        }

        return super.processInteract(player, hand, stack);
    }
    
    @Override
    protected boolean isMovementBlocked() {
        return this.isPlayerSleeping() || super.isMovementBlocked(); //this.getRidingEntity() != null || this.riddenByEntity instanceof EntityPlayer || super.isMovementBlocked();
    }

    @Override
    public double getYOffset() {
        return this.getRidingEntity() instanceof EntityPlayer ? 0.5D : 0.0D;
    }
    
    @Override
    public boolean isPotionApplicable(PotionEffect potionEffect) {
        if(this.isIncapacicated())
            return false;

        if(!TalentHelper.isPostionApplicable(this, potionEffect))
        	return false;

        return true;
    }
    
    @Override
    public void setFire(int amount) {
    	if(TalentHelper.setFire(this, amount))
    		super.setFire(amount);
    }
    
    public int foodValue(ItemStack stack) {
    	if(stack == null || stack.getItem() == null)
    		return 0;
    	
    	int foodValue = 0;
    	
    	Item item = stack.getItem();
    	
        if(stack.getItem() != Items.ROTTEN_FLESH && item instanceof ItemFood) {
            ItemFood itemfood = (ItemFood)item;

            if (itemfood.isWolfsFavoriteMeat())
            	foodValue = 40;
        }
        
        foodValue = TalentHelper.changeFoodValue(this, stack, foodValue);

        return foodValue;
    }
    
    public int masterOrder() {
    	int order = 0;
        EntityPlayer player = (EntityPlayer)this.getOwner();

        if (player != null) {
        	
            float distanceAway = player.getDistanceToEntity(this);
            ItemStack itemstack = player.inventory.getCurrentItem();

            if (itemstack != null && (itemstack.getItem() instanceof ItemTool) && distanceAway <= 20F)
                order = 1;

            if (itemstack != null && ((itemstack.getItem() instanceof ItemSword) || (itemstack.getItem() instanceof ItemBow)))
                order = 2;

            if (itemstack != null && itemstack.getItem() == Items.WHEAT)
                order = 3;
        }

        return order;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void handleStatusUpdate(byte id) {
        if (id == 8) {
            this.isShaking = true;
            this.timeWolfIsShaking = 0.0F;
            this.prevTimeWolfIsShaking = 0.0F;
        }
        else
            super.handleStatusUpdate(id);
    }
    
    public float getWagAngle(float f, float f1) {
        float f2 = (this.prevTimeWolfIsHappy + (this.timeWolfIsHappy - this.prevTimeWolfIsHappy) * f + f1) / 2.0F;
        if (f2 < 0.0F)
        	f2 = 0.0F;
        else if (f2 > 2.0F)
        	f2 %= 2.0F;
        return MathHelper.sin(f2 * (float)Math.PI * 11.0F) * 0.3F * (float)Math.PI;
      }

    @SideOnly(Side.CLIENT)
    public float getTailRotation() {
        return this.isTamed() ? (0.55F - ((this.getMaxHealth() - this.getHealth()) / (this.getMaxHealth() / 20.0F)) * 0.02F) * (float)Math.PI : ((float)Math.PI / 5F);
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return stack != null && DoggyTalentsAPI.BREED_WHITELIST.containsItem(stack);
    }

    @Override
    public boolean isPlayerSleeping() {
        return false;
    }
    
    @Override
    public boolean canBreatheUnderwater() {
        return TalentHelper.canBreatheUnderwater(this);
    }
    
    public boolean canInteract(EntityPlayer player) {
    	return this.isOwner(player) || this.willObeyOthers();
    }
    
    public int nourishment() {
        int amount = 0;

        if (this.getDogHunger() > 0) {
            amount = 40 + 4 * (MathHelper.floor_double(this.effectiveLevel()) + 1);

            if (isSitting() && this.talents.getLevel("quickhealer") == 5) {
                amount += 20 + 2 * (MathHelper.floor_double(this.effectiveLevel()) + 1);
            }

            if (!this.isSitting()) {
                amount *= 5 + this.talents.getLevel("quickhealer");
                amount /= 10;
            }
        }

        return amount;
    }
    
    @Override
    public void playTameEffect(boolean successful) {
       super.playTameEffect(successful);
    }
    
    public double effectiveLevel() {
        return (this.levels.getLevel() + this.levels.getDireLevel()) / 10.0D;
    }

    public int getTameSkin() {
    	 return this.dataManager.get(DOG_TEXTURE);
    }

    public void setTameSkin(int index) {
    	this.dataManager.set(DOG_TEXTURE, (byte)index);
    }
    
    public String getDogName() {
        return this.dataManager.get(DOG_NAME);
    }
    
    public void setDogName(String var1) {
    	this.dataManager.set(DOG_NAME, var1);
    }
    
    public void setWillObeyOthers(boolean flag) {
    	this.dataManager.set(OBEY_OTHERS, flag);
    }
    
    public boolean willObeyOthers() {
    	return this.dataManager.get(OBEY_OTHERS);
    }
    
    public int points() {
        return this.levels.getLevel() + this.levels.getDireLevel() + (this.levels.isDireDog() ? 15 : 0) + (this.getGrowingAge() < 0 ? 0 : 15);
    }

    public int spendablePoints() {
        return this.points() - this.usedPoints();
    }
    
    public int usedPoints() {
		return TalentHelper.getUsedPoints(this);
    }
    
    public int deductive(int par1) {
        byte byte0 = 0;
        switch(par1) {
        case 1: return 1;
		case 2: return 3;
        case 3: return 6;
        case 4: return 10;
        case 5: return 15;
        default: return 0;
        }
    }
    
    
    
    @Override
    public EntityDog createChild(EntityAgeable entityAgeable) {
    	EntityDog entitydog = new EntityDog(this.worldObj);
        UUID uuid = this.getOwnerId();

        if (uuid != null) {
            entitydog.setOwnerId(uuid);
            entitydog.setTamed(true);
        }
         
        entitydog.setGrowingAge(-24000 * (Constants.TEN_DAY_PUPS ? 10 : 1));

        return entitydog;
    }

    public boolean isBegging() {
        return ((Boolean)this.dataManager.get(BEGGING)).booleanValue();
    }
    
    public void setBegging(boolean beg) {
        this.dataManager.set(BEGGING, Boolean.valueOf(beg));
    }
    
    public int getDogHunger() {
		return ((Integer)this.dataManager.get(HUNGER)).intValue();
	}
    
    public void setDogHunger(int par1) {
    	this.dataManager.set(HUNGER, Integer.valueOf(par1));
    }
    
    public void hasRadarCollar(boolean flag) {
    	this.dataManager.set(RADAR_COLLAR, Boolean.valueOf(flag));
    }
    
    public boolean hasRadarCollar() {
    	return ((Boolean)this.dataManager.get(RADAR_COLLAR)).booleanValue();
    }
    
    public void setHasBone(boolean hasBone) {
    	this.hasBone = hasBone;
    }
    
    public boolean hasBone() {
    	return this.hasBone;
    }
    
    @Override
    public boolean canMateWith(EntityAnimal entityAnimal) {
        if (entityAnimal == this)
            return false;
        else if (!this.isTamed())
            return false;
        else if (!(entityAnimal instanceof EntityDog))
            return false;
        else {
            EntityDog entityDog = (EntityDog)entityAnimal;
            return !entityDog.isTamed() ? false : (entityDog.isSitting() ? false : this.isInLove() && entityDog.isInLove());
        }
    }

    @Override
    protected boolean canDespawn() {
        return false;
    }

    @Override
    public boolean shouldAttackEntity(EntityLivingBase entityToAttack, EntityLivingBase owner) {
    	if(TalentHelper.canAttackEntity(this, entityToAttack))
    		return true;
    	
        if (!(entityToAttack instanceof EntityCreeper) && !(entityToAttack instanceof EntityGhast)) {
            if (entityToAttack instanceof EntityDog) {
                EntityDog entityDog = (EntityDog)entityToAttack;

                if (entityDog.isTamed() && entityDog.getOwner() == owner)
                    return false;
            }

            return entityToAttack instanceof EntityPlayer && owner instanceof EntityPlayer && !((EntityPlayer)owner).canAttackPlayer((EntityPlayer)entityToAttack) ? false : !(entityToAttack instanceof EntityHorse) || !((EntityHorse)entityToAttack).isTame();
        }
        else {
            return false;
        }
    }
    
    @Override
    public boolean canAttackClass(Class p_70686_1_) {
    	if(TalentHelper.canAttackClass(this, p_70686_1_))
    		return true;
    	
        return super.canAttackClass(p_70686_1_);
    }
    
    public boolean isIncapacicated() {
    	return Constants.DOGS_IMMORTAL && this.getHealth() <= 1;
    }
    
    @Override
    public boolean canRiderInteract() {
        return true;
    }
    
    @Override
    public boolean shouldDismountInWater(Entity rider) {
    	if(!TalentHelper.shouldDismountInWater(this, rider))
    		return false;
    		
		return super.shouldDismountInWater(rider);
	}
    
    @Override
    public void updatePassenger(Entity passenger){
        super.updatePassenger(passenger);

        if(passenger instanceof EntityLiving) {
            EntityLiving entityliving = (EntityLiving)passenger;
            this.renderYawOffset = entityliving.renderYawOffset;
        }
    }
    
    @Override
    public boolean canBeSteered() {
        return true;
    }
    
    @Override
    public void onDeath(DamageSource cause) {
    	//if(!this.worldObj.isRemote && this.worldObj.getGameRules().getBoolean("showDeathMessages") && this.getOwner() instanceof EntityPlayerMP) {
         //   this.getOwner().addChatMessage(ChatHelper.getChatComponent(this.getDogName() + " has been incapacitated."));
        //}
    }
}