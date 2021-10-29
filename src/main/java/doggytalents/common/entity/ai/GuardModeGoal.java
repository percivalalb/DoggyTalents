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
    public boolean canUse() {
        LivingEntity owner = this.dog.getOwner();
        if (owner == null) {
            return false;
        }

        if (!this.dog.isMode(EnumMode.GUARD)) {
            return false;
        }

        this.owner = owner;

        if (super.canUse()) {
            this.owner = owner;
            return true;
        }

        return false;
    }

    @Override
    protected double getFollowDistance() {
        return 6D;
    }


    @Override
    protected void findTarget() {
       this.target = this.dog.level.getNearestLoadedEntity(this.targetType, this.targetConditions, this.dog, this.dog.getX(), this.dog.getEyeY(), this.dog.getZ(), this.getTargetSearchArea(this.getFollowDistance()));
    }
}