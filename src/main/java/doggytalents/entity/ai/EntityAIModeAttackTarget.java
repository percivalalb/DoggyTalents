package doggytalents.entity.ai;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.monster.EntityMob;
import doggytalents.core.helper.LogHelper;
import doggytalents.entity.EntityDTDoggy;
import doggytalents.entity.data.EnumMode;

public class EntityAIModeAttackTarget extends EntityAITarget
{
    private EntityDTDoggy dog;
    private EntityLivingBase entityToAttack;
    private int field_142051_e;

    public EntityAIModeAttackTarget(EntityDTDoggy par1EntityDTDoggy)
    {
        super(par1EntityDTDoggy, true);
        this.dog = par1EntityDTDoggy;
        this.setMutexBits(1);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean shouldExecute()
    {
        if (!this.dog.isTamed() || this.dog.mode.isMode(EnumMode.DOCILE) || this.dog.getHealth() <= 1)
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
            	if(this.dog.mode.isMode(EnumMode.BERSERKER)) {
	           	 	double distance = 16D;
	           	 	List list = dog.worldObj.getEntitiesWithinAABBExcludingEntity(dog, dog.boundingBox.expand(distance, distance, distance));
	
	           	 	for (int count = 0; count < list.size(); count++) {
	           	 		Entity entity1 = (Entity)list.get(count);
	           	 		if (!(entity1 instanceof EntityMob)) {
	           	 			continue;
	           	 		}
	           	 		this.entityToAttack = (EntityLivingBase)entity1;
	           	 		if(this.isSuitableTarget(this.entityToAttack, false) && this.dog.func_142018_a(this.entityToAttack, entitylivingbase))
	           	 			return true;
	           	 	}
            	}
            	else if(this.dog.mode.isMode(EnumMode.AGGRESIVE)) {
            		this.entityToAttack = entitylivingbase.getLastAttacker();
                    int i = entitylivingbase.getLastAttackerTime();
                    LogHelper.info("daweewaeawe");
                    return i != this.field_142051_e && this.isSuitableTarget(this.entityToAttack, false) && this.dog.func_142018_a(this.entityToAttack, entitylivingbase);
            	}
           	 	return false;
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
    @Override
    public void startExecuting()
    {
        this.taskOwner.setAttackTarget(this.entityToAttack);
        EntityLivingBase entitylivingbase = this.dog.getOwner();

        if (entitylivingbase != null)
        {
            this.field_142051_e = entitylivingbase.func_142015_aE();
        }

        super.startExecuting();
    }
}
