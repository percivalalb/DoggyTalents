package doggytalents.entity.ai;

import doggytalents.core.helper.LogHelper;
import doggytalents.entity.EntityDTDoggy;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityAIFollowOwner extends EntityAIBase
{
    private EntityDTDoggy theDog;
    private EntityLivingBase theOwner;
    World theWorld;
    private double field_75336_f;
    private PathNavigate petPathfinder;
    private int field_75343_h;
    float maxDist;
    float minDist;
    private boolean field_75344_i;

    public EntityAIFollowOwner(EntityDTDoggy par1EntityDTDoggy, double par2, float par4, float par5)
    {
        this.theDog = par1EntityDTDoggy;
        this.theWorld = par1EntityDTDoggy.worldObj;
        this.field_75336_f = par2;
        this.petPathfinder = par1EntityDTDoggy.getNavigator();
        this.minDist = par4;
        this.maxDist = par5;
        this.setMutexBits(3);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        EntityLivingBase entitylivingbase = this.theDog.func_130012_q();
        int order = this.theDog.masterOrder();
        
        if (entitylivingbase == null)
        {
            return false;
        }
        else if (this.theDog.isSitting())
        {
            return false;
        }
        //else if(this.theDog.masterOrder() != 0) {
        //	return false;
       // }
        else if(!(this.theDog.aiFetchBone.getCurrentTarget() == null || this.theDog.aiFetchBone.getCurrentTarget().isDead)) 
        {
        	return false;
        }
        else if (this.theDog.getDistanceSqToEntity(entitylivingbase) < (double)(this.minDist * this.minDist) && !this.theDog.hasBone() && order == 0)
        {
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
    public boolean continueExecuting()
    {
        return !this.petPathfinder.noPath() /**&& this.theDog.masterOrder() == 0**/ && (this.theDog.getDistanceSqToEntity(this.theOwner) > (double)(this.maxDist * this.maxDist)) && !this.theDog.isSitting() && (this.theDog.aiFetchBone.getCurrentTarget() == null || this.theDog.aiFetchBone.getCurrentTarget().isDead);
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        this.field_75343_h = 0;
        this.field_75344_i = this.theDog.getNavigator().getAvoidsWater();
        this.theDog.getNavigator().setAvoidsWater(false);
    }

    /**
     * Resets the task
     */
    public void resetTask()
    {
        this.theOwner = null;
        this.petPathfinder.clearPathEntity();
        this.theDog.getNavigator().setAvoidsWater(this.field_75344_i);
    }

    /**
     * Updates the task
     */
    public void updateTask()
    {
    	int order = this.theDog.masterOrder();
    	int masterX = MathHelper.floor_double(this.theOwner.posX);
    	int masterY = MathHelper.floor_double(this.theOwner.posY);
    	int masterZ = MathHelper.floor_double(this.theOwner.posZ);
    	float distanceAway = this.theOwner.getDistanceToEntity(this.theDog);
    	
    	if(order == 0 || order == 3 || distanceAway >= 20F) {
    		
	        this.theDog.getLookHelper().setLookPositionWithEntity(this.theOwner, 10.0F, (float)this.theDog.getVerticalFaceSpeed());
	
	        if (!this.theDog.isSitting())
	        {
	            if (--this.field_75343_h <= 0)
	            {
	                this.field_75343_h = 10;
	
	                if (!this.petPathfinder.tryMoveToEntityLiving(this.theOwner, this.field_75336_f))
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
	                                    if ((l < 1 || i1 < 1 || l > 3 || i1 > 3) && this.theWorld.doesBlockHaveSolidTopSurface(i + l, k - 1, j + i1) && !this.theWorld.isBlockNormalCube(i + l, k, j + i1) && !this.theWorld.isBlockNormalCube(i + l, k + 1, j + i1) && this.theWorld.getBlockId(i + l, k + 1, j + i1) != Block.lavaMoving.blockID && this.theWorld.getBlockId(i + l, k + 1, j + i1) != Block.lavaStill.blockID)
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
            	if(!this.theDog.getNavigator().tryMoveToXYZ(j3, dogY, k3, this.field_75336_f)) {
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
	                                if ((l < 1 || i1 < 1 || l > 3 || i1 > 3) && this.theWorld.doesBlockHaveSolidTopSurface(i + l, k - 1, j + i1) && !this.theWorld.isBlockNormalCube(i + l, k, j + i1) && !this.theWorld.isBlockNormalCube(i + l, k + 1, j + i1) && this.theWorld.getBlockId(i + l, k + 1, j + i1) != Block.lavaMoving.blockID && this.theWorld.getBlockId(i + l, k + 1, j + i1) != Block.lavaStill.blockID)
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
                                if ((l < 1 || i1 < 1 || l > 3 || i1 > 3) && this.theWorld.doesBlockHaveSolidTopSurface(i + l, k - 1, j + i1) && !this.theWorld.isBlockNormalCube(i + l, k, j + i1) && !this.theWorld.isBlockNormalCube(i + l, k + 1, j + i1) && this.theWorld.getBlockId(i + l, k + 1, j + i1) != Block.lavaMoving.blockID && this.theWorld.getBlockId(i + l, k + 1, j + i1) != Block.lavaStill.blockID)
                                {
                                    this.theDog.setLocationAndAngles((double)((float)(i + l) + 0.5F), (double)k, (double)((float)(j + i1) + 0.5F), this.theDog.rotationYaw, this.theDog.rotationPitch);
                                    this.petPathfinder.clearPathEntity();
                                    return;
                                }
                            }
                        }
                    }
                }
            	this.theDog.getNavigator().tryMoveToXYZ(l3, dogY, i4, this.field_75336_f);
            }
    	}
    }
}
