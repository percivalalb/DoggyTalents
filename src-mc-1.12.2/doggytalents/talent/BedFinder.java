package doggytalents.talent;

import doggytalents.api.inferface.ITalent;
import doggytalents.entity.EntityDog;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

/**
 * @author ProPercivalalb
 */
public class BedFinder extends ITalent {
	
	@Override
	public void onLivingUpdate(EntityDog dog) {
		int level = dog.talents.getLevel(this);
		
		Entity entityRidden = dog.getRidingEntity();
		
		if(entityRidden instanceof EntityPlayer && dog.isServer()) {
			
			EntityPlayer player = (EntityPlayer)entityRidden;
			if(player != null && player.getBedLocation(player.dimension) != null) {
	            dog.coords.setBedPos(player.getBedLocation(player.dimension).getX(), player.getBedLocation(player.dimension).getY(), player.getBedLocation(player.dimension).getZ());
	        }
		}
	}
	
	@Override
	public String getKey() {
		return "bedfinder";
	}

}
