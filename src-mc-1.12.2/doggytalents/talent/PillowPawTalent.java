package doggytalents.talent;

import doggytalents.api.inferface.Talent;
import doggytalents.entity.EntityDog;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;

/**
 * @author ProPercivalalb
 */
public class PillowPawTalent extends Talent {

    @Override
    public void livingTick(EntityDog dog) {
        if(dog.TALENTS.getLevel(this) == 5)
            if(dog.motionY < -0.12F && !dog.isInWater())
                dog.motionY = -0.12F;
    }
    
    @Override
    public boolean isImmuneToFalls(EntityDog dog) { 
        return dog.TALENTS.getLevel(this) == 5; 
    }
    
    @Override
    public ActionResult<Integer> fallProtection(EntityDog dog) { 
        return ActionResult.newResult(EnumActionResult.SUCCESS, dog.TALENTS.getLevel(this) * 3);
    }
}
