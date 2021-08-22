package doggytalents.common.entity.ai;

import java.util.EnumSet;

import doggytalents.api.feature.EnumMode;
import doggytalents.api.inferface.IThrowableItem;
import doggytalents.common.entity.DogEntity;
import doggytalents.common.util.EntityUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.world.World;

public class DogFollowOwnerGoal extends Goal {

    private final DogEntity dog;
    private final PathNavigator navigator;
    private final World world;
    private final double followSpeed;
    private final float stopDist; // If closer than stopDist stop moving towards owner
    private final float startDist; // If further than startDist moving towards owner

    private LivingEntity owner;
    private int timeToRecalcPath;
    private float oldWaterCost;

    public DogFollowOwnerGoal(DogEntity dogIn, double speedIn, float minDistIn, float maxDistIn) {
        this.dog = dogIn;
        this.world = dogIn.level;
        this.followSpeed = speedIn;
        this.navigator = dogIn.getNavigation();
        this.startDist = minDistIn;
        this.stopDist = maxDistIn;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        LivingEntity owner = this.dog.getOwner();
        if (owner == null) {
            return false;
        } else if (this.dog.getMode() == EnumMode.PATROL) {
            return false;
        } else if (owner.isSpectator()) {
            return false;
        } else if (this.dog.isInSittingPose()) {
            return false;
        } else if (!this.dog.hasBone() && this.dog.distanceToSqr(owner) < this.getMinStartDistanceSq()) {
            return false;
        } else {
            this.owner = owner;
            return true;
        }
    }

    @Override
    public boolean canContinueToUse() {
        if (this.navigator.isDone()) {
            return false;
        } else if (this.dog.isInSittingPose()) {
            return false;
        } else {
            return this.dog.distanceToSqr(this.owner) > this.stopDist * this.stopDist;
        }
    }

    @Override
    public void start() {
        this.timeToRecalcPath = 0;
        this.oldWaterCost = this.dog.getPathfindingMalus(PathNodeType.WATER);
        this.dog.setPathfindingMalus(PathNodeType.WATER, 0.0F);
    }

    @Override
    public void stop() {
        if (this.dog.hasBone()) {
            double distanceToOwner = this.owner.distanceToSqr(this.dog);
            if (distanceToOwner <= this.stopDist * this.stopDist) {
                IThrowableItem throwableItem = this.dog.getThrowableItem();
                ItemStack fetchItem = throwableItem != null ? throwableItem.getReturnStack(this.dog.getBoneVariant()) : this.dog.getBoneVariant();

                this.dog.spawnAtLocation(fetchItem, 0.0F);
                this.dog.setBoneVariant(ItemStack.EMPTY);
            }
        }

        this.owner = null;
        this.navigator.stop();
        this.dog.setPathfindingMalus(PathNodeType.WATER, this.oldWaterCost);
    }

    @Override
    public void tick() {
        this.dog.getLookControl().setLookAt(this.owner, 10.0F, this.dog.getMaxHeadXRot());
        if (--this.timeToRecalcPath <= 0) {
            this.timeToRecalcPath = 10;
            if (!this.dog.isLeashed() && !this.dog.isPassenger()) { // Is not leashed and is not a passenger
                if (this.dog.distanceToSqr(this.owner) >= 144.0D) { // Further than 12 blocks away teleport
                    EntityUtil.tryToTeleportNearEntity(this.dog, this.navigator, this.owner, 4);
                } else {
                    this.navigator.moveTo(this.owner, this.followSpeed);
                }
            }
        }
    }

    public float getMinStartDistanceSq() {
        if (this.dog.isMode(EnumMode.GUARD)) {
            return 4F;
        }

        return this.startDist * this.startDist;
    }
}