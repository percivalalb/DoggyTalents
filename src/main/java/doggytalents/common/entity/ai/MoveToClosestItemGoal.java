package doggytalents.common.entity.ai;

import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import doggytalents.common.entity.DogEntity;
import doggytalents.common.util.EntityUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.PathNodeType;

public class MoveToClosestItemGoal extends Goal {

    protected final DogEntity dog;
    protected final Predicate<ItemEntity> predicate;
    protected final Comparator<Entity> sorter;
    protected final double followSpeed;
    protected final PathNavigator dogNavigator;
    protected final float minDist;

    protected ItemEntity target;
    private int timeToRecalcPath;
    private float maxDist;
    private float oldWaterCost;
    private double oldRangeSense;

    public MoveToClosestItemGoal(DogEntity dogIn, double speedIn, float maxDist, float stopDist, @Nullable Predicate<ItemStack> targetSelector) {
        this.dog = dogIn;
        this.dogNavigator = dogIn.getNavigator();
        this.followSpeed = speedIn;
        this.maxDist = maxDist;
        this.minDist = stopDist;
        this.predicate = (entity) -> {
            if (entity.isInvisible()) {
                return false;
            } else if (targetSelector != null && !targetSelector.test(entity.getItem())) {
                return false;
            } else {
                return entity.getDistance(this.dog) <= EntityUtil.getFollowRange(this.dog);
            }
        };
        this.sorter = new EntityUtil.Sorter(dogIn);
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean shouldExecute() {
        double d0 = EntityUtil.getFollowRange(this.dog);
        List<ItemEntity> list = this.dog.world.getEntitiesWithinAABB(ItemEntity.class, this.dog.getBoundingBox().grow(d0, 4.0D, d0), this.predicate);
        if (list.isEmpty()) {
            return false;
        } else {
            Collections.sort(list, this.sorter);
            this.target = list.get(0);
            return true;
        }
    }

    @Override
    public boolean shouldContinueExecuting() {
        ItemEntity target = this.target;
        if (target == null || !target.isAlive()) {
            return false;
        } else {
            double d0 = EntityUtil.getFollowRange(this.dog);
            double dist = this.dog.getDistanceSq(target);
            if (dist > d0 * d0 || dist < this.minDist * this.minDist) {
                return false;
            } else {
                return true;
            }
        }
    }

    @Override
    public void startExecuting() {
        this.timeToRecalcPath = 0;
        this.oldWaterCost = this.dog.getPathPriority(PathNodeType.WATER);
        this.dog.setPathPriority(PathNodeType.WATER, 0.0F);
        this.oldRangeSense = this.dog.getAttribute(Attributes.FOLLOW_RANGE).getValue();
        this.dog.getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(this.maxDist);
    }

    @Override
    public void tick() {
        if (!this.dog.isEntitySleeping()) {
            if (--this.timeToRecalcPath <= 0) {
                this.timeToRecalcPath = 10;

                if (!this.dogNavigator.tryMoveToEntityLiving(this.target, this.followSpeed)) {
                    this.dog.getLookController().setLookPositionWithEntity(this.target, 10.0F, this.dog.getVerticalFaceSpeed());
                }
            }
        }
    }

    @Override
    public void resetTask() {
        this.target = null;
        this.dogNavigator.clearPath();
        this.dog.setPathPriority(PathNodeType.WATER, this.oldWaterCost);
        this.dog.getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(this.oldRangeSense);
    }
}