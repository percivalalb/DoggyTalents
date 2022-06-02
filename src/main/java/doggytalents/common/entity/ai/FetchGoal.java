package doggytalents.common.entity.ai;

import doggytalents.api.feature.EnumMode;
import doggytalents.api.inferface.IThrowableItem;
import doggytalents.common.entity.Dog;
import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

public class FetchGoal extends MoveToClosestItemGoal {

    public static Predicate<ItemStack> BONE_PREDICATE = (item) -> item.getItem() instanceof IThrowableItem;

    public FetchGoal(Dog dogIn, double speedIn, float range) {
        super(dogIn, speedIn, range, 2, BONE_PREDICATE);
    }

    @Override
    public boolean canUse() {
        if (this.dog.isInSittingPose()) {
            return false;
        } else if (this.dog.hasBone()) {
            return false;
        }

        return this.dog.getMode() == EnumMode.DOCILE && super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        if (this.dog.isInSittingPose()) {
            return false;
        } else if (this.dog.hasBone()) {
            return false;
        }

        return this.dog.getMode() == EnumMode.DOCILE && super.canContinueToUse();
    }

    @Override
    public void tick() {
        super.tick();


    }

    @Override
    public void stop() {
        // Dog doesn't have bone and is close enough to target
        if (!this.dog.hasBone() && this.dog.distanceTo(this.target) < this.minDist * this.minDist) {
            if (this.target.isAlive() && !this.target.hasPickUpDelay()) {

                this.dog.setBoneVariant(this.target.getItem());

                this.target.discard();
            }
        }

        super.stop();
    }
}
