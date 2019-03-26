package doggytalents.talent;

import doggytalents.api.inferface.ITalent;
import doggytalents.entity.EntityDog;
import doggytalents.lib.Reference;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class RangedAttacker extends ITalent {
	
	@Override
	public void onClassCreation(EntityDog dog) {
		dog.objects.put("rangedcooldown", 0);
		dog.objects.put("rangedattacktype", "");
	}
	
	@Override
	public void writeToNBT(EntityDog dog, NBTTagCompound tagCompound) {
		int rangedCooldown = (Integer)dog.objects.get("rangedcooldown");
		tagCompound.setInt("rangedcooldown", rangedCooldown);
		
		String rangedAttackType = tagCompound.getString("rangedattacktype");
		tagCompound.setString("rangedattacktype", rangedAttackType);
	}
	
	@Override
	public void readFromNBT(EntityDog dog, NBTTagCompound tagCompound) {
		dog.objects.put("rangedcooldown", tagCompound.getInt("rangedcooldown"));
	}
	
	@Override
	public boolean interactWithPlayer(EntityDog dog, EntityPlayer player, ItemStack stack) {
		if(stack.isEmpty() && dog.canInteract(player)) {
        	if(dog.TALENTS.getLevel(this) > 0 && player.getRidingEntity() == null  && !player.onGround && !dog.isIncapacicated()) {
        		if(dog.isServer()) {
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
