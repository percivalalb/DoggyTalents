package doggytalents.entity.ai;

import doggytalents.entity.EntityDog;
import doggytalents.entity.ModeUtil.EnumMode;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITarget;

/**
 * @author ProPercivalalb
 */
public class EntityAIOwnerHurtTarget extends EntityAITarget {
	
    private EntityDog dog;
    private EntityLivingBase attacker;
    private int timestamp;

    public EntityAIOwnerHurtTarget(EntityDog dog) {
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
                this.attacker = owner.getLastAttacker();
                int i = owner.getLastAttackerTime();
                return i != this.timestamp && this.isSuitableTarget(this.attacker, false) && this.dog.func_142018_a(this.attacker, owner);
            }
        }
    }
    
    @Override
    public boolean continueExecuting() {
    	return !this.dog.isIncapacicated() && !this.dog.isSitting() && super.continueExecuting();
    }
    
    @Override
    public void startExecuting() {
        this.taskOwner.setAttackTarget(this.attacker);
        EntityLivingBase owner = this.dog.getOwner();

        if (owner != null)
        	this.timestamp = attacker.getLastAttackerTime();

        super.startExecuting();
    }
}