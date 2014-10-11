package doggytalents.entity;

import java.util.Iterator;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAISit;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

import com.google.common.base.Strings;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import doggytalents.DoggyTalentsMod;
import doggytalents.ModItems;
import doggytalents.api.IDogTreat;
import doggytalents.api.IDogTreat.EnumFeedBack;
import doggytalents.core.helper.ChatHelper;
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
import doggytalents.entity.ai.EntityAIModeAttackTarget;
import doggytalents.entity.ai.EntityAIOwnerHurtByTarget;
import doggytalents.entity.ai.EntityAIOwnerHurtTarget;
import doggytalents.entity.ai.EntityAIShepard;
import doggytalents.entity.ai.EntityAISwimming;
import doggytalents.entity.ai.EntityAITargetNonTamed;
import doggytalents.entity.ai.EntityAIWander;
import doggytalents.entity.ai.EntityAIWatchClosest;
import doggytalents.entity.data.DogLevel;
import doggytalents.entity.data.DogMode;
import doggytalents.entity.data.DogSavePosition;
import doggytalents.entity.data.DogTalents;
import doggytalents.entity.data.EnumMode;
import doggytalents.entity.data.EnumTalents;
import doggytalents.entity.data.WatchableDataLib;
import doggytalents.inventory.InventoryPackPuppy;
import doggytalents.lib.Constants;

/**
 * @author ProPercivalalb
 */
