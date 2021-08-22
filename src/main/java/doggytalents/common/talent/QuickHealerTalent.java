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
            if (dogIn.isInSittingPose() && this.level() >= 5) {
                if (dogIn.getNoActionTime() > 100) {
                    healingTick *= 15;
                } else {
                    healingTick *= 10;
                }
            } else {
                healingTick *= this.level();
            }

            return ActionResult.success(healingTick);
        }


        return ActionResult.pass(healingTick);
    }
}
