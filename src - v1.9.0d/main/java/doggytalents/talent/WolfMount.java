package doggytalents.talent;

import net.minecraft.entity.player.EntityPlayer;
import doggytalents.api.inferface.ITalent;
import doggytalents.entity.EntityDog;

/**
 * @author ProPercivalalb
 */
public class WolfMount extends ITalent {

	@Override
	public boolean interactWithPlayer(EntityDog dog, EntityPlayer player) { 
		if(player.getHeldItem() == null) {
        	if(dog.talents.getLevel(this) > 0 && player.ridingEntity == null && !player.onGround) {
        		dog.getSitAI().setSitting(false);
        		dog.setSitting(false);
        		player.mountEntity(dog);
        		return true;
        	}
        }
		
		return false; 
	}
	
	@Override
	public int onHungerTick(EntityDog dog, int totalInTick) { 
		if(dog.riddenByEntity instanceof EntityPlayer)
			if(dog.talents.getLevel(this) >= 5)
				totalInTick += 1;
			else
				totalInTick += 3;
		return totalInTick;
	}
	
	@Override
	public String getKey() {
		return "wolfmount";
	}
}
