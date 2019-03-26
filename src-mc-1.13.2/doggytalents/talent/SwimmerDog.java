package doggytalents.talent;

import doggytalents.api.inferface.ITalent;
import doggytalents.entity.EntityDog;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;

/**
 * @author ProPercivalalb
 */
public class SwimmerDog extends ITalent {
	
	@Override
	public void onLivingUpdate(EntityDog dog) {
		if(dog.TALENTS.getLevel(this) == 5 && dog.getControllingPassenger() instanceof EntityPlayer) {
			EntityPlayer rider = (EntityPlayer)dog.getControllingPassenger();
			if(rider.isInWater())
				rider.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 80, 1, true, false));
		}
	}
	
	@Override
	public String getKey() {
		return "swimmerdog";
	}

	@Override
	public boolean shouldDismountInWater(EntityDog dog, Entity rider) {
		return !(dog.TALENTS.getLevel(this) > 0);
	}
}
