package doggytalents.entity;

import java.util.Iterator;
import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import doggytalents.DoggyTalentsMod;
import doggytalents.ModItems;
import doggytalents.api.IDogTreat;
import doggytalents.api.IDogTreat.EnumFeedBack;
import doggytalents.api.Properties;
import doggytalents.core.helper.ReflectionHelper;
import doggytalents.lib.TexturePaths;

import net.minecraft.block.Block;
import net.minecraft.block.StepSound;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

/**
 * @author ProPercivalalb
 **/
public class olds extends EntityAnimal
{
    private boolean looksWithInterest;
    private float field_25048_b;
    private float field_25054_c;
    private boolean isShaking;
    private int snowyTime;
    private boolean field_25052_g;
    private float timeWolfIsShaking;
    private float prevTimeWolfIsShaking;
    public DoggySkills talents;
    public DoggyLevel level;
    public DoggyMode mode;
    public DoggySavePosition saveposition;
    public boolean ageCheck;
    private boolean canSeeCreeper;
    boolean hasBone;
    public float field_752_b;
    public float destPos;
    public float field_757_d;
    public float field_756_e;
    public float field_755_h;
    public int HealingTick;
    public boolean StandBy;
    public int guardTime;
    public int HungerCheck = 100;
    public InventoryPackPuppy inventory;
    public int ReversionTime;

    public olds(World world) {
        super(world);
        //this.texture = TexturePaths.TEX_MOBS + "doggywild.png";
        this.setSize(0.8F, 0.8F);
        if(talents == null) {
        	this.talents = new DoggySkills(this);
        }
        if(level == null) {
      	  this.level = new DoggyLevel(this);
        }
        if(mode == null) {
        	this.mode = new DoggyMode(this);
        }
        this.saveposition = new DoggySavePosition(this);
        inventory = new InventoryPackPuppy(this, 3 * this.talents.getTalentLevel(EnumSkills.PACKPUPPY));
        this.snowyTime = 0;
    }
    
    @Override
    protected void func_110147_ax() {
        super.func_110147_ax();
        this.updateHealthaSpeed();
    }
    
    public void updateHealthaSpeed() {
        this.func_110148_a(SharedMonsterAttributes.field_111263_d).func_111128_a(this.getSpeedModifier());
        
        this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a(this.getMaxHealth());
    }

    @Override
    public boolean isAIEnabled() {
        return false;
    }
    
    public double getMaxHealth() {
        return 20D + (effectiveLevel() + 1D) * 2D;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(17, Byte.valueOf((byte)0)); // Angry or not
        this.dataWatcher.addObject(18, ""); // Owner Name
        this.dataWatcher.addObject(19, ""); // Wolf Name
        this.dataWatcher.addObject(20, "0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0"); // Talent Data
        this.dataWatcher.addObject(21, "0:0"); // Levels
        this.dataWatcher.addObject(22, 0); // Will obey others
        this.dataWatcher.addObject(23, new Integer(0)); // Tummy Tick
        this.dataWatcher.addObject(24, new Integer(60)); // Wolf Tummy
        this.dataWatcher.addObject(25, new Integer(1)); // Mode
        this.dataWatcher.addObject(26, "-1:-1:-1:-1:-1:-1"); // Bed loction
        this.dataWatcher.addObject(29, new Byte((byte)0)); // Texture
    }
    
    @Override
    protected boolean canTriggerWalking() {
        return false;
    }

