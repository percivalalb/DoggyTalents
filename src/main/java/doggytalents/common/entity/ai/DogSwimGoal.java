package doggytalents.common.entity.ai;

import java.util.EnumSet;

import doggytalents.common.entity.DogEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.tags.FluidTags;

public class DogSwimGoal extends Goal {
    private final DogEntity dog;

    public DogSwimGoal(DogEntity d) {
        this.dog = d;
        this.setFlags(EnumSet.of(Goal.Flag.JUMP));
        d.getNavigation().setCanFloat(true);
    }

    public boolean canUse() {
        return this.dog.isInWater() && !this.dog.canSwim() && this.dog.getFluidHeight(FluidTags.WATER) > this.dog.getFluidJumpThreshold() || this.dog.isInLava();
    }

    public void tick() {
        if (this.dog.getRandom().nextFloat() < 0.8F) {
            this.dog.getJumpControl().jump();
        }
    }
}
