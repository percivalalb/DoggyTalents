package doggytalents.talent;

import doggytalents.api.inferface.ITalent;
import doggytalents.entity.EntityDog;

/**
 * @author ProPercivalalb
 */
public class FisherDog extends ITalent {
	
	@Override
	public boolean canBreatheUnderwater(EntityDog dog) { 
		return dog.TALENTS.getLevel(this) == 5;
	}
	
	@Override
	public String getKey() {
		return "fisherdog";
	}

}
