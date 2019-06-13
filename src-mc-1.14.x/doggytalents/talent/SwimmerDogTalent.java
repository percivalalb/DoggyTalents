package doggytalents.talent;

import doggytalents.api.inferface.Talent;
import doggytalents.entity.EntityDog;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;

/**
 * @author ProPercivalalb
 */
public class SwimmerDogTalent extends Talent {
	
	@Override
	public void livingTick(EntityDog dog) {
		if(dog.TALENTS.getLevel(this) == 5 && dog.getControllingPassenger() instanceof PlayerEntity) {
			PlayerEntity rider = (PlayerEntity)dog.getControllingPassenger();
			if(rider.isInWater())
				rider.addPotionEffect(new EffectInstance(Effects.NIGHT_VISION, 80, 1, true, false));
		}
	}

	@Override
	public boolean shouldDismountInWater(EntityDog dog, Entity rider) {
		return !(dog.TALENTS.getLevel(this) > 0);
	}
	
	@Override
	public boolean canBreatheUnderwater(EntityDog dog) {
		return dog.TALENTS.getLevel(this) == 5;
	}
}
