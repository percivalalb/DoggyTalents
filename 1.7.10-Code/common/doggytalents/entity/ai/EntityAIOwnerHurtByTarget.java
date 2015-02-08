package doggytalents.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITarget;
import doggytalents.entity.EntityDTDoggy;
import doggytalents.entity.data.EnumMode;

public class EntityAIOwnerHurtByTarget extends EntityAITarget
{
    private EntityDTDoggy dog;
    private EntityLivingBase theOwnerAttacker;
    private int field_142051_e;

    public EntityAIOwnerHurtByTarget(EntityDTDoggy par1EntityDTDoggy)
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
            EntityLivingBase entitylivingbase = this.dog.getOwner();

            if (entitylivingbase == null)
            {
                return false;
            }
            else
            {
                this.theOwnerAttacker = entitylivingbase.getAITarget();
                int i = entitylivingbase.func_142015_aE();
                return i != this.field_142051_e && this.isSuitableTarget(this.theOwnerAttacker, false) && this.dog.func_142018_a(this.theOwnerAttacker, entitylivingbase);
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
        this.taskOwner.setAttackTarget(this.theOwnerAttacker);
        EntityLivingBase entitylivingbase = this.dog.getOwner();

        if (entitylivingbase != null)
        {
            this.field_142051_e = entitylivingbase.func_142015_aE();
        }

        super.startExecuting();
    }
}
