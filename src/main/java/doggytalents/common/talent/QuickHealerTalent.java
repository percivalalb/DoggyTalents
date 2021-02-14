package doggytalents.common.talent;

import doggytalents.DoggyTalents2;
import doggytalents.api.inferface.AbstractDogEntity;
import doggytalents.api.registry.Talent;
import doggytalents.api.registry.TalentInstance;
import net.minecraft.util.ActionResult;

public class QuickHealerTalent extends TalentInstance {

    public QuickHealerTalent(Talent talentIn, int levelIn) {
        super(talentIn, levelIn);
    }

    @Override
    public ActionResult<Integer> healingTick(AbstractDogEntity dogIn, int healingTick) {
        if (this.level() > 0) {
            if (dogIn.isEntitySleeping() && this.level() >= 5) {
                if (dogIn.getIdleTime() > 100) {
                    healingTick *= 15;
                } else {
                    healingTick *= 10;
                }
            } else {
                healingTick *= this.level();
            }

            return ActionResult.resultSuccess(healingTick);
        }


        return ActionResult.resultPass(healingTick);
    }
}
