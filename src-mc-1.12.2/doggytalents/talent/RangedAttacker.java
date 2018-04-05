package doggytalents.talent;

import doggytalents.api.inferface.ITalent;
import doggytalents.entity.EntityDog;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class RangedAttacker extends ITalent {
	
	@Override
	public boolean interactWithPlayer(EntityDog dog, EntityPlayer player, ItemStack stack) {
		if(stack.isEmpty() && dog.canInteract(player)) {
        	if(dog.talents.getLevel(this) > 0 && !player.isRiding() && !player.onGround && !dog.isIncapacicated()) {
        		if(!dog.world.isRemote) {
        			//TODO RangedAttacker
        		}
        		return true;
        	}
        }
		
		return false; 
	}

	@Override
	public String getKey() {
		return "rangedattacker";
	}

}
