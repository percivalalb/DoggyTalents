package doggytalents.entity.ai;

import doggytalents.entity.EntityDog;
import doggytalents.entity.ModeUtil.EnumMode;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITarget;

/**
 * @author ProPercivalalb
 */
public class EntityAIOwnerHurtByTarget extends EntityAITarget {
	
    private EntityDog dog;
    private EntityLivingBase theOwnerAttacker;
    private int timestamp;

    public EntityAIOwnerHurtByTarget(EntityDog dog) {
        super(dog, false);
        this.dog = dog;
        this.setMutexBits(1);
    }
    
    @Override
    public boolean shouldExecute() {
        if(!this.dog.isTamed() || !this.dog.mode.isMode(EnumMode.AGGRESIVE) || this.dog.isIncapacicated() || this.dog.isSitting())
            return false;
        else {
            EntityLivingBase owner = this.dog.getOwner();

            if(owner == null)
                return false;
            else {
                this.theOwnerAttacker = owner.getRevengeTarget();
                int i = owner.getRevengeTimer();
                return i != this.timestamp && this.isSuitableTarget(this.theOwnerAttacker, false) && this.dog.shouldAttackEntity(this.theOwnerAttacker, owner);
            }
        }
    }

    @Override
    public boolean shouldContinueExecuting() {
    	return !this.dog.isIncapacicated() && !this.dog.isSitting() && super.shouldContinueExecuting();
    }

    @Override
    public void startExecuting() {
        this.taskOwner.setAttackTarget(this.theOwnerAttacker);
        EntityLivingBase owner = this.dog.getOwner();

        if(owner != null)
            this.timestamp = owner.getRevengeTimer();

        super.startExecuting();
    }
}