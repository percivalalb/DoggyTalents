package doggytalents.talent;

import doggytalents.api.inferface.ITalent;
import doggytalents.base.ObjectLib;
import doggytalents.entity.EntityDog;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;

/**
 * @author ProPercivalalb
 */
public class SwimmerDog extends ITalent {
	
	@Override
	public void onLivingUpdate(EntityDog dog) {
		if(dog.talents.getLevel(this) == 5 && dog.getControllingPassenger() instanceof EntityPlayer) {
			EntityPlayer rider = (EntityPlayer)dog.getControllingPassenger();
			if(rider.isInsideOfMaterial(Material.WATER))
				ObjectLib.BRIDGE.addNightVision(rider, 80, 1);
		}
	}
	
	@Override
	public String getKey() {
		return "swimmerdog";
	}

	@Override
	public boolean shouldDismountInWater(EntityDog dog, Entity rider) {
		return !(dog.talents.getLevel(this) > 0);
	}
}
