package doggytalents.common.entity.ai;

import java.util.function.Predicate;

import doggytalents.api.feature.EnumMode;
import doggytalents.api.inferface.IThrowableItem;
import doggytalents.common.entity.DogEntity;
import net.minecraft.item.ItemStack;

public class FetchGoal extends MoveToClosestItemGoal {

    public static Predicate<ItemStack> BONE_PREDICATE = (item) -> item.getItem() instanceof IThrowableItem;

    public FetchGoal(DogEntity dogIn, double speedIn, float range) {
        super(dogIn, speedIn, range, 2, BONE_PREDICATE);
    }

    @Override
    public boolean shouldExecute() {
        if (this.dog.isSitting()) {
            return false;
        } else if (this.dog.hasBone()) {
            return false;
        }

        return this.dog.getMode() == EnumMode.DOCILE && super.shouldExecute();
    }

    @Override
    public boolean shouldContinueExecuting() {
        if (this.dog.isSitting()) {
            return false;
        } else if (this.dog.hasBone()) {
            return false;
        }

        return this.dog.getMode() == EnumMode.DOCILE && super.shouldContinueExecuting();
    }

    @Override
    public void tick() {
        super.tick();


    }

    @Override
    public void resetTask() {
        // Dog doesn't have bone and is close enough to target
        if(!this.dog.hasBone() && this.dog.getDistance(this.target) < this.minDist * this.minDist) {
            if(this.target.isAlive() && !this.target.cannotPickup()) {

                this.dog.setBoneVariant(this.target.getItem());

                this.target.remove();
            }
        }

        super.resetTask();
    }
}