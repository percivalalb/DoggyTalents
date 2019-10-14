package doggytalents.entity.ai;

import doggytalents.api.inferface.IWaterMovement;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityAIExtinguishFire extends EntityAIBase {

    private EntityCreature creature;
    private PathNavigateGround navigator;
    private final double movementSpeed;
    private final int searchLength;
    private IWaterMovement waterMovement;
    
    private int timeoutCounter;
    private boolean oldCanSwim;
    
    protected BlockPos destinationBlock = BlockPos.ORIGIN;
    
    public EntityAIExtinguishFire(EntityCreature creature, double speedIn, int length) {
        this.creature = creature;
        this.navigator = (PathNavigateGround)creature.getNavigator();
        this.movementSpeed = speedIn;
        this.searchLength = length;
        this.waterMovement = new WaterMovementHandler(creature);
        this.setMutexBits(1);
        //EntityAIMoveToBlock
    }

    @Override
    public boolean shouldExecute() {
        return this.creature.isBurning() && this.searchForDestination();
    }
    
    @Override
    public boolean shouldContinueExecuting() {
        return this.timeoutCounter < 400 && this.creature.isBurning() && this.shouldMoveTo(this.creature.world, this.destinationBlock);
    }
    
    @Override
    public void startExecuting() {
        this.waterMovement.startExecuting();
        this.oldCanSwim = this.navigator.getCanSwim();
        this.navigator.setCanSwim(true);
        this.timeoutCounter = 0;
        this.creature.getNavigator().tryMoveToXYZ((double)((float)this.destinationBlock.getX()) + 0.5D, (double)(this.destinationBlock.getY()), (double)((float)this.destinationBlock.getZ()) + 0.5D, this.movementSpeed);
    }
    
    @Override
    public void resetTask() {
        super.resetTask();
        this.waterMovement.resetTask();
        
        this.navigator.setCanSwim(this.oldCanSwim);
    }
    
    @Override
    public void updateTask() {
        ++this.timeoutCounter;

        if(this.timeoutCounter % 40 == 0) {
            this.creature.getNavigator().tryMoveToXYZ((double)((float)this.destinationBlock.getX()) + 0.5D, (double)(this.destinationBlock.getY()), (double)((float)this.destinationBlock.getZ()) + 0.5D, 1.0D);
        }
    }

    private boolean searchForDestination() {
        BlockPos blockpos = new BlockPos(this.creature);

        for (int y = 0; y <= 5; y = y > 0 ? -y : 1 - y) {
            for(int s = 0; s < this.searchLength; ++s) {
                for(int x = 0; x <= s; x = x > 0 ? -x : 1 - x) {
                    for(int z = x < s && x > -s ? s : 0; z <= s; z = z > 0 ? -z : 1 - z) {
                        BlockPos blockpos1 = blockpos.add(x, y - 1, z);

                        if(this.shouldMoveTo(this.creature.world, blockpos1)) {
                            this.destinationBlock = blockpos1;
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }
    
    protected boolean shouldMoveTo(World worldIn, BlockPos pos) {
        if(worldIn.isRainingAt(pos)) {
            return true;
        } else {
            return this.isWaterDestination(worldIn, pos);
        }
    }
    
    protected boolean isWaterDestination(World world, BlockPos pos) {
        return world.getBlockState(pos).getMaterial() == Material.WATER;
    }
}
