package doggytalents.entity;

import java.util.HashMap;
import java.util.Map;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import doggytalents.DoggyTalents;
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
import doggytalents.helper.DogUtil;
import doggytalents.inventory.InventoryTreatBag;
import doggytalents.lib.Constants;
import doggytalents.lib.Reference;
import doggytalents.proxy.CommonProxy;
import net.minecraft.block.BlockColored;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAISit;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITargetNonTamed;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

/**
 * @author ProPercivalalb
 */
public class EntityDog extends EntityAbstractDog {
	
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
    
    //Timers
    private int hungerTick;
   	private int prevHungerTick;
    private int healingTick;
    private int prevHealingTick;
    private int regenerationTick;
    private int prevRegenerationTick;
    private int foodBowlCheck;

    public EntityDog(World word) {
        super(word);
        this.objects = new HashMap<String, Object>();
        
        this.aiSit = new EntityAISit(this);
        this.aiFetchBone = new EntityAIFetch(this, 1.0D, 20.0F);
        		
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, this.aiSit);
        this.tasks.addTask(3, new EntityAILeapAtTarget(this, 0.4F));
        this.tasks.addTask(4, new EntityAIAttackOnCollide(this, 1.0D, true));
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
        this.targetTasks.addTask(3, new EntityAIHurtByTarget(this, true));
        this.targetTasks.addTask(4, new EntityAITargetNonTamed(this, EntitySheep.class, 200, false));
        this.targetTasks.addTask(6, new EntityAIShepherdDog(this, EntityAnimal.class, 0, false));
        this.setTamed(false);
        TalentHelper.onClassCreation(this);
    }
    
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.30000001192092896D);
        this.updateEntityAttributes();
    }
    
    public void updateEntityAttributes() {
    	if(this.isTamed())
            this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(20.0D);
        else
            this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(8.0D);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public boolean getAlwaysRenderNameTagForRender() {
        return this.hasCustomNameTag();
    }
    
    @Override
    protected void entityInit() {
        super.entityInit();
        this.talents = new TalentUtil(this);
        this.levels = new LevelUtil(this);
        this.mode = new ModeUtil(this);
        this.coords = new CoordUtil(this);
        
        this.dataWatcher.addObject(20, new Integer((-2))); //Dog Collar
        this.dataWatcher.addObject(21, new String("")); //Dog Name
        this.dataWatcher.addObject(22, new String("")); //Talent Data
        this.dataWatcher.addObject(23, new Integer(60)); //Dog Hunger
        this.dataWatcher.addObject(24, new String("0:0")); //Level Data
        this.dataWatcher.addObject(25, new Integer(0)); //Radio Collar
        this.dataWatcher.addObject(26, new Integer(0)); //Obey Others
        this.dataWatcher.addObject(27, new Integer(0)); //Dog Mode
        this.dataWatcher.addObject(28, "-1:-1:-1:-1:-1:-1"); //Dog Mode
        
       /** this.dataManager.register(COLLAR_COLOUR, -2);
        this.dataManager.register(TALENTS, "");
        this.dataManager.register(HUNGER, Integer.valueOf(60));
        this.dataManager.register(OBEY_OTHERS, Boolean.valueOf(false));
        this.dataManager.register(FRIENDLY_FIRE, Boolean.valueOf(false));
        this.dataManager.register(HAS_BONE, Boolean.valueOf(false));
        this.dataManager.register(RADAR_COLLAR, Boolean.valueOf(false));
        this.dataManager.register(MODE, Integer.valueOf(0));
        this.dataManager.register(LEVEL, Integer.valueOf(0));
        this.dataManager.register(LEVEL_DIRE, Integer.valueOf(0));
        this.dataManager.register(BOWL_POS, Optional.absent());
        this.dataManager.register(BED_POS, Optional.absent());**/
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setString("version", Reference.MOD_VERSION);
        
        //TODO tagCompound.setInteger("doggyTex", this.getTameSkin());
        tagCompound.setInteger("collarColour", this.getCollarColour());
        tagCompound.setInteger("dogHunger", this.getDogHunger());
        tagCompound.setBoolean("willObey", this.willObeyOthers());
        tagCompound.setBoolean("friendlyFire", this.canFriendlyFire());
        tagCompound.setBoolean("radioCollar", this.hasRadarCollar());
        tagCompound.setBoolean("sunglasses", this.hasSunglasses());
        tagCompound.setBoolean("cape", this.hasCape());
        
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
        //TODO this.setTameSkin(tagCompound.getInteger("doggyTex"));
        if(tagCompound.hasKey("collarColour", 99))
        	this.setCollarColour(tagCompound.getInteger("collarColour"));
        this.setDogHunger(tagCompound.getInteger("dogHunger"));
        this.setWillObeyOthers(tagCompound.getBoolean("willObey"));
        this.setFriendlyFire(tagCompound.getBoolean("friendlyFire"));
        this.hasRadarCollar(tagCompound.getBoolean("radioCollar"));
        this.hasSunglasses(tagCompound.getBoolean("sunglasses"));
        this.hasCape(tagCompound.getBoolean("cape"));
        
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
    protected String getLivingSound() {
    	String sound = TalentHelper.getLivingSound(this);
        return sound != null ? sound : super.getLivingSound();
    }
    
    public EntityAISit getSitAI() {
    	return this.aiSit;
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        
        if(Constants.IS_HUNGER_ON) {
        	this.prevHungerTick = this.hungerTick;
        	
	        if(this.riddenByEntity == null && !this.isSitting() /** && !this.mode.isMode(EnumMode.WANDERING) && !this.level.isDireDog() || worldObj.getWorldInfo().getWorldTime() % 2L == 0L **/)
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
        
        if(this.getHealth() <= 0 && this.isImmortal()) {
            this.deathTime = 0;
            this.setHealth(1);
        }
        
        if(this.getDogHunger() <= 0 && this.worldObj.getWorldInfo().getWorldTime() % 100L == 0L && this.getHealth() > 1) {
            this.attackEntityFrom(DamageSource.generic, 1);
            //this.fleeingTick = 0;
        }
        
        if(this.levels.isDireDog() && Constants.DIRE_PARTICLES)
            for(int i = 0; i < 2; i++)
            	worldObj.spawnParticle("portal", this.posX + (this.rand.nextDouble() - 0.5D) * (double)this.width, (this.posY + rand.nextDouble() * (double)height) - 0.25D, posZ + (rand.nextDouble() - 0.5D) * (double)this.width, (this.rand.nextDouble() - 0.5D) * 2D, -this.rand.nextDouble(), (this.rand.nextDouble() - 0.5D) * 2D);
        
        if(this.reversionTime > 0)
        	this.reversionTime -= 1;
        
        //Remove dog from players head if sneaking
        if(this.ridingEntity instanceof EntityPlayer)
        	if(this.ridingEntity.isSneaking()) {
        		this.ridingEntity.riddenByEntity = null;
        		this.ridingEntity = null;
        	}
        
        //Check if dog bowl still exists every 50t/2.5s, if not remove
        //TOOD
        /**if(this.coords.hasBowlPos() && this.foodBowlCheck++ > 50) {
        	if(this.worldObj.isBlockLoaded(this.coords.getBowlPos()))
        		if(this.worldObj.getBlockState(this.coords.getBowlPos()).getBlock() != ModBlocks.FOOD_BOWL)
        			this.coords.resetBowlPosition();
        	
        	this.foodBowlCheck = 0;
        }*/
        
        TalentHelper.onLivingUpdate(this);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        
        if(this.rand.nextInt(200) == 0)
        	this.hiyaMaster = true;
        
        if(((this.isBegging()) || (this.hiyaMaster)) && (!this.isWolfHappy)) {
        	this.isWolfHappy = true;
          	this.timeWolfIsHappy = 0.0F;
          	this.prevTimeWolfIsHappy = 0.0F;
        }
        else
        	this.hiyaMaster = false;
        
        if(this.isWolfHappy) {
        	if(this.timeWolfIsHappy % 1.0F == 0.0F)
        		this.worldObj.playSoundAtEntity(this, "mob.wolf.panting", this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
        	this.prevTimeWolfIsHappy = this.timeWolfIsHappy;
        	this.timeWolfIsHappy += 0.05F;
        	if (this.prevTimeWolfIsHappy >= 8.0F) {
        		this.isWolfHappy = false;
        		this.prevTimeWolfIsHappy = 0.0F;
        		this.timeWolfIsHappy = 0.0F;
        	}
        }
        
        if(this.isTamed()) {
    		EntityPlayer player = (EntityPlayer)this.getOwner();
    		
    		if(player != null) {
    			float distanceToOwner = player.getDistanceToEntity(this);

                if(distanceToOwner <= 2F && this.hasBone()) {
                	if(!this.worldObj.isRemote) {
                		this.entityDropItem(new ItemStack(ModItems.THROW_BONE, 1, 1), 0.0F);
                	}
                	
                    this.setHasBone(false);
                }
    		}
    	}
        
        if(this.talents.getLevel("wolfmount") > 0) {
        	this.setSize(1.2F, 1.2F);
        }
        
        TalentHelper.onUpdate(this);
    }
    
    public boolean isControllingPassengerPlayer() {
        return this.riddenByEntity instanceof EntityPlayer;
    }
    
    @Override
	public void moveEntityWithHeading(float strafe, float forward) {
		if(this.isControllingPassengerPlayer()) {
			EntityLivingBase entitylivingbase = (EntityLivingBase)this.riddenByEntity;
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
           this.jumpMovementFactor = this.getAIMoveSpeed() * 0.3F;

           if (entitylivingbase instanceof EntityPlayer)
           {
        	   float f = (float)this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue() * 0.5F;

        	   this.setAIMoveSpeed(f);
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
           float f4 = MathHelper.sqrt_float((float)(d0 * d0 + d1 * d1)) * 4.0F;

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
    
    public boolean isImmortal() {
        return this.isTamed() && Constants.DOGS_IMMORTAL || this.levels.isDireDog();
    }

    @Override
    public void fall(float distance) {
    	if(!TalentHelper.isImmuneToFalls(this))
    		super.fall(distance - TalentHelper.fallProtection(this) - (this.riddenByEntity instanceof EntityPlayer ? 1 : 0));
    }

    @Override
    public boolean attackEntityFrom(DamageSource damageSource, float damage) {
        if(this.isEntityInvulnerable())
            return false;
        else {
            Entity entity = damageSource.getEntity();
            //Friendly fire
            if(!this.canFriendlyFire() && entity instanceof EntityPlayer && (this.willObeyOthers() || this.func_152114_e((EntityPlayer)entity)))
            	return false;
            
        	if(!TalentHelper.attackEntityFrom(this, damageSource, damage))
        		return false;
        	
            if(this.aiSit != null)
            	this.aiSit.setSitting(false);

            if(entity != null && !(entity instanceof EntityPlayer) && !(entity instanceof EntityArrow))
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
        
        if(entity instanceof EntityZombie)
            ((EntityZombie)entity).setAttackTarget(this);
        
        return entity.attackEntityFrom(DamageSource.causeMobDamage(this), (float)damage);
    }

    @Override
    public void setTamed(boolean p_70903_1_) {
        super.setTamed(p_70903_1_);

        if (p_70903_1_)
            this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(20.0D);
        else
            this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(8.0D);
    }
    
    @Override
    public boolean interact(EntityPlayer player) {
    	ItemStack stack = player.getHeldItem();
    	
        if(TalentHelper.interactWithPlayer(this, player))
        	return true;
        
        if(this.isTamed()) {
            if(stack != null) {
            	int foodValue = this.foodValue(stack);
            	
            	if(foodValue != 0 && this.getDogHunger() < 120 && this.canInteract(player) && !this.isIncapacicated()) {
            		if(!player.capabilities.isCreativeMode)
            			--stack.stackSize;
            		
                    this.setDogHunger(this.getDogHunger() + foodValue);
                    if(stack.getItem() == ModItems.CHEW_STICK) {
                    	//this.addPotionEffect(new PotionEffect(MobEffects.GLOWING, 100, 1, false, true));
                    	this.addPotionEffect(new PotionEffect(Potion.moveSpeed.getId(), 200, 6, false));
                    	this.addPotionEffect(new PotionEffect(Potion.regeneration.getId(), 100, 2, false));
                    }
                    return true;
                }
            	else if(stack.getItem() == Items.bone && this.canInteract(player)) {
            		if(!this.worldObj.isRemote) {
                        if(this.ridingEntity != null)
                        	this.mountEntity(null);
                        else
                         	this.mountEntity(player);
                    }
            		
            		if(this.aiSit != null)
            			this.aiSit.setSitting(true);
            		
                    return true;
                }
            	else if(stack.getItem() == Items.stick && this.canInteract(player) && !this.isIncapacicated()) {
            		player.openGui(DoggyTalents.INSTANCE, CommonProxy.GUI_ID_DOGGY, this.worldObj, this.getEntityId(), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ));
                 	return true;
                }
                else if(stack.getItem() == ModItems.RADIO_COLLAR && this.canInteract(player) && !this.hasRadarCollar() && !this.isIncapacicated()) {
                 	this.hasRadarCollar(true);
                 	
                	if(!player.capabilities.isCreativeMode)
                		--stack.stackSize;
                 	return true;
                }
                else if(stack.getItem() == ModItems.WOOL_COLLAR && this.canInteract(player) && !this.hasCollar() && !this.isIncapacicated()) {
                	int colour = -1;
                	
                	if(stack.hasTagCompound() && stack.getTagCompound().hasKey("collar_colour"))
                		colour = stack.getTagCompound().getInteger("collar_colour");
                	
                 	this.setCollarColour(colour);
                 	
                   	if(!player.capabilities.isCreativeMode)
                   		--stack.stackSize;
                 	return true;
                }
                else if(stack.getItem() == ModItems.CAPE && this.canInteract(player) && !this.hasCape() && !this.isIncapacicated()) { 
                	this.hasCape(true);
                	if(!player.capabilities.isCreativeMode)
                   		--stack.stackSize;
                 	return true;
                }
                else if(stack.getItem() == ModItems.SUNGLASSES && this.canInteract(player) && !this.hasSunglasses() && !this.isIncapacicated()) { 
                	this.hasSunglasses(true);
                	if(!player.capabilities.isCreativeMode)
                   		--stack.stackSize;
                 	return true;
                }
                else if(stack.getItem() instanceof IDogTreat && this.canInteract(player) && !this.isIncapacicated()) {
                 	IDogTreat treat = (IDogTreat)stack.getItem();
                 	EnumFeedBack type = treat.canGiveToDog(player, this, this.levels.getLevel(), this.levels.getDireLevel());
                 	treat.giveTreat(type, player, stack, this);
                 	return true;
                }
                else if(stack.getItem() == ModItems.COLLAR_SHEARS && this.canInteract(player)) {
                	if(!this.worldObj.isRemote) {
                		if(this.hasCollar()) {
                			this.reversionTime = 40;
                			ItemStack collarDrop = new ItemStack(ModItems.WOOL_COLLAR, 1, 0);
                			collarDrop.setTagCompound(new NBTTagCompound());
                			collarDrop.getTagCompound().setInteger("collar_colour", this.getCollarColour());
                	     	this.entityDropItem(collarDrop, 1);
                	     	this.setCollarColour(-2);
                		}
                		else if(this.reversionTime < 1) {
                			this.setTamed(false);
	                	    this.getNavigator().clearPathEntity();
	                        this.setSitting(false);
	                        this.setHealth(8);
	                        this.talents.resetTalents();
	                        this.func_152115_b(null);
	                        this.setWillObeyOthers(false);
	                        this.mode.setMode(EnumMode.DOCILE);
	                        if(this.hasRadarCollar())
	                        	this.dropItem(ModItems.RADIO_COLLAR, 1);
	                        this.hasRadarCollar(false);
	                        this.reversionTime = 40;
                		}
                     }

                	return true;
                }
                else if(stack.getItem() == Items.cake && this.canInteract(player) && this.isIncapacicated()) {
                	if(!player.capabilities.isCreativeMode)
                		--stack.stackSize;
                	
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
                else if(stack.getItem() == Items.dye && this.canInteract(player)) {
                    if(!this.hasCollar())
                    	return true;
                    
                    
                    if(this.hasNoColour()) {
                    	float[] rgb = EntitySheep.fleeceColorTable[BlockColored.func_150032_b(stack.getItemDamage())];
                    	int i1 = (int)(rgb[0] * 255F);
                        int j1 = (int)(rgb[1] * 255F);
                        int k1 = (int)(rgb[2] * 255F);
                    	this.setCollarColour((((i1 << 8) + j1) << 8) + k1);
                    }
                    else {
                        int[] aint = new int[3];
                        int i = 0;
                        int count = 2; //The number of different sources of colour
                        
                        int l = this.getCollarColour();
                        float f = (float)(l >> 16 & 255) / 255.0F;
                        float f1 = (float)(l >> 8 & 255) / 255.0F;
                        float f2 = (float)(l & 255) / 255.0F;
                        i = (int)((float)i + Math.max(f, Math.max(f1, f2)) * 255.0F);
                        aint[0] = (int)((float)aint[0] + f * 255.0F);
                        aint[1] = (int)((float)aint[1] + f1 * 255.0F);
                        aint[2] = (int)((float)aint[2] + f2 * 255.0F);

                        float[] afloat = EntitySheep.fleeceColorTable[BlockColored.func_150032_b(stack.getItemDamage())];
                        int l1 = (int)(afloat[0] * 255.0F);
                        int i2 = (int)(afloat[1] * 255.0F);
                        int j2 = (int)(afloat[2] * 255.0F);
                        i += Math.max(l1, Math.max(i2, j2));
                        aint[0] += l1;
                        aint[1] += i2;
                        aint[2] += j2;

                        int i1 = aint[0] / count;
                     	int j1 = aint[1] / count;
                    	int k1 = aint[2] / count;
                     	float f3 = (float)i / (float)count;
                     	float f4 = (float)Math.max(i1, Math.max(j1, k1));
                     	i1 = (int)((float)i1 * f3 / f4);
                     	j1 = (int)((float)j1 * f3 / f4);
                     	k1 = (int)((float)k1 * f3 / f4);
                     	int k2 = (i1 << 8) + j1;
                     	k2 = (k2 << 8) + k1;
                     	this.setCollarColour(k2);
                    }
                    return true;
                }
                else if(stack.getItem() == ModItems.TREAT_BAG && this.getDogHunger() < 120 && this.canInteract(player)) {
                	
                	InventoryTreatBag treatBag = new InventoryTreatBag(player, player.inventory.currentItem, stack);
            		treatBag.openInventory();
                	
                	int slotIndex = DogUtil.getFirstSlotWithFood(this, treatBag);
                 	if(slotIndex >= 0)
                 		DogUtil.feedDog(this, treatBag, slotIndex);
                 	
            		treatBag.closeInventory();
                 	return true;
                }
            }

            if(!this.worldObj.isRemote && !this.isBreedingItem(stack) && this.canInteract(player)) {
                this.aiSit.setSitting(!this.isSitting());
                this.isJumping = false;
                this.getNavigator().clearPathEntity();
                this.setAttackTarget((EntityLivingBase)null);
                return true;
            }
        }
        else if(stack != null && stack.getItem() == ModItems.COLLAR_SHEARS && this.reversionTime < 1 && !this.worldObj.isRemote) {
            this.setDead();
            EntityWolf wolf = new EntityWolf(this.worldObj);
            wolf.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
            this.worldObj.spawnEntityInWorld(wolf);
            return true;
        }
        else if(stack != null && stack.getItem() == Items.bone) {
        	if(!player.capabilities.isCreativeMode)
        		--stack.stackSize;

            if(!this.worldObj.isRemote) {
                if(this.rand.nextInt(3) == 0) {
                    this.setTamed(true);
                    this.getNavigator().clearPathEntity();
                    this.setAttackTarget((EntityLivingBase)null);
                    this.aiSit.setSitting(true);
                    this.setHealth(20.0F);
                    this.func_152115_b(player.getUniqueID().toString());
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

        return false;
    }
    
    @Override
    protected boolean isMovementBlocked() {
        return this.isPlayerSleeping() || super.isMovementBlocked(); //this.ridingEntity != null || this.riddenByEntity instanceof EntityPlayer || super.isMovementBlocked();
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
    	if(stack == null)
    		return 0;
    	
    	int foodValue = 0;
    	
    	Item item = stack.getItem();
    	
        if(stack.getItem() != Items.rotten_flesh && item instanceof ItemFood) {
            ItemFood itemfood = (ItemFood)item;

            if (itemfood.isWolfsFavoriteMeat())
            	foodValue = 40;
        }
        else if(stack.getItem() == ModItems.CHEW_STICK) {
        	return 10;
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

            if (itemstack != null && itemstack.getItem() == Items.wheat)
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
    	return this.func_152114_e(player) || this.willObeyOthers();
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
    
    public void setWillObeyOthers(boolean flag) {
    	this.dataWatcher.updateObject(26, flag ? 1 : 0);
    }
    
    public boolean willObeyOthers() {
    	return this.dataWatcher.getWatchableObjectInt(26) != 0;
    }
    
    public void setFriendlyFire(boolean flag) {
    	this.setCustomData(2, flag);
    }
    
    public boolean canFriendlyFire() {
    	return this.getCustomData(2);
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
    	String uuid = this.func_152113_b();

        if(uuid != null && uuid.trim().length() > 0) {
            entitydog.func_152115_b(uuid);
            entitydog.setTamed(true);
        }
         
        entitydog.setGrowingAge(-24000 * (Constants.TEN_DAY_PUPS ? 10 : 1));

        return entitydog;
    }
    
    public int getDogHunger() {
		return this.dataWatcher.getWatchableObjectInt(23);
	}
    
    public void setDogHunger(int par1) {
    	this.dataWatcher.updateObject(23, MathHelper.clamp_int(par1, 0, 120));
    }
    
    public void hasRadarCollar(boolean flag) {
    	this.setCustomData(1, flag);
    }
    
    public boolean hasRadarCollar() {
      	return this.getCustomData(1);
    }
    
	public void hasCape(boolean hasCape) {
		this.setCustomData(3, hasCape);
    }
    
    public boolean hasCape() {
    	return this.getCustomData(3);
    }
    
    public void hasSunglasses(boolean hasSunglasses) {
    	this.setCustomData(4, hasSunglasses);
    }
    
    public boolean hasSunglasses() {
    	return this.getCustomData(4);
    }
    
    public void setHasBone(boolean hasBone) {
    	this.setCustomData(0, hasBone);
    }
    
    public boolean hasBone() {
    	return this.getCustomData(0);
    }
    
    public void setCustomData(int BIT, boolean flag) {
    	int in = this.dataWatcher.getWatchableObjectInt(25);
    	if(flag) in |= (1 << BIT);
    	else in &= ~(1 << BIT);
    	this.dataWatcher.updateObject(25, in);
    }
    
    public boolean getCustomData(int BIT) {
    	return (this.dataWatcher.getWatchableObjectInt(25) & (1 << BIT)) == (1 << BIT);
    }

    @Override
    public boolean func_142018_a(EntityLivingBase entityToAttack, EntityLivingBase owner) {
    	if(TalentHelper.canAttackEntity(this, entityToAttack))
    		return true;
    	
        return super.func_142018_a(entityToAttack, owner);
    }
    
    @Override
    public boolean canAttackClass(Class cls) {
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
    
    //Collar related functions
    public int getCollarColour() {
    	return this.dataWatcher.getWatchableObjectInt(20);
    }
    
    public void setCollarColour(int rgb) {
    	this.dataWatcher.updateObject(20, rgb);
    }
    
	public boolean hasCollar() {
		return this.getCollarColour() >= -1;
	}
	
	public boolean hasNoColour() {
		return this.getCollarColour() <= -1;
	}
	
	public void setHasCollar() {
		this.setCollarColour(-1);
	}
	
	public float[] getCollar() {
		int argb = this.getCollarColour();
		
		int r = (argb >> 16) &0xFF;
		int g = (argb >> 8) &0xFF;
		int b = (argb >> 0) &0xFF;
		
		return new float[] {(float)r / 255F, (float)g / 255F, (float)b / 255F};
	}
	
	private void onFinishShaking() {
		if(!this.worldObj.isRemote) {
			int lvlFisherDog = this.talents.getLevel("fisherdog");
			int lvlHellHound = this.talents.getLevel("hellhound");
			
			if(this.rand.nextInt(15) < lvlFisherDog * 2)
				this.dropItem(this.rand.nextInt(15) < lvlHellHound * 2 ? Items.cooked_fished : Items.fish, 1);
		}
	}
	
	public void updateRiderPosition()
    {
        if (this.riddenByEntity != null)
        {
        	float f = MathHelper.sin(this.renderYawOffset * (float)Math.PI / 180.0F);
            float f1 = MathHelper.cos(this.renderYawOffset * (float)Math.PI / 180.0F);
            float f2 = 0.7F * 0.7F;
            this.riddenByEntity.setPosition(this.posX + (double)(f2 * f), this.posY + this.getMountedYOffset() + this.riddenByEntity.getYOffset(), this.posZ - (double)(f2 * f1));
        }
    }

}