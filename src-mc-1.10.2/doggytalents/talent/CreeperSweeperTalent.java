package doggytalents.talent;

import java.util.List;
import java.util.Random;

import doggytalents.api.inferface.Talent;
import doggytalents.entity.EntityDog;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.init.SoundEvents;

/**
 * @author ProPercivalalb
 */
public class CreeperSweeperTalent extends Talent {

	private Random rand = new Random();
	
	@Override
	public void onClassCreation(EntityDog dog) {
		dog.objects.put("creeper_timer", 0);
		dog.objects.put("random_time", 30 + this.rand.nextInt(20));
	}

	@Override
	public void tick(EntityDog dog) {
		int level = dog.TALENTS.getLevel(this);
		
		if(dog.getAttackTarget() == null && dog.isTamed() && level > 0) {
            List<EntityCreeper> list = dog.world.getEntitiesWithinAABB(EntityCreeper.class, dog.getEntityBoundingBox().expand(level * 5, level * 2, level * 5));

            if(!list.isEmpty() && !dog.isSitting() && dog.getHealth() > 1)
            	dog.objects.put("creeper_timer", (int)dog.objects.get("creeper_timer") + 1);
        }
		
		if((int)dog.objects.get("creeper_timer") >= (int)dog.objects.get("random_time")) {
			dog.playSound(SoundEvents.ENTITY_WOLF_GROWL, dog.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
			dog.objects.put("creeper_timer", 0);
			dog.objects.put("random_time", 30 + this.rand.nextInt(40));
		}
		
		
		
		if(dog.getAttackTarget() instanceof EntityCreeper) {
        	EntityCreeper creeper = (EntityCreeper)dog.getAttackTarget();
        	creeper.setCreeperState(-1);
        }
	}
	
	@Override
	public boolean canAttackClass(EntityDog dog, Class entityClass) {
		return EntityCreeper.class == entityClass && dog.TALENTS.getLevel(this) == 5; 
	}
	
	@Override
	public boolean canAttackEntity(EntityDog dog, Entity entity) {
		return entity instanceof EntityCreeper && dog.TALENTS.getLevel(this) == 5;
	}
}
