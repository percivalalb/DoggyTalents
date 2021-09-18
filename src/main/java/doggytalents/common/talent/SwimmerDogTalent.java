package doggytalents.common.talent;

import doggytalents.api.inferface.AbstractDogEntity;
import doggytalents.api.registry.Talent;
import doggytalents.api.registry.TalentInstance;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;

public class SwimmerDogTalent extends TalentInstance {

    //TODO add ai goal to follow owner through water

    public SwimmerDogTalent(Talent talentIn, int levelIn) {
        super(talentIn, levelIn);
    }

    @Override
    public void livingTick(AbstractDogEntity dogIn) {
        if (this.level() >= 5 && dogIn.isVehicle() && dogIn.canBeControlledByRider()) {
            // canBeSteered checks entity is LivingEntity
            LivingEntity rider = (LivingEntity) dogIn.getControllingPassenger();
            if (rider.isInWater()) {
                rider.addEffect(new EffectInstance(Effects.NIGHT_VISION, 80, 1, true, false));
            }
        }
    }

    @Override
    public ActionResultType canBeRiddenInWater(AbstractDogEntity dogIn, Entity rider) {
        return this.level() >= 5 ? ActionResultType.SUCCESS : ActionResultType.PASS;
    }

    @Override
    public ActionResultType canBreatheUnderwater(AbstractDogEntity dogIn) {
        return this.level() >= 5 ? ActionResultType.SUCCESS : ActionResultType.PASS;
    }

    @Override
    public ActionResult<Integer> decreaseAirSupply(AbstractDogEntity dogIn, int air) {
        if (this.level() > 0 && dogIn.getRandom().nextInt(this.level() + 1) > 0) {
            return ActionResult.success(air);
        }

        return ActionResult.pass(air);
    }

    @Override
    public ActionResult<Integer> determineNextAir(AbstractDogEntity dogIn, int currentAir) {
        if (this.level() > 0) {
            return ActionResult.pass(currentAir + this.level());
        }

        return ActionResult.pass(currentAir);
    }
}
