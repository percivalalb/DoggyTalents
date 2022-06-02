package doggytalents.common.entity.ai;

import doggytalents.api.feature.EnumMode;
import doggytalents.common.entity.Dog;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;

public class GuardModeGoal extends NearestAttackableTargetGoal<Monster> {

    private final Dog dog;
    private LivingEntity owner;

    public GuardModeGoal(Dog dogIn, boolean checkSight) {
        super(dogIn, Monster.class, 0, checkSight, false, null);
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
       this.target = this.dog.level.getNearestEntity(this.targetType, this.targetConditions, this.dog, this.owner.getX(), this.owner.getEyeY(), this.owner.getZ(), this.getTargetSearchArea(this.getFollowDistance()));
    }
}
