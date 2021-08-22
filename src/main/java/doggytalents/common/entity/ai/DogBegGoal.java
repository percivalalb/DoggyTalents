package doggytalents.common.entity.ai;

import java.util.EnumSet;

import doggytalents.DoggyTags;
import doggytalents.api.feature.FoodHandler;
import doggytalents.common.entity.DogEntity;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class DogBegGoal extends Goal {

    private final DogEntity dog;
    private PlayerEntity player;
    private final World world;
    private final float minPlayerDistance;
    private int timeoutCounter;
    private final EntityPredicate playerPredicate;

    public DogBegGoal(DogEntity wolf, float minDistance) {
        this.dog = wolf;
        this.world = wolf.level;
        this.minPlayerDistance = minDistance;
        this.playerPredicate = (new EntityPredicate()).range(minDistance).allowInvulnerable().allowSameTeam().allowNonAttackable();
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

    private boolean hasTemptationItemInHand(PlayerEntity player) {
        for (Hand hand : Hand.values()) {
            ItemStack itemstack = player.getItemInHand(hand);
            if (itemstack.getItem().is(this.dog.isTame() ? DoggyTags.BEG_ITEMS_TAMED : DoggyTags.BEG_ITEMS_UNTAMED)) {
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