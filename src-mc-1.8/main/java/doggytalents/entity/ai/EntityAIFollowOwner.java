package doggytalents.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.Blocks;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import doggytalents.entity.EntityDog;
import doggytalents.entity.ModeUtil.EnumMode;

public class EntityAIFollowOwner extends EntityAIBase
{
    private EntityDog theDog;
    private EntityLivingBase theOwner;
    World theWorld;
    private double moveSpeed;
    private PathNavigate petPathfinder;
    private int field_75343_h;
    float maxDist;
    float minDist;
    private boolean preShouldAvoidWater;
    private static final String __OBFID = "CL_00001585";

    public EntityAIFollowOwner(EntityDog p_i1625_1_, double p_i1625_2_, float p_i1625_4_, float p_i1625_5_)
    {
        this.theDog = p_i1625_1_;
        this.theWorld = p_i1625_1_.worldObj;
        this.moveSpeed = p_i1625_2_;
        this.petPathfinder = p_i1625_1_.getNavigator();
        this.minDist = p_i1625_4_;
        this.maxDist = p_i1625_5_;
        this.setMutexBits(3);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        EntityLivingBase entitylivingbase = this.theDog.getOwnerEntity();
        int order = this.theDog.masterOrder();
        
        if (entitylivingbase == null)
        {
            return false;
        }
        else if (this.theDog.isSitting()) {
            return false;
        }
        else if(this.theDog.mode.isMode(EnumMode.WANDERING)) {
        	return false;
        }
        else if(!(this.theDog.aiFetchBone.getCurrentTarget() == null || this.theDog.aiFetchBone.getCurrentTarget().isDead)) 
        {
        	return false;
        }
        else if (this.theDog.getDistanceSqToEntity(entitylivingbase) < (double)(this.minDist * this.minDist))
        {
            return false;
        }
        else if(this.theDog.riddenByEntity == null && order == 3 && this.theDog.talents.getLevel("shepherddog") > 0) {
        	return false;
        }
        else
        {
            this.theOwner = entitylivingbase;
            return true;
        }
    }

    @Override
    public boolean continueExecuting() {
        return !this.petPathfinder.noPath() && this.theDog.getDistanceSqToEntity(this.theOwner) > (double)(this.maxDist * this.maxDist) && !this.theDog.isSitting() && (this.theDog.aiFetchBone.getCurrentTarget() == null || this.theDog.aiFetchBone.getCurrentTarget().isDead);
    }

    @Override
    public void startExecuting() {
        this.field_75343_h = 0;
        this.preShouldAvoidWater = ((PathNavigateGround)this.theDog.getNavigator()).func_179689_e();
        ((PathNavigateGround)this.theDog.getNavigator()).func_179690_a(false);
    }

    /**
     * Resets the task
     */
    public void resetTask()
    {
        this.theOwner = null;
        this.petPathfinder.clearPathEntity();
        ((PathNavigateGround)this.theDog.getNavigator()).func_179690_a(this.preShouldAvoidWater);
    }

