package doggytalents.talent;

import doggytalents.api.inferface.ITalent;
import doggytalents.entity.EntityDog;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

/**
 * @author ProPercivalalb
 */
public class BedFinder extends ITalent {
	
	@Override
	public void onLivingUpdate(EntityDog dog) {
		int level = dog.TALENTS.getLevel(this);
		
		Entity entityRidden = dog.getRidingEntity();
		
		if(entityRidden instanceof EntityPlayer && dog.isServer()) {
			
			EntityPlayer player = (EntityPlayer)entityRidden;
			if(player != null && player.getBedLocation(player.dimension) != null) {
	            dog.COORDS.setBedPos(player.getBedLocation(player.dimension).getX(), player.getBedLocation(player.dimension).getY(), player.getBedLocation(player.dimension).getZ());
	        }
		}
	}
	
	
	@Override
	public boolean interactWithPlayer(EntityDog dog, EntityPlayer player, ItemStack stack) {
		int level = dog.TALENTS.getLevel(this);
		if(level > 0 && stack.getItem() == Items.BONE && dog.canInteract(player)) {
			dog.startRiding(player);
			if(!dog.world.isRemote) {
				if(!dog.isSitting()) {
					dog.getAISit().setSitting(true);
				}
			}
    		return true;
        }
		return false;
	}



	@Override
	public String getKey() {
		return "bedfinder";
	}

}