package doggytalents.common.entity.ai;

import java.util.EnumSet;

import doggytalents.DoggyTalents2;
import doggytalents.api.feature.EnumMode;
import doggytalents.common.entity.DogEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class PatrolAreaGoal extends Goal {

    public final DogEntity dog;
    private final PathNavigator navigator;
    public int index;
    private int timeToRecalcPath;

    public PatrolAreaGoal(DogEntity dogIn) {
        this.dog = dogIn;
        this.navigator = dogIn.getNavigator();
        this.setMutexFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean shouldExecute() {
        return this.dog.getMode() == EnumMode.PATROL && this.dog.getAttackTarget() == null && !this.dog.patrolPos.isEmpty();
    }

    @Override
    public boolean shouldContinueExecuting() {
        return this.dog.getMode() == EnumMode.PATROL && this.dog.getAttackTarget() == null && !this.dog.patrolPos.isEmpty();
    }

    @Override
    public void startExecuting() {
        this.timeToRecalcPath = 0;
        this.index = 0;
    }

    @Override
    public void resetTask() {
        this.dog.getNavigator().clearPath();
    }

    @Override
    public void tick() {
        if(!this.dog.isSitting()) {
            if(--this.timeToRecalcPath <= 0) {
                this.timeToRecalcPath = 10;

                this.index = MathHelper.clamp(this.index, 0, this.dog.patrolPos.size() - 1);
                BlockPos pos = this.dog.patrolPos.get(this.index);

                DoggyTalents2.LOGGER.info("Update" + this.index);

                if (new BlockPos(this.dog).withinDistance(pos, 2D) || !this.navigator.tryMoveToXYZ(pos.getX(), pos.getY(), pos.getZ(), 0.8D)) {
                    ++this.index;
                    this.index %= this.dog.patrolPos.size();
                }
            }
        }
    }

}