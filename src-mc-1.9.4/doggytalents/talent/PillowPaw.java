package doggytalents.talent;

import doggytalents.api.inferface.ITalent;
import doggytalents.entity.EntityDog;

/**
 * @author ProPercivalalb
 */
public class PillowPaw extends ITalent {

	@Override
	public void onLivingUpdate(EntityDog dog) {
		if(dog.talents.getLevel(this) == 5)
			if(dog.motionY < -0.12F && !dog.isInWater())
				dog.motionY = -0.12F;
	}
	
	@Override
	public boolean isImmuneToFalls(EntityDog dog) { 
		return dog.talents.getLevel(this) == 5; 
	}
	
	@Override
	public int fallProtection(EntityDog dog) { 
		return dog.talents.getLevel(this) * 3;
	}
	
	@Override
	public String getKey() {
		return "pillowpaw";
	}

}
