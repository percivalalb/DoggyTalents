package doggytalents.talent;

import doggytalents.api.inferface.ITalent;
import doggytalents.entity.EntityDog;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * @author ProPercivalalb
 */
public class WolfMount extends ITalent {

	@Override
	public boolean interactWithPlayer(EntityDog dog, EntityPlayer player, ItemStack stack) { 
		if(stack.isEmpty() && dog.canInteract(player)) {
        	if(dog.TALENTS.getLevel(this) > 0 && player.getRidingEntity() == null && !player.onGround && !dog.isIncapacicated()) {
        		if(dog.isServer()) {
        			dog.setSitting(false);
        			dog.mountTo(player);
        		}
        		return true;
        	}
        }
		
		return false; 
	}
	
	@Override
	public void onLivingUpdate(EntityDog dog) {
		if((dog.getDogHunger() <= 0 || dog.isIncapacicated()) && dog.isBeingRidden()) {
			if(dog.getOwner() instanceof EntityPlayer)
				((EntityPlayer)dog.getOwner()).sendMessage(new TextComponentTranslation("dogtalent.puppyeyes.wolfmount.outofhunger", dog.getName()));
			
			dog.removePassengers();
		}	
	}
	
	@Override
	public int onHungerTick(EntityDog dog, int totalInTick) { 
		if(dog.getControllingPassenger() instanceof EntityPlayer)
			if(dog.TALENTS.getLevel(this) >= 5)
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
