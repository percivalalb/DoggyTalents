package doggytalents.common.entity.ai;

import java.util.EnumSet;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import doggytalents.api.feature.EnumMode;
import doggytalents.common.entity.DogEntity;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.TargetGoal;
import net.minecraft.util.math.AxisAlignedBB;

public class BerserkerGoal<T extends LivingEntity> extends TargetGoal {

    private final DogEntity dog;
    private final Class<T> targetClass;
    private final int targetChance;

    protected LivingEntity nearestTarget;
    protected EntityPredicate targetEntitySelector;

    public BerserkerGoal(DogEntity dogIn, Class<T> targetClassIn, boolean checkSight) {
        this(dogIn, targetClassIn, checkSight, false);
    }

    public BerserkerGoal(DogEntity dogIn, Class<T> targetClassIn, boolean checkSight, boolean nearbyOnlyIn) {
        this(dogIn, targetClassIn, 0, checkSight, nearbyOnlyIn, (Predicate<LivingEntity>) null);
    }

    public BerserkerGoal(DogEntity dogIn, Class<T> targetClassIn, int targetChanceIn, boolean checkSight, boolean nearbyOnlyIn, @Nullable Predicate<LivingEntity> customPredicate) {
        super(dogIn, checkSight, nearbyOnlyIn);
        this.dog = dogIn;
        this.targetClass = targetClassIn;
        this.targetChance = targetChanceIn;
        this.setMutexFlags(EnumSet.of(Goal.Flag.TARGET));
        this.targetEntitySelector = (new EntityPredicate()).setDistance(this.getTargetDistance()).setCustomPredicate(customPredicate);
    }

    @Override
    public boolean shouldExecute() {
        if (this.dog.getMode() != EnumMode.BERSERKER) {
            return false;
        } else if (this.targetChance > 0 && this.goalOwner.getRNG().nextInt(this.targetChance) != 0) {
            return false;
        } else {
            this.nearestTarget = this.goalOwner.world.getClosestEntityWithinAABB(this.targetClass, this.targetEntitySelector, this.goalOwner, this.goalOwner.getPosX(), this.goalOwner.getPosY() + this.goalOwner.getEyeHeight(), this.goalOwner.getPosZ(), this.getTargetableArea(this.getTargetDistance()));
            if (this.nearestTarget == null) {
                return false;
            }
            LivingEntity owner = this.dog.getOwner();
            if (owner == null) {
                return false;
            } else {
                return this.dog.shouldAttackEntity(this.nearestTarget, owner);
            }
        }
    }

    protected AxisAlignedBB getTargetableArea(double targetDistance) {
        return this.goalOwner.getBoundingBox().grow(targetDistance, 4.0D, targetDistance);
    }

    @Override
    public void startExecuting() {
        this.goalOwner.setAttackTarget(this.nearestTarget);
        super.startExecuting();
    }
}