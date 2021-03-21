package doggytalents.common.entity.ai;

import doggytalents.api.feature.EnumMode;
import doggytalents.common.entity.DogEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.monster.MonsterEntity;

public class GuardModeGoal extends NearestAttackableTargetGoal<MonsterEntity> {

    private final DogEntity dog;
    private LivingEntity owner;

    public GuardModeGoal(DogEntity dogIn, boolean checkSight) {
        super(dogIn, MonsterEntity.class, 0, checkSight, false, null);
        this.dog = dogIn;
    }

    @Override
    public boolean shouldExecute() {
        LivingEntity owner = this.dog.getOwner();
        if (owner == null) {
            return false;
        }

        if (!this.dog.isMode(EnumMode.GUARD)) {
            return false;
        }

        this.owner = owner;

        if (super.shouldExecute()) {
            this.owner = owner;
            return true;
        }

        return false;
    }

    @Override
    protected double getTargetDistance() {
        return 6D;
    }

    @Override
    protected void findNearestTarget() {
       this.nearestTarget = this.dog.world.getClosestEntity(this.targetClass, this.targetEntitySelector, this.owner, this.dog.getPosX(), this.dog.getPosYEye(), this.dog.getPosZ(), this.getTargetableArea(this.getTargetDistance()));
    }
}