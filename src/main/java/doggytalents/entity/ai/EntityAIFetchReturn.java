package doggytalents.entity.ai;

import doggytalents.entity.EntityDog;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.goal.FollowOwnerGoal;

public class EntityAIFetchReturn extends FollowOwnerGoal {

    private EntityDog dog;
    private double oldRangeSense;

    public EntityAIFetchReturn(EntityDog dogIn, double followSpeedIn) {
        super(dogIn, followSpeedIn, 0, 1F, false); //TODO
        this.dog = dogIn;
    }

    @Override
    public boolean shouldExecute() {
        return this.dog.hasBone() && super.shouldExecute();
    }

    @Override
    public boolean shouldContinueExecuting() {
        return this.dog.hasBone() && super.shouldContinueExecuting();
    }

    @Override
    public void startExecuting() {
        super.startExecuting();
        this.oldRangeSense = this.dog.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).getValue();
        this.dog.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(32);
    }

    @Override
    public void resetTask() {
        super.resetTask();
        this.dog.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(this.oldRangeSense);
    }

    // Forces the dog to walk back to the owner every time
    //TODO @Override
    //protected boolean canTeleportToBlock(BlockPos pos) {
    //    return false;
    //}
}
