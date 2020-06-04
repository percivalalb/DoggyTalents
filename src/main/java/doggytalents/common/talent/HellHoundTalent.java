package doggytalents.common.talent;

import doggytalents.api.inferface.Talent;
import doggytalents.common.entity.DogEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;

public class HellHoundTalent extends Talent {

    @Override
    public ActionResult<Integer> setFire(DogEntity dogIn, int second) {
        int level = dogIn.getLevel(this);
        return ActionResult.resultSuccess(level > 0 ? second / level : second);
    }

    @Override
    public ActionResultType isImmuneToFire(DogEntity dogIn) {
        int level = dogIn.getLevel(this);
        return level >= 5 ? ActionResultType.SUCCESS : ActionResultType.PASS;
    }

    @Override
    public ActionResultType isInvulnerableTo(DogEntity dogIn, DamageSource source) {
        if (source.isFireDamage()) {
            int level = dogIn.getLevel(this);
            return level >= 5 ? ActionResultType.SUCCESS : ActionResultType.PASS;
        }

        return ActionResultType.PASS;
    }
}
