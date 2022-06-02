package doggytalents.common.entity.ai;

import doggytalents.DoggyTags;
import doggytalents.api.feature.FoodHandler;
import doggytalents.common.entity.Dog;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.EnumSet;

public class DogBegGoal extends Goal {

    private final Dog dog;
    private Player player;
    private final Level world;
    private final float minPlayerDistance;
    private int timeoutCounter;
    private final TargetingConditions playerPredicate;

    public DogBegGoal(Dog wolf, float minDistance) {
        this.dog = wolf;
        this.world = wolf.level;
        this.minPlayerDistance = minDistance;
        this.playerPredicate = TargetingConditions.forNonCombat().range(minDistance); // TODO check
        this.setFlags(EnumSet.of(Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        this.player = this.world.getNearestPlayer(this.playerPredicate, this.dog);
        return this.player == null ? false : this.hasTemptationItemInHand(this.player);
    }

    @Override
    public boolean canContinueToUse() {
        if (!this.player.isAlive()) {
            return false;
        } else if (this.dog.distanceToSqr(this.player) > this.minPlayerDistance * this.minPlayerDistance) {
            return false;
        } else {
            return this.timeoutCounter > 0 && this.hasTemptationItemInHand(this.player);
        }
    }

    @Override
    public void start() {
        this.dog.setBegging(true);
        this.timeoutCounter = 40 + this.dog.getRandom().nextInt(40);
    }

    @Override
    public void stop() {
        this.dog.setBegging(false);
        this.player = null;
    }

    @Override
    public void tick() {
        this.dog.getLookControl().setLookAt(this.player.getX(), this.player.getEyeY(), this.player.getZ(), 10.0F, this.dog.getMaxHeadXRot());
        --this.timeoutCounter;
    }

    private boolean hasTemptationItemInHand(Player player) {
        for (InteractionHand hand : InteractionHand.values()) {
            ItemStack itemstack = player.getItemInHand(hand);
            if (itemstack.is(this.dog.isTame() ? DoggyTags.BEG_ITEMS_TAMED : DoggyTags.BEG_ITEMS_UNTAMED)) {
                return true;
            }

            if (itemstack.is(DoggyTags.TREATS)) {
                return true;
            }

            if (FoodHandler.isFood(itemstack).isPresent()) {
                return true;
            }

            if (this.dog.isFood(itemstack)) {
                return true;
            }
        }

        return false;
    }
}
