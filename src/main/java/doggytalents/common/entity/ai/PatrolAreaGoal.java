package doggytalents.common.entity.ai;

import doggytalents.DoggyTalents2;
import doggytalents.api.feature.EnumMode;
import doggytalents.common.entity.Dog;
import doggytalents.common.item.PatrolItem;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;

import java.util.EnumSet;
import java.util.List;

public class PatrolAreaGoal extends Goal {

    public final Dog dog;
    private final PathNavigation navigator;
    public int index;
    private int timeToRecalcPath;

    public PatrolAreaGoal(Dog dogIn) {
        this.dog = dogIn;
        this.navigator = dogIn.getNavigation();
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        return this.dog.getMode() == EnumMode.PATROL && this.dog.getTarget() == null && !this.dog.getData(PatrolItem.POS).isEmpty();
    }

    @Override
    public boolean canContinueToUse() {
        return this.dog.getMode() == EnumMode.PATROL && this.dog.getTarget() == null && !this.dog.getData(PatrolItem.POS).isEmpty();
    }

    @Override
    public void start() {
        this.timeToRecalcPath = 0;
        this.index = 0;
    }

    @Override
    public void stop() {
        this.dog.getNavigation().stop();
    }

    @Override
    public void tick() {
        if (!this.dog.isInSittingPose()) {
            if (--this.timeToRecalcPath <= 0) {
                this.timeToRecalcPath = 10;

                List<BlockPos> patrolPos = this.dog.getData(PatrolItem.POS);

                this.index = Mth.clamp(this.index, 0, patrolPos.size() - 1);
                BlockPos pos = patrolPos.get(this.index);

                DoggyTalents2.LOGGER.info("Update" + this.index);

                if (this.dog.blockPosition().closerThan(pos, 2D) || !this.navigator.moveTo(pos.getX(), pos.getY(), pos.getZ(), 0.8D)) {
                    ++this.index;
                    this.index %= patrolPos.size();
                }
            }
        }
    }

}
