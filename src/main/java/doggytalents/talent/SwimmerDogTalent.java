package doggytalents.talent;

import doggytalents.api.inferface.IDogEntity;
import doggytalents.api.inferface.Talent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ActionResultType;

/**
 * @author ProPercivalalb
 */
public class SwimmerDogTalent extends Talent {

    @Override
    public void livingTick(IDogEntity dog) {
        if(dog.getTalentFeature().getLevel(this) == 5 && dog.getControllingPassenger() instanceof PlayerEntity) {
            PlayerEntity rider = (PlayerEntity)dog.getControllingPassenger();
            if(rider.isInWater())
                rider.addPotionEffect(new EffectInstance(Effects.NIGHT_VISION, 80, 1, true, false));
        }
    }

    @Override
    public ActionResultType canBeRiddenInWater(IDogEntity dog, Entity rider) {
        return dog.getTalentFeature().getLevel(this) < 5 ? ActionResultType.PASS : ActionResultType.SUCCESS;
    }

    @Override
    public boolean canBreatheUnderwater(IDogEntity dog) {
        return dog.getTalentFeature().getLevel(this) == 5;
    }

    @Override
    public boolean shouldDecreaseAir(IDogEntity dogIn, int air) {
        int level = dogIn.getTalentFeature().getLevel(this);
        return level > 0 && dogIn.getRNG().nextInt(level + 1) > 0 ? false : true;
    }
}
