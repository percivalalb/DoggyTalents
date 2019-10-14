package doggytalents.entity.ai;

import doggytalents.entity.EntityDog;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIFollowOwner;

public class EntityAIFetchReturn extends EntityAIFollowOwner {

    private EntityDog dog;
    private double oldRangeSense;
    
    public EntityAIFetchReturn(EntityDog dogIn, double followSpeedIn) {
        super(dogIn, followSpeedIn, 0, 1F);
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
        this.oldRangeSense = this.dog.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).getBaseValue();
        this.dog.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(32);
    }

    @Override
    public void resetTask() {
        super.resetTask();
        this.dog.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(this.oldRangeSense);
    }
    
    // Forces the dog to walk back to the owner every time
    @Override
    protected boolean isTeleportFriendlyBlock(int x, int z, int y, int xOffset, int zOffset) {
        return false;
    }
}