public class EntityDTDoggy extends EntityTameable {
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
    private int guardTime;
    private int tummyTick;
    private int healingTick;
    private int regenerationTick;
    private int reversionTime;
    private int charmerCharge;
    private float timeWolfIsHappy;
    private float prevTimeWolfIsHappy;
    private boolean isWolfHappy;
    public boolean hiyaMaster;
    private boolean hasBone;
    private boolean canSeeCreeper;
    private EntityAgeable lastBaby;
    public EntityAIFetchBone aiFetchBone;
    public DogTalents talents;
    public DogLevel level;
    public DogMode mode;
    public DogSavePosition saveposition;
    public InventoryPackPuppy inventory;
    //TODO
    public int bedHealTick = 0;

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
        this.tasks.addTask(8, new EntityAIMate(this, 1.0D));
        this.tasks.addTask(9, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(10, new EntityAIBeg(this, 8.0F));
        this.tasks.addTask(11, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(12, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIOwnerHurtByTarget(this));
        this.targetTasks.addTask(2, new EntityAIOwnerHurtTarget(this));
        this.targetTasks.addTask(3, new EntityAIModeAttackTarget(this));
        this.targetTasks.addTask(4, new EntityAIHurtByTarget(this, true));
        this.targetTasks.addTask(5, new EntityAITargetNonTamed(this, EntitySheep.class, 200, false));
        this.setTamed(false);
        this.inventory = new InventoryPackPuppy(this);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.redoAttributes();
    }
    
    public void redoAttributes() {
    	this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(20D + (effectiveLevel() + 1D) * 2D);
    	this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(getSpeedModifier());
    }

    /**
     * Returns true if the newer Entity AI code should be run
     */
    @Override
    public boolean isAIEnabled() {
        return true;
    }

    /**
     * Sets the active target the Task system uses for tracking
     */
    @Override
    public void setAttackTarget(EntityLivingBase par1EntityLivingBase) {
        super.setAttackTarget(par1EntityLivingBase);
    }
    
    public EntityAISit getSitAI() {
    	return this.aiSit;
    }

    /**
     * main AI tick function, replaces updateEntityActionState
     */
    @Override
    protected void updateAITick() {
        this.dataWatcher.updateObject(18, Float.valueOf(this.getHealth()));
    }

    @Override
    protected void entityInit() {
    	this.talents = new DogTalents(this);
    	this.level = new DogLevel(this);
    	this.mode = new DogMode(this);
    	this.saveposition = new DogSavePosition(this);
    	
        super.entityInit();
        this.dataWatcher.addObject(18, new Float(this.getHealth()));
        this.dataWatcher.addObject(19, new Byte((byte)0));
        this.dataWatcher.addObject(WatchableDataLib.ID_TALENTS, talents.getDefaultStr());
        this.dataWatcher.addObject(WatchableDataLib.ID_LEVEL, level.getDefaultStr());
        this.dataWatcher.addObject(WatchableDataLib.ID_MODE, mode.getDefaultInt());
        this.dataWatcher.addObject(WatchableDataLib.ID_SAVE_POSITION, saveposition.getDefaultStr());
        this.dataWatcher.addObject(WatchableDataLib.ID_TAME_SKIN, new Byte((byte)0));
        this.dataWatcher.addObject(WatchableDataLib.ID_DOG_NAME, "");
        this.dataWatcher.addObject(WatchableDataLib.ID_WOLF_TUMMY, new Integer(60));
        this.dataWatcher.addObject(WatchableDataLib.ID_WILL_OBEY_OTHERS, 0);
    }

    /**
     * Plays step sound at given x, y, z for the entity
     */
    @Override
    protected void func_145780_a(int x, int y, int z, Block block) {
        this.playSound("mob.wolf.step", 0.15F, 1.0F);
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
    public void writeEntityToNBT(NBTTagCompound tag) {
        super.writeEntityToNBT(tag);
        tag.setInteger("doggyTex", this.getTameSkin());
        this.talents.writeToNBT(tag);
        this.level.writeToNBT(tag);
        this.mode.writeToNBT(tag);
        this.inventory.writeToNBT(tag);
        tag.setBoolean("willObey", this.willObeyOthers());
        tag.setString("wolfName", this.getWolfName());
        tag.setInteger("wolfTummy", this.getDogTummy());
        tag.setInteger("tummyTick", this.tummyTick);
        tag.setInteger("healingTick", this.healingTick);
        tag.setInteger("regenTick", this.regenerationTick);
        tag.setInteger("reversionTime", this.reversionTime);
        tag.setInteger("charmerCharge", this.charmerCharge);
    }
    
    @Override
    public String getCommandSenderName() {
    	String name = this.getWolfName();
    	if(!Strings.isNullOrEmpty(name))
    		return name;
    	return super.getCommandSenderName();
    }
    
    public String getWolfName() {
        return this.dataWatcher.getWatchableObjectString(WatchableDataLib.ID_DOG_NAME);
    }
    
    public void setWolfName(String var1) {
       this.dataWatcher.updateObject(WatchableDataLib.ID_DOG_NAME, var1);
    }

    @Override
    public void moveEntityWithHeading(float par1, float par2) {
        if (this.riddenByEntity instanceof EntityPlayer) {
            this.prevRotationYaw = this.rotationYaw = this.riddenByEntity.rotationYaw;
            this.rotationPitch = this.riddenByEntity.rotationPitch * 0.5F;
            this.setRotation(this.rotationYaw, this.rotationPitch);
            this.rotationYawHead = this.renderYawOffset = this.rotationYaw;
            par1 = ((EntityPlayer)this.riddenByEntity).moveStrafing * 0.5F;
            par2 = ((EntityPlayer)this.riddenByEntity).moveForward;

            if (par2 <= 0.0F) {
                par2 *= 0.25F;
            }

            if (this.onGround)
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
    
    public double getSpeedModifier() {
    	double var1 = 0.3D;

        if (this.getAttackTarget() != null && !(getAttackTarget() instanceof EntityDTDoggy) && !(getAttackTarget() instanceof EntityPlayer))
        {
            if (this.talents.getTalentLevel(EnumTalents.DOGGYDASH) == 5)
            {
                var1 += 0.04D;
            }

            if (this.level.isDireDog())
            {
                var1 += 0.05D;
            }
        }
        
        for (int i = 0; i < this.talents.getTalentLevel(EnumTalents.DOGGYDASH); i++)
        {
            var1 += 0.03D;
        }

        return var1;
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
        this.inventory.readFromNBT(tag);
        this.setWillObeyOthers(tag.getBoolean("willObey"));
        this.setTameSkin(tag.getInteger("doggyTex"));
        this.setWolfName(tag.getString("wolfName"));
        this.setDogTummy(tag.getInteger("wolfTummy"));
        this.tummyTick = tag.getInteger("tummyTick");
        this.healingTick = tag.getInteger("healingTick");
        this.regenerationTick = tag.getInteger("regenTick");
        this.reversionTime = tag.getInteger("reversionTime");
        this.charmerCharge = tag.getInteger("charmerCharge");
    }
    
    @Override
    protected String getLivingSound() {
        if (this.canSeeCreeper)
           return "mob.wolf.growl";
        if (rand.nextInt(10) < Constants.barkRate) {
            if (rand.nextInt(3) == 0) {
                if (isTamed() && this.dataWatcher.getWatchableObjectFloat(18) == 1)
                    return "mob.wolf.whine";
                else
                    return "mob.wolf.panting";
            }
            else
                return "mob.wolf.bark";
        }
        else
            return "";
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
    protected Item getDropItem() {
        return Item.getItemById(-1);
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
        
        if(this.entityToAttack != null && this.entityToAttack.isDead) {
        	this.entityToAttack = null;
        }
        

        if(isTamed() && masterOrder() != 3 && riddenByEntity instanceof EntityAnimal && this.talents.getTalentLevel(EnumTalents.SHEPHERDDOG) > 0) {
        	this.riddenByEntity.ridingEntity = null;
        	this.riddenByEntity = null;
        }
        
        if (this.level.isDireDog() && !Constants.direParticalsOff) {
            for (int i = 0; i < 2; i++) {
                worldObj.spawnParticle("portal", posX + (rand.nextDouble() - 0.5D) * (double)width, (posY + rand.nextDouble() * (double)height) - 0.25D, posZ + (rand.nextDouble() - 0.5D) * (double)width, (rand.nextDouble() - 0.5D) * 2D, -rand.nextDouble(), (rand.nextDouble() - 0.5D) * 2D);
            }
        }
        
        this.faceBed();
        if (this.talents.getTalentLevel(EnumTalents.PACKPUPPY) == 5 && this.getHealth() > 1) {
        	this.autoPickup();
        }
        
        if(reversionTime > 0) {
        	--reversionTime;
        }
        
        EntityPlayer player = (EntityPlayer)this.getOwner();
        
        if (player != null &&player.getHealth() <= 6 && this.talents.getTalentLevel(EnumTalents.RESCUEDOG) != 0 && this.getDogTummy() > healCost()) {
            player.heal((int)(this.talents.getTalentLevel(EnumTalents.RESCUEDOG) * 1.5D));
            this.setDogTummy(this.getDogTummy() - healCost());
        }
        
        if (this.getHealth() <= 0 && this.isImmortal()) {
            this.deathTime = 0;
            this.setHealth(1);
        }

        if (player != null && player.getBedLocation() != null) {
            this.saveposition.setBedX(player.getBedLocation().posX);
            this.saveposition.setBedY(player.getBedLocation().posY);
            this.saveposition.setBedZ(player.getBedLocation().posZ);
        }
        
        if (this.getAttackTarget() == null && isTamed() && this.talents.getTalentLevel(EnumTalents.CREEPERSWEEPER) > 0) {
            List list1 = worldObj.getEntitiesWithinAABB(EntityCreeper.class, AxisAlignedBB.getBoundingBox(posX, posY, posZ, posX + 1.0D, posY + 1.0D, posZ + 1.0D).expand(sniffRange(), 4D, sniffRange()));

            if (!list1.isEmpty() && !isSitting() && this.getHealth() > 1) {
                canSeeCreeper = true;
            }
            else {
                canSeeCreeper = false;
            }
        }
        
        if (this.getCharmerCharge() > 0 && isTamed()) {
            this.setCharmerCharge(this.getCharmerCharge() - 1);
        }
        
        if (riddenByEntity == null && (!isSitting() && !this.mode.isMode(EnumMode.WANDERING) && !this.level.isDireDog() || worldObj.getWorldInfo().getWorldTime() % 2L == 0L) && Constants.isHungerOn) {
        	this.tummyTick += 1;
        }

        if (riddenByEntity != null)
        {
            if (Constants.isHungerOn && (this.talents.getTalentLevel(EnumTalents.WOLFMOUNT) != 5 || worldObj.getWorldInfo().getWorldTime() % 2L == 0L))
            {
            	tummyTick += 3;
            }

            if (Constants.isHungerOn && !(riddenByEntity instanceof EntityPlayer))
            {
            	tummyTick += 5 - this.talents.getTalentLevel(EnumTalents.SHEPHERDDOG);
            }
        }

        healingTick += nourishment();

        if (this.getDogTummy() > 120)
        {
            this.setDogTummy(120);
        }

        if (this.getDogTummy() == 0 && worldObj.getWorldInfo().getWorldTime() % 100L == 0L && this.getHealth() > 1)
        {
            attackEntityFrom(DamageSource.generic, 1);
            fleeingTick = 0;
        }

        if (this.tummyTick > 300)
        {
            if (this.getDogTummy() > 0)
            {
                this.setDogTummy(this.getDogTummy() -1);
            }

            this.tummyTick = 0;
        }

        if (healingTick >= 6000)
        {
            if (this.getHealth() < getMaxHealth() && this.getHealth() != 1)
            {
            	this.setHealth(this.getHealth() + 1);
            }

            if (this.getHealth() > getMaxHealth())
            {
                this.setHealth(this.getMaxHealth());
            }

            if (this.getHealth() < getMaxHealth() && this.getHealth() == 1)
            {
            	regenerationTick += 1;

                if (this.talents.getTalentLevel(EnumTalents.GUARDDOG) == 5 && rand.nextInt(2) == 0)
                {
                	regenerationTick += 1;
                }
            }

            healingTick = 0;
        }

        if (regenerationTick >= 200)
        {
            this.setHealth(this.getHealth() + 1);
            this.worldObj.setEntityState(this, (byte)7);
            regenerationTick = 0;
        }
        
        if (this.talents.getTalentLevel(EnumTalents.PESTFIGHTER) != 0)
        {
            byte byte0 = 1;

            if (this.talents.getTalentLevel(EnumTalents.PESTFIGHTER) == 5)
            {
                byte0 = 2;
            }

            slaySilverFish(byte0);
        }
        
        if(this.lastBaby != null && Constants.tenDayPuppies) {
        	this.lastBaby.setGrowingAge(-24000 * 10);
        	this.lastBaby = null;
        }
        
        if (!this.worldObj.isRemote && this.talents.getTalentLevel(EnumTalents.PUPPYEYES) != 0 && this.getCharmerCharge() == 0) {
            EntityLiving entityliving = charmVillagers(this, 5D);

            if (entityliving != null && player != null) {
                int j1 = rand.nextInt(this.talents.getTalentLevel(EnumTalents.PUPPYEYES)) + (this.talents.getTalentLevel(EnumTalents.PUPPYEYES) != 5 ? 0 : 1);

                if (j1 == 0)
                {
                	player.addChatMessage(ChatHelper.getChatComponentTranslation("dogTalent.puppyeyes.1.part1"));
                	player.addChatMessage(ChatHelper.getChatComponentTranslation("dogTalent.puppyeyes.1.part2"));
                    entityliving.dropItem(Items.porkchop, 2);
                }

                if (j1 == 1)
                {
                  	player.addChatMessage(ChatHelper.getChatComponentTranslation("dogTalent.puppyeyes.2.part1"));
                  	player.addChatMessage(ChatHelper.getChatComponentTranslation("dogTalent.puppyeyes.2.part2"));
                	player.addChatMessage(ChatHelper.getChatComponentTranslation("dogTalent.puppyeyes.2.part3"));
                    entityliving.dropItem(Items.porkchop, 5);
                }

                if (j1 == 2)
                {
                	player.addChatMessage(ChatHelper.getChatComponentTranslation("dogTalent.puppyeyes.3.part1"));
                  	player.addChatMessage(ChatHelper.getChatComponentTranslation("dogTalent.puppyeyes.3.part2"));
                	player.addChatMessage(ChatHelper.getChatComponentTranslation("dogTalent.puppyeyes.3.part3"));
                    entityliving.dropItem(Items.iron_ingot, 3);
                }

                if (j1 == 3)
                {
                	player.addChatMessage(ChatHelper.getChatComponentTranslation("dogTalent.puppyeyes.4.part1"));
                  	player.addChatMessage(ChatHelper.getChatComponentTranslation("dogTalent.puppyeyes.4.part2"));
                	player.addChatMessage(ChatHelper.getChatComponentTranslation("dogTalent.puppyeyes.4.part3"));
                    entityliving.dropItem(Items.gold_ingot, 2);
                }

                if (j1 == 4)
                {
                	player.addChatMessage(ChatHelper.getChatComponentTranslation("dogTalent.puppyeyes.5.part1"));
                  	player.addChatMessage(ChatHelper.getChatComponentTranslation("dogTalent.puppyeyes.5.part2"));
                	player.addChatMessage(ChatHelper.getChatComponentTranslation("dogTalent.puppyeyes.5.part3"));
                    entityliving.dropItem(Items.diamond, 1);
                }

                if (j1 == 5)
                {
                	player.addChatMessage(ChatHelper.getChatComponentTranslation("dogTalent.puppyeyes.6.part1"));
                  	player.addChatMessage(ChatHelper.getChatComponentTranslation("dogTalent.puppyeyes.6.part2"));
                	player.addChatMessage(ChatHelper.getChatComponentTranslation("dogTalent.puppyeyes.6.part3"));
                    entityliving.dropItem(Items.apple, 1);
                    entityliving.dropItem(Items.cake, 1);
                    entityliving.dropItem(Items.slime_ball, 3);
                    entityliving.dropItem(Items.porkchop, 5);
                }

                this.setCharmerCharge(this.talents.getTalentLevel(EnumTalents.PUPPYEYES) != 5 ? 48000 : 24000);
            }
        }
    }

    /**
     * Called to update the entity's position/logic.
     */
    @Override
    public void onUpdate()
    {
    	this.redoAttributes();
        super.onUpdate();
        /**
        if (masterOrder() == 3 && this.getHealth() > 1 && riddenByEntity == null && this.getAttackTarget() == null && isTamed() && this.talents.getTalentLevel(EnumTalents.SHEPHERDOG) > 0) {
            List list1 = worldObj.getEntitiesWithinAABB(EntityAnimal.class, AxisAlignedBB.getBoundingBox(posX, posY, posZ, posX + 1.0D, posY + 1.0D, posZ + 1.0D).expand(2D, 2D, 2D));
            EntityAnimal finalAnimal = null;
            for(int i = 0; i < list1.size(); ++i) {
            	EntityAnimal animal = (EntityAnimal)list1.get(i);
            	finalAnimal = animal;
            }
            if(this.riddenByEntity == null && finalAnimal != null) {
            	finalAnimal.mountEntity(this);
            }
        }**/
        this.field_70924_f = this.field_70926_e;

        if (this.isBegging())
        {
            this.field_70926_e += (1.0F - this.field_70926_e) * 0.4F;
        }
        else
        {
            this.field_70926_e += (0.0F - this.field_70926_e) * 0.4F;
        }

        if (this.isBegging())
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
            	if (didWolfFish()) {
                    if (didWolfCook()) {
                    	if(!this.worldObj.isRemote) {
                    		dropItem(Items.cooked_fished, 1);
                    	}
                    }
                    else {
                    	if(!this.worldObj.isRemote) {
                    		dropItem(Items.fish, 1);
                    	}
                    }
                }
            	
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
        		this.worldObj.playSoundAtEntity(this, "mob.wolf.panting", this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
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
        
        if (guardTime > 0) {
            guardTime--;
        }
    }
    
    @Override
    public void onEntityUpdate() {
        super.onEntityUpdate();

        if (this.getHealth() <= 0 && isImmortal()) {
            deathTime = 0;
            this.setHealth(1);
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
    
    @Override
    protected void fall(float par1) {
    	par1 = ForgeHooks.onLivingFall(this, par1);
        if (par1 <= 0) return;
        PotionEffect potioneffect = this.getActivePotionEffect(Potion.jump);
        float f1 = potioneffect != null ? (float)(potioneffect.getAmplifier() + 1) : 0.0F;
        int i = MathHelper.ceiling_float_int(par1 - 3.0F - f1) - fallProtection();

        if (i > 0 && !isImmuneToFalls() && ridingEntity == null)
        {
            if (i > 4)
            {
                this.playSound("damage.fallbig", 1.0F, 1.0F);
            }
            else
            {
                this.playSound("damage.fallsmall", 1.0F, 1.0F);
            }

            this.attackEntityFrom(DamageSource.fall, (float)i);
            Block block = this.worldObj.getBlock(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY - 0.20000000298023224D - (double)this.yOffset), MathHelper.floor_double(this.posZ));

            if (block.getMaterial() != Material.air) {
                Block.SoundType soundtype = block.stepSound;
                this.playSound(soundtype.getStepResourcePath(), soundtype.getVolume() * 0.5F, soundtype.getPitch() * 0.75F);
            }
        }
    }

    /**
     * Called when the entity is attacked.
     */
    @Override
    public boolean attackEntityFrom(DamageSource par1DamageSource, float damage)
    {
        if (this.isEntityInvulnerable())
        {
            return false;
        }
        else
        {
        	if(this.talents.getTalentLevel(EnumTalents.HELLHOUND) == 5) {
        		if(par1DamageSource.isFireDamage()) {
        			return false;
        		}
        	}
        		
            Entity entity = par1DamageSource.getEntity();
            this.aiSit.setSitting(false);

            if (entity != null && !(entity instanceof EntityPlayer) && !(entity instanceof EntityArrow))
            {
            	damage = (damage + 1.0F) / 2.0F;
            }
            
            if (entity != null && guardTime <= 0)
            {
                int blockChance = this.talents.getTalentLevel(EnumTalents.GUARDDOG) != 5 ? 0 : 1;
                blockChance += this.talents.getTalentLevel(EnumTalents.GUARDDOG);

                if (rand.nextInt(12) < blockChance)
                {
                    guardTime = 10;
                    worldObj.playSoundAtEntity(this, "random.break", getSoundVolume(), (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
                    return false;
                }
            }

            return super.attackEntityFrom(par1DamageSource, damage);
        }
    }

    @Override
    public boolean attackEntityAsMob(Entity entity) {
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
        
        if (this.talents.getTalentLevel(EnumTalents.HELLHOUND) != 0) {
            entity.setFire(this.talents.getTalentLevel(EnumTalents.HELLHOUND));
        }
        
        if ((entity instanceof EntityLivingBase) && this.talents.getTalentLevel(EnumTalents.POSIONFANG) > 0) {
            ((EntityLivingBase)entity).addPotionEffect(new PotionEffect(Potion.poison.id, this.talents.getTalentLevel(EnumTalents.POSIONFANG) * 20, 0));
        }

        if (entity instanceof EntityZombie) {
            EntityZombie entityzombie = (EntityZombie)entity;
            entityzombie.setAttackTarget(this);
        }
        
        return entity.attackEntityFrom(DamageSource.causeMobDamage(this), (float)damage);
    }
    
    protected void slaySilverFish(int damage) {
        List list = worldObj.getEntitiesWithinAABB(EntitySilverfish.class, AxisAlignedBB.getBoundingBox(posX, posY, posZ, posX + 1.0D, posY + 1.0D, posZ + 1.0D).expand(this.talents.getTalentLevel(EnumTalents.PESTFIGHTER) * 3, 4D, this.talents.getTalentLevel(EnumTalents.PESTFIGHTER) * 3));
        Iterator iterator = list.iterator();
        
        while(iterator.hasNext()) {
        	EntitySilverfish entitySilverfish = (EntitySilverfish)iterator.next();
        	if(rand.nextInt(20) == 0) {
        		entitySilverfish.attackEntityFrom(DamageSource.generic, damage);
        	}
        }
    }
    
    public EntityLiving charmVillagers(Entity entity, double d) {
        double d1 = -1D;
        EntityPlayer entityplayer = (EntityPlayer)this.getOwner();
        EntityLiving entityliving = null;
        List list = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox.expand(d, d, d));

        for (int i = 0; i < list.size(); i++)
        {
            Entity entity1 = (Entity)list.get(i);

            if (!(entity1 instanceof EntityVillager))
            {
                continue;
            }

            double d2 = entity1.getDistanceSq(entity.posX, entity.posY, entity.posZ);

            if ((d < 0.0D || d2 < d * d) && (d1 == -1D || d2 < d1) && ((EntityLiving)entity1).canEntityBeSeen(entity))
            {
                d1 = d2;
                entityliving = (EntityLiving)entity1;
            }
        }

        return entityliving;
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
            this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(20.0D);
        }
        else
        {
            this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(8.0D);
        }
    }

    @Override
    public boolean interact(EntityPlayer player) {
        ItemStack stack = player.inventory.getCurrentItem();

        if (this.isTamed()) {
            if (stack != null) {
            	int foodValue = DoggyUtil.foodValue(this, stack);
            	
            	if(foodValue != 0 && this.getDogTummy() < 120) {
            		DoggyUtil.loseStack(player, stack);
            		
                    this.setDogTummy(this.getDogTummy() + foodValue);
                    return true;
                }
            	else if(stack.getItem() == Items.bone && canInteract(player)) {
                    this.aiSit.setSitting(true);
            		if (!worldObj.isRemote) {
                        if(this.ridingEntity != null)
                        	this.mountEntity(null);
                        else
                         	this.mountEntity(player);
                    }
                    return true;
                }
                else if(stack.getItem() == Items.stick && canInteract(player)) {
                	player.openGui(DoggyTalentsMod.instance, CommonProxy.GUI_ID_DOGGY, this.worldObj, this.getEntityId(), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ));
                	return true;
                }
                else if(stack.getItem() == Item.getItemFromBlock(Blocks.planks) && canInteract(player)) {
                	player.openGui(DoggyTalentsMod.instance, CommonProxy.GUI_ID_PACKPUPPY, this.worldObj, this.getEntityId(), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ));
                	return true;
                }
                else if(stack.getItem() instanceof IDogTreat && canInteract(player)) {
                	IDogTreat treat = (IDogTreat)stack.getItem();
                	EnumFeedBack type = treat.canGiveToDog(player, this, this.level.getLevel(), this.level.getDireLevel());
                	treat.giveTreat(type, player, this, this.talents);
                	return true;
                }
                else if (stack.getItem() == ModItems.collarShears && this.func_152114_e(player)) {
                    if (!this.worldObj.isRemote) {
                    	this.setTamed(false);
                        this.setPathToEntity(null);
                        this.setSitting(false);
                        this.setHealth(8);
                        this.talents.resetTalents();
                        this.func_152115_b("");
                        this.setWillObeyOthers(false);
                        this.mode.setMode(EnumMode.DOCILE);
                        this.reversionTime = 40;
                    }

                    return true;
                }
                else if(stack.getItem() == Items.cake) {
                	DoggyUtil.loseStack(player, stack);
                	
                    if (!this.worldObj.isRemote) {
                        this.aiSit.setSitting(true);
                        this.setHealth(this.getHealth());
                        this.regenerationTick = 0;
                        this.setPathToEntity((PathEntity)null);
                        this.setTarget((Entity)null);
                        this.setAttackTarget((EntityLivingBase)null);
                        this.playTameEffect(true);
                        worldObj.setEntityState(this, (byte)7);
                    }

                    return true;
                }
            }
            else {
            	if(this.talents.getTalentLevel(EnumTalents.WOLFMOUNT) > 0 && player.ridingEntity == null && !player.onGround) {
            		this.aiSit.setSitting(false);
            		player.mountEntity(this);
            		return true;
            	}
            }

            if (canInteract(player) && !this.worldObj.isRemote && player.ridingEntity == null && !this.isBreedingItem(stack)) {
                this.aiSit.setSitting(!this.isSitting());
                this.isJumping = false;
                this.setPathToEntity((PathEntity)null);
                this.setTarget((Entity)null);
                this.setAttackTarget((EntityLivingBase)null);
            }
        }
        else if (stack.getItem() == ModItems.collarShears && this.reversionTime < 1 && !worldObj.isRemote) {
            this.setDead();
            EntityWolf wolf = new EntityWolf(this.worldObj);
            wolf.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
            this.worldObj.spawnEntityInWorld(wolf);
            return true;
        }
        else if (stack != null && stack.getItem() == Items.bone) {
        	DoggyUtil.loseStack(player, stack);

            if (!this.worldObj.isRemote) {
                if (this.rand.nextInt(3) == 0) {
                    this.setTamed(true);
                    this.setPathToEntity((PathEntity)null);
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

        return super.interact(player);
    }
    
    public boolean canInteract(EntityPlayer player) {
    	if(this.func_152114_e(player))
    		return true;
    	
    	return this.willObeyOthers();
    }
    
    public int healCost() {
        byte byte0 = 100;

        if (this.talents.getTalentLevel(EnumTalents.RESCUEDOG) == 5) {
            byte0 = 80;
        }

        return byte0;
    }
    
    @Override
    public void playTameEffect(boolean par1) {
        String s = "heart";

        if (!par1)
            s = "smoke";

        for (int i = 0; i < 7; ++i) {
            double d0 = this.rand.nextGaussian() * 0.02D;
            double d1 = this.rand.nextGaussian() * 0.02D;
            double d2 = this.rand.nextGaussian() * 0.02D;
            this.worldObj.spawnParticle(s, this.posX + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, this.posY + 0.5D + (double)(this.rand.nextFloat() * this.height), this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, d0, d1, d2);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void handleHealthUpdate(byte par1) {
        if (par1 == 8) {
            this.field_70928_h = true;
            this.timeWolfIsShaking = 0.0F;
            this.prevTimeWolfIsShaking = 0.0F;
        }
        else {
            super.handleHealthUpdate(par1);
        }
    }

    public float getWagAngle(float f, float f1) {
        float f2 = (this.prevTimeWolfIsHappy + (this.timeWolfIsHappy - this.prevTimeWolfIsHappy) * f + f1) / 2.0F;
        if (f2 < 0.0F)
        {
          f2 = 0.0F;
        }
        else if (f2 > 2.0F)
        {
          f2 %= 2.0F;
        }
        return MathHelper.sin(f2 * 3.141593F * 11.0F) * 0.3F * 3.141593F;
      }
    
    @SideOnly(Side.CLIENT)
    public float getTailRotation() {
        return this.isTamed() ? (0.55F - ((float)(this.getMaxHealth() - this.getHealth()) / tailMod()) * 0.02F) * (float)Math.PI : ((float)Math.PI / 5F);
    }

    public float tailMod() {
        float f = 1.0F;

        for (int i = 1; i < getMaxHealth(); i++)
        {
            f++;
        }

        f /= 20F;
        return f;
    }
    
    @Override
    public boolean isPotionApplicable(PotionEffect par1PotionEffect) {
        if (this.getHealth() <= 1) {
            return false;
        }

        if (this.talents.getTalentLevel(EnumTalents.POSIONFANG) >= 3) {
            int i = par1PotionEffect.getPotionID();

            if (i == Potion.poison.id) {
                return false;
            }
        }

        return true;
    }
    
    @Override
    public boolean isBreedingItem(ItemStack par1ItemStack) {
        return par1ItemStack == null ? false : par1ItemStack.getItem() == ModItems.breedingBone;
    }

    public int getTameSkin()
    {
        return this.dataWatcher.getWatchableObjectByte(WatchableDataLib.ID_TAME_SKIN);
    }

    public void setTameSkin(int par1) {
        this.dataWatcher.updateObject(WatchableDataLib.ID_TAME_SKIN, Byte.valueOf((byte)par1));
    }

    public double sniffRange(){
        double d = 0.0D;
        for (int i = 0; i < this.talents.getTalentLevel(EnumTalents.CREEPERSWEEPER) * 6; i++) {
            d++;
        }
        return d;
    }
    
    @Override
    public void onKillEntity(EntityLivingBase entityliving) {
        if (didWolfSlaughter()) {
            entityliving.onDeath(DamageSource.generic);
        }
    }

    public boolean didWolfSlaughter() {
        int chance = this.talents.getTalentLevel(EnumTalents.HUNTERDOG);

        if (this.talents.getTalentLevel(EnumTalents.HUNTERDOG) == 5) {
        	chance++;
        }

        return rand.nextInt(10) < chance;
    }
    
    public boolean didWolfFish() {
        return rand.nextInt(15) < this.talents.getTalentLevel(EnumTalents.FISHERDOG) * 2;
    }

    public boolean didWolfCook() {
        return rand.nextInt(15) < this.talents.getTalentLevel(EnumTalents.HELLHOUND) * 2;
    }
    
    public void autoPickup() {
        List list = worldObj.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(posX - 2.5D, posY - 1.0D, posZ - 2.5D, posX + 2.5D, posY + 1.0D, posZ + 2.5D));

        if (!list.isEmpty()) {
            for(int i = 0; i < list.size(); i++) {
                EntityItem entityitem = (EntityItem)list.get(i);
                if (!this.worldObj.isRemote && entityitem.getEntityItem().getItem() != ModItems.throwBone) {
                    if(TileEntityHopper.func_145898_a(this.inventory, entityitem)) {
                    	this.worldObj.playSoundAtEntity(this, "random.pop", 0.2F, ((rand.nextFloat() - rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                    }
                }
            }
        }
    }
    
    public void faceBed() {
		if (this.ridingEntity != null) {
	        int dogX = MathHelper.floor_double(this.posX);
	        int dogZ = MathHelper.floor_double(this.posZ);
	        int bedX = MathHelper.floor_double(this.saveposition.getBedX());
	        int bedZ = MathHelper.floor_double(this.saveposition.getBedZ());
	        float distance = (float)this.talents.getTalentLevel(EnumTalents.BEDFINDER) * 200F - (MathHelper.abs(dogZ - bedZ) + MathHelper.abs(dogX - bedX));

	        if (this.talents.getTalentLevel(EnumTalents.BEDFINDER) == 5 || distance >= 0.0F) {
	            findBed(10F, this.getVerticalFaceSpeed());
	        }
	    }
	}

	public void findBed(float f, float f1) {
        double d = this.saveposition.getBedX() - this.posX;
        double d1 = this.saveposition.getBedZ() - this.posZ;
        double d2 = this.saveposition.getBedY() - this.posY;
        double d3 = MathHelper.sqrt_double(d * d + d1 * d1);
        float f2 = (float)((Math.atan2(d1, d) * 180D) / Math.PI) - 90F;
        float f3 = (float)(-((Math.atan2(d2, d3) * 180D) / Math.PI));
        this.rotationPitch = -doggyRotation(this.rotationPitch, f3, f1);
        this.rotationYaw = doggyRotation(this.rotationYaw, f2, f);
    }
    
    private static float doggyRotation(float f, float f1, float f2){
        float f3;
        for (f3 = f1 - f; f3 < -180F; f3 += 360F) {}
        for (; f3 >= 180F; f3 -= 360F) {}
        if (f3 > f2) {
            f3 = f2;
        }
        if (f3 < -f2) {
            f3 = -f2;
        }
        return f + f3;
    }
    
    @Override
    public void setFire(int amount) {
    	if(this.talents.getTalentLevel(EnumTalents.HELLHOUND) != 5)
    		super.setFire(amount);
    }
    
    public int nourishment()
    {
        int amount = 0;

        if (this.getDogTummy() > 0)
        {
            amount = 40 + 4 * (effectiveLevel() + 1);

            if (isSitting() && this.talents.getTalentLevel(EnumTalents.QUICKHEALER) == 5)
            {
                amount += 20 + 2 * (effectiveLevel() + 1);
            }

            if (!isSitting())
            {
                amount *= 5 + this.talents.getTalentLevel(EnumTalents.QUICKHEALER);
                amount /= 10;
            }
        }

        return amount;
    }

    public void setIsBegging(boolean par1) {
        if (par1)
            this.dataWatcher.updateObject(19, Byte.valueOf((byte)1));
        else
            this.dataWatcher.updateObject(19, Byte.valueOf((byte)0));
    }

    /**
     * Returns true if the mob is currently able to mate with the specified mob.
     */
    @Override
    public boolean canMateWith(EntityAnimal par1EntityAnimal){
        if (par1EntityAnimal == this)
            return false;
        else if (!this.isTamed())
            return false;
        else if (!(par1EntityAnimal instanceof EntityDTDoggy))
            return false;
        else
        {
            EntityDTDoggy entitywolf = (EntityDTDoggy)par1EntityAnimal;
            return !entitywolf.isTamed() ? false : (entitywolf.isSitting() ? false : this.isInLove() && entitywolf.isInLove());
        }
    }

    public boolean isBegging()
    {
        return this.dataWatcher.getWatchableObjectByte(19) == 1;
    }
    
    public int masterOrder() {
    	int order = 0;
        EntityPlayer player = (EntityPlayer)this.getOwner();

        if (player != null) {
        	
            float distanceAway = player.getDistanceToEntity(this);
            ItemStack itemstack = player.inventory.getCurrentItem();

            if (itemstack != null && (itemstack.getItem() instanceof ItemTool) && distanceAway <= 20F)
            {
                order = 1;
            }

            if (itemstack != null && ((itemstack.getItem() instanceof ItemSword) || (itemstack.getItem() instanceof ItemBow)))
            {
                order = 2;
            }

            if (itemstack != null && itemstack.getItem() == Items.wheat)
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
    
    public void setCharmerCharge(int par1) {
    	this.charmerCharge = par1;
    }
    
    public int getCharmerCharge() {
    	return this.charmerCharge;
    }
    
    public int fallProtection() {
        return this.talents.getTalentLevel(EnumTalents.PILLOWPAW) * 3;
    }

    public boolean isImmuneToFalls() {
        return this.talents.getTalentLevel(EnumTalents.PILLOWPAW) == 5;
    }

    @Override
    public boolean canBreatheUnderwater() {
        return this.talents.getTalentLevel(EnumTalents.FISHERDOG) == 5;
    }
    
    /**
     * Determines if an entity can be despawned, used on idle far away entities
     */
    @Override
    protected boolean canDespawn()
    {
        return false;
    }

    @Override
    public boolean func_142018_a(EntityLivingBase par1EntityLivingBase, EntityLivingBase par2EntityLivingBase)
    {
        if ((!(par1EntityLivingBase instanceof EntityCreeper) || this.talents.getTalentLevel(EnumTalents.CREEPERSWEEPER) == 5) && !(par1EntityLivingBase instanceof EntityGhast))
        {
            if (par1EntityLivingBase instanceof EntityDTDoggy && par1EntityLivingBase != this)
            {
            	return false;
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

    public boolean isImmortal() {
        return this.isTamed() && !Constants.allowPermaDeath || this.level.isDireDog();
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
    public double getYOffset() {
        return (double)-1.0F;
    }
    
    @Override
    public EntityAgeable createChild(EntityAgeable par1EntityAgeable) {
    	EntityDTDoggy entitywolf = new EntityDTDoggy(this.worldObj);
        String s = this.func_152113_b();

        if (s != null && s.trim().length() > 0) {
            entitywolf.func_152115_b(s);
            entitywolf.setTamed(true);
        }
        
        this.lastBaby = entitywolf;

        return entitywolf;
    }

    public int getDogTummy() {
		return this.dataWatcher.getWatchableObjectInt(WatchableDataLib.ID_WOLF_TUMMY);
	}
    
    public void setDogTummy(int par1) {
    	if(par1 > 120) {
    		par1 = 120;
    	}
    	if(par1 < 0) {
    		par1 = 0;
    	}
    	
    	this.dataWatcher.updateObject(WatchableDataLib.ID_WOLF_TUMMY, par1);
    }
}
