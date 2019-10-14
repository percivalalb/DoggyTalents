package doggytalents.entity.ai;

import doggytalents.ModBlocks;
import doggytalents.entity.EntityDog;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityAISitOnBed extends EntityAIBase {
    
    private final EntityDog dog;
    private final double movementSpeed;
    /** Controls task execution delay */
    protected int runDelay;
    private int timeoutCounter;
    private int maxStayTicks;
    /** Block to move to */
    protected BlockPos destinationBlock = BlockPos.ORIGIN;
    private boolean isAboveDestination;
    private final int searchLength;

    public EntityAISitOnBed(EntityDog dogIn, double speedIn) {
        this.dog = dogIn;
        this.movementSpeed = speedIn;
        this.searchLength = 8;
        this.setMutexBits(5);
    }

    @Override
    public boolean shouldExecute() {
        if (this.runDelay > 0) {
            --this.runDelay;
            return false;
        } else if(!this.dog.isTamed()) {
            return false;
        } else if(this.dog.isSitting()) {
            return false;
        } else {
            return this.searchForDestination() && !this.isOtherDogSitting();
        }
    }

    @Override
    public boolean shouldContinueExecuting() {
        return this.timeoutCounter >= -this.maxStayTicks && this.timeoutCounter <= 1200 && this.shouldMoveTo(this.dog.world, this.destinationBlock) && !this.isOtherDogSitting();
    }

    @Override
    public void startExecuting() {
        this.dog.getNavigator().tryMoveToXYZ((double)((float)this.destinationBlock.getX()) + 0.5D, (double)(this.destinationBlock.getY() + 1), (double)((float)this.destinationBlock.getZ()) + 0.5D, this.movementSpeed);
        this.timeoutCounter = 0;
        this.maxStayTicks = this.dog.getRNG().nextInt(this.dog.getRNG().nextInt(1200) + 1200) + 1200;
        
        this.dog.getAISit().setSitting(false);
    }
    
    @Override
    public void resetTask() {
        super.resetTask();
        this.runDelay = 200 + this.dog.getRNG().nextInt(200);
        this.dog.setSitting(false);
    }

    @Override
    public void updateTask() {
        if(this.dog.getDistanceSqToCenter(this.destinationBlock) > 1.0D) {
            this.isAboveDestination = false;
            ++this.timeoutCounter;

            if(this.timeoutCounter % 40 == 0) {
                this.dog.getNavigator().tryMoveToXYZ((double)((float)this.destinationBlock.getX()) + 0.5D, (double)(this.destinationBlock.getY() + 1), (double)((float)this.destinationBlock.getZ()) + 0.5D, this.movementSpeed);
            }
        } else {
            this.isAboveDestination = true;
            --this.timeoutCounter;
        }
          this.dog.getAISit().setSitting(false);
        if (!this.getIsAboveDestination()) {
            this.dog.setSitting(false);
        } else if(!this.dog.isSitting()) {
            this.dog.setSitting(true);
        }
    }


    protected boolean getIsAboveDestination() {
        return this.isAboveDestination;
    }

    private boolean searchForDestination() {
        int i = this.searchLength;
        int j = 1;
        BlockPos blockpos = new BlockPos(this.dog);

        for(int k = 0; k <= 1; k = k > 0 ? -k : 1 - k) {
            for(int l = 0; l < i; ++l) {
                for(int i1 = 0; i1 <= l; i1 = i1 > 0 ? -i1 : 1 - i1) {
                    for(int j1 = i1 < l && i1 > -l ? l : 0; j1 <= l; j1 = j1 > 0 ? -j1 : 1 - j1) {
                        BlockPos blockpos1 = blockpos.add(i1, k - 1, j1);

                        if (this.dog.isWithinHomeDistanceFromPosition(blockpos1) && this.shouldMoveTo(this.dog.world, blockpos1)) {
                            this.destinationBlock = blockpos1;
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }
    
    private boolean isOtherDogSitting() {
        for(EntityDog otherDog : this.dog.world.getEntitiesWithinAABB(EntityDog.class, (new AxisAlignedBB(this.destinationBlock)).grow(2.0D))) {
           if (otherDog != this.dog && otherDog.isLyingDown()) {
              return true;
           }
        }

        return false;
     }

    protected boolean shouldMoveTo(World worldIn, BlockPos pos) {
        if (!worldIn.isAirBlock(pos.up())) {
            return false;
        } else {
            IBlockState blockstate = worldIn.getBlockState(pos);
            Block block = blockstate.getBlock();

            return block == ModBlocks.DOG_BED;
        }
    }
}