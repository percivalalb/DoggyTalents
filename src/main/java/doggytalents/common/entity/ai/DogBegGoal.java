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
        this.world = wolf.world;
        this.minPlayerDistance = minDistance;
        this.playerPredicate = (new EntityPredicate()).setDistance(minDistance).allowInvulnerable().allowFriendlyFire().setSkipAttackChecks();
        this.setMutexFlags(EnumSet.of(Goal.Flag.LOOK));
    }

    @Override
    public boolean shouldExecute() {
        this.player = this.world.getClosestPlayer(this.playerPredicate, this.dog);
        return this.player == null ? false : this.hasTemptationItemInHand(this.player);
    }

    @Override
    public boolean shouldContinueExecuting() {
        if (!this.player.isAlive()) {
            return false;
        } else if (this.dog.getDistanceSq(this.player) > this.minPlayerDistance * this.minPlayerDistance) {
            return false;
        } else {
            return this.timeoutCounter > 0 && this.hasTemptationItemInHand(this.player);
        }
    }

    @Override
    public void startExecuting() {
        this.dog.setBegging(true);
        this.timeoutCounter = 40 + this.dog.getRNG().nextInt(40);
    }

    @Override
    public void resetTask() {
        this.dog.setBegging(false);
        this.player = null;
    }

    @Override
    public void tick() {
        this.dog.getLookController().setLookPosition(this.player.getPosX(), this.player.getPosYEye(), this.player.getPosZ(), 10.0F, this.dog.getVerticalFaceSpeed());
        --this.timeoutCounter;
    }

    private boolean hasTemptationItemInHand(PlayerEntity player) {
        for (Hand hand : Hand.values()) {
            ItemStack itemstack = player.getHeldItem(hand);
            if (itemstack.getItem().isIn(this.dog.isTamed() ? DoggyTags.BEG_ITEMS_TAMED : DoggyTags.BEG_ITEMS_UNTAMED)) {
                return true;
            }

            if (FoodHandler.isFood(itemstack).isPresent()) {
                return true;
            }

            if (this.dog.isBreedingItem(itemstack)) {
                return true;
            }
        }

        return false;
    }
}