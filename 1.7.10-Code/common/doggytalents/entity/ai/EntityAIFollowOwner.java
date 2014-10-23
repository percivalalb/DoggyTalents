package doggytalents.entity.ai;

import java.util.List;

import cpw.mods.fml.common.FMLLog;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.init.Blocks;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import doggytalents.entity.EntityDTDoggy;
import doggytalents.entity.data.EnumMode;
import doggytalents.entity.data.EnumTalents;

public class EntityAIFollowOwner extends EntityAIBase
{
    private EntityDTDoggy theDog;
    private EntityLivingBase theOwner;
    World theWorld;
    private double moveSpeed;
    private PathNavigate petPathfinder;
    private int field_75343_h;
    float maxDist;
    float minDist;
    private boolean field_75344_i;

    public EntityAIFollowOwner(EntityDTDoggy par1EntityDTDoggy, double par2, float par4, float par5)
    {
        this.theDog = par1EntityDTDoggy;
        this.theWorld = par1EntityDTDoggy.worldObj;
        this.moveSpeed = par2;
        this.petPathfinder = par1EntityDTDoggy.getNavigator();
        this.minDist = par4;
        this.maxDist = par5;
        this.setMutexBits(3);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean shouldExecute() {
        EntityLivingBase entitylivingbase = (EntityLivingBase)this.theDog.getOwner();
        int order = this.theDog.masterOrder();
        
        if(entitylivingbase == null) {
        	entitylivingbase = theOwner;
        }
        if (entitylivingbase == null)
        {
            return false;
        }
        else if (this.theDog.isSitting())
        {
            return false;
        }
        else if(this.theDog.mode.isMode(EnumMode.WANDERING)) {
        	return false;
        }
        //else if(this.theDog.masterOrder() != 0) {
        //	return false;
       // }
        else if(!(this.theDog.aiFetchBone.getCurrentTarget() == null || this.theDog.aiFetchBone.getCurrentTarget().isDead)) 
        {
        	return false;
        }
        else if ((this.theDog.getDistanceSqToEntity(entitylivingbase) < (double)(this.minDist * this.minDist)))
        {
            return false;
        }
        else if(this.theDog.riddenByEntity == null && order == 3) {
        	return false;
        }
        else
        {
            this.theOwner = entitylivingbase;
            return true;
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    @Override
    public boolean continueExecuting()
    {
        return !this.petPathfinder.noPath() /**&& this.theDog.masterOrder() == 0**/ && (this.theDog.getDistanceSqToEntity(this.theOwner) > (double)(this.maxDist * this.maxDist)) && !this.theDog.isSitting() && (this.theDog.aiFetchBone.getCurrentTarget() == null || this.theDog.aiFetchBone.getCurrentTarget().isDead);
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    @Override
    public void startExecuting()
    {
        this.field_75343_h = 0;
        this.field_75344_i = this.theDog.getNavigator().getAvoidsWater();
        this.theDog.getNavigator().setAvoidsWater(false);
    }

    /**
     * Resets the task
     */
    @Override
    public void resetTask()
    {
        this.theOwner = null;
        this.petPathfinder.clearPathEntity();
        this.theDog.getNavigator().setAvoidsWater(this.field_75344_i);
    }

    /**
     * Updates the task
     */
    @Override
    public void updateTask()
    {
    	int order = this.theDog.masterOrder();
    	int masterX = MathHelper.floor_double(this.theOwner.posX);
    	int masterY = MathHelper.floor_double(this.theOwner.posY);
    	int masterZ = MathHelper.floor_double(this.theOwner.posZ);
    	float distanceAway = this.theOwner.getDistanceToEntity(this.theDog);
    	
    	if((order == 0 || order == 3) && distanceAway >= 2F) {
    		
	        this.theDog.getLookHelper().setLookPositionWithEntity(this.theOwner, 10.0F, (float)this.theDog.getVerticalFaceSpeed());
	
	        if (!this.theDog.isSitting())
	        {
	            if (--this.field_75343_h <= 0)
	            {
	                this.field_75343_h = 10;
	
	                if (!this.petPathfinder.tryMoveToEntityLiving(this.theOwner, this.moveSpeed))
	                {
	                    if (!this.theDog.getLeashed())
	                    {
	                        if (this.theDog.getDistanceSqToEntity(this.theOwner) >= 225.0D)
	                        {
	                            int i = MathHelper.floor_double(this.theOwner.posX) - 2;
	                            int j = MathHelper.floor_double(this.theOwner.posZ) - 2;
	                            int k = MathHelper.floor_double(this.theOwner.boundingBox.minY);
	
	                            for (int l = 0; l <= 4; ++l)
	                            {
	                                for (int i1 = 0; i1 <= 4; ++i1)
	                                {
	                                    if ((l < 1 || i1 < 1 || l > 3 || i1 > 3) && World.doesBlockHaveSolidTopSurface(this.theWorld, i + l, k - 1, j + i1) && !this.theWorld.getBlock(i + l, k, j + i1).isNormalCube() && !this.theWorld.getBlock(i + l, k + 1, j + i1).isNormalCube() && this.theWorld.getBlock(i + l, k + 1, j + i1) != Blocks.flowing_lava && this.theWorld.getBlock(i + l, k + 1, j + i1) != Blocks.lava)
	                                    {
	                                        this.theDog.setLocationAndAngles((double)((float)(i + l) + 0.5F), (double)k, (double)((float)(j + i1) + 0.5F), this.theDog.rotationYaw, this.theDog.rotationPitch);
	                                        this.petPathfinder.clearPathEntity();
	                                        return;
	                                    }
	                                }
	                            }
	                        }
	                    }
	                }
	            }
	        }
    	}
    	else if(order == 1 || order == 2) {
    		int dogX = MathHelper.floor_double(this.theDog.posX);
            int dogY = MathHelper.floor_double(this.theDog.posY - 0.20000000298023224D - (double)this.theDog.yOffset);
            int dogZ = MathHelper.floor_double(this.theDog.posZ);
            int k2 = dogX - masterX;
            int i3 = dogZ - masterZ;
            int j3 = masterX + k2 * 2;
            int k3 = masterZ + i3 * 2;
            int l3 = masterX + k2 / 2;
            int i4 = masterZ + i3 / 2;
            if (distanceAway < 5F)
            {
            	if(!this.theDog.getNavigator().tryMoveToXYZ(j3, dogY, k3, this.moveSpeed)) {
            		 if (!this.theDog.getLeashed()) {
	                    if (this.theDog.getDistanceSqToEntity(this.theOwner) >= 350.0D)
	                    {
	                        int i = MathHelper.floor_double(this.theOwner.posX) - 2;
	                        int j = MathHelper.floor_double(this.theOwner.posZ) - 2;
	                        int k = MathHelper.floor_double(this.theOwner.boundingBox.minY);
	
	                        for (int l = 0; l <= 4; ++l)
	                        {
	                            for (int i1 = 0; i1 <= 4; ++i1)
	                            {
	                                if ((l < 1 || i1 < 1 || l > 3 || i1 > 3) && World.doesBlockHaveSolidTopSurface(this.theWorld, i + l, k - 1, j + i1) && !this.theWorld.getBlock(i + l, k, j + i1).isNormalCube() && !this.theWorld.getBlock(i + l, k + 1, j + i1).isNormalCube() && this.theWorld.getBlock(i + l, k + 1, j + i1) != Blocks.flowing_lava && this.theWorld.getBlock(i + l, k + 1, j + i1) != Blocks.lava)
	                                {
	                                    this.theDog.setLocationAndAngles((double)((float)(i + l) + 0.5F), (double)k, (double)((float)(j + i1) + 0.5F), this.theDog.rotationYaw, this.theDog.rotationPitch);
	                                    this.petPathfinder.clearPathEntity();
	                                    return;
	                                }
	                            }
	                        }
	                    }
	                }
            	}
            }

            if (distanceAway > 12F && distanceAway < 20F)
            {
            	if(!this.theDog.getNavigator().tryMoveToXYZ(l3, dogY, i4, this.moveSpeed)) {
	            	if (!this.theDog.getLeashed()) {
	                    if (this.theDog.getDistanceSqToEntity(this.theOwner) >= 350.0D)
	                    {
	                        int i = MathHelper.floor_double(this.theOwner.posX) - 2;
	                        int j = MathHelper.floor_double(this.theOwner.posZ) - 2;
	                        int k = MathHelper.floor_double(this.theOwner.boundingBox.minY);
	
	                        for (int l = 0; l <= 4; ++l)
	                        {
	                            for (int i1 = 0; i1 <= 4; ++i1)
	                            {
	                                if ((l < 1 || i1 < 1 || l > 3 || i1 > 3) && World.doesBlockHaveSolidTopSurface(this.theWorld, i + l, k - 1, j + i1) && !this.theWorld.getBlock(i + l, k, j + i1).isNormalCube() && !this.theWorld.getBlock(i + l, k + 1, j + i1).isNormalCube() && this.theWorld.getBlock(i + l, k + 1, j + i1) != Blocks.flowing_lava && this.theWorld.getBlock(i + l, k + 1, j + i1) != Blocks.lava)
	                                {
	                                    this.theDog.setLocationAndAngles((double)((float)(i + l) + 0.5F), (double)k, (double)((float)(j + i1) + 0.5F), this.theDog.rotationYaw, this.theDog.rotationPitch);
	                                    this.petPathfinder.clearPathEntity();
	                                    return;
	                                }
	                            }
	                        }
	                    }
	                }
            	}
            }
    	}
    }
}
