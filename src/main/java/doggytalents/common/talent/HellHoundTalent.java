package doggytalents.common.talent;

import doggytalents.api.inferface.AbstractDogEntity;
import doggytalents.api.registry.Talent;
import net.minecraft.entity.Entity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;

public class HellHoundTalent extends Talent {

    @Override
    public ActionResult<Integer> setFire(AbstractDogEntity dogIn, int second) {
        int level = dogIn.getLevel(this);
        return ActionResult.resultSuccess(level > 0 ? second / level : second);
    }

    @Override
    public ActionResultType isImmuneToFire(AbstractDogEntity dogIn) {
        int level = dogIn.getLevel(this);
        return level >= 5 ? ActionResultType.SUCCESS : ActionResultType.PASS;
    }

    @Override
    public ActionResultType isInvulnerableTo(AbstractDogEntity dogIn, DamageSource source) {
        if (source.isFireDamage()) {
            int level = dogIn.getLevel(this);
            return level >= 5 ? ActionResultType.SUCCESS : ActionResultType.PASS;
        }

        return ActionResultType.PASS;
    }

    @Override
    public ActionResultType attackEntityAsMob(AbstractDogEntity dogIn, Entity entity) {
        int level = dogIn.getLevel(this);

        if (level > 0) {
            entity.setFire(level);
            return ActionResultType.PASS;
        }


        return ActionResultType.PASS;
    }
}
