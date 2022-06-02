package doggytalents.common.entity.ai;

import doggytalents.common.entity.Dog;
import doggytalents.common.util.EntityUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.pathfinder.BlockPathTypes;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

public class MoveToClosestItemGoal extends Goal {

    protected final Dog dog;
    protected final Predicate<ItemEntity> predicate;
    protected final Comparator<Entity> sorter;
    protected final double followSpeed;
    protected final PathNavigation dogNavigator;
    protected final float minDist;

    protected ItemEntity target;
    private int timeToRecalcPath;
    private float maxDist;
    private float oldWaterCost;
    private double oldRangeSense;

    public MoveToClosestItemGoal(Dog dogIn, double speedIn, float maxDist, float stopDist, @Nullable Predicate<ItemStack> targetSelector) {
        this.dog = dogIn;
        this.dogNavigator = dogIn.getNavigation();
        this.followSpeed = speedIn;
        this.maxDist = maxDist;
        this.minDist = stopDist;
        this.predicate = (entity) -> {
            if (entity.isInvisible()) {
                return false;
            } else if (targetSelector != null && !targetSelector.test(entity.getItem())) {
                return false;
            } else {
                return entity.distanceTo(this.dog) <= EntityUtil.getFollowRange(this.dog);
            }
        };
        this.sorter = new EntityUtil.Sorter(dogIn);
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        double d0 = EntityUtil.getFollowRange(this.dog);
        List<ItemEntity> list = this.dog.level.getEntitiesOfClass(ItemEntity.class, this.dog.getBoundingBox().inflate(d0, 4.0D, d0), this.predicate);
        if (list.isEmpty()) {
            return false;
        } else {
            Collections.sort(list, this.sorter);
            this.target = list.get(0);
            return true;
        }
    }

    @Override
    public boolean canContinueToUse() {
        ItemEntity target = this.target;
        if (target == null || !target.isAlive()) {
            return false;
        } else {
            double d0 = EntityUtil.getFollowRange(this.dog);
            double dist = this.dog.distanceToSqr(target);
            if (dist > d0 * d0 || dist < this.minDist * this.minDist) {
                return false;
            } else {
                return true;
            }
        }
    }

    @Override
    public void start() {
        this.timeToRecalcPath = 0;
        this.oldWaterCost = this.dog.getPathfindingMalus(BlockPathTypes.WATER);
        this.dog.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
        this.oldRangeSense = this.dog.getAttribute(Attributes.FOLLOW_RANGE).getValue();
        this.dog.getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(this.maxDist);
    }

    @Override
    public void tick() {
        if (!this.dog.isInSittingPose()) {
            if (--this.timeToRecalcPath <= 0) {
                this.timeToRecalcPath = 10;

                if (!this.dogNavigator.moveTo(this.target, this.followSpeed)) {
                    this.dog.getLookControl().setLookAt(this.target, 10.0F, this.dog.getMaxHeadXRot());
                }
            }
        }
    }

    @Override
    public void stop() {
        this.target = null;
        this.dogNavigator.stop();
        this.dog.setPathfindingMalus(BlockPathTypes.WATER, this.oldWaterCost);
        this.dog.getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(this.oldRangeSense);
    }
}
