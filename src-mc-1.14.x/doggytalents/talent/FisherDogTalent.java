package doggytalents.talent;

import doggytalents.api.inferface.Talent;
import doggytalents.entity.EntityDog;

/**
 * @author ProPercivalalb
 */
public class FisherDogTalent extends Talent {
    
    @Override
    public boolean canBreatheUnderwater(EntityDog dog) { 
        return dog.TALENTS.getLevel(this) == 5;
    }
}
