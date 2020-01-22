package doggytalents.entity.ai;

import java.util.EnumSet;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import doggytalents.api.feature.EnumMode;
import doggytalents.entity.EntityDog;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.TargetGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.AxisAlignedBB;

public class EntityAIBerserkerMode<T extends LivingEntity> extends TargetGoal {

    private final EntityDog dog;
    private final Class<T> targetClass;
    private final int targetChance;

    protected LivingEntity nearestTarget;
    protected EntityPredicate targetEntitySelector;

    public EntityAIBerserkerMode(EntityDog p_i50313_1_, Class<T> p_i50313_2_, boolean p_i50313_3_) {
        this(p_i50313_1_, p_i50313_2_, p_i50313_3_, false);
    }

    public EntityAIBerserkerMode(EntityDog p_i50314_1_, Class<T> p_i50314_2_, boolean p_i50314_3_,
            boolean p_i50314_4_) {
        this(p_i50314_1_, p_i50314_2_, 0, p_i50314_3_, p_i50314_4_, (Predicate<LivingEntity>) null);
    }

    public EntityAIBerserkerMode(EntityDog p_i50315_1_, Class<T> p_i50315_2_, int p_i50315_3_, boolean p_i50315_4_, boolean p_i50315_5_, @Nullable Predicate<LivingEntity> p_i50315_6_) {
        super(p_i50315_1_, p_i50315_4_, p_i50315_5_);
        this.dog = p_i50315_1_;
        this.targetClass = p_i50315_2_;
        this.targetChance = p_i50315_3_;
        this.setMutexFlags(EnumSet.of(Goal.Flag.TARGET));
        this.targetEntitySelector = (new EntityPredicate()).setDistance(this.getTargetDistance()).setCustomPredicate(p_i50315_6_);
    }

    @Override
    public boolean shouldExecute() {
        if (!this.dog.MODE.isMode(EnumMode.BERSERKER)) {
            return false;
        } else if (this.targetChance > 0 && this.goalOwner.getRNG().nextInt(this.targetChance) != 0) {
            return false;
        } else {
            this.findNearestTarget();
            return this.nearestTarget != null && this.dog.shouldAttackEntity(this.nearestTarget, this.dog.getOwner());
        }
    }

    protected AxisAlignedBB getTargetableArea(double targetDistance) {
        return this.goalOwner.getBoundingBox().grow(targetDistance, 4.0D, targetDistance);
    }

    protected void findNearestTarget() {
        if (this.targetClass != PlayerEntity.class && this.targetClass != ServerPlayerEntity.class) {
            this.nearestTarget = this.goalOwner.world.<T>getClosestEntityWithinAABB(this.targetClass,
                    this.targetEntitySelector, this.goalOwner, this.goalOwner.getPosX(),
                    this.goalOwner.getPosY() + this.goalOwner.getEyeHeight(), this.goalOwner.getPosZ(),
                    this.getTargetableArea(this.getTargetDistance()));
        } else {
            this.nearestTarget = this.goalOwner.world.getClosestPlayer(this.targetEntitySelector, this.goalOwner,
                    this.goalOwner.getPosX(), this.goalOwner.getPosY() + this.goalOwner.getEyeHeight(),
                    this.goalOwner.getPosZ());
        }

    }

    @Override
    public void startExecuting() {
        this.goalOwner.setAttackTarget(this.nearestTarget);
        super.startExecuting();
    }
}