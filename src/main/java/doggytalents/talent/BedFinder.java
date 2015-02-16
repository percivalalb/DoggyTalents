package doggytalents.talent;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import doggytalents.api.inferface.ITalent;
import doggytalents.entity.EntityDog;

/**
 * @author ProPercivalalb
 */
public class BedFinder extends ITalent {
	
	@Override
	public void onLivingUpdate(EntityDog dog) {
		int level = dog.talents.getLevel(this);
		
		if (dog.ridingEntity instanceof EntityPlayer && !dog.worldObj.isRemote) {
			EntityPlayer player = (EntityPlayer)dog.ridingEntity;
			BlockPos bedLocation = player.getBedLocation(player.dimension);
			if(player != null && bedLocation != null) {
	            dog.coords.setBedX(bedLocation.getX());
	            dog.coords.setBedY(bedLocation.getY());
	            dog.coords.setBedZ(bedLocation.getZ());
	        }
	    }
	}
	
	@Override
	public String getKey() {
		return "bedfinder";
	}

}
