package doggytalents.common.talent;

import doggytalents.DoggyTalents2;
import doggytalents.api.inferface.AbstractDogEntity;
import doggytalents.api.registry.Talent;
import net.minecraft.util.ActionResult;

public class QuickHealerTalent extends Talent {

    @Override
    public ActionResult<Integer> healingTick(AbstractDogEntity dogIn, int healingTick) {
        int level = dogIn.getLevel(this);

        if (level > 0) {
            if (dogIn.isSitting() && level >= 5) {
                if (dogIn.getIdleTime() > 100) {
                    healingTick *= 15;
                } else {
                    healingTick *= 10;
                }
            } else {
                healingTick *= level;
            }
            DoggyTalents2.LOGGER.debug("Healing tick increase: {} {}", healingTick, dogIn.getIdleTime());
            return ActionResult.resultSuccess(healingTick);
        }


        return ActionResult.resultPass(healingTick);
    }
}