    @Override
    public void updateTask() {
    	int order = this.theDog.masterOrder();
    	int masterX = MathHelper.floor_double(this.theOwner.posX);
    	int masterY = MathHelper.floor_double(this.theOwner.posY);
    	int masterZ = MathHelper.floor_double(this.theOwner.posZ);
    	double distanceAway = this.theDog.getDistanceSqToEntity(this.theOwner);
    	
    	if(((order == 0 || order == 3) && distanceAway >= 4.0D) || this.theDog.hasBone()) {
	    		
	        if (!this.theDog.isSitting())
	        {
	            if (--this.field_75343_h <= 0)
	            {
	                this.field_75343_h = 10;
	
	                if (!this.petPathfinder.tryMoveToEntityLiving(this.theOwner, this.moveSpeed))
	                {
	                    if (!this.theDog.getLeashed())
	                    {
	                        if (distanceAway >= 255.0D)
	                        {
	                            int i = MathHelper.floor_double(this.theOwner.posX) - 2;
	                            int j = MathHelper.floor_double(this.theOwner.posZ) - 2;
	                            int k = MathHelper.floor_double(this.theOwner.getEntityBoundingBox().minY);
	
	                            for (int l = 0; l <= 4; ++l)
	                            {
	                                for (int i1 = 0; i1 <= 4; ++i1)
	                                {
	                                    if ((l < 1 || i1 < 1 || l > 3 || i1 > 3) && World.doesBlockHaveSolidTopSurface(this.theWorld, new BlockPos(i + l, k - 1, j + i1)) && !this.theWorld.getBlockState(new BlockPos(i + l, k, j + i1)).getBlock().isFullBlock() && !this.theWorld.getBlockState(new BlockPos(i + l, k + 1, j + i1)).getBlock().isFullBlock() && this.theWorld.getBlockState(new BlockPos(i + l, k + 1, j + i1)) != Blocks.flowing_lava && this.theWorld.getBlockState(new BlockPos(i + l, k + 1, j + i1)) != Blocks.lava)
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
            int dogY = MathHelper.floor_double(this.theDog.posY - 0.20000000298023224D - (double)this.theDog.getYOffset());
            int dogZ = MathHelper.floor_double(this.theDog.posZ);
            int k2 = dogX - masterX;
            int i3 = dogZ - masterZ;
            int j3 = masterX + k2 * 2;
            int k3 = masterZ + i3 * 2;
            int l3 = masterX + k2 / 2;
            int i4 = masterZ + i3 / 2;
            if (distanceAway < 25.0D)
            {
            	if(!this.theDog.getNavigator().tryMoveToXYZ(j3, dogY, k3, this.moveSpeed)) {
            		 if (!this.theDog.getLeashed()) {
	                    if (distanceAway >= 350.0D)
	                    {
	                        int i = MathHelper.floor_double(this.theOwner.posX) - 2;
	                        int j = MathHelper.floor_double(this.theOwner.posZ) - 2;
	                        int k = MathHelper.floor_double(this.theOwner.getEntityBoundingBox().minY);
	
	                        for (int l = 0; l <= 4; ++l)
	                        {
	                            for (int i1 = 0; i1 <= 4; ++i1)
	                            {
	                                if ((l < 1 || i1 < 1 || l > 3 || i1 > 3) && World.doesBlockHaveSolidTopSurface(this.theWorld, new BlockPos(i + l, k - 1, j + i1)) && !this.theWorld.getBlockState(new BlockPos(i + l, k, j + i1)).getBlock().isFullBlock() && !this.theWorld.getBlockState(new BlockPos(i + l, k + 1, j + i1)).getBlock().isFullBlock() && this.theWorld.getBlockState(new BlockPos(i + l, k + 1, j + i1)) != Blocks.flowing_lava && this.theWorld.getBlockState(new BlockPos(i + l, k + 1, j + i1)) != Blocks.lava)
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

            if (distanceAway > 144.0D && distanceAway < 400.0D)
            {
            	if(!this.theDog.getNavigator().tryMoveToXYZ(l3, dogY, i4, this.moveSpeed)) {
	            	if (!this.theDog.getLeashed()) {
	                    if (distanceAway >= 350.0D)
	                    {
	                        int i = MathHelper.floor_double(this.theOwner.posX) - 2;
	                        int j = MathHelper.floor_double(this.theOwner.posZ) - 2;
	                        int k = MathHelper.floor_double(this.theOwner.getEntityBoundingBox().minY);
	
	                        for (int l = 0; l <= 4; ++l)
	                        {
	                            for (int i1 = 0; i1 <= 4; ++i1)
	                            {
	                                if ((l < 1 || i1 < 1 || l > 3 || i1 > 3) && World.doesBlockHaveSolidTopSurface(this.theWorld, new BlockPos(i + l, k - 1, j + i1)) && !this.theWorld.getBlockState(new BlockPos(i + l, k, j + i1)).getBlock().isFullBlock() && !this.theWorld.getBlockState(new BlockPos(i + l, k + 1, j + i1)).getBlock().isFullBlock() && this.theWorld.getBlockState(new BlockPos(i + l, k + 1, j + i1)) != Blocks.flowing_lava && this.theWorld.getBlockState(new BlockPos(i + l, k + 1, j + i1)) != Blocks.lava)
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