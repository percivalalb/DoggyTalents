package doggytalents.entity.ai;

import doggytalents.entity.EntityDTDoggy;
import doggytalents.entity.data.EnumMode;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.passive.EntityTameable;

public class EntityAIOwnerHurtTarget extends EntityAITarget
{
    private EntityDTDoggy dog;
    EntityLivingBase theTarget;
    private int field_142050_e;

    public EntityAIOwnerHurtTarget(EntityDTDoggy par1EntityDTDoggy)
    {
        super(par1EntityDTDoggy, false);
        this.dog = par1EntityDTDoggy;
        this.setMutexBits(1);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        if (!this.dog.isTamed() || !this.dog.mode.isMode(EnumMode.AGGRESIVE) || this.dog.getHealth() <= 1)
        {
            return false;
        }
        else
        {
            EntityLivingBase entitylivingbase = this.dog.func_130012_q();

            if (entitylivingbase == null)
            {
                return false;
            }
            else
            {
                this.theTarget = entitylivingbase.getLastAttacker();
                int i = entitylivingbase.getLastAttackerTime();
                return i != this.field_142050_e && this.isSuitableTarget(this.theTarget, false) && this.dog.func_142018_a(this.theTarget, entitylivingbase);
            }
        }
    }
    
    @Override
    public boolean continueExecuting() {
    	return this.dog.getHealth() > 1 && super.continueExecuting();
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        this.taskOwner.setAttackTarget(this.theTarget);
        EntityLivingBase entitylivingbase = this.dog.func_130012_q();

        if (entitylivingbase != null)
        {
            this.field_142050_e = entitylivingbase.getLastAttackerTime();
        }

        super.startExecuting();
    }
}
