package doggytalents.entity.ai;

import java.util.List;

import doggytalents.entity.EntityDog;
import doggytalents.entity.ModeUtil.EnumMode;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.monster.EntityMob;

/**
 * @author ProPercivalalb
 **/
public class EntityAIModeAttackTarget extends EntityAITarget {
	
    private EntityDog dog;
    private EntityLivingBase entityToAttack;
    private int field_142051_e;

    public EntityAIModeAttackTarget(EntityDog dog) {
        super(dog, true);
        this.dog = dog;
        this.setMutexBits(1);
    }

    @Override
    public boolean shouldExecute() {
        if (!this.dog.isTamed() || this.dog.mode.isMode(EnumMode.DOCILE) || this.dog.isIncapacicated() || this.dog.isSitting())
            return false;
        else {
            EntityLivingBase entitylivingbase = this.dog.getOwner();

            if (entitylivingbase == null)
                return false;
            else {
            	if(this.dog.mode.isMode(EnumMode.BERSERKER)) {
	           	 	double distance = 16D;
	           	 	List list = dog.worldObj.getEntitiesWithinAABBExcludingEntity(dog, dog.getEntityBoundingBox().expand(distance, distance, distance));
	
	           	 	for (int count = 0; count < list.size(); count++) {
	           	 		Entity entity1 = (Entity)list.get(count);
	           	 		if (!(entity1 instanceof EntityMob)) {
	           	 			continue;
	           	 		}
	           	 		this.entityToAttack = (EntityLivingBase)entity1;
	           	 		if(this.isSuitableTarget(this.entityToAttack, false) && this.dog.shouldAttackEntity(this.entityToAttack, entitylivingbase))
	           	 			return true;
	           	 	}
            	}
            	else if(this.dog.mode.isMode(EnumMode.AGGRESIVE)) {
            		this.entityToAttack = entitylivingbase.getLastAttacker();
                    int i = entitylivingbase.getLastAttackerTime();
                    return i != this.field_142051_e && this.isSuitableTarget(this.entityToAttack, false) && this.dog.shouldAttackEntity(this.entityToAttack, entitylivingbase);
            	}
           	 	return false;
            }
        }
    }
    
    @Override
    public boolean continueExecuting() {
    	return !this.dog.isIncapacicated() && !this.dog.isSitting() && super.continueExecuting();
    }

    @Override
    public void startExecuting() {
        this.taskOwner.setAttackTarget(this.entityToAttack);
        EntityLivingBase entitylivingbase = this.dog.getOwner();

        if (entitylivingbase != null)
            this.field_142051_e = entitylivingbase.getLastAttackerTime();

        super.startExecuting();
    }
}
