package doggytalents.talent;

import doggytalents.api.inferface.IDogEntity;
import doggytalents.api.inferface.Talent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumActionResult;

/**
 * @author ProPercivalalb
 */
public class SwimmerDogTalent extends Talent {

    @Override
    public void livingTick(IDogEntity dog) {
        if(dog.getTalentFeature().getLevel(this) == 5 && dog.getControllingPassenger() instanceof EntityPlayer) {
            EntityPlayer rider = (EntityPlayer)dog.getControllingPassenger();
            if(rider.isInWater())
                rider.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 80, 1, true, false));
        }
    }

    @Override
    public EnumActionResult canBeRiddenInWater(IDogEntity dog, Entity rider) {
        return dog.getTalentFeature().getLevel(this) < 5 ? EnumActionResult.PASS : EnumActionResult.SUCCESS;
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
