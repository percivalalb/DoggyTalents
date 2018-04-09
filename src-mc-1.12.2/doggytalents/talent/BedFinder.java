package doggytalents.talent;

import doggytalents.api.inferface.ITalent;
import doggytalents.entity.EntityDog;
import net.minecraft.entity.player.EntityPlayer;

/**
 * @author ProPercivalalb
 */
public class BedFinder extends ITalent {
	
	@Override
	public void onLivingUpdate(EntityDog dog) {
		int level = dog.talents.getLevel(this);
		
		if(dog.getEntityWeAreRiding() instanceof EntityPlayer && dog.isServer()) {
			
			EntityPlayer player = (EntityPlayer)dog.getEntityWeAreRiding();
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
