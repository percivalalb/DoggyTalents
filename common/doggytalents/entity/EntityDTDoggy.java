package doggytalents.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import doggytalents.DoggyTalentsMod;
import doggytalents.ModItems;
import doggytalents.core.helper.LogHelper;
import doggytalents.core.helper.ReflectionHelper;
import doggytalents.core.proxy.CommonProxy;
import doggytalents.entity.ai.EntityAIAttackOnCollide;
import doggytalents.entity.ai.EntityAIBeg;
import doggytalents.entity.ai.EntityAIFetchBone;
import doggytalents.entity.ai.EntityAIFollowOwner;
import doggytalents.entity.ai.EntityAIHurtByTarget;
import doggytalents.entity.ai.EntityAILeapAtTarget;
import doggytalents.entity.ai.EntityAILookIdle;
import doggytalents.entity.ai.EntityAIMate;
import doggytalents.entity.ai.EntityAIOwnerHurtByTarget;
import doggytalents.entity.ai.EntityAIOwnerHurtTarget;
import doggytalents.entity.ai.EntityAISwimming;
import doggytalents.entity.ai.EntityAITargetNonTamed;
import doggytalents.entity.ai.EntityAIWander;
import doggytalents.entity.ai.EntityAIWatchClosest;
import doggytalents.entity.data.DogLevel;
import doggytalents.entity.data.DogMode;
import doggytalents.entity.data.DogTalents;
import doggytalents.entity.data.EnumTalents;
import doggytalents.entity.data.WatchableDataLib;
import net.minecraft.block.BlockColored;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.potion.Potion;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

/**
 * @author ProPercivalalb
 */
public class EntityDTDoggy extends EntityTameable
{
    private float field_70926_e;
    private float field_70924_f;

    /** true is the wolf is wet else false */
    private boolean isShaking;
    private boolean field_70928_h;

    /**
     * This time increases while wolf is shaking and emitting water particles.
     */
    private float timeWolfIsShaking;
    private float prevTimeWolfIsShaking;
    private boolean hasBone;
    public EntityAIFetchBone aiFetchBone;
    public DogTalents talents;
    public DogLevel level;
    public DogMode mode;

    public EntityDTDoggy(World par1World)
    {
        super(par1World);
        this.setSize(0.6F, 0.8F);
        this.getNavigator().setAvoidsWater(true);
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, this.aiSit);
        this.tasks.addTask(3, new EntityAILeapAtTarget(this, 0.4F));
        this.tasks.addTask(4, new EntityAIAttackOnCollide(this, 1.0D, true));
        this.tasks.addTask(5, new EntityAIFollowOwner(this, 1.0D, 10.0F, 2.0F));
        this.tasks.addTask(6, aiFetchBone = new EntityAIFetchBone(this, 1.0D, 0.5F, 20.0F));
        this.tasks.addTask(7, new EntityAIMate(this, 1.0D));
        this.tasks.addTask(8, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(9, new EntityAIBeg(this, 8.0F));
        this.tasks.addTask(10, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(10, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIOwnerHurtByTarget(this));
        this.targetTasks.addTask(2, new EntityAIOwnerHurtTarget(this));
        this.targetTasks.addTask(3, new EntityAIHurtByTarget(this, true));
        this.targetTasks.addTask(4, new EntityAITargetNonTamed(this, EntitySheep.class, 200, false));
        this.setTamed(false);
    }

    @Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setAttribute(0.30000001192092896D);

        if (this.isTamed())
        {
            this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setAttribute(20.0D);
        }
        else
        {
            this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setAttribute(8.0D);
        }
    }

    /**
     * Returns true if the newer Entity AI code should be run
     */
    @Override
    public boolean isAIEnabled()
    {
        return true;
    }

    /**
     * Sets the active target the Task system uses for tracking
     */
    @Override
    public void setAttackTarget(EntityLivingBase par1EntityLivingBase)
    {
        super.setAttackTarget(par1EntityLivingBase);
    }

    /**
     * main AI tick function, replaces updateEntityActionState
     */
    @Override
    protected void updateAITick()
    {
        this.dataWatcher.updateObject(18, Float.valueOf(this.getHealth()));
    }

