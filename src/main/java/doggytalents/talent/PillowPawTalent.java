package doggytalents.talent;

import doggytalents.api.inferface.IDogEntity;
import doggytalents.api.inferface.Talent;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;

/**
 * @author ProPercivalalb
 */
public class PillowPawTalent extends Talent {

    @Override
    public void livingTick(IDogEntity dog) {
        if(dog.getTalentFeature().getLevel(this) == 5)
            if(dog.getMotion().getY() < -0.12F && !dog.isInWater())
                dog.setMotion(dog.getMotion().getX(), -0.12F, dog.getMotion().getZ());
    }

    @Override
    public boolean isImmuneToFalls(IDogEntity dog) {
        return dog.getTalentFeature().getLevel(this) == 5;
    }

    @Override
    public ActionResult<Integer> fallProtection(IDogEntity dog) {
        return ActionResult.newResult(ActionResultType.SUCCESS, dog.getTalentFeature().getLevel(this) * 3);
    }
}