    //TODO 23
    /**
    @SideOnly(Side.CLIENT)
    public String getTexture() {
        if (isTamed() && this.getTameSkin() == 0) {
            return TexturePaths.TEX_MOBS + "doggytex0.png";
        }

        if (isTamed() && this.getTameSkin() != 0) {
            return TexturePaths.TEX_MOBS + "doggytex" + this.getTameSkin() + ".png";
        }

        if (isAngry()) {
            return "/mob/wolf_angry.png";
        }
        return super.getTexture();
    }**/

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        NBTDataHelper.writeEntityToNBT(this, nbt);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        NBTDataHelper.readEntityFromNBT(this, nbt);
    }

    protected boolean canDespawn() {
        return isAngry();
    }

    @Override
    protected String getLivingSound() {
        if (canSeeCreeper) {
           return "mob.wolf.growl";
        }
        if (rand.nextInt(10) < Properties.barkRate) {
            if (rand.nextInt(3) == 0) {
                if (isTamed() && func_110143_aJ() == 1) {
                    return "mob.wolf.whine";
                }
                else {
                    return "mob.wolf.panting";
                }
            }
            else {
                return "mob.wolf.bark";
            }
        }
        else {
            return "";
        }
    }

    @Override
    protected String getHurtSound() {
        return "mob.wolf.hurt";
    }

    @Override
    protected String getDeathSound() {
        return "mob.wolf.death";
    }
    
    @Override
    protected void playStepSound(int par1, int par2, int par3, int par4){
        this.playSound("mob.wolf.step", 0.15F, 1.0F);
    }

    @Override
    protected float getSoundVolume() {
        return 0.4F;
    }

    protected int getDropItemId() {
        return -1;
    }

    public void onEntityUpdate() {
        super.onEntityUpdate();

        if (this.func_110143_aJ() <= 0 && isImmortal()) {
            deathTime = 0;
            this.setEntityHealth(1);
        }
    }

    public boolean canAttack() {
    	EntityPlayer entityplayer = worldObj.getPlayerEntityByName(getOwner());
    	return !this.isSitting() && this.func_110143_aJ() != 1 && entityplayer != null;
    }
    
    @Override
    protected void updateEntityActionState()
    {
    	this.updateHealthaSpeed();
    	EntityPlayer entityplayer = worldObj.getPlayerEntityByName(getOwner());
    	
    	if(this.mode.isMode(EnumMode.AGGRESIVE)) {
    		if(canAttack() && entityplayer.func_110144_aD() != null) {
    			if(!(entityplayer.func_110144_aD() instanceof EntityCreeper) || this.talents.getTalentLevel(EnumSkills.CREEPERSWEEPER) == 5) {
    				this.entityToAttack = entityplayer.func_110144_aD();
    			}
    		}
    	
    		else if(canAttack() && entityplayer.getAITarget() != null) {
    			if(!(entityplayer.func_110144_aD() instanceof EntityCreeper) || this.talents.getTalentLevel(EnumSkills.CREEPERSWEEPER) == 5) {
    				this.entityToAttack = entityplayer.getAITarget();
    			}
    		}
    	}
    	
    	if(this.mode.isMode(EnumMode.BERSERKER)) {
    		Entity attackMob = this.getMobToAttack();
    		if(attackMob != null && (!(attackMob instanceof EntityCreeper) || this.talents.getTalentLevel(EnumSkills.CREEPERSWEEPER) == 5)) {
    			this.entityToAttack = attackMob;
    		}
    	}
    	
    	if(this.isSitting()) {
    		entityToAttack = null;
    	}
    	
    	if(!this.hasPath()) {
    		this.setJumping(false);
    	}
    	
        super.updateEntityActionState();
        
        TalentHelper.faceBed(this);
        
        if (ageCheck) {
            if (getGrowingAge() < 0 && Properties.tenDayPuppies)
            {
                setGrowingAge(0xfffc5680);
            }

            ageCheck = false;
        }
        
        if (this.mode.isMode(EnumMode.WANDERING) && this.getWolfTummy() <= 60 && (saveposition.getBowlX() != -1 && saveposition.getBowlY() != -1 && saveposition.getBowlZ() != -1) && HungerCheck == 0 && !this.isSitting())
        {
            int j = MathHelper.floor_double(saveposition.getBowlX()) + 1;
            int l = MathHelper.floor_double(saveposition.getBowlY() - 0.20000000298023224D - (double)yOffset);
            int j1 = MathHelper.floor_double(saveposition.getBowlZ()) + 1;
            PathEntity pathentity = worldObj.getEntityPathToXYZ(this, j, l, j1, 100F, true, false, false, true);
            setPathToEntity(pathentity);
            HungerCheck = 100;
        }
        else if (!hasAttacked && !hasPath() && isTamed() && !this.mode.isMode(EnumMode.WANDERING) && ridingEntity == null)
        {
            EntityPlayer entityplayer5 = worldObj.getPlayerEntityByName(getOwner());

            if (entityplayer5 != null)
            {
                float f = entityplayer5.getDistanceToEntity(this);

                if (f <= 3F && hasBone)
                {
                	if(!this.worldObj.isRemote)
                	{
                		this.entityDropItem(new ItemStack(ModItems.throwBone.itemID, 1, 1), 0.0F);
                	}
                    hasBone = false;
                }
                else if (masterOrder() == 2 || masterOrder() == 1 && !this.mode.isMode(EnumMode.WANDERING))
                {
                    int k1 = MathHelper.floor_double(posX);
                    int i2 = MathHelper.floor_double(posY - 0.20000000298023224D - (double)yOffset);
                    int j2 = MathHelper.floor_double(posZ);
                    int k2 = k1 - masterPosX();
                    int i3 = j2 - masterPosZ();
                    int j3 = masterPosX() + k2 * 2;
                    int k3 = masterPosZ() + i3 * 2;
                    int l3 = masterPosX() + k2 / 2;
                    int i4 = masterPosZ() + i3 / 2;
                    float f4 = entityplayer5.getDistanceToEntity(this);

                    if (f4 < 5F)
                    {
                        PathEntity pathentity1 = worldObj.getEntityPathToXYZ(this, j3, i2, k3, 10F, true, false, false, true);
                        setPathToEntity(pathentity1);
                    }

                    if (f4 > 10F && f4 < 20F)
                    {
                        PathEntity pathentity2 = worldObj.getEntityPathToXYZ(this, l3, i2, i4, 10F, true, false, false, true);
                        setPathToEntity(pathentity2);
                    }

                    if (f4 >= 20F)
                    {
                        getPathOrWalkableBlock(entityplayer5, f4);
                    }
                }

                if (masterOrder() == 3 && this.talents.getTalentLevel(EnumSkills.SHEPHERDOG) > 0 && !hasPath() && !this.mode.isMode(EnumMode.WANDERING) && !isSitting() && riddenByEntity == null)
                {
                    List list1 = worldObj.getEntitiesWithinAABB(EntityAnimal.class, AxisAlignedBB.getBoundingBox(posX, posY, posZ, posX + 1.0D, posY + 1.0D, posZ + 1.0D).expand(10D, 2D, 10D));

                    if (!list1.isEmpty())
                    {
                        EntityAnimal entityanimal = null;
                        boolean flag = false;

                        for (int l2 = 0; l2 < list1.size() && entityanimal == null; l2++)
                        {
                            boolean flag1 = false;
                            EntityAnimal entityanimal1 = (EntityAnimal)list1.get(l2);

                            if (((entityanimal1 instanceof EntityPig) || (entityanimal1 instanceof EntityChicken) || (entityanimal1 instanceof EntityCow) || (entityanimal1 instanceof EntitySheep)))
                            {
                                flag1 = true;
                            }

                            if (flag1)
                            {
                                entityanimal = entityanimal1;
                            }
                        }

                        if (entityanimal != null)
                        {
                            float f3 = entityanimal.getDistanceToEntity(this);

                            if (f3 > 2.0F)
                            {
                                getPathOrWalkableBlock(entityanimal, f3);
                            }
                        }
                    }
                }
                else if ((masterOrder() == 0 || masterOrder() == 3 && riddenByEntity != entityplayer5) && !this.mode.isMode(EnumMode.WANDERING))
                {
                    float f1 = entityplayer5.getDistanceToEntity(this);

                    if (f1 > 5F)
                    {
                        getPathOrWalkableBlock(entityplayer5, f1);
                    }
                }
            }
            else if (!isInWater() && !this.mode.isMode(EnumMode.WANDERING))
            {
                setSitting(true);
            }
        }
        else if (entityToAttack == null && !hasPath() && !isTamed() && worldObj.rand.nextInt(100) == 0)
        {
            List list = worldObj.getEntitiesWithinAABB(EntitySheep.class, AxisAlignedBB.getBoundingBox(posX, posY, posZ, posX + 1.0D, posY + 1.0D, posZ + 1.0D).expand(16D, 4D, 16D));

            if (!list.isEmpty())
            {
                entityToAttack = (Entity)list.get(worldObj.rand.nextInt(list.size()));
            }
        }
    }
    
    @Override
    public void onLivingUpdate() {	
        super.onLivingUpdate();
        
        if (isTamed() && masterOrder() == 3 && func_110143_aJ() != 1 && riddenByEntity == null && this.talents.getTalentLevel(EnumSkills.SHEPHERDOG) > 0 && !isSitting() && !this.mode.isMode(EnumMode.WANDERING))
        {
            List list = worldObj.getEntitiesWithinAABB(EntityAnimal.class, AxisAlignedBB.getBoundingBox(posX, posY, posZ, posX + 1.0D, posY + 1.0D, posZ + 1.0D).expand(2D, 2D, 2D));

            if (!list.isEmpty())
            {
                boolean flag = false;
                EntityAnimal entityanimal = null;

                for (int l = 0; l < list.size() && entityanimal == null; l++)
                {
                    boolean flag1 = false;
                    EntityAnimal entityanimal1 = (EntityAnimal)list.get(l);

                    if (((entityanimal1 instanceof EntityPig) || (entityanimal1 instanceof EntityChicken) || (entityanimal1 instanceof EntityCow) || (entityanimal1 instanceof EntitySheep)))
                    {
                        flag1 = true;
                    }

                    if (flag1)
                    {
                        entityanimal = entityanimal1;
                    }
                }

                if (entityanimal != null)
                {
                    entityanimal.mountEntity(this);
                }
            }
        }
        if(isTamed() && masterOrder() != 3 && riddenByEntity instanceof EntityAnimal && this.talents.getTalentLevel(EnumSkills.SHEPHERDOG) > 0) {
        	this.riddenByEntity.ridingEntity = null;
        	this.riddenByEntity = null;
        }
        
        if (this.level.isDireDog() && !Properties.direParticalsOff) {
            for (int i = 0; i < 2; i++) {
                worldObj.spawnParticle("portal", posX + (rand.nextDouble() - 0.5D) * (double)width, (posY + rand.nextDouble() * (double)height) - 0.25D, posZ + (rand.nextDouble() - 0.5D) * (double)width, (rand.nextDouble() - 0.5D) * 2D, -rand.nextDouble(), (rand.nextDouble() - 0.5D) * 2D);
            }
        }
        
        if (ReversionTime > 0)
        {
            ReversionTime--;
        }
        
        if (guardTime > 0)
        {
            guardTime--;
        }

        if (HungerCheck > 0)
        {
            HungerCheck--;
        }
        
        EntityPlayer entityplayer = worldObj.getPlayerEntityByName(getOwner());
        
        if (entityplayer != null && entityplayer.func_110143_aJ() <= 6 && this.talents.getTalentLevel(EnumSkills.RESCUEDOG) != 0 && this.getWolfTummy() > healCost())
        {
            entityplayer.heal((int)(this.talents.getTalentLevel(EnumSkills.RESCUEDOG) * 1.5D));
            this.setWolfTummy(this.getWolfTummy() - healCost());
        }
        
        if (this.func_110143_aJ() <= 0 && isImmortal())
        {
            deathTime = 0;
            this.setEntityHealth(1);
        }
        
        if(this.talents.getTalentLevel(EnumSkills.HELLHOUND) == 5) {
        	this.isImmuneToFire = true;
        }

        if (entityplayer != null && entityplayer.getBedLocation() != null) {
            this.saveposition.setBedX(entityplayer.getBedLocation().posX);
            this.saveposition.setBedY(entityplayer.getBedLocation().posY);
            this.saveposition.setBedZ(entityplayer.getBedLocation().posZ);
        }

        if (this.func_110143_aJ() <= 0 && isImmortal())
        {
            deathTime = 0;
            this.setEntityHealth(1);
        }

        if (this.getCharmerCharge() > 0 && isTamed())
        {
            this.setCharmerCharge(this.getCharmerCharge() - 1);
        }

        if (riddenByEntity == null && (!isSitting() && !this.mode.isMode(EnumMode.WANDERING) && !this.level.isDireDog() || worldObj.getWorldInfo().getWorldTime() % 2L == 0L) && Properties.isHungerOn)
        {
        	this.setTummyTick(this.getTummyTick() + 1);
        }

        if (riddenByEntity != null)
        {
            if (Properties.isHungerOn && (this.talents.getTalentLevel(EnumSkills.WOLFMOUNT) != 5 || worldObj.getWorldInfo().getWorldTime() % 2L == 0L))
            {
            	this.setTummyTick(this.getTummyTick() + 3);
            }

            if (!(riddenByEntity instanceof EntityPlayer))
            {
                this.setTummyTick(this.getTummyTick() + 5 - this.talents.getTalentLevel(EnumSkills.SHEPHERDOG));
            }
        }

        
        int j = MathHelper.floor_double(posX);
        int k = MathHelper.floor_double(boundingBox.minY);
        int i1 = MathHelper.floor_double(posZ);

        if (worldObj.getBlockId(j, k, i1) == Block.snow.blockID)
        {
        	this.snowyTime = 100;
        }
        else if (snowyTime > 0)
        {
            this.snowyTime--;
        }
        

        if (points() < usedPoints())
        {
            this.level.increaseLevel(1);
        }

        HealingTick += nourishment();

        if (this.getWolfTummy() > 120)
        {
            this.setWolfTummy(120);
        }

        if (this.getWolfTummy() == 0 && worldObj.getWorldInfo().getWorldTime() % 100L == 0L && this.func_110143_aJ() > 1)
        {
            attackEntityFrom(DamageSource.generic, 1);
            fleeingTick = 0;
        }

        if (this.getTummyTick() > 300)
        {
            if (this.getWolfTummy() > 0)
            {
                this.setWolfTummy(this.getWolfTummy() -1);
            }

            this.setTummyTick(0);
        }

        if (HealingTick >= 6000)
        {
            if (this.func_110143_aJ() < getMaxHealth() && this.func_110143_aJ() != 1)
            {
            	this.setEntityHealth(this.func_110143_aJ() + 1);
            }

            if (this.func_110143_aJ() > getMaxHealth())
            {
                this.setEntityHealth(this.func_110138_aP());
            }

            if (this.func_110143_aJ() < getMaxHealth() && this.func_110143_aJ() == 1)
            {
                this.setRegenTick(this.getRegenTick() + 1);

                if (this.talents.getTalentLevel(EnumSkills.GUARDDOG) == 5 && rand.nextInt(2) == 0)
                {
                	this.setRegenTick(this.getRegenTick() + 1);
                }
            }

            HealingTick = 0;
        }

        if (this.getRegenTick() >= 200)
        {
            this.setEntityHealth(this.func_110143_aJ() + 1);
            showHeartsOrSmokeFX(true);
            this.setRegenTick(0);
        }

        if (isImmuneToFalls())
        {
            field_756_e = field_752_b;
            field_757_d = destPos;
            destPos += (double)(onGround ? -1 : 4) * 0.29999999999999999D;

            if (destPos < 0.0F)
            {
                destPos = 0.0F;
            }

            if (destPos > 1.0F)
            {
                destPos = 1.0F;
            }

            if (!onGround && field_755_h < 1.0F)
            {
                field_755_h = 1.0F;
            }

            field_755_h *= 0.90000000000000002D;

            if (!onGround && motionY < 0.0D)
            {
                motionY *= 0.80000000000000004D;
            }
        }

        if (!this.mode.isMode(EnumMode.WANDERING) && !isSitting())
        {
            if (masterOrder() == 0)
            {
                StandBy = false;
            }

            if (masterOrder() == 2)
            {
                StandBy = false;
            }

            if (masterOrder() == 3)
            {
                StandBy = false;
            }
        }

        if (this.talents.getTalentLevel(EnumSkills.PESTFIGHTER) != 0 && rand.nextInt(20) == 0)
        {
            byte byte0 = 1;

            if (this.talents.getTalentLevel(EnumSkills.PESTFIGHTER) == 5)
            {
                byte0 = 2;
            }

            slaySilverFish(byte0);
        }

        if (!this.worldObj.isRemote && this.talents.getTalentLevel(EnumSkills.PUPPYEYES) != 0 && this.getCharmerCharge() == 0)
        {
            EntityLiving entityliving = charmVillagers(this, 5D);

            if (entityliving != null && entityplayer != null)
            {
                int j1 = rand.nextInt(this.talents.getTalentLevel(EnumSkills.PUPPYEYES)) + (this.talents.getTalentLevel(EnumSkills.PUPPYEYES) != 5 ? 0 : 1);

                if (j1 == 0)
                {
                    entityplayer.addChatMessage((new StringBuilder()).append("Villager: Cute dog! Take good care of him. Or her. ").toString());
                    entityplayer.addChatMessage((new StringBuilder()).append("Here, it can have some pork.").toString());
                    entityliving.dropItem(Item.porkRaw.itemID, 2);
                }

                if (j1 == 1)
                {
                    entityplayer.addChatMessage((new StringBuilder()).append("Villager: Awww, who's a good boy?").toString());
                    entityplayer.addChatMessage((new StringBuilder()).append("Whoozagoodboy? You are! YOU ARE!").toString());
                    entityplayer.addChatMessage((new StringBuilder()).append("Yes you are! And a good boy's gotta eat!").toString());
                    entityliving.dropItem(Item.porkRaw.itemID, 5);
                }

                if (j1 == 2)
                {
                    entityplayer.addChatMessage((new StringBuilder()).append("Villager: Oh my goodness! Where did you get that little bundle").toString());
                    entityplayer.addChatMessage((new StringBuilder()).append("of happiness and loyalty? Here, take this").toString());
                    entityplayer.addChatMessage((new StringBuilder()).append("iron, make some gear, keep your buddy safe!").toString());
                    entityliving.dropItem(Item.ingotIron.itemID, 3);
                }

                if (j1 == 3)
                {
                    entityplayer.addChatMessage((new StringBuilder()).append("Villager: I think my heart just grew beyond the").toString());
                    entityplayer.addChatMessage((new StringBuilder()).append("size of my nose. Here, take this.").toString());
                    entityplayer.addChatMessage((new StringBuilder()).append("No, I insist!").toString());
                    entityliving.dropItem(Item.ingotGold.itemID, 2);
                }

                if (j1 == 4)
                {
                    entityplayer.addChatMessage((new StringBuilder()).append("Villager: I'd just like to tell you that you have").toString());
                    entityplayer.addChatMessage((new StringBuilder()).append("the bestest doggy in the universe!").toString());
                    entityplayer.addChatMessage((new StringBuilder()).append("And also to give you a diamond!").toString());
                    entityliving.dropItem(Item.diamond.itemID, 1);
                }

                if (j1 == 5)
                {
                    entityplayer.addChatMessage((new StringBuilder()).append("Villager: aaaaAAAAAAAAAAAAAAAAAAAAAAAAA").toString());
                    entityplayer.addChatMessage((new StringBuilder()).append("AAAAAAAAAAAAAAAAAAAaaaaaaaaaa").toString());
                    entityplayer.addChatMessage((new StringBuilder()).append("aaaaaaaaaAAAAAAAAWWWWWWWWW!").toString());
                    entityliving.dropItem(Item.appleRed.itemID, 1);
                    entityliving.dropItem(Item.cake.itemID, 1);
                    entityliving.dropItem(Item.slimeBall.itemID, 3);
                    entityliving.dropItem(Item.porkRaw.itemID, 5);
                }

                this.setCharmerCharge(this.talents.getTalentLevel(EnumSkills.PUPPYEYES) != 5 ? 48000 : 24000);
            }
        }

        if (this.func_110143_aJ() == 1) {//&& !(entityToAttack instanceof EntityItem) && !(entityToAttack instanceof EntityDTDoggy)) {
            entityToAttack = null;
        }
        
        if (entityToAttack == null && isTamed() && this.talents.getTalentLevel(EnumSkills.CREEPERSWEEPER) > 0)
        {
            List list1 = worldObj.getEntitiesWithinAABB(EntityCreeper.class, AxisAlignedBB.getBoundingBox(posX, posY, posZ, posX + 1.0D, posY + 1.0D, posZ + 1.0D).expand(sniffRange(), 4D, sniffRange()));

            if (!list1.isEmpty() && !isSitting() && this.func_110143_aJ() > 1)
            {
                canSeeCreeper = true;
            }
            else
            {
                canSeeCreeper = false;
            }
        }
        
        if (getEntityToAttack() == null && !isSitting() && this.mode.getMode() != EnumMode.WANDERING && isTamed()) {
        	Entity attack = fetch(this, 15D);
        	entityToAttack = attack;
        }
        
        if (!worldObj.isRemote && isShaking && !field_25052_g && !hasPath() && onGround) {
            field_25052_g = true;
            timeWolfIsShaking = 0.0F;
            prevTimeWolfIsShaking = 0.0F;
            worldObj.setEntityState(this, (byte)8);
        }
    }

    private boolean hasCurrentTarget()  {
		return entityToAttack != null;
	}

	private Entity getCurrentTarget()  {
		return this.entityToAttack;
	}

	@Override
	public void onUpdate() {
        super.onUpdate();
        
        if (this.talents.getTalentLevel(EnumSkills.PACKPUPPY) == 5 && this.func_110143_aJ() > 1) {
            autoPickup();
        }
        
        field_25054_c = field_25048_b;
        if (looksWithInterest)
        {
            field_25048_b = field_25048_b + (1.0F - field_25048_b) * 0.4F;
        }
        else
        {
            field_25048_b = field_25048_b + (0.0F - field_25048_b) * 0.4F;
        }
        if (looksWithInterest)
        {
            numTicksToChaseTarget = 10;
        }
        if (isWet())
        {
            isShaking = true;
            field_25052_g = false;
            timeWolfIsShaking = 0.0F;
            prevTimeWolfIsShaking = 0.0F;
        }
        else if ((isShaking || field_25052_g) && field_25052_g)
        {
            if (timeWolfIsShaking == 0.0F)
            {
                worldObj.playSoundAtEntity(this, "mob.wolf.shake", getSoundVolume(), (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
            }
            prevTimeWolfIsShaking = timeWolfIsShaking;
            timeWolfIsShaking += 0.05F;
            if (prevTimeWolfIsShaking >= 2.0F)
            {
            	if (didWolfFish()) {
                    if (didWolfCook()) {
                    	if(!this.worldObj.isRemote) {
                    		dropItem(Item.fishCooked.itemID, 1);
                    	}
                    }
                    else {
                    	if(!this.worldObj.isRemote) {
                    		dropItem(Item.fishRaw.itemID, 1);
                    	}
                    }
                }
            	
                isShaking = false;
                field_25052_g = false;
                prevTimeWolfIsShaking = 0.0F;
                timeWolfIsShaking = 0.0F;
            }
            if (timeWolfIsShaking > 0.4F)
            {
                float f = (float)boundingBox.minY;
                int i = (int)(MathHelper.sin((timeWolfIsShaking - 0.4F) * 3.141593F) * 7F);
                for (int j = 0; j < i; j++)
                {
                    float f1 = (rand.nextFloat() * 2.0F - 1.0F) * width * 0.5F;
                    float f2 = (rand.nextFloat() * 2.0F - 1.0F) * width * 0.5F;
                    if (snowyTime > 0)
                    {
                        worldObj.spawnParticle("snowballpoof", posX + (double)f1, f + 0.8F, posZ + (double)f2, motionX, motionY, motionZ);
                    }
                    else
                    {
                        worldObj.spawnParticle("splash", posX + (double)f1, f + 0.8F, posZ + (double)f2, motionX, motionY, motionZ);
                    }
                }
            }
        }
    }

    public boolean getWolfShaking()
    {
        return isShaking;
    }

    public float getShadingWhileShaking(float f)
    {
        return 0.75F + ((prevTimeWolfIsShaking + (timeWolfIsShaking - prevTimeWolfIsShaking) * f) / 2.0F) * 0.25F;
    }

    public float getShakeAngle(float f, float f1)
    {
        float f2 = (prevTimeWolfIsShaking + (timeWolfIsShaking - prevTimeWolfIsShaking) * f + f1) / 1.8F;
        if (f2 < 0.0F)
        {
            f2 = 0.0F;
        }
        else if (f2 > 1.0F)
        {
            f2 = 1.0F;
        }
        return MathHelper.sin(f2 * 3.141593F) * MathHelper.sin(f2 * 3.141593F * 11F) * 0.15F * 3.141593F;
    }

    public float getInterestedAngle(float f)
    {
        return (field_25054_c + (field_25048_b - field_25054_c) * f) * 0.15F * 3.141593F;
    }

    @Override
    public float getEyeHeight()
    {
        return height * 0.8F;
    }

    @Override
    public int getVerticalFaceSpeed()
    {
        if (isSitting())
        {
            return 20;
        }
        else
        {
            return super.getVerticalFaceSpeed();
        }
    }

    private void getPathOrWalkableBlock(Entity entity, float f)
    {
        PathEntity pathentity = worldObj.getPathEntityToEntity(this, entity, 16F, false, false, false, true);
        if (pathentity == null && f > 20F)
        {
            int i = MathHelper.floor_double(entity.posX) - 2;
            int j = MathHelper.floor_double(entity.posZ) - 2;
            int k = MathHelper.floor_double(entity.boundingBox.minY);
            for (int l = 0; l <= 4; l++)
            {
                for (int i1 = 0; i1 <= 4; i1++)
                {
                    if ((l < 1 || i1 < 1 || l > 3 || i1 > 3) && worldObj.isBlockNormalCube(i + l, k - 1, j + i1) && !worldObj.isBlockNormalCube(i + l, k, j + i1) && !worldObj.isBlockNormalCube(i + l, k + 1, j + i1) && worldObj.getBlockId(i + l, k + 1, j + i1) != Block.lavaMoving.blockID && worldObj.getBlockId(i + l, k + 1, j + i1) != Block.lavaStill.blockID)
                    {
                        setLocationAndAngles((float)(i + l) + 0.5F, k, (float)(j + i1) + 0.5F, rotationYaw, rotationPitch);
                        return;
                    }
                }
            }
        }
        else
        {
            setPathToEntity(pathentity);
        }
    }

    @Override
    protected boolean isMovementCeased() {
        return isSitting() || field_25052_g || StandBy && entityToAttack == null;
    }

    @Override
    public boolean attackEntityFrom(DamageSource damagesource, float i)
    {
        Entity entity = damagesource.getEntity();

        if ((entity instanceof EntityArrow) && ((EntityArrow)entity).shootingEntity != null)
        {
            entity = ((EntityArrow)entity).shootingEntity;
            return true;
        }

        setSitting(false);

        if (entity != null)
        {
            if (guardTime > 0)
            {
                return false;
            }

            int j = this.talents.getTalentLevel(EnumSkills.GUARDDOG) != 5 ? 0 : 1;
            j += this.talents.getTalentLevel(EnumSkills.GUARDDOG);

            if (rand.nextInt(12) < j)
            {
                guardTime = 10;
                worldObj.playSoundAtEntity(this, "random.break", getSoundVolume(), (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
                return false;
            }

            i = (i + 1) / 2;
        }

        if (entity != null && !(entity instanceof EntityPlayer) && !(entity instanceof EntityArrow) && (entity instanceof EntityLiving))
        {
            alertDoggies((EntityLiving)entity, false);
        }

        if (super.attackEntityFrom(damagesource, i))
        {
            if (!isTamed() && !isAngry())
            {
                if ((entity instanceof EntityArrow) && ((EntityArrow)entity).shootingEntity != null)
                {
                    entity = ((EntityArrow)entity).shootingEntity;
                }

                if (entity instanceof EntityLiving)
                {
                    List list = worldObj.getEntitiesWithinAABB(olds.class, AxisAlignedBB.getBoundingBox(posX, posY, posZ, posX + 1.0D, posY + 1.0D, posZ + 1.0D).expand(16D, 4D, 16D));
                    Iterator iterator = list.iterator();

                    do
                    {
                        if (!iterator.hasNext())
                        {
                            break;
                        }

                        Entity entity1 = (Entity)iterator.next();
                        olds entitydtdoggy = (olds)entity1;

                        if (entitydtdoggy.isTamed() && entitydtdoggy.entityToAttack == null)
                        {
                            entitydtdoggy.entityToAttack = entity;
                        }
                    }
                    while (true);
                }
            }
            else if (entity != this && entity != null)
            {
                if (isTamed() && (entity instanceof EntityPlayer) && riddenByEntity != null && ((EntityPlayer)entity).username.equalsIgnoreCase(getOwner()))
                {
                    return false;
                }

                if (isTamed() && (entity instanceof EntityPlayer) && ((EntityPlayer)entity).username.equalsIgnoreCase(getOwner()))
                {
                    return false;
                }

                entityToAttack = entity;
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    @Override
    protected Entity findPlayerToAttack()
    {
        if (isAngry())
        {
            return worldObj.getClosestPlayerToEntity(this, 16D);
        }
        else
        {
        	float f = 8.0F;
            List list;
            int i;
            olds entityanimal;

            if (this.inLove > 0 && !this.isSitting())
            {
                list = this.worldObj.getEntitiesWithinAABB(this.getClass(), this.boundingBox.expand((double)f, (double)f, (double)f));

                for (i = 0; i < list.size(); ++i)
                {
                    entityanimal = (olds)list.get(i);

                    if (entityanimal != this && entityanimal.inLove > 0 && !entityanimal.isSitting())
                    {
                        return entityanimal;
                    }
                }
            }
        }
        
        return null;
    }

    private int breeding_2 = 0;
    
    @Override
    protected void attackEntity(Entity entity, float f)
    {
    	EntityPlayer entityplayer = worldObj.getPlayerEntityByName(getOwner());

        if (entity instanceof olds) {
            super.attackEntity(entity, f);
            return;
        }

        if (entity instanceof EntityPlayer) {
            entityToAttack = null;
            return;
        }
        else if (f > 2.0F && f < 6F && rand.nextInt(20) == 0) {
            if (onGround) {
                double d = entity.posX - posX;
                double d1 = entity.posZ - posZ;
                float f1 = MathHelper.sqrt_double(d * d + d1 * d1);
                motionX = ((d / (double)f1) * 0.5D * 0.80000001192092896D + motionX * 0.20000000298023224D) * (double)getSpeedModifier();
                motionZ = ((d1 / (double)f1) * 0.5D * 0.80000001192092896D + motionZ * 0.20000000298023224D) * (double)getSpeedModifier();
                motionY = 0.40000000596046448D;
            }
        }
        else if (this.attackTime <= 0 && (double)f < 1.5D && entity.boundingBox.maxY > boundingBox.minY && entity.boundingBox.minY < boundingBox.maxY) {
        	 this.attackTime = 20;
        	 int i = 4 + (effectiveLevel() + 1) / 2;
             int j = this.talents.getTalentLevel(EnumSkills.BLACKPELT) != 5 ? 0 : 1;
             j += this.talents.getTalentLevel(EnumSkills.BLACKPELT);

             if (rand.nextInt(6) < j)
             {
                 i += (i + 1) / 2;
                 onCriticalHit(entity);
             }

             if (entity instanceof EntityCreeper) {
            	 EntityCreeper creeper = (EntityCreeper)entity;
            	 ReflectionHelper.setField(EntityCreeper.class, creeper, 1, 1);
             }

             if (this.talents.getTalentLevel(EnumSkills.HELLHOUND) != 0)
             {
                 entity.setFire(this.talents.getTalentLevel(EnumSkills.HELLHOUND));
             }
             
             if ((entity instanceof EntityItem) && ((EntityItem)entity).getEntityItem().itemID == ModItems.throwBone.itemID && ((EntityItem)entity).getEntityItem().getItemDamage() == 0)
             {
                 i = 12;
                 hasBone = true;
             }

             if ((entity instanceof EntityLiving) && this.talents.getTalentLevel(EnumSkills.POSIONFANG) > 0)
             {
                 ((EntityLiving)entity).addPotionEffect(new PotionEffect(Potion.poison.id, this.talents.getTalentLevel(EnumSkills.POSIONFANG) * 20, 0));
             }

             if (entity instanceof EntityZombie)
             {
                 EntityZombie entityzombie = (EntityZombie)entity;
                 entityzombie.setAttackTarget(this);
             }

             entity.attackEntityFrom(DamageSource.causeMobDamage(this), i);
        }
    }

    private void procreate(olds par1EntityAnimal)
    {
        olds entityageable = (olds)this.createChild(par1EntityAnimal);

        if (entityageable != null)
        {
            this.setGrowingAge(6000);
            par1EntityAnimal.setGrowingAge(6000);
            this.inLove = 0;
            this.breeding_2 = 0;
            this.entityToAttack = null;
            par1EntityAnimal.entityToAttack = null;
            par1EntityAnimal.breeding_2 = 0;
            par1EntityAnimal.inLove = 0;
            entityageable.setGrowingAge(-24000);
            entityageable.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);

            for (int i = 0; i < 7; ++i)
            {
                double d0 = this.rand.nextGaussian() * 0.02D;
                double d1 = this.rand.nextGaussian() * 0.02D;
                double d2 = this.rand.nextGaussian() * 0.02D;
                this.worldObj.spawnParticle("heart", this.posX + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, this.posY + 0.5D + (double)(this.rand.nextFloat() * this.height), this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, d0, d1, d2);
            }

            this.worldObj.spawnEntityInWorld(entityageable);
        }
    }
    
    @Override
    public boolean interact(EntityPlayer entityplayer)
    {
        ItemStack itemstack = entityplayer.inventory.getCurrentItem();
        if (!isTamed())
        {
            if (itemstack != null && itemstack.itemID == Item.bone.itemID && !isAngry())
            {
                itemstack.stackSize--;
                if (itemstack.stackSize <= 0)
                {
                    entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, null);
                }
                if (!worldObj.isRemote)
                {
                    if (rand.nextInt(3) == 0)
                    {
                        setTamed(true);
                        setPathToEntity(null);
                        this.setSitting(true);
                        setEntityHealth(20);
                        setOwner(entityplayer.username);
                        showHeartsOrSmokeFX(true);
                        worldObj.setEntityState(this, (byte)7);
                    }
                    else
                    {
                        showHeartsOrSmokeFX(false);
                        worldObj.setEntityState(this, (byte)6);
                    }
                }
                return true;
            }
            if (itemstack != null && itemstack.itemID == ModItems.collarShears.itemID && this.ReversionTime < 1 && !worldObj.isRemote)
            {

                	this.setDead();
                	EntityWolf wolf = new EntityWolf(this.worldObj);
                	wolf.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);

                    	this.worldObj.spawnEntityInWorld(wolf);
                    
                    return true;
            }
        }
        else
        {
        	 if (itemstack != null && (Item.itemsList[itemstack.itemID] instanceof ItemFood) && foodValue(itemstack) != 0 && this.getWolfTummy() < 120)
             {
        		 if(!entityplayer.capabilities.isCreativeMode) {
     				itemstack.stackSize--;
     			 }

                 if (itemstack.stackSize <= 0)
                 {
                     entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, null);
                 }

                 this.setWolfTummy(this.getWolfTummy() + foodValue(itemstack));
                 return true;
             }
            if (itemstack != null && (Item.itemsList[itemstack.itemID] instanceof IDogTreat) && canInteract(entityplayer))
            {
            	IDogTreat treat = (IDogTreat)Item.itemsList[itemstack.itemID];
            	EnumFeedBack type = treat.canGiveToDog(entityplayer, this, this.level.getLevel(), this.level.getDireLevel());
            	treat.giveTreat(type, entityplayer, this, this.talents);
            	return true;
            }
            if (itemstack != null && itemstack.itemID == ModItems.collarShears.itemID && !isAngry() && entityplayer.username.equalsIgnoreCase(this.getOwner()))
            {
                if (!worldObj.isRemote)
                {
                	this.setTamed(false);
                    this.setPathToEntity(null);
                    this.setSitting(false);
                    this.setEntityHealth(8);
                    this.talents.resetTalents();
                    this.isImmuneToFire = false;
                    this.showHeartsOrSmokeFX(false);
                    this.setOwner("");
                    this.setWillObeyOthers(false);
                    this.mode.setMode(EnumMode.DOCILE);
                    this.ReversionTime = 40;
                }

                return true;
            }
            if (itemstack != null && itemstack.itemID == Item.bone.itemID && canInteract(entityplayer))
            {
                if (!worldObj.isRemote)
                {
                    this.setSitting(true);
                    if(this.ridingEntity != null) {
                    	this.mountEntity(null);
                    }
                    else {
                    	this.mountEntity(entityplayer);
                    }
                }
                return true;
            }
            if (itemstack != null && itemstack.itemID == Item.stick.itemID && canInteract(entityplayer))
            {
            	entityplayer.openGui(DoggyTalentsMod.instance, 1, this.worldObj, this.entityId, MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ));
            	return true;
            }
            if (itemstack != null && itemstack.itemID == Block.planks.blockID && canInteract(entityplayer))
            {
            	entityplayer.openGui(DoggyTalentsMod.instance, 2, this.worldObj, this.entityId, MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ));
            	return true;
            }
            if (itemstack != null && this.isBreedingItem(itemstack) && this.getGrowingAge() == 0 && this.inLove <= 0 && canInteract(entityplayer))
            {
                if (!entityplayer.capabilities.isCreativeMode)
                {
                    --itemstack.stackSize;

                    if (itemstack.stackSize <= 0)
                    {
                        entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, (ItemStack)null);
                    }
                }

                this.inLove = 600;
                this.entityToAttack = null;

                for (int i = 0; i < 7; ++i)
                {
                    double d0 = this.rand.nextGaussian() * 0.02D;
                    double d1 = this.rand.nextGaussian() * 0.02D;
                    double d2 = this.rand.nextGaussian() * 0.02D;
                    this.worldObj.spawnParticle("heart", this.posX + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, this.posY + 0.5D + (double)(this.rand.nextFloat() * this.height), this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, d0, d1, d2);
                }

                return true;
            }
            if (itemstack != null && itemstack.itemID == Item.cake.itemID)
            {
            	if(!entityplayer.capabilities.isCreativeMode) {
            		itemstack.stackSize--;
            	}

                if (itemstack.stackSize <= 0)
                {
                    entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, null);
                }

                if (!worldObj.isRemote)
                {
                    setPathToEntity(null);
                    setSitting(true);
                    this.setEntityHealth(this.func_110138_aP());
                    this.setRegenTick(0);
                    showHeartsOrSmokeFX(true);
                    worldObj.setEntityState(this, (byte)7);
                }

                return true;
            }
            if (this.canInteract(entityplayer))
            {
                if (!worldObj.isRemote)
                {
                    if (riddenByEntity != null && riddenByEntity != entityplayer)
                    {
                        riddenByEntity = null;
                    }

                    if (!entityplayer.isCollidedVertically && this.talents.getTalentLevel(EnumSkills.WOLFMOUNT) != 0)
                    {
                        setSitting(false);
                        entityplayer.mountEntity(this);
                    }
                    else
                    {
                        if (itemstack == null || itemstack.itemID != ModItems.commandEmblem.itemID)
                        {
                            setSitting(!isSitting());
                        }

                        //this.mode.setMode(EnumMode.DOCILE);
                        isJumping = false;
                        setPathToEntity((PathEntity)null);
                    }
                    
                    return true;
                }
            }
            if (canInteract(entityplayer) && this.ridingEntity == null)
            {
                if (!worldObj.isRemote)
                {
                    this.setSitting(!isSitting());
                    isJumping = false;
                    setPathToEntity(null);
                }
                return true;
            }
        }
        
        return super.interact(entityplayer);
    }

    public void showHeartsOrSmokeFX(boolean flag)
    {
        String s = "heart";
        if (!flag)
        {
            s = "smoke";
        }
        for (int i = 0; i < 7; i++)
        {
            double d = rand.nextGaussian() * 0.02D;
            double d1 = rand.nextGaussian() * 0.02D;
            double d2 = rand.nextGaussian() * 0.02D;
            worldObj.spawnParticle(s, (posX + (double)(rand.nextFloat() * width * 2.0F)) - (double)width, posY + 0.5D + (double)(rand.nextFloat() * height), (posZ + (double)(rand.nextFloat() * width * 2.0F)) - (double)width, d, d1, d2);
        }
    }

    @Override
    public void handleHealthUpdate(byte byte0)
    {
        if (byte0 == 7)
        {
            showHeartsOrSmokeFX(true);
        }
        else if (byte0 == 6)
        {
            showHeartsOrSmokeFX(false);
        }
        else if (byte0 == 8)
        {
            field_25052_g = true;
            timeWolfIsShaking = 0.0F;
            prevTimeWolfIsShaking = 0.0F;
        }
        else
        {
            super.handleHealthUpdate(byte0);
        }
    }
    
    protected void alertDoggies(EntityLivingBase entityliving, boolean flag) {
        if ((entityliving instanceof EntityCreeper) || (entityliving instanceof EntityGhast))
        {
            return;
        }

        if (entityliving instanceof olds)
        {
            olds entitydtdoggy = (olds)entityliving;

            if (entitydtdoggy.isTamed() && getOwner() == entitydtdoggy.getOwner())
            {
                return;
            }
        }

        if (entityliving instanceof EntityPlayer)
        {
            return;
        }

        List list = worldObj.getEntitiesWithinAABB(olds.class, AxisAlignedBB.getBoundingBox(posX, posY, posZ, posX + 1.0D, posY + 1.0D, posZ + 1.0D).expand(16D, 4D, 16D));
        Iterator iterator = list.iterator();

        do
        {
            if (!iterator.hasNext())
            {
                break;
            }

            Entity entity = (Entity)iterator.next();
            olds entitydtdoggy1 = (olds)entity;

            if (entitydtdoggy1.isTamed() && entitydtdoggy1.getEntityToAttack() == null && getOwner() == entitydtdoggy1.getOwner() && (!flag || !entitydtdoggy1.isSitting()))
            {
                entitydtdoggy1.setSitting(false);
                entitydtdoggy1.entityToAttack = entityliving;
            }
        }
        while (true);
    }
    
    public EntityItem fetch(Entity entity, double d) {
        double d1 = -1D;
        EntityItem entityitem = null;
        if(this.hasBone) {
        	return null;
        }
        List list = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox.expand(d, d, d));

        for (int i = 0; i < list.size(); i++) {
            Entity entity1 = (Entity)list.get(i);

            if ((entity1 instanceof EntityItem) && ((EntityItem)entity1).getEntityItem().itemID == ModItems.throwBone.itemID && ((EntityItem)entity1).getEntityItem().getItemDamage() == 0) {
                entityitem = (EntityItem)entity1;
            }
        }
        return entityitem;
    }
    
    public double sniffRange(){
        double d = 0.0D;

        for (int i = 0; i < this.talents.getTalentLevel(EnumSkills.CREEPERSWEEPER) * 6; i++)
        {
            d++;
        }

        return d;
    }
    
    public void onKillEntity(EntityLiving entityliving) {
        if (didWolfSlaughter()) {
            entityliving.onDeath(DamageSource.generic);
        }
    }

    public boolean didWolfSlaughter() {
        int i = this.talents.getTalentLevel(EnumSkills.HUNTERDOG);

        if (this.talents.getTalentLevel(EnumSkills.HUNTERDOG) == 5) {
            i++;
        }

        return rand.nextInt(10) < i;
    }

    public boolean didWolfFish() {
        return rand.nextInt(15) < this.talents.getTalentLevel(EnumSkills.FISHERDOG) * 2;
    }

    public boolean didWolfCook() {
        return rand.nextInt(15) < this.talents.getTalentLevel(EnumSkills.HELLHOUND) * 2;
    }
    
    public int healCost() {
        byte byte0 = 100;

        if (this.talents.getTalentLevel(EnumSkills.RESCUEDOG) == 5)
        {
            byte0 = 80;
        }

        return byte0;
    }
    
    public int foodValue(ItemStack itemstack)
    {
        int i = 0;

        if (itemstack != null && (itemstack.itemID == Item.fishRaw.itemID || itemstack.itemID == Item.fishCooked.itemID) && this.talents.getTalentLevel(EnumSkills.HAPPYEATER) == 5)
        {
            i = 30 + 3 * this.talents.getTalentLevel(EnumSkills.HAPPYEATER);
        }

        if (itemstack != null && itemstack.itemID == Item.rottenFlesh.itemID && this.talents.getTalentLevel(EnumSkills.HAPPYEATER) >= 3)
        {
            i = 30 + 3 * this.talents.getTalentLevel(EnumSkills.HAPPYEATER);
        }

        if (itemstack != null && itemstack.itemID == Item.rottenFlesh.itemID && this.talents.getTalentLevel(EnumSkills.HAPPYEATER) < 3)
        {
            i = 0;
        }

        if (itemstack != null && (itemstack.itemID == Item.porkRaw.itemID || itemstack.itemID == Item.porkCooked.itemID || itemstack.itemID == Item.beefRaw.itemID || itemstack.itemID == Item.beefCooked.itemID || itemstack.itemID == Item.chickenCooked.itemID || itemstack.itemID == Item.chickenRaw.itemID))
        {
            i = 40 + 4 * this.talents.getTalentLevel(EnumSkills.HAPPYEATER);
        }
        else if (itemstack != null && itemstack.itemID != Item.rottenFlesh.itemID && (Item.itemsList[itemstack.itemID] instanceof ItemFood))
        {
            ItemFood itemfood = (ItemFood)Item.itemsList[itemstack.itemID];

            if (itemfood.isWolfsFavoriteMeat())
            {
                i = 40 + 4 * this.talents.getTalentLevel(EnumSkills.HAPPYEATER);
            }
        }

        return i;
    }
    
    public EntityLiving charmVillagers(Entity entity, double d) {
        double d1 = -1D;
        EntityPlayer entityplayer = worldObj.getPlayerEntityByName(getOwner());
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

    public float getTailRotation()
    {
        if (isAngry())
        {
            return 1.53938F;
        }
        if (isTamed())
        {
        	 return (0.55F - ((float)(getMaxHealth() - this.func_110143_aJ()) / tailMod()) * 0.02F) * (float)Math.PI;
        }
        else
        {
            return 0.6283185F;
        }
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

    private int charmerCharge = 0;
    public void setCharmerCharge(int par1) {
    	this.charmerCharge = par1;
    }
    
    public int getCharmerCharge() {
    	return this.charmerCharge;
    }
    
    @Override
    public int getMaxSpawnedInChunk() {
        return 8;
    }

    public String getOwner()
    {
        return dataWatcher.getWatchableObjectString(18);
    }

    public void setOwner(String s) {
        dataWatcher.updateObject(18, s);
    }
    
    public void onCriticalHit(Entity entity) {
        worldObj.spawnParticle("crit", posX + (rand.nextDouble() - 0.5D) * (double)width, (posY + rand.nextDouble() * (double)height) - 0.25D, posZ + (rand.nextDouble() - 0.5D) * (double)width, (rand.nextDouble() - 0.5D) * 2D, -rand.nextDouble(), (rand.nextDouble() - 0.5D) * 2D);
    }
    
    public int masterOrder() {
        byte byte0 = 0;
        EntityPlayer entityplayer = worldObj.getPlayerEntityByName(getOwner());

        if (entityplayer != null)
        {
            float f = entityplayer.getDistanceToEntity(this);
            ItemStack itemstack = entityplayer.inventory.getCurrentItem();

            if (itemstack != null && (Item.itemsList[itemstack.itemID] instanceof ItemTool) && f <= 20F)
            {
                byte0 = 1;
            }

            if (itemstack != null && ((Item.itemsList[itemstack.itemID] instanceof ItemSword) || (Item.itemsList[itemstack.itemID] instanceof ItemBow)))
            {
                byte0 = 2;
            }

            if (itemstack != null && itemstack.itemID == Item.wheat.itemID)
            {
                byte0 = 3;
            }
        }

        return byte0;
    }
    
    public void autoPickup() {
        List list = worldObj.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(posX - 2.5D, posY - 1.0D, posZ - 2.5D, posX + 2.5D, posY + 1.0D, posZ + 2.5D));

        if (!list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                EntityItem entityitem = (EntityItem)list.get(i);
                if (entityitem.isEntityAlive() && !this.worldObj.isRemote && entityitem.getEntityItem().itemID != ModItems.throwBone.itemID) {
                    if(InventoryPackPuppy.addItem(this.inventory, (EntityItem)entityitem)) {
                    	worldObj.playSoundAtEntity(this, "random.pop", 0.2F, ((rand.nextFloat() - rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                    }
                }
            }
        }
    }

    public boolean isSitting()
    {
        return (dataWatcher.getWatchableObjectByte(17) & 1) != 0;
    }

    public void setSitting(boolean flag)
    {
        byte byte0 = dataWatcher.getWatchableObjectByte(17);
        if (flag)
        {
            dataWatcher.updateObject(17, Byte.valueOf((byte)(byte0 | 1)));
        }
        else
        {
            dataWatcher.updateObject(17, Byte.valueOf((byte)(byte0 & -2)));
        }
    }

    public boolean isAngry()
    {
        return (dataWatcher.getWatchableObjectByte(17) & 2) != 0;
    }

    public void setAngry(boolean flag)
    {
        byte byte0 = dataWatcher.getWatchableObjectByte(17);
        if (flag)
        {
            dataWatcher.updateObject(17, Byte.valueOf((byte)(byte0 | 2)));
        }
        else
        {
            dataWatcher.updateObject(17, Byte.valueOf((byte)(byte0 & -3)));
        }
    }

    public boolean isTamed()
    {
        return (dataWatcher.getWatchableObjectByte(17) & 4) != 0;
    }

    public void setTamed(boolean flag)
    {
        byte byte0 = dataWatcher.getWatchableObjectByte(17);
        if (flag)
        {
            dataWatcher.updateObject(17, Byte.valueOf((byte)(byte0 | 4)));
        }
        else
        {
            dataWatcher.updateObject(17, Byte.valueOf((byte)(byte0 & -5)));
        }
    }

    public int getTameSkin()
    {
        return this.dataWatcher.getWatchableObjectByte(29);
    }

    public void setTameSkin(int par1)
    {
        this.dataWatcher.updateObject(29, Byte.valueOf((byte)par1));
    }

    @Override
    public EntityAnimal createChild(EntityAgeable entityanimal)
    {
    	olds dog = new olds(worldObj);
    	dog.setTamed(true);
    	dog.setOwner(this.getOwner());
        return dog;
    }
    
    public double getSpeedModifier() {
    	double var1 = 1.0D;

        if (entityToAttack != null && !(entityToAttack instanceof olds) && !(entityToAttack instanceof EntityItem) && !(entityToAttack instanceof EntityPlayer))
        {
            for (int i = 0; i < this.talents.getTalentLevel(EnumSkills.DOGGYDASH); i++)
            {
                var1 += 0.12D;
            }

            if (this.talents.getTalentLevel(EnumSkills.DOGGYDASH) == 5)
            {
                var1 += 0.15D;
            }

            if (this.level.isDireDog())
            {
                var1 += 0.15D;
            }
        }

        return var1;
    }
    
    public int masterPosX()
    {
        EntityPlayer entityplayer = worldObj.getPlayerEntityByName(getOwner());
        int i = 0;

        if (entityplayer != null)
        {
            double d = entityplayer.posX;
            i = MathHelper.floor_double(d);
        }

        return i;
    }

    public int masterPosY()
    {
        EntityPlayer entityplayer = worldObj.getPlayerEntityByName(getOwner());
        int i = 0;

        if (entityplayer != null)
        {
            double d = entityplayer.posY;
            i = MathHelper.floor_double(d);
        }

        return i;
    }

    public int masterPosZ()
    {
        EntityPlayer entityplayer = worldObj.getPlayerEntityByName(getOwner());
        int i = 0;

        if (entityplayer != null)
        {
            double d = entityplayer.posZ;
            i = MathHelper.floor_double(d);
        }

        return i;
    }

    @Override
    protected void fall(float f)
    {
        int i = (int)Math.ceil(f - 3F) - fallProtection();

        if (i > 0 && !isImmuneToFalls() && ridingEntity == null)
        {
            attackEntityFrom(DamageSource.fall, i);
            int j = worldObj.getBlockId(MathHelper.floor_double(posX), MathHelper.floor_double(posY - 0.20000000298023224D - (double)yOffset), MathHelper.floor_double(posZ));

            if (j > 0)
            {
                StepSound stepsound = Block.blocksList[j].stepSound;
                worldObj.playSoundAtEntity(this, stepsound.getStepSound(), stepsound.getVolume() * 0.5F, stepsound.getPitch() * 0.75F);
            }
        }
    }

    @Override
    public float getBlockPathWeight(int i, int j, int k)
    {
        int l = MathHelper.floor_double(saveposition.getBowlX());
        int i1 = MathHelper.floor_double(saveposition.getBowlZ());
        float f = 100F;

        if (worldObj.getBlockId(i, j, k) == Block.tilledField.blockID || worldObj.getBlockId(i, j, k) == Block.lavaMoving.blockID || worldObj.getBlockId(i, j, k) == Block.lavaStill.blockID)
        {
            return -100F;
        }

        if (this.mode.isMode(EnumMode.WANDERING))
        {
            float f1;

            if ((i - l <= 20 || i - l >= -20) && (k - i1 <= 20 || k - i1 >= -20))
            {
                f1 = worldObj.getLightBrightness(i, j, k) + 100F;
            }
            else
            {
                f1 = 100F - 5F * (MathHelper.abs(k - i1) + MathHelper.abs(i - l));
            }

            return f1;
        }
        else
        {
            return 5F;
        }
    }
    
    protected void slaySilverFish(int i)
    {
        List list = worldObj.getEntitiesWithinAABB(EntitySilverfish.class, AxisAlignedBB.getBoundingBox(posX, posY, posZ, posX + 1.0D, posY + 1.0D, posZ + 1.0D).expand(this.talents.getTalentLevel(EnumSkills.PESTFIGHTER) * 3, 4D, this.talents.getTalentLevel(EnumSkills.PESTFIGHTER) * 3));
        EntitySilverfish entitysilverfish;

        for (Iterator iterator = list.iterator(); iterator.hasNext(); entitysilverfish.attackEntityFrom(DamageSource.generic, i))
        {
            Entity entity = (Entity)iterator.next();
            entitysilverfish = (EntitySilverfish)entity;
        }
    }
    
    public Entity getMobToAttack() {
    	 double d1 = -1D;
    	 double distance = 16D;
         EntityLiving entityliving = null;
         List list = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox.expand(distance, distance, distance));

         for (int i = 0; i < list.size(); i++)
         {
             Entity entity1 = (Entity)list.get(i);

             if (!(entity1 instanceof EntityMob))
             {
                 continue;
             }

             double d2 = entity1.getDistanceSq(this.posX, this.posY, this.posZ);

             if ((distance < 0.0D || d2 < distance * distance) && (d1 == -1D || d2 < d1) && ((EntityLiving)entity1).canEntityBeSeen(this))
             {
                 d1 = d2;
                 entityliving = (EntityLiving)entity1;
             }
         }

         return entityliving;
    }

    public int fallProtection() {
        return this.talents.getTalentLevel(EnumSkills.PILLOWPAW) * 3;
    }

    public boolean isImmuneToFalls() {
        return this.talents.getTalentLevel(EnumSkills.PILLOWPAW) == 5;
    }
    
    public boolean willObeyOthers() {
    	return this.dataWatcher.getWatchableObjectInt(22) != 0;
    }
    
    public void setWillObeyOthers(boolean par1) {
    	int var1 = 0;
    	if(par1) {
    		var1 = 1;
    	}
    	this.dataWatcher.updateObject(22, var1);
    }
    
    public boolean canInteract(EntityPlayer player) {
    	if(player.username.equalsIgnoreCase(this.getOwner())) {
    		return true;
    	}
    	
    	return this.willObeyOthers();
    }
    
    private int regenTick = 0;
    public int getRegenTick() {
    	return this.regenTick;
    }
    public void setRegenTick(int par1) {
    	this.regenTick = par1;
    }
    
    public int nourishment()
    {
        int i = 0;

        if (this.getWolfTummy() > 0)
        {
            i = 40 + 4 * (effectiveLevel() + 1);

            if (isSitting() && this.talents.getTalentLevel(EnumSkills.QUICKHEALER) == 5)
            {
                i += 20 + 2 * (effectiveLevel() + 1);
            }

            if (!isSitting())
            {
                i *= 5 + this.talents.getTalentLevel(EnumSkills.QUICKHEALER);
                i /= 10;
            }
        }

        return i;
    }
    
    public boolean isImmortal() {
        return this.isTamed() && !Properties.allowPermaDeath || this.level.isDireDog();
    }
    
    public int points() {
        return this.level.getLevel() + this.level.getDireLevel() + (this.level.isDireDog() ? 15 : 0) + (getGrowingAge() < 0 ? 0 : 15);
    }

    public int spendablePoints() {
        return points() - usedPoints();
    }
    
    public int usedPoints() {
    	int var1;
		int total = 0;
		for(var1 = 0; var1 < EnumSkills.getUpperLimit(); ++var1) {
			int level = this.talents.getTalentLevel(var1);
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

    public int effectiveLevel() {
        if(talents == null) {this.talents = new DoggySkills(this);}
        if(level == null) {this.level = new DoggyLevel(this);}
        if(mode == null) {this.mode = new DoggyMode(this);}
        return (this.level.getLevel() + this.level.getDireLevel()) / 10;
    }
    
    @Override
    public boolean canBreatheUnderwater() {
        return this.talents.getTalentLevel(EnumSkills.FISHERDOG) == 5;
    }
    
    @Override
    public boolean isPotionApplicable(PotionEffect par1PotionEffect) {
        if (this.func_110143_aJ() == 1) {
            return false;
        }

        if (this.talents.getTalentLevel(EnumSkills.POSIONFANG) >= 3) {
            int i = par1PotionEffect.getPotionID();

            if (i == Potion.poison.id) {
                return false;
            }
        }

        return true;
    }
    
    @Override
    public boolean isBreedingItem(ItemStack par1ItemStack) {
        return par1ItemStack.itemID == ModItems.breedingBone.itemID;
    }

    public String getWolfName() {
        return this.dataWatcher.getWatchableObjectString(19);
    }
    
    public void setWolfName(String var1) {
       this.dataWatcher.updateObject(19, var1);
    }
    
    public int getWolfTummy() {
		return this.dataWatcher.getWatchableObjectInt(24);
	}
    public void setWolfTummy(int par1) {
    	if(par1 > 120) {
    		par1 = 120;
    	}
    	if(par1 < 0) {
    		par1 = 0;
    	}
    	
    	this.dataWatcher.updateObject(24, par1);
    }
    
    public int getTummyTick() {
		return this.dataWatcher.getWatchableObjectInt(23);
	}
    public void setTummyTick(int par1){
    	this.dataWatcher.updateObject(23, par1);
    }
    
    @Override
    public double getYOffset() {
        return (double)-1.0F;
    }
    
    @Override
    public void setFire(int amount) {
    	if(this.talents.getTalentLevel(EnumSkills.HELLHOUND) != 5) {
    		super.setFire(amount);
    	}
    }
}
