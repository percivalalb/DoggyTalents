package doggytalents.common.entity.ai;

import java.util.EnumSet;

import doggytalents.common.block.tileentity.DogBedTileEntity;
import doggytalents.common.entity.DogEntity;
import doggytalents.common.lib.Constants;
import doggytalents.common.util.WorldUtil;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;

public class MoveToBlockGoal extends Goal {

    protected final DogEntity dog;

    public MoveToBlockGoal(DogEntity dogIn) {
        this.dog = dogIn;
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean shouldExecute() {
        return this.dog.getTargetBlock() != null && !this.dog.isSitting();
    }

    @Override
    public boolean shouldContinueExecuting() {
        return this.dog.hasPath() && !this.dog.getTargetBlock().withinDistance(this.dog.getPositionVec(), 0.5);
    }

    @Override
    public void resetTask() {
        BlockPos target = this.dog.getTargetBlock();

        DogBedTileEntity dogBedTileEntity = WorldUtil.getTileEntity(dog.world, target, DogBedTileEntity.class);

        if (dogBedTileEntity != null) {
            // Double check the bed still has no owner
            if (dogBedTileEntity.getOwnerUUID() == null) {
                dogBedTileEntity.setOwner(this.dog);
                this.dog.setBedPos(this.dog.dimension, target);
            }
        }

        this.dog.setTargetBlock(null);
        this.dog.getAISit().setSitting(true);

        this.dog.world.setEntityState(this.dog, Constants.EntityState.WOLF_HEARTS);
    }

    @Override
    public void startExecuting() {
        BlockPos target = this.dog.getTargetBlock();
        this.dog.getNavigator().tryMoveToXYZ((target.getX()) + 0.5D, target.getY() + 1, (target.getZ()) + 0.5D, 1.0D);
    }

}
