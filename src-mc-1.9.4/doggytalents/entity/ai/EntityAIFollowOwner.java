package doggytalents.entity.ai;

import doggytalents.entity.EntityDog;
import doggytalents.entity.ModeUtil.EnumMode;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityAIFollowOwner extends EntityAIBase
{
    private final EntityDog theDog;
    private EntityLivingBase theOwner;
    World theWorld;
    private final double followSpeed;
    private final PathNavigate petPathfinder;
    private int timeToRecalcPath;
    float maxDist;
    float minDist;
    private float oldWaterCost;

    public EntityAIFollowOwner(EntityDog thePetIn, double followSpeedIn, float minDistIn, float maxDistIn)
    {
        this.theDog = thePetIn;
        this.theWorld = thePetIn.worldObj;
        this.followSpeed = followSpeedIn;
        this.petPathfinder = thePetIn.getNavigator();
        this.minDist = minDistIn;
        this.maxDist = maxDistIn;
        this.setMutexBits(3);

        if (!(thePetIn.getNavigator() instanceof PathNavigateGround))
        {
            throw new IllegalArgumentException("Unsupported mob type for FollowOwnerGoal");
        }
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        EntityLivingBase entitylivingbase = this.theDog.getOwner();
        
        if(entitylivingbase == null)
            return false;
        else if(entitylivingbase instanceof EntityPlayer && ((EntityPlayer)entitylivingbase).isSpectator())
            return false;
        else if(this.theDog.isSitting())
            return false;
        else if(this.theDog.mode.isMode(EnumMode.WANDERING))
        	return false;
        //TODO Bone ai
        else if(this.theDog.getDistanceSqToEntity(entitylivingbase) < (double)(this.minDist * this.minDist))
            return false;
        else {
            this.theOwner = entitylivingbase;
            return true;
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        return !this.petPathfinder.noPath() && this.theDog.getDistanceSqToEntity(this.theOwner) > (double)(this.maxDist * this.maxDist) && !this.theDog.isSitting();
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        this.timeToRecalcPath = 0;
        this.oldWaterCost = this.theDog.getPathPriority(PathNodeType.WATER);
        this.theDog.setPathPriority(PathNodeType.WATER, 0.0F);
    }

    /**
     * Resets the task
     */
    public void resetTask()
    {
        this.theOwner = null;
        this.petPathfinder.clearPathEntity();
        this.theDog.setPathPriority(PathNodeType.WATER, this.oldWaterCost);
    }

    private boolean isEmptyBlock(BlockPos pos)
    {
        IBlockState iblockstate = this.theWorld.getBlockState(pos);
        return iblockstate.getMaterial() == Material.AIR ? true : !iblockstate.isFullCube();
    }

    /**
     * Updates the task
     */
    public void updateTask()
    {
        this.theDog.getLookHelper().setLookPositionWithEntity(this.theOwner, 10.0F, (float)this.theDog.getVerticalFaceSpeed());

        if (!this.theDog.isSitting())
        {
            if (--this.timeToRecalcPath <= 0)
            {
                this.timeToRecalcPath = 10;

                if (!this.petPathfinder.tryMoveToEntityLiving(this.theOwner, this.followSpeed))
                {
                    if (!this.theDog.getLeashed())
                    {
                        if (this.theDog.getDistanceSqToEntity(this.theOwner) >= 144.0D)
                        {
                            int i = MathHelper.floor_double(this.theOwner.posX) - 2;
                            int j = MathHelper.floor_double(this.theOwner.posZ) - 2;
                            int k = MathHelper.floor_double(this.theOwner.getEntityBoundingBox().minY);

                            for (int l = 0; l <= 4; ++l)
                            {
                                for (int i1 = 0; i1 <= 4; ++i1)
                                {
                                    if ((l < 1 || i1 < 1 || l > 3 || i1 > 3) && this.theWorld.getBlockState(new BlockPos(i + l, k - 1, j + i1)).isFullyOpaque() && this.isEmptyBlock(new BlockPos(i + l, k, j + i1)) && this.isEmptyBlock(new BlockPos(i + l, k + 1, j + i1)))
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