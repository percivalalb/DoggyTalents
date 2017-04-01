package doggytalents.talent;

import doggytalents.api.inferface.ITalent;
import doggytalents.entity.EntityDog;
import net.minecraft.entity.player.EntityPlayer;

/**
 * @author ProPercivalalb
 */
public class DoggyDash extends ITalent {

	@Override
	public double addToMoveSpeed(EntityDog dog) {
		double speed = 0.0D;
		int level = dog.talents.getLevel(this);

		if((!(dog.getAttackTarget() instanceof EntityDog) && !(dog.getAttackTarget() instanceof EntityPlayer)) || dog.getControllingPassenger() instanceof EntityPlayer)
			if(level == 5)
				speed += 0.04D;
		
		speed += 0.03D * level;
		
		return speed;
	}
	
	@Override
	public String getKey() {
		return "doggydash";
	}

}
