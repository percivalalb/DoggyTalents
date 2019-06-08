package doggytalents.talent;

import doggytalents.api.inferface.Talent;
import doggytalents.entity.EntityDog;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;

/**
 * @author ProPercivalalb
 */
public class PillowPawTalent extends Talent {

	@Override
	public void livingTick(EntityDog dog) {
		if(dog.TALENTS.getLevel(this) == 5)
			if(dog.getMotion().getY() < -0.12F && !dog.isInWater())
				dog.setMotion(dog.getMotion().getX(), -0.12F, dog.getMotion().getZ());
	}
	
	@Override
	public boolean isImmuneToFalls(EntityDog dog) { 
		return dog.TALENTS.getLevel(this) == 5; 
	}
	
	@Override
	public ActionResult<Integer> fallProtection(EntityDog dog) { 
		return ActionResult.newResult(ActionResultType.SUCCESS, dog.TALENTS.getLevel(this) * 3);
	}
}
