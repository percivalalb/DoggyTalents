package doggytalents.talent;

import doggytalents.api.inferface.ITalent;
import doggytalents.entity.EntityDog;
import doggytalents.helper.ChatHelper;
import net.minecraft.entity.player.EntityPlayer;

/**
 * @author ProPercivalalb
 */
public class WolfMount extends ITalent {

	@Override
	public boolean interactWithPlayer(EntityDog dog, EntityPlayer player) { 
		if(player.getHeldItem() == null && dog.canInteract(player)) {
        	if(dog.talents.getLevel(this) > 0 && !player.isRiding() && !player.onGround && !dog.isIncapacicated()) {
        		if(!dog.worldObj.isRemote) {
        			dog.getSitAI().setSitting(false);
            		player.mountEntity(dog);
        		}
        		return true;
        	}
        }
		
		return false; 
	}
	
	@Override
	public void onLivingUpdate(EntityDog dog) {
		if((dog.getDogHunger() <= 0 || dog.isIncapacicated()) && dog.riddenByEntity != null) {
			ChatHelper.getChatComponentTranslation("dogtalent.puppyeyes.wolfmount.outofhunger", dog.getName());
			dog.riddenByEntity.mountEntity(null);
		}	
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
