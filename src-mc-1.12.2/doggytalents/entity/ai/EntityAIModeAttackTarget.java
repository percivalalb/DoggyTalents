package doggytalents.entity.ai;

import java.util.List;

import doggytalents.entity.EntityDog;
import doggytalents.entity.ModeUtil.EnumMode;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityWolf;

/**
 * @author ProPercivalalb
 **/
public class EntityAIModeAttackTarget extends EntityAITarget {
	
    private EntityDog dog;
    private EntityLivingBase entityToAttack;
    private int timestamp;

    public EntityAIModeAttackTarget(EntityDog dog) {
        super(dog, true);
        this.dog = dog;
        this.setMutexBits(1);
    }

    @Override
    public boolean shouldExecute() {
        if(!this.dog.isTamed() || this.dog.mode.isMode(EnumMode.DOCILE) || this.dog.isIncapacicated() || this.dog.isSitting())
            return false;
        else {
            EntityLivingBase entitylivingbase = this.dog.getOwner();

            if(entitylivingbase == null)
                return false;
            else {	           	 	
           	 	if(this.dog.mode.isMode(EnumMode.BERSERKER)) {
           	 		double distance1 = 16D;	           	 	
           	 		List list1 = dog.world.getEntitiesWithinAABBExcludingEntity(dog, dog.getEntityBoundingBox().grow(distance1, distance1, distance1));

	           	 	for (int count = 0; count < list1.size(); count++) {
	           	 		Entity entity1 = (Entity)list1.get(count);
	           	 		if (!(entity1 instanceof EntityMob)) {
	           	 			continue;
	           	 		}
	           	 		this.entityToAttack = (EntityLivingBase)entity1;
	           	 		if(this.isSuitableTarget(this.entityToAttack, false) && this.dog.shouldAttackEntity(this.entityToAttack, entitylivingbase))
	           	 			return true;
	           	 	}
           	 	}	           	 	
           	 	
           	 	if(this.dog.mode.isMode(EnumMode.GUARD)) {
           	 		double distance2 = 2D;
           	 		List list2 = dog.world.getEntitiesWithinAABBExcludingEntity(dog, dog.getOwner().getEntityBoundingBox().grow(distance2, distance2, distance2));

	           	 	for (int count = 0; count < list2.size(); count++) {
	           	 		Entity entity2 = (Entity)list2.get(count);
		           	 	if (!(entity2 instanceof EntityCreature && !(entity2 instanceof EntityDog) && !(entity2 instanceof EntityWolf))) {
           	 				continue;
	           	 		}
	           	 		this.entityToAttack = (EntityLivingBase)entity2;
	           	 		if(this.isSuitableTarget(this.entityToAttack, false) && this.dog.shouldAttackEntity(this.entityToAttack, entitylivingbase))
	           	 			return true;
	           	 	}
           	 	}
           	 	
            	if(this.dog.mode.isMode(EnumMode.HUNT_ALL)) {
	           	 	double distance = 16D;
	           	 	List list = dog.world.getEntitiesWithinAABBExcludingEntity(dog, dog.getEntityBoundingBox().grow(distance, distance, distance));
	
	           	 	for (int count = 0; count < list.size(); count++) {
	           	 		Entity entity1 = (Entity)list.get(count);
	           	 		if (!(entity1 instanceof EntityAnimal && !(entity1 instanceof EntityDog) && !(entity1 instanceof EntityWolf))) {
           	 				continue;
	           	 		}
	           	 		this.entityToAttack = (EntityLivingBase)entity1;
	           	 		if(this.isSuitableTarget(this.entityToAttack, false) && this.dog.shouldAttackEntity(this.entityToAttack, entitylivingbase))
	           	 			return true;
	           	 	}
            	}
            	else if(this.dog.mode.isMode(EnumMode.AGGRESIVE)) {
            		this.entityToAttack = entitylivingbase.getRevengeTarget();
                    int i = entitylivingbase.getRevengeTimer();
                    return i != this.timestamp && this.isSuitableTarget(this.entityToAttack, false) && this.dog.shouldAttackEntity(this.entityToAttack, entitylivingbase);
            	}
           	 	return false;
            }
        }
    }
    
    @Override
    public boolean shouldContinueExecuting() {
    	return !this.dog.isIncapacicated() && !this.dog.isSitting() && super.shouldContinueExecuting();
    }

    @Override
    public void startExecuting() {
        this.taskOwner.setAttackTarget(this.entityToAttack);
        EntityLivingBase entitylivingbase = this.dog.getOwner();

        if (entitylivingbase != null)
            this.timestamp = entitylivingbase.getRevengeTimer();

        super.startExecuting();
    }
}
