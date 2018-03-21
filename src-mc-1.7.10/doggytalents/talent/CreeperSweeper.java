package doggytalents.talent;

import java.util.List;
import java.util.Random;

import doggytalents.api.inferface.ITalent;
import doggytalents.entity.EntityDog;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityCreeper;

/**
 * @author ProPercivalalb
 */
public class CreeperSweeper extends ITalent {

	private Random rand = new Random();
	
	@Override
	public void onClassCreation(EntityDog dog) {
		dog.objects.put("creeper_timer", 0);
		dog.objects.put("random_time", 30 + this.rand.nextInt(20));
	}

	@Override
	public void onUpdate(EntityDog dog) {
		int level = dog.talents.getLevel(this);
		
		if(dog.getAttackTarget() == null && dog.isTamed() && level > 0) {
            List<EntityCreeper> list = dog.worldObj.getEntitiesWithinAABB(EntityCreeper.class, dog.boundingBox.expand(level * 5, level * 2, level * 5));

            if(!list.isEmpty() && !dog.isSitting() && dog.getHealth() > 1)
            	dog.objects.put("creeper_timer", (Integer)dog.objects.get("creeper_timer") + 1);
        }
		
		if((Integer)dog.objects.get("creeper_timer") >= (Integer)dog.objects.get("random_time")) {
			dog.playSound("mob.wolf.growl", dog.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
			dog.objects.put("creeper_timer", 0);
			dog.objects.put("random_time", 30 + this.rand.nextInt(20));
		}
		
		
		
		if(dog.getAttackTarget() instanceof EntityCreeper) {
        	EntityCreeper creeper = (EntityCreeper)dog.getAttackTarget();
        	creeper.setCreeperState(-1);
        }
	}
	
	@Override
	public boolean canAttackClass(EntityDog dog, Class entityClass) {
		return EntityCreeper.class == entityClass && dog.talents.getLevel(this) == 5; 
	}
	
	@Override
	public boolean canAttackEntity(EntityDog dog, Entity entity) {
		return entity instanceof EntityCreeper && dog.talents.getLevel(this) == 5;
	}
	
	@Override
	public String getKey() {
		return "creepersweeper";
	}

}