    @Override
    protected void entityInit()
    {
    	this.talents = new DogTalents(this);
    	this.level = new DogLevel(this);
    	this.mode = new DogMode(this);
    	
        super.entityInit();
        this.dataWatcher.addObject(18, new Float(this.getHealth()));
        this.dataWatcher.addObject(19, new Byte((byte)0));
        this.dataWatcher.addObject(WatchableDataLib.ID_TALENTS, talents.getDefaultStr());
        this.dataWatcher.addObject(WatchableDataLib.ID_LEVEL, level.getDefaultStr());
        this.dataWatcher.addObject(WatchableDataLib.ID_MODE, mode.getDefaultInt());
        this.dataWatcher.addObject(WatchableDataLib.ID_TAME_SKIN, new Byte((byte)0));
        this.dataWatcher.addObject(WatchableDataLib.ID_DOG_NAME, "");
    }

    /**
     * Plays step sound at given x, y, z for the entity
     */
    @Override
    protected void playStepSound(int par1, int par2, int par3, int par4)
    {
        this.playSound("mob.wolf.step", 0.15F, 1.0F);
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
    public void writeEntityToNBT(NBTTagCompound tag)
    {
        super.writeEntityToNBT(tag);
        tag.setInteger("doggyTex", this.getTameSkin());
        this.talents.writeToNBT(tag);
        this.level.writeToNBT(tag);
        this.mode.writeToNBT(tag);
        tag.setBoolean("willObey", this.willObeyOthers());
        tag.setString("WolfName", this.getWolfName());
    }
    
    @Override
    public String getEntityName() {
    	String name = this.getWolfName();
    	if(!name.equals(""))
    		return name;
    	return super.getEntityName();
    }
    
    public String getWolfName() {
        return this.dataWatcher.getWatchableObjectString(WatchableDataLib.ID_DOG_NAME);
    }
    
    public void setWolfName(String var1) {
       this.dataWatcher.updateObject(WatchableDataLib.ID_DOG_NAME, var1);
    }

    @Override
    public void moveEntityWithHeading(float par1, float par2)
    {
        if (this.riddenByEntity instanceof EntityPlayer)
        {
            this.prevRotationYaw = this.rotationYaw = this.riddenByEntity.rotationYaw;
            this.rotationPitch = this.riddenByEntity.rotationPitch * 0.5F;
            this.setRotation(this.rotationYaw, this.rotationPitch);
            this.rotationYawHead = this.renderYawOffset = this.rotationYaw;
            par1 = ((EntityPlayer)this.riddenByEntity).moveStrafing * 0.5F;
            par2 = ((EntityPlayer)this.riddenByEntity).moveForward;

            if (par2 <= 0.0F)
            {
                par2 *= 0.25F;
            }

            if (this.onGround && false)
            {
                //this.motionY = 0.02F;

                if (this.isPotionActive(Potion.jump))
                {
                    this.motionY += (double)((float)(this.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1F);
                }

                //this.isAirBorne = true;

                if (par2 > 0.0F)
                {
                    float f2 = MathHelper.sin(this.rotationYaw * (float)Math.PI / 180.0F);
                    float f3 = MathHelper.cos(this.rotationYaw * (float)Math.PI / 180.0F);
                    this.motionX += (double)(-0.4F * f2 * 0.15F); // May change
                    this.motionZ += (double)(0.4F * f3 * 0.15F);
                }
            }
            
            
            if(this.rand.nextInt(40) == 0) {
            	this.playSound("mob.wolf.panting", 0.4F, 1.0F);
            }

            this.stepHeight = 1.0F;
            this.jumpMovementFactor = this.getAIMoveSpeed() * 0.1F;

            if (!this.worldObj.isRemote)
            {
                this.setAIMoveSpeed((float)this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue() / 4);
                super.moveEntityWithHeading(par1, par2);
            }

            if (this.onGround)
            {
                //this.jumpPower = 0.0F;
               // this.setHorseJumping(false);
            }

            this.prevLimbSwingAmount = this.limbSwingAmount;
            double d0 = this.posX - this.prevPosX;
            double d1 = this.posZ - this.prevPosZ;
            float f4 = MathHelper.sqrt_double(d0 * d0 + d1 * d1) * 4.0F;

            if (f4 > 1.0F)
            {
                f4 = 1.0F;
            }

            this.limbSwingAmount += (f4 - this.limbSwingAmount) * 0.4F;
            this.limbSwing += this.limbSwingAmount;
        }
        else
        {
            this.stepHeight = 0.5F;
            this.jumpMovementFactor = 0.02F;
            super.moveEntityWithHeading(par1, par2);
        }
    }
    
    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    public void readEntityFromNBT(NBTTagCompound tag)
    {
        super.readEntityFromNBT(tag);
        this.talents.readFromNBT(tag);
        this.level.readFromNBT(tag);
        this.mode.readFromNBT(tag);  
        this.setWillObeyOthers(tag.getBoolean("willObey"));
        this.setTameSkin(tag.getInteger("doggyTex"));
        this.setWolfName(tag.getString("WolfName"));
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    @Override
    protected String getLivingSound()
    {
        return this.rand.nextInt(3) == 0 ? (this.isTamed() && this.dataWatcher.getWatchableObjectFloat(18) < 10.0F ? "mob.wolf.whine" : "mob.wolf.panting") : "mob.wolf.bark";
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    @Override
    protected String getHurtSound()
    {
        return "mob.wolf.hurt";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    @Override
    protected String getDeathSound()
    {
        return "mob.wolf.death";
    }

    /**
     * Returns the volume for the sounds this mob makes.
     */
    @Override
    protected float getSoundVolume()
    {
        return 0.4F;
    }

    /**
     * Returns the item ID for the item the mob drops on death.
     */
    @Override
    protected int getDropItemId()
    {
        return -1;
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    @Override
    public void onLivingUpdate()
    {
        super.onLivingUpdate();

        if (!this.worldObj.isRemote && this.isShaking && !this.field_70928_h && !this.hasPath() && this.onGround)
        {
            this.field_70928_h = true;
            this.timeWolfIsShaking = 0.0F;
            this.prevTimeWolfIsShaking = 0.0F;
            this.worldObj.setEntityState(this, (byte)8);
        }
    }
    
    

    /**
     * Called to update the entity's position/logic.
     */
    @Override
    public void onUpdate()
    {
        super.onUpdate();
        this.field_70924_f = this.field_70926_e;

        if (this.func_70922_bv())
        {
            this.field_70926_e += (1.0F - this.field_70926_e) * 0.4F;
        }
        else
        {
            this.field_70926_e += (0.0F - this.field_70926_e) * 0.4F;
        }

        if (this.func_70922_bv())
        {
            this.numTicksToChaseTarget = 10;
        }

        if (this.isWet())
        {
            this.isShaking = true;
            this.field_70928_h = false;
            this.timeWolfIsShaking = 0.0F;
            this.prevTimeWolfIsShaking = 0.0F;
        }
        else if ((this.isShaking || this.field_70928_h) && this.field_70928_h)
        {
            if (this.timeWolfIsShaking == 0.0F)
            {
                this.playSound("mob.wolf.shake", this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
            }

            this.prevTimeWolfIsShaking = this.timeWolfIsShaking;
            this.timeWolfIsShaking += 0.05F;

            if (this.prevTimeWolfIsShaking >= 2.0F)
            {
                this.isShaking = false;
                this.field_70928_h = false;
                this.prevTimeWolfIsShaking = 0.0F;
                this.timeWolfIsShaking = 0.0F;
            }

            if (this.timeWolfIsShaking > 0.4F)
            {
                float f = (float)this.boundingBox.minY;
                int i = (int)(MathHelper.sin((this.timeWolfIsShaking - 0.4F) * (float)Math.PI) * 7.0F);

                for (int j = 0; j < i; ++j)
                {
                    float f1 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width * 0.5F;
                    float f2 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width * 0.5F;
                    this.worldObj.spawnParticle("splash", this.posX + (double)f1, (double)(f + 0.8F), this.posZ + (double)f2, this.motionX, this.motionY, this.motionZ);
                }
            }
        }
        
        if(this.isTamed()) {
    		EntityPlayer player = (EntityPlayer)this.getOwner();
    		
    		if(player != null) {
    			float distanceToOwner = player.getDistanceToEntity(this);

                if (distanceToOwner <= 2F && this.hasBone()) {
                	if(!this.worldObj.isRemote) {
                		this.entityDropItem(new ItemStack(ModItems.throwBone.itemID, 1, 1), 0.0F);
                	}
                	
                    this.setHasBone(false);
                }
    		}
    	}
    }

    @SideOnly(Side.CLIENT)
    public boolean getWolfShaking()
    {
        return this.isShaking;
    }

    @SideOnly(Side.CLIENT)

    /**
     * Used when calculating the amount of shading to apply while the wolf is shaking.
     */
    public float getShadingWhileShaking(float par1)
    {
        return 0.75F + (this.prevTimeWolfIsShaking + (this.timeWolfIsShaking - this.prevTimeWolfIsShaking) * par1) / 2.0F * 0.25F;
    }

    @SideOnly(Side.CLIENT)
    public float getShakeAngle(float par1, float par2)
    {
        float f2 = (this.prevTimeWolfIsShaking + (this.timeWolfIsShaking - this.prevTimeWolfIsShaking) * par1 + par2) / 1.8F;

        if (f2 < 0.0F)
        {
            f2 = 0.0F;
        }
        else if (f2 > 1.0F)
        {
            f2 = 1.0F;
        }

        return MathHelper.sin(f2 * (float)Math.PI) * MathHelper.sin(f2 * (float)Math.PI * 11.0F) * 0.15F * (float)Math.PI;
    }

    @SideOnly(Side.CLIENT)
    public float getInterestedAngle(float par1)
    {
        return (this.field_70924_f + (this.field_70926_e - this.field_70924_f) * par1) * 0.15F * (float)Math.PI;
    }

    @Override
    public float getEyeHeight()
    {
        return this.height * 0.8F;
    }

    /**
     * The speed it takes to move the entityliving's rotationPitch through the faceEntity method. This is only currently
     * use in wolves.
     */
    @Override
    public int getVerticalFaceSpeed()
    {
        return this.isSitting() ? 20 : super.getVerticalFaceSpeed();
    }

    /**
     * Called when the entity is attacked.
     */
    @Override
    public boolean attackEntityFrom(DamageSource par1DamageSource, float par2)
    {
        if (this.isEntityInvulnerable())
        {
            return false;
        }
        else
        {
            Entity entity = par1DamageSource.getEntity();
            this.aiSit.setSitting(false);

            if (entity != null && !(entity instanceof EntityPlayer) && !(entity instanceof EntityArrow))
            {
                par2 = (par2 + 1.0F) / 2.0F;
            }

            return super.attackEntityFrom(par1DamageSource, par2);
        }
    }

    @Override
    public boolean attackEntityAsMob(Entity entity)
    {
        int damage = 4 + (effectiveLevel() + 1) / 2;
        int critChance = this.talents.getTalentLevel(EnumTalents.BLACKPELT) != 5 ? 0 : 1;
        critChance += this.talents.getTalentLevel(EnumTalents.BLACKPELT);
        
        if (rand.nextInt(6) < critChance) {
        	damage += (damage + 1) / 2;
        	DoggyTalentsMod.proxy.spawnCrit(worldObj, entity);
        }

        if (entity instanceof EntityCreeper) {
        	EntityCreeper creeper = (EntityCreeper)entity;
        	ReflectionHelper.setField(EntityCreeper.class, creeper, 1, 1);
        }
        
        if (this.talents.getTalentLevel(EnumTalents.HELLHOUND) != 0)
        {
            entity.setFire(this.talents.getTalentLevel(EnumTalents.HELLHOUND));
        }
        
        return entity.attackEntityFrom(DamageSource.causeMobDamage(this), (float)damage);
    }
    
    @Override
    protected boolean isMovementBlocked()
    {
        return this.riddenByEntity != null ? true : super.isMovementBlocked();
    }

    @Override
    public void setTamed(boolean par1)
    {
        super.setTamed(par1);

        if (par1)
        {
            this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setAttribute(20.0D);
        }
        else
        {
            this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setAttribute(8.0D);
        }
    }

    /**
     * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a pig.
     */
    @Override
    public boolean interact(EntityPlayer par1EntityPlayer)
    {
    	if(par1EntityPlayer.ridingEntity == null && !par1EntityPlayer.isSneaking()) {
    		par1EntityPlayer.mountEntity(this);
    		return true;
    	}
        ItemStack itemstack = par1EntityPlayer.inventory.getCurrentItem();

        if (this.isTamed())
        {
            if (itemstack != null)
            {
                if (Item.itemsList[itemstack.itemID] instanceof ItemFood)
                {
                    ItemFood itemfood = (ItemFood)Item.itemsList[itemstack.itemID];

                    if (itemfood.isWolfsFavoriteMeat() && this.dataWatcher.getWatchableObjectFloat(18) < 20.0F)
                    {
                        if (!par1EntityPlayer.capabilities.isCreativeMode)
                        {
                            --itemstack.stackSize;
                        }

                        this.heal((float)itemfood.getHealAmount());

                        if (itemstack.stackSize <= 0)
                        {
                            par1EntityPlayer.inventory.setInventorySlotContents(par1EntityPlayer.inventory.currentItem, (ItemStack)null);
                        }

                        return true;
                    }
                }
                else if (itemstack.itemID == Item.stick.itemID)
                {
                	par1EntityPlayer.openGui(DoggyTalentsMod.instance, CommonProxy.GUI_ID_DOGGY, this.worldObj, this.entityId, MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ));
                	return true;
                }
            }

            if (par1EntityPlayer.getCommandSenderName().equalsIgnoreCase(this.getOwnerName()) && !this.worldObj.isRemote && !this.isBreedingItem(itemstack))
            {
                this.aiSit.setSitting(!this.isSitting());
                this.isJumping = false;
                this.setPathToEntity((PathEntity)null);
                this.setTarget((Entity)null);
                this.setAttackTarget((EntityLivingBase)null);
            }
        }
        else if (itemstack != null && itemstack.itemID == Item.bone.itemID)
        {
            if (!par1EntityPlayer.capabilities.isCreativeMode)
            {
                --itemstack.stackSize;
            }

            if (itemstack.stackSize <= 0)
            {
                par1EntityPlayer.inventory.setInventorySlotContents(par1EntityPlayer.inventory.currentItem, (ItemStack)null);
            }

            if (!this.worldObj.isRemote)
            {
                if (this.rand.nextInt(3) == 0)
                {
                    this.setTamed(true);
                    this.setPathToEntity((PathEntity)null);
                    this.setAttackTarget((EntityLivingBase)null);
                    this.aiSit.setSitting(true);
                    this.setHealth(20.0F);
                    this.setOwner(par1EntityPlayer.getCommandSenderName());
                    this.playTameEffect(true);
                    this.worldObj.setEntityState(this, (byte)7);
                }
                else
                {
                    this.playTameEffect(false);
                    this.worldObj.setEntityState(this, (byte)6);
                }
            }

            return true;
        }

        return super.interact(par1EntityPlayer);
    }
    
    

    @Override
    @SideOnly(Side.CLIENT)
    public void handleHealthUpdate(byte par1)
    {
        if (par1 == 8)
        {
            this.field_70928_h = true;
            this.timeWolfIsShaking = 0.0F;
            this.prevTimeWolfIsShaking = 0.0F;
        }
        else
        {
            super.handleHealthUpdate(par1);
        }
    }

    @SideOnly(Side.CLIENT)
    public float getTailRotation()
    {
        return this.isTamed() ? (0.55F - (20.0F - this.dataWatcher.getWatchableObjectFloat(18)) * 0.02F) * (float)Math.PI : ((float)Math.PI / 5F);
    }

    /**
     * Checks if the parameter is an item which this animal can be fed to breed it (wheat, carrots or seeds depending on
     * the animal type)
     */
    @Override
    public boolean isBreedingItem(ItemStack par1ItemStack)
    {
        return par1ItemStack == null ? false : (!(Item.itemsList[par1ItemStack.itemID] instanceof ItemFood) ? false : ((ItemFood)Item.itemsList[par1ItemStack.itemID]).isWolfsFavoriteMeat());
    }

    public int getTameSkin()
    {
        return this.dataWatcher.getWatchableObjectByte(WatchableDataLib.ID_TAME_SKIN);
    }

    public void setTameSkin(int par1)
    {
    	LogHelper.logInfo("Tame Skin: " + par1);
        this.dataWatcher.updateObject(WatchableDataLib.ID_TAME_SKIN, Byte.valueOf((byte)par1));
    }

    /**
     * This function is used when two same-species animals in 'love mode' breed to generate the new baby animal.
     */
    public EntityDTDoggy spawnBabyAnimal(EntityAgeable par1EntityAgeable)
    {
        EntityDTDoggy entitywolf = new EntityDTDoggy(this.worldObj);
        String s = this.getOwnerName();

        if (s != null && s.trim().length() > 0)
        {
            entitywolf.setOwner(s);
            entitywolf.setTamed(true);
        }

        return entitywolf;
    }

    public void func_70918_i(boolean par1)
    {
        if (par1)
        {
            this.dataWatcher.updateObject(19, Byte.valueOf((byte)1));
        }
        else
        {
            this.dataWatcher.updateObject(19, Byte.valueOf((byte)0));
        }
    }

    /**
     * Returns true if the mob is currently able to mate with the specified mob.
     */
    @Override
    public boolean canMateWith(EntityAnimal par1EntityAnimal)
    {
        if (par1EntityAnimal == this)
        {
            return false;
        }
        else if (!this.isTamed())
        {
            return false;
        }
        else if (!(par1EntityAnimal instanceof EntityDTDoggy))
        {
            return false;
        }
        else
        {
            EntityDTDoggy entitywolf = (EntityDTDoggy)par1EntityAnimal;
            return !entitywolf.isTamed() ? false : (entitywolf.isSitting() ? false : this.isInLove() && entitywolf.isInLove());
        }
    }

    public boolean func_70922_bv()
    {
        return this.dataWatcher.getWatchableObjectByte(19) == 1;
    }
    
    public int masterOrder() {
       
    	int order = 0;
        EntityPlayer player = worldObj.getPlayerEntityByName(getOwnerName());

        if (player != null) {
        	
            float distanceAway = player.getDistanceToEntity(this);
            ItemStack itemstack = player.inventory.getCurrentItem();

            if (itemstack != null && (Item.itemsList[itemstack.itemID] instanceof ItemTool) && distanceAway <= 20F)
            {
                order = 1;
            }

            if (itemstack != null && ((Item.itemsList[itemstack.itemID] instanceof ItemSword) || (Item.itemsList[itemstack.itemID] instanceof ItemBow)))
            {
                order = 2;
            }

            if (itemstack != null && itemstack.itemID == Item.wheat.itemID)
            {
                order = 3;
            }
        }

        return order;
    }
    
    public void setWillObeyOthers(boolean flag) {
    	this.dataWatcher.updateObject(WatchableDataLib.ID_WILL_OBEY_OTHERS, flag ? 1 : 0);
    }
    
    public boolean willObeyOthers() {
    	return this.dataWatcher.getWatchableObjectInt(WatchableDataLib.ID_WILL_OBEY_OTHERS) != 0;
    }

    /**
     * Determines if an entity can be despawned, used on idle far away entities
     */
    @Override
    protected boolean canDespawn()
    {
        return !this.isTamed() && this.ticksExisted > 2400;
    }

    @Override
    public boolean func_142018_a(EntityLivingBase par1EntityLivingBase, EntityLivingBase par2EntityLivingBase)
    {
        if (!(par1EntityLivingBase instanceof EntityCreeper) && !(par1EntityLivingBase instanceof EntityGhast))
        {
            if (par1EntityLivingBase instanceof EntityDTDoggy)
            {
                EntityDTDoggy entitywolf = (EntityDTDoggy)par1EntityLivingBase;

                if (entitywolf.isTamed() && entitywolf.func_130012_q() == par2EntityLivingBase)
                {
                    return false;
                }
            }

            return par1EntityLivingBase instanceof EntityPlayer && par2EntityLivingBase instanceof EntityPlayer && !((EntityPlayer)par2EntityLivingBase).canAttackPlayer((EntityPlayer)par1EntityLivingBase) ? false : !(par1EntityLivingBase instanceof EntityHorse) || !((EntityHorse)par1EntityLivingBase).isTame();
        }
        else
        {
            return false;
        }
    }
    
    public void setHasBone(boolean hasBone) {
    	this.hasBone = hasBone;
    }
    
    public boolean hasBone() {
    	return this.hasBone;
    }

    public int points() {
        return this.level.getLevel() + this.level.getDireLevel() + (this.level.isDireDog() ? 15 : 0) + (getGrowingAge() < 0 ? 0 : 15);
    }
    
    public int effectiveLevel() {
        return (this.level.getLevel() + this.level.getDireLevel()) / 10;
    }
    
    @Override
    protected void attackEntity(Entity entity, float damage)
    {
    	
    	super.attackEntity(entity, damage);
    }

    public int spendablePoints() {
        return points() - usedPoints();
    }
    
    public int usedPoints() {
		int total = 0;
		for(EnumTalents talent : EnumTalents.values()) {
			int level = this.talents.getTalentLevel(talent);
			total += deductive(level);
		}
		return total;
    }
    
    public int deductive(int par1)
    {
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

    //TODO EffectiveLevel
    /**
    public int effectiveLevel() {
        if(talents == null) {this.talents = new DoggySkills(this);}
        if(level == null) {this.level = new DoggyLevel(this);}
        if(mode == null) {this.mode = new DoggyMode(this);}
        return (this.level.getLevel() + this.level.getDireLevel()) / 10;
    }**/
    
    @Override
    public EntityAgeable createChild(EntityAgeable par1EntityAgeable)
    {
        return this.spawnBabyAnimal(par1EntityAgeable);
    }

	public int getWolfTummy() {
		//TODO Add hunger system
		return 60;
	}
}
