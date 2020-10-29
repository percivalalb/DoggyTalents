package doggytalents.common.talent;

import doggytalents.api.inferface.AbstractDogEntity;
import doggytalents.api.registry.Talent;
import doggytalents.api.registry.TalentInstance;
import net.minecraft.entity.Entity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;

public class HellHoundTalent extends TalentInstance {

    public HellHoundTalent(Talent talentIn, int levelIn) {
        super(talentIn, levelIn);
    }

    @Override
    public ActionResult<Integer> setFire(AbstractDogEntity dogIn, int second) {
        return ActionResult.resultSuccess(this.level() > 0 ? second / this.level() : second);
    }

    @Override
    public ActionResultType isImmuneToFire(AbstractDogEntity dogIn) {
        return this.level() >= 5 ? ActionResultType.SUCCESS : ActionResultType.PASS;
    }

    @Override
    public ActionResultType isInvulnerableTo(AbstractDogEntity dogIn, DamageSource source) {
        if (source.isFireDamage()) {
            return this.level() >= 5 ? ActionResultType.SUCCESS : ActionResultType.PASS;
        }

        return ActionResultType.PASS;
    }

    @Override
    public ActionResultType attackEntityAsMob(AbstractDogEntity dogIn, Entity entity) {
        if (this.level() > 0) {
            entity.setFire(this.level());
            return ActionResultType.PASS;
        }

        return ActionResultType.PASS;
    }
}
