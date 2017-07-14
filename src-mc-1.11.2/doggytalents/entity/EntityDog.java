package doggytalents.entity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;

import doggytalents.DoggyTalentsMod;
import doggytalents.ModItems;
import doggytalents.api.IDogTreat;
import doggytalents.api.IDogTreat.EnumFeedBack;
import doggytalents.entity.ModeUtil.EnumMode;
import doggytalents.entity.ai.EntityAIDogBeg;
import doggytalents.entity.ai.EntityAIDogWander;
import doggytalents.entity.ai.EntityAIFetch;
import doggytalents.entity.ai.EntityAIFollowOwner;
import doggytalents.entity.ai.EntityAIModeAttackTarget;
import doggytalents.entity.ai.EntityAIOwnerHurtByTarget;
import doggytalents.entity.ai.EntityAIOwnerHurtTarget;
import doggytalents.entity.ai.EntityAIShepherdDog;
import doggytalents.lib.Constants;
import doggytalents.lib.Reference;
import doggytalents.proxy.CommonProxy;
import jline.internal.Nullable;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
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
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
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
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ProPercivalalb
 */
public class EntityDog extends EntityAbstractDog {
	
	public static final DataParameter<Byte> DOG_TEXTURE = EntityDataManager.<Byte>createKey(EntityDog.class, DataSerializers.BYTE);
	public static final DataParameter<Integer> LEVEL = EntityDataManager.<Integer>createKey(EntityDog.class, DataSerializers.VARINT);
	public static final DataParameter<Integer> LEVEL_DIRE = EntityDataManager.<Integer>createKey(EntityDog.class, DataSerializers.VARINT);
	public static final DataParameter<Integer> MODE = EntityDataManager.<Integer>createKey(EntityDog.class, DataSerializers.VARINT);
	public static final DataParameter<String> TALENTS = EntityDataManager.<String>createKey(EntityDog.class, DataSerializers.STRING);
	public static final DataParameter<Integer> HUNGER = EntityDataManager.<Integer>createKey(EntityDog.class, DataSerializers.VARINT);
	public static final DataParameter<Boolean> HAS_BONE = EntityDataManager.<Boolean>createKey(EntityDog.class, DataSerializers.BOOLEAN);
	public static final DataParameter<Boolean> FRIENDLY_FIRE = EntityDataManager.<Boolean>createKey(EntityDog.class, DataSerializers.BOOLEAN);
	public static final DataParameter<Boolean> OBEY_OTHERS = EntityDataManager.<Boolean>createKey(EntityDog.class, DataSerializers.BOOLEAN);
	public static final DataParameter<Boolean> RADAR_COLLAR = EntityDataManager.<Boolean>createKey(EntityDog.class, DataSerializers.BOOLEAN);
	public static final DataParameter<Optional<BlockPos>> BOWL_POS = EntityDataManager.<Optional<BlockPos>>createKey(EntityDog.class, DataSerializers.OPTIONAL_BLOCK_POS);
	public static final DataParameter<Optional<BlockPos>> BED_POS = EntityDataManager.<Optional<BlockPos>>createKey(EntityDog.class, DataSerializers.OPTIONAL_BLOCK_POS);

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
    public EntityAIFetch aiFetchBone;
    public TalentUtil talents;
    public LevelUtil levels;
    public ModeUtil mode;
    public CoordUtil coords;
    public Map<String, Object> objects;

    public EntityDog(World word) {
        super(word);
        this.objects = new HashMap<String, Object>();
        
        TalentHelper.onClassCreation(this);
    }
    
