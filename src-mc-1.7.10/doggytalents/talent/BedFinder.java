package doggytalents.talent;

import doggytalents.api.inferface.ITalent;
import doggytalents.entity.EntityDog;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChunkCoordinates;

/**
 * @author ProPercivalalb
 */
public class BedFinder extends ITalent {
	
	@Override
	public void onLivingUpdate(EntityDog dog) {
		int level = dog.talents.getLevel(this);
		
		if(dog.ridingEntity instanceof EntityPlayer && !dog.worldObj.isRemote) {
			
			EntityPlayer player = (EntityPlayer)dog.ridingEntity;
			ChunkCoordinates bedLocation = player.getBedLocation(player.dimension);
			if(player != null && bedLocation != null) {
	            dog.coords.setBedX(bedLocation.posX);
	            dog.coords.setBedY(bedLocation.posY);
	            dog.coords.setBedZ(bedLocation.posZ);
	        }
	    }
	}
	
	@Override
	public String getKey() {
		return "bedfinder";
	}

}
