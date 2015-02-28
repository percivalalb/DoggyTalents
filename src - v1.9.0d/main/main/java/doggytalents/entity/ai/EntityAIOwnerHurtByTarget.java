package doggytalents.entity.ai;

import doggytalents.entity.EntityDog;
import doggytalents.entity.ModeUtil.EnumMode;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.passive.EntityTameable;

/**
 * @author ProPercivalalb
 */
public class EntityAIOwnerHurtByTarget extends EntityAITarget
{
    private EntityDog dog;
    private EntityLivingBase theOwnerAttacker;
    private int field_142051_e;
    private static final String __OBFID = "CL_00001624";

    public EntityAIOwnerHurtByTarget(EntityDog dog)
    {
        super(dog, false);
        this.dog = dog;
        this.setMutexBits(1);
    }
    
    @Override
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

    @Override
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