    @Override
    protected void initEntityAI() {
        this.aiSit = new EntityAISit(this);
        this.aiFetchBone = new EntityAIFetch(this, 20.0F);
        		
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, this.aiSit);
        this.tasks.addTask(3, new EntityAILeapAtTarget(this, 0.4F));
        this.tasks.addTask(4, new EntityAIAttackMelee(this, 1.0D, true));
        this.tasks.addTask(6, new EntityAIFollowOwner(this, 1.0D, 10.0F, 2.0F));
        this.tasks.addTask(5, this.aiFetchBone);
        this.tasks.addTask(7, new EntityAIMate(this, 1.0D));
        this.tasks.addTask(8, new EntityAIDogWander(this, 1.0D));
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
    	if(this.isTamed())
            this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0D + (this.effectiveLevel() + 1.0D));
        else
            this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(8.0D);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public boolean getAlwaysRenderNameTagForRender() {
        return this.hasCustomName();
    }
    
    @Override
    protected void entityInit() {
        super.entityInit();
        this.talents = new TalentUtil(this);
        this.levels = new LevelUtil(this);
        this.mode = new ModeUtil(this);
        this.coords = new CoordUtil(this);
        
        this.dataManager.register(DOG_TEXTURE, Byte.valueOf((byte)0));
        this.dataManager.register(TALENTS, "");
        this.dataManager.register(HUNGER, Integer.valueOf(60));
        this.dataManager.register(OBEY_OTHERS, Boolean.valueOf(false));
        this.dataManager.register(HAS_BONE, Boolean.valueOf(false));
        this.dataManager.register(FRIENDLY_FIRE, Boolean.valueOf(false));
        this.dataManager.register(RADAR_COLLAR, Boolean.valueOf(false));
        this.dataManager.register(MODE, Integer.valueOf(0));
        this.dataManager.register(LEVEL, Integer.valueOf(0));
        this.dataManager.register(LEVEL_DIRE, Integer.valueOf(0));
        this.dataManager.register(BOWL_POS, Optional.absent());
        this.dataManager.register(BED_POS, Optional.absent());
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
        tagCompound.setInteger("dogHunger", this.getDogHunger());
        tagCompound.setBoolean("willObey", this.willObeyOthers());
        tagCompound.setBoolean("friendlyFire", this.canFriendlyFire());
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
        this.setDogHunger(tagCompound.getInteger("dogHunger"));
        this.setWillObeyOthers(tagCompound.getBoolean("willObey"));
        this.setFriendlyFire(tagCompound.getBoolean("friendlyFire"));
        this.hasRadarCollar(tagCompound.getBoolean("radioCollar"));
        
        this.talents.readTalentsFromNBT(tagCompound);
        this.levels.readTalentsFromNBT(tagCompound);
        this.mode.readFromNBT(tagCompound);
        this.coords.readFromNBT(tagCompound);
        TalentHelper.readFromNBT(this, tagCompound);
        
        //Backwards Compatibility
        if(tagCompound.hasKey("dogName"))
        	this.setCustomNameTag(tagCompound.getString("dogName"));
    }
    
    @Override
    protected SoundEvent getAmbientSound() {
    	SoundEvent sound = TalentHelper.getLivingSound(this);
        return sound != null ? sound : super.getAmbientSound();
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
		            this.world.setEntityState(this, (byte)7);
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
        
        if(this.getDogHunger() == 0 && this.world.getWorldInfo().getWorldTime() % 100L == 0L && this.getHealth() > 1) {
            this.attackEntityFrom(DamageSource.GENERIC, 1);
            //this.fleeingTick = 0;
        }
        
        if (this.levels.isDireDog() && Constants.DIRE_PARTICLES) {
            for (int i = 0; i < 2; i++) {
                world.spawnParticle(EnumParticleTypes.PORTAL, this.posX + (this.rand.nextDouble() - 0.5D) * (double)this.width, (this.posY + rand.nextDouble() * (double)height) - 0.25D, posZ + (rand.nextDouble() - 0.5D) * (double)this.width, (this.rand.nextDouble() - 0.5D) * 2D, -this.rand.nextDouble(), (this.rand.nextDouble() - 0.5D) * 2D);
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
                	if(!this.world.isRemote) {
                		this.entityDropItem(new ItemStack(ModItems.THROW_BONE, 1, 1), 0.0F);
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
            float f4 = MathHelper.sqrt(d0 * d0 + d1 * d1) * 4.0F;

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
    
    public boolean isImmortal() {
        return this.isTamed() && Constants.DOGS_IMMORTAL || this.levels.isDireDog();
    }

    @Override
    public void fall(float distance, float damageMultiplier) {
    	if(!TalentHelper.isImmuneToFalls(this))
    		super.fall(distance - TalentHelper.fallProtection(this), damageMultiplier);
    }

    @Override
    public boolean attackEntityFrom(DamageSource damageSource, float damage) {
        if(this.isEntityInvulnerable(damageSource))
            return false;
        else {
        	Entity entity = damageSource.getEntity();
            //Friendly fire
            if(!this.canFriendlyFire() && entity instanceof EntityPlayer && (this.willObeyOthers() || this.isOwner((EntityPlayer)entity)))
            	return false;
        	
        	if(!TalentHelper.attackEntityFrom(this, damageSource, damage))
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
    	if(!TalentHelper.shouldDamageMob(this, entity))
    		return false;
    	
    	int damage = 4 + (MathHelper.floor(this.effectiveLevel()) + 1) / 2;
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

        if(!this.world.isRemote)
            entityLiving.startRiding(this);
    }
    
    @Override
    public boolean processInteract(EntityPlayer player, EnumHand hand) {
    	ItemStack stack = player.getHeldItem(hand);
    	
        if(TalentHelper.interactWithPlayer(this, player))
        	return true;
        
        if(this.isTamed()) {
            if(!stack.isEmpty()) {
            	int foodValue = this.foodValue(stack);
            	
            	if(foodValue != 0 && this.getDogHunger() < 120 && this.canInteract(player) && !this.isIncapacicated()) {
            		if(!player.capabilities.isCreativeMode)
                		stack.shrink(1);
            		
                    this.setDogHunger(this.getDogHunger() + foodValue);
                    return true;
                }
            	else if(stack.getItem() == Items.BONE && this.canInteract(player)) {
            		this.startRiding(player);
            		if(this.aiSit != null)
            			this.aiSit.setSitting(true);
            		
                    return true;
                }
            	else if(stack.getItem() == Items.STICK && this.canInteract(player) && !this.isIncapacicated()) {
            		player.openGui(DoggyTalentsMod.instance, CommonProxy.GUI_ID_DOGGY, this.world, this.getEntityId(), MathHelper.floor(this.posY), MathHelper.floor(this.posZ));
                 	return true;
                }
                else if(stack.getItem() == ModItems.RADIO_COLLAR && this.canInteract(player) && !this.hasRadarCollar()&& !this.isIncapacicated()) {
                	if(!player.capabilities.isCreativeMode)
                		stack.shrink(1);
                 	this.hasRadarCollar(true);
                 	return true;
                }
                else if(stack.getItem() instanceof IDogTreat && this.canInteract(player) && !this.isIncapacicated()) {
                 	IDogTreat treat = (IDogTreat)stack.getItem();
                 	EnumFeedBack type = treat.canGiveToDog(player, this, this.levels.getLevel(), this.levels.getDireLevel());
                 	treat.giveTreat(type, player, stack, this);
                 	return true;
                }
                else if(stack.getItem() == ModItems.COLLAR_SHEARS && this.isOwner(player)) {
                	if(!this.world.isRemote) {
                		this.setTamed(false);
                	    this.navigator.clearPathEntity();
                        this.setSitting(false);
                        this.setHealth(8);
                        this.talents.resetTalents();
                        this.setOwnerId(null);
                        this.setWillObeyOthers(false);
                        this.mode.setMode(EnumMode.DOCILE);
                        if(this.hasRadarCollar())
                        	this.dropItem(ModItems.RADIO_COLLAR, 1);
                        this.hasRadarCollar(false);
                        this.reversionTime = 40;
                     }

                	return true;
                }
                else if(stack.getItem() == Items.CAKE && this.canInteract(player) && this.isIncapacicated()) {
                	if(!player.capabilities.isCreativeMode)
                		stack.shrink(1);
                	
                    if(!this.world.isRemote) {
                        this.aiSit.setSitting(true);
                        this.setHealth(this.getMaxHealth());
                        this.setDogHunger(120);
                        this.regenerationTick = 0;
                        this.setAttackTarget((EntityLivingBase)null);
                        this.playTameEffect(true);
                        this.world.setEntityState(this, (byte)7);
                    }

                    return true;
                }
            }

            if(!this.world.isRemote && !this.isBreedingItem(stack) && this.canInteract(player)) {
                this.aiSit.setSitting(!this.isSitting());
                this.isJumping = false;
                this.navigator.clearPathEntity();
                this.setAttackTarget((EntityLivingBase)null);
            }
        }
        else if(stack != null && stack.getItem() == ModItems.COLLAR_SHEARS && this.reversionTime < 1 && !this.world.isRemote) {
            this.setDead();
            EntityWolf wolf = new EntityWolf(this.world);
            wolf.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
            this.world.spawnEntity(wolf);
            return true;
        }
        else if(stack != null && stack.getItem() == Items.BONE) {
        	if(!player.capabilities.isCreativeMode)
        		stack.shrink(1);

            if(!this.world.isRemote) {
                if(this.rand.nextInt(3) == 0) {
                    this.setTamed(true);
                    this.navigator.clearPathEntity();
                    this.setAttackTarget((EntityLivingBase)null);
                    this.aiSit.setSitting(true);
                    this.setHealth(20.0F);
                    this.setOwnerId(player.getUniqueID());
                    this.playTameEffect(true);
                    this.world.setEntityState(this, (byte)7);
                }
                else {
                    this.playTameEffect(false);
                    this.world.setEntityState(this, (byte)6);
                }
            }

            return true;
        }

        return super.processInteract(player, hand);
    }
    
    @Override
    protected boolean isMovementBlocked() {
        return this.isPlayerSleeping() || super.isMovementBlocked(); //this.getRidingEntity() != null || this.riddenByEntity instanceof EntityPlayer || super.isMovementBlocked();
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
    	if(stack.isEmpty() || stack.getItem() == null)
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
    
    public float getWagAngle(float partialTickTime, float offset) {
        float f = (this.prevTimeWolfIsHappy + (this.timeWolfIsHappy - this.prevTimeWolfIsHappy) * partialTickTime + offset) / 2.0F;
        if(f < 0.0F) f = 0.0F;
        else if(f > 2.0F) f %= 2.0F;
        return MathHelper.sin(f * (float)Math.PI * 11.0F) * 0.3F * (float)Math.PI;
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
    
    public boolean canInteract(EntityPlayer player) {
    	return this.isOwner(player) || this.willObeyOthers();
    }
    
    public int nourishment() {
        int amount = 0;

        if (this.getDogHunger() > 0) {
            amount = 40 + 4 * (MathHelper.floor(this.effectiveLevel()) + 1);

            if (isSitting() && this.talents.getLevel("quickhealer") == 5) {
                amount += 20 + 2 * (MathHelper.floor(this.effectiveLevel()) + 1);
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
    	EntityDog entitydog = new EntityDog(this.world);
        UUID uuid = this.getOwnerId();

        if(uuid != null) {
            entitydog.setOwnerId(uuid);
            entitydog.setTamed(true);
        }
         
        entitydog.setGrowingAge(-24000 * (Constants.TEN_DAY_PUPS ? 10 : 1));

        return entitydog;
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
    	this.dataManager.set(HAS_BONE, hasBone);
    }
    
    public boolean hasBone() {
    	return ((Boolean)this.dataManager.get(HAS_BONE)).booleanValue();
    }

    @Override
    public boolean shouldAttackEntity(EntityLivingBase entityToAttack, EntityLivingBase owner) {
    	if(TalentHelper.canAttackEntity(this, entityToAttack))
    		return true;
    	
        return super.shouldAttackEntity(entityToAttack, owner);
    }
    
    @Override
    public boolean canAttackClass(Class<? extends EntityLivingBase> cls) {
    	if(TalentHelper.canAttackClass(this, cls))
    		return true;
    	
        return super.canAttackClass(cls);
    }
    
    public boolean isIncapacicated() {
    	return Constants.DOGS_IMMORTAL && this.getHealth() <= 1;
    }
    
    @Override
    public boolean shouldDismountInWater(Entity rider) {
    	if(!TalentHelper.shouldDismountInWater(this, rider))
    		return false;
    		
		return super.shouldDismountInWater(rider);
	}
    
	
	private void onFinishShaking() {
		if(!this.world.isRemote) {
			int lvlFisherDog = this.talents.getLevel("fisherdog");
			int lvlHellHound = this.talents.getLevel("hellhound");
			
			if(this.rand.nextInt(15) < lvlFisherDog * 2)
				this.dropItem(this.rand.nextInt(15) < lvlHellHound * 2 ? Items.COOKED_FISH : Items.FISH, 1);
		}
	}
}