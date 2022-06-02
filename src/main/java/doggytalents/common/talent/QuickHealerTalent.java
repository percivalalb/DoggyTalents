package doggytalents.common.talent;

import doggytalents.api.inferface.AbstractDog;
import doggytalents.api.registry.Talent;
import doggytalents.api.registry.TalentInstance;
import net.minecraft.world.InteractionResultHolder;

public class QuickHealerTalent extends TalentInstance {

    public QuickHealerTalent(Talent talentIn, int levelIn) {
        super(talentIn, levelIn);
    }

    @Override
    public InteractionResultHolder<Integer> healingTick(AbstractDog dogIn, int healingTick) {
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

            return InteractionResultHolder.success(healingTick);
        }


        return InteractionResultHolder.pass(healingTick);
    }
}
