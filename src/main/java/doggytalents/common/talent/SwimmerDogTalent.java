package doggytalents.common.talent;

import doggytalents.api.registry.Talent;
import doggytalents.common.entity.DogEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;

public class SwimmerDogTalent extends Talent {

    //TODO add ai goal to follow owner through water

    @Override
    public void livingTick(DogEntity dogIn) {
        int level = dogIn.getLevel(this);

        if (level >= 5 && dogIn.isBeingRidden() && dogIn.canBeSteered()) {
            // canBeSteered checks entity is LivingEntity
            LivingEntity rider = (LivingEntity) dogIn.getControllingPassenger();
            if (rider.isInWater()) {
                rider.addPotionEffect(new EffectInstance(Effects.NIGHT_VISION, 80, 1, true, false));
            }
        }
    }

    @Override
    public ActionResultType canBeRiddenInWater(DogEntity dogIn, Entity rider) {
        return dogIn.getLevel(this) >= 5 ? ActionResultType.SUCCESS : ActionResultType.PASS;
    }

    @Override
    public ActionResultType canBreatheUnderwater(DogEntity dogIn) {
        return dogIn.getLevel(this) >= 5 ? ActionResultType.SUCCESS : ActionResultType.PASS;
    }

    @Override
    public ActionResult<Integer> decreaseAirSupply(DogEntity dogIn, int air) {
        int level = dogIn.getLevel(this);

        if (level > 0 && dogIn.getRNG().nextInt(level + 1) > 0) {
            return ActionResult.resultSuccess(air);
        }

        return ActionResult.resultPass(air);
    }

    @Override
    public ActionResult<Integer> determineNextAir(DogEntity dogIn, int currentAir) {
        int level = dogIn.getLevel(this);

        if (level > 0) {
            return ActionResult.resultPass(currentAir + level);
        }

        return ActionResult.resultPass(currentAir);
    }
}
