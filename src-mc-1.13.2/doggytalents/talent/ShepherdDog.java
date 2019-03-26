package doggytalents.talent;

import doggytalents.api.inferface.ITalent;
import doggytalents.entity.EntityDog;
import doggytalents.entity.ai.ModeFeature.EnumMode;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityAnimal;

/**
 * @author ProPercivalalb
 */
public class ShepherdDog extends ITalent {

	@Override
	public void onLivingUpdate(EntityDog dog) {
		int level = dog.TALENTS.getLevel(this);
		int masterOrder = dog.masterOrder();
		
		if(level > 0) {
			if(masterOrder == 3 && dog.getAttackTarget() != null) {
	        	double d0 = dog.getDistance(dog.getAttackTarget());
	            if(d0 <= 4.0D) {
	            	//TODO dog.mountTo(dog.getAttackTarget());
	            	dog.setAttackTarget(null);
	            }
	        }
			
			if(dog.isTamed() && masterOrder != 3 && dog.getControllingPassenger() instanceof EntityAnimal) {
				dog.removePassengers();
			}
		}
	}
	
	@Override
	public int onHungerTick(EntityDog dog, int totalInTick) { 
		//TODO if(dog.getControllingPassenger() != null && !dog.isControllingPassengerPlayer())
		//	totalInTick += 5 - dog.TALENTS.getLevel(this);
		return totalInTick;
	}
	
	@Override
	public boolean shouldDamageMob(EntityDog dog, Entity entity) { 
		if(dog.MODE.isMode(EnumMode.DOCILE) && dog.masterOrder() == 3)
			return false;
		return true;
	}
	
	@Override
	public String getKey() {
		return "shepherddog";
	}

}
