package doggytalents.entity;

import java.util.*;

import doggytalents.entity.doggyhelper.DataWatcherHelper;
import doggytalents.entity.doggyhelper.GeneralHelper;
import doggytalents.entity.doggyhelper.InteractHelper;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityDTDoggy extends EntityAnimal
{
    public float BEG_VAR_1;
    public float BEG_VAR_2;
    public int BEG_TIMER;
    private boolean isShaking;
    private boolean field_25052_g;
    private float timeWolfIsShaking;
    private float prevTimeWolfIsShaking;

    public EntityDTDoggy(World world)
    {
        super(world);
        this.texture = "/mob/wolf.png";
        setSize(0.8F, 0.8F);
        this.moveSpeed = 1.1F;
    }

    public int getMaxHealth()
    {
        return !isTamed() ? 8 : 20;
    }

    protected void entityInit()
    {
        super.entityInit();
        dataWatcher.addObject(16, Byte.valueOf((byte)0));
        dataWatcher.addObject(17, "");
        dataWatcher.addObject(18, new Integer(getHealth()));
        DataWatcherHelper.entityInit(this);
    }

    protected boolean canTriggerWalking()
    {
        return false;
    }

    public String getTexture()
    {
        if (isTamed())
        {
            return "/mob/wolf_tame.png";
        }
        if (isAngry())
        {
            return "/mob/wolf_angry.png";
        }
        else
        {
            return super.getTexture();
        }
    }

    public void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setBoolean("Angry", isAngry());
        nbttagcompound.setBoolean("Sitting", isSitting());
        if (getOwner() == null)
        {
            nbttagcompound.setString("Owner", "");
        }
        else
        {
            nbttagcompound.setString("Owner", getOwner());
        }
    }

    public void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readEntityFromNBT(nbttagcompound);
        setAngry(nbttagcompound.getBoolean("Angry"));
        setIsSitting(nbttagcompound.getBoolean("Sitting"));
        String s = nbttagcompound.getString("Owner");
        if (s.length() > 0)
        {
            setOwner(s);
            setIsTamed(true);
        }
    }

    protected boolean canDespawn()
    {
        return isAngry();
    }

    protected String getLivingSound()
    {
        if (isAngry())
        {
            return "mob.wolf.growl";
        }
        if (rand.nextInt(3) == 0)
        {
            if (isTamed() && dataWatcher.getWatchableObjectInt(18) < 10)
            {
                return "mob.wolf.whine";
            }
            else
            {
                return "mob.wolf.panting";
            }
        }
        else
        {
            return "mob.wolf.bark";
        }
    }

    protected String getHurtSound()
    {
        return "mob.wolf.hurt";
    }

    protected String getDeathSound()
    {
        return "mob.wolf.death";
    }

    protected float getSoundVolume()
    {
        return 0.4F;
    }

    protected int getDropItemId()
    {
        return -1;
    }

    protected void updateEntityActionState()
    {
        super.updateEntityActionState();
        if (!hasAttacked && !hasPath() && isTamed() && ridingEntity == null)
        {
            EntityPlayer entityplayer = worldObj.getPlayerEntityByName(getOwner());
            if (entityplayer != null)
            {
                float f = entityplayer.getDistanceToEntity(this);
                if (f > 5F)
                {
                    getPathOrWalkableBlock(entityplayer, f);
                }
            }
            else if (!isInWater())
            {
                setIsSitting(true);
            }
        }
        else if (entityToAttack == null && !hasPath() && !isTamed() && worldObj.rand.nextInt(100) == 0)
        {
            List list = worldObj.getEntitiesWithinAABB(EntitySheep.class, AxisAlignedBB.getBoundingBox(posX, posY, posZ, posX + 1.0D, posY + 1.0D, posZ + 1.0D).expand(16D, 4D, 16D));
            if (!list.isEmpty())
            {
                setEntityToAttack((Entity)list.get(worldObj.rand.nextInt(list.size())));
            }
        }
        if (isInWater())
        {
            setIsSitting(false);
        }
        if (!worldObj.isRemote)
        {
            dataWatcher.updateObject(18, Integer.valueOf(getHealth()));
        }
    }

	public void onLivingUpdate()
    {
        super.onLivingUpdate();
        EntityPlayer thePlayer = this.worldObj.getClosestPlayerToEntity(this, (double)8.0F);
        if(GeneralHelper.hasPlayerGotBoneInHand(thePlayer, this) && !DataWatcherHelper.isBegging(this)) {
        	DataWatcherHelper.setBegging(this, true);
            this.BEG_TIMER = 40 + this.getRNG().nextInt(40);
        }
        if(DataWatcherHelper.isBegging(this)) {
        	if(GeneralHelper.shouldContinueBegging(thePlayer, this)) {
        		this.getLookHelper().setLookPosition(thePlayer.posX, thePlayer.posY + (double)thePlayer.getEyeHeight(), thePlayer.posZ, 10.0F, (float)this.getVerticalFaceSpeed());
                --this.BEG_TIMER;
        	}
        	else {
        		DataWatcherHelper.setBegging(this, false);
        	}
        }
        
        if (!worldObj.isRemote && isShaking && !field_25052_g && !hasPath() && onGround)
        {
            field_25052_g = true;
            timeWolfIsShaking = 0.0F;
            prevTimeWolfIsShaking = 0.0F;
            worldObj.setEntityState(this, (byte)8);
        }
    }

    private boolean hasCurrentTarget() {
		return this.entityToAttack != null;
	}

	private Entity getCurrentTarget() {
		return this.entityToAttack;
	}

	public void onUpdate()
    {
        super.onUpdate();
        BEG_VAR_2 = BEG_VAR_1;
        if (DataWatcherHelper.isBegging(this))
        {
            BEG_VAR_1 = BEG_VAR_1 + (1.0F - BEG_VAR_1) * 0.4F;
        }
        else
        {
            BEG_VAR_1 = BEG_VAR_1 + (0.0F - BEG_VAR_1) * 0.4F;
        }
        if (DataWatcherHelper.isBegging(this))
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
                    worldObj.spawnParticle("splash", posX + (double)f1, f + 0.8F, posZ + (double)f2, motionX, motionY, motionZ);
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

    public float getEyeHeight()
    {
        return height * 0.8F;
    }

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
        if (pathentity == null && f > 12F)
        {
            int i = MathHelper.floor_double(entity.posX) - 2;
            int j = MathHelper.floor_double(entity.posZ) - 2;
            int k = MathHelper.floor_double(entity.boundingBox.minY);
            for (int l = 0; l <= 4; l++)
            {
                for (int i1 = 0; i1 <= 4; i1++)
                {
                    if ((l < 1 || i1 < 1 || l > 3 || i1 > 3) && worldObj.isBlockNormalCube(i + l, k - 1, j + i1) && !worldObj.isBlockNormalCube(i + l, k, j + i1) && !worldObj.isBlockNormalCube(i + l, k + 1, j + i1))
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

    protected boolean isMovementCeased()
    {
        return isSitting() || field_25052_g;
    }

    public boolean attackEntityFrom(DamageSource damagesource, int i)
    {
        Entity entity = damagesource.getEntity();
        setIsSitting(false);
        if (entity != null && !(entity instanceof EntityPlayer) && !(entity instanceof EntityArrow))
        {
            i = (i + 1) / 2;
        }
        if (super.attackEntityFrom(damagesource, i))
        {
            if (!isTamed() && !isAngry())
            {
                if (entity instanceof EntityPlayer)
                {
                    setAngry(true);
                    entityToAttack = entity;
                }
                if ((entity instanceof EntityArrow) && ((EntityArrow)entity).shootingEntity != null)
                {
                    entity = ((EntityArrow)entity).shootingEntity;
                }
                if (entity instanceof EntityLiving)
                {
                    List list = worldObj.getEntitiesWithinAABB(EntityDTDoggy.class, AxisAlignedBB.getBoundingBox(posX, posY, posZ, posX + 1.0D, posY + 1.0D, posZ + 1.0D).expand(16D, 4D, 16D));
                    Iterator iterator = list.iterator();
                    do
                    {
                        if (!iterator.hasNext())
                        {
                            break;
                        }
                        Entity entity1 = (Entity)iterator.next();
                        EntityDTDoggy entitywolf = (EntityDTDoggy)entity1;
                        if (!entitywolf.isTamed() && entitywolf.entityToAttack == null)
                        {
                            entitywolf.entityToAttack = entity;
                            if (entity instanceof EntityPlayer)
                            {
                                entitywolf.setAngry(true);
                            }
                        }
                    }
                    while (true);
                }
            }
            else if (entity != this && entity != null)
            {
                if (isTamed() && (entity instanceof EntityPlayer) && ((EntityPlayer)entity).username.equalsIgnoreCase(getOwner()))
                {
                    return true;
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

    protected Entity findPlayerToAttack()
    {
        if (isAngry())
        {
            return worldObj.getClosestPlayerToEntity(this, 16D);
        }
        else
        {
            return null;
        }
    }

    protected void attackEntity(Entity entity, float f)
    {
        if (f > 2.0F && f < 6F && rand.nextInt(10) == 0)
        {
            if (onGround)
            {
                double d = entity.posX - posX;
                double d1 = entity.posZ - posZ;
                float f1 = MathHelper.sqrt_double(d * d + d1 * d1);
                motionX = (d / (double)f1) * 0.5D * 0.80000001192092896D + motionX * 0.20000000298023224D;
                motionZ = (d1 / (double)f1) * 0.5D * 0.80000001192092896D + motionZ * 0.20000000298023224D;
                motionY = 0.40000000596046448D;
            }
        }
        else if ((double)f < 1.5D && entity.boundingBox.maxY > boundingBox.minY && entity.boundingBox.minY < boundingBox.maxY)
        {
            attackTime = 20;
            byte byte0 = 2;
            if (isTamed())
            {
                byte0 = 4;
            }
            entity.attackEntityFrom(DamageSource.causeMobDamage(this), byte0);
        }
    }

    @Override
    public boolean interact(EntityPlayer player) {
        return InteractHelper.interact(this, player);
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

    public float setTailRotation()
    {
        if (isAngry())
        {
            return 1.53938F;
        }
        if (isTamed())
        {
            return (0.55F - (float)(20 - dataWatcher.getWatchableObjectInt(18)) * 0.02F) * 3.141593F;
        }
        else
        {
            return 0.6283185F;
        }
    }

    public int getMaxSpawnedInChunk()
    {
        return 8;
    }

    public String getOwner()
    {
        return dataWatcher.getWatchableObjectString(17);
    }

    public void setOwner(String s)
    {
        dataWatcher.updateObject(17, s);
    }

    public boolean isSitting()
    {
        return (dataWatcher.getWatchableObjectByte(16) & 1) != 0;
    }

    public void setIsSitting(boolean flag)
    {
        byte byte0 = dataWatcher.getWatchableObjectByte(16);
        if (flag)
        {
            dataWatcher.updateObject(16, Byte.valueOf((byte)(byte0 | 1)));
        }
        else
        {
            dataWatcher.updateObject(16, Byte.valueOf((byte)(byte0 & -2)));
        }
    }

    public boolean isAngry()
    {
        return (dataWatcher.getWatchableObjectByte(16) & 2) != 0;
    }

    public void setAngry(boolean flag)
    {
        byte byte0 = dataWatcher.getWatchableObjectByte(16);
        if (flag)
        {
            dataWatcher.updateObject(16, Byte.valueOf((byte)(byte0 | 2)));
        }
        else
        {
            dataWatcher.updateObject(16, Byte.valueOf((byte)(byte0 & -3)));
        }
    }

    public boolean isTamed()
    {
        return (dataWatcher.getWatchableObjectByte(16) & 4) != 0;
    }

    public void setIsTamed(boolean flag)
    {
        byte byte0 = dataWatcher.getWatchableObjectByte(16);
        if (flag)
        {
            dataWatcher.updateObject(16, Byte.valueOf((byte)(byte0 | 4)));
        }
        else
        {
            dataWatcher.updateObject(16, Byte.valueOf((byte)(byte0 & -5)));
        }
    }

    protected EntityAnimal spawnBabyAnimal(EntityAnimal entityanimal)
    {
        return new EntityDTDoggy(worldObj);
    }

	@Override
	public EntityAgeable createChild(EntityAgeable entityageable) {
		// TODO Auto-generated method stub
		return null;
	}
	
    private void setEntityToAttack(Entity entity) {
		entityToAttack = entity;
	}
	
	//Getter Helpers
	
	
}
