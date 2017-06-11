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
    private EntityLivingBase theTarget;
    private int field_142050_e;
    private static final String __OBFID = "CL_00001625";

    public EntityAIOwnerHurtTarget(EntityDog dog)
    {
        super(dog, false);
        this.dog = dog;
        this.setMutexBits(1);
    }

    @Override
    public boolean shouldExecute() {
        if (!this.dog.isTamed() || !this.dog.mode.isMode(EnumMode.AGGRESIVE) || this.dog.isIncapacicated() || this.dog.isSitting())
            return false;
        else {
            EntityLivingBase owner = this.dog.getOwner();

            if (owner == null)
                return false;
            else {
                this.theTarget = owner.getLastAttacker();
                int i = owner.getLastAttackerTime();
                return i != this.field_142050_e && this.isSuitableTarget(this.theTarget, false) && this.dog.shouldAttackEntity(this.theTarget, owner);
            }
        }
    }
    
    @Override
    public boolean continueExecuting() {
    	return !this.dog.isIncapacicated() && !this.dog.isSitting() && super.continueExecuting();
    }
    
    @Override
    public void startExecuting() {
        this.taskOwner.setAttackTarget(this.theTarget);
        EntityLivingBase owner = this.dog.getOwner();

        if (owner != null)
            this.field_142050_e = owner.getLastAttackerTime();

        super.startExecuting();
    }
}