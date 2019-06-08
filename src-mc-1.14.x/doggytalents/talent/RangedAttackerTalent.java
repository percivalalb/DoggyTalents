package doggytalents.talent;

import doggytalents.api.inferface.Talent;
import doggytalents.entity.EntityDog;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;

public class RangedAttackerTalent extends Talent {
	
	@Override
	public void onClassCreation(EntityDog dog) {
		dog.objects.put("rangedcooldown", 0);
		dog.objects.put("rangedattacktype", "");
	}
	
	@Override
	public void writeAdditional(EntityDog dog, CompoundNBT tagCompound) {
		int rangedCooldown = (Integer)dog.objects.get("rangedcooldown");
		tagCompound.putInt("rangedcooldown", rangedCooldown);
		
		String rangedAttackType = tagCompound.getString("rangedattacktype");
		tagCompound.putString("rangedattacktype", rangedAttackType);
	}
	
	@Override
	public void readAdditional(EntityDog dog, CompoundNBT tagCompound) {
		dog.objects.put("rangedcooldown", tagCompound.getInt("rangedcooldown"));
	}
	
	@Override
	public ActionResult<ItemStack> onInteract(EntityDog dog, PlayerEntity player, ItemStack stack) {
		if(stack.isEmpty() && dog.canInteract(player)) {
        	if(dog.TALENTS.getLevel(this) > 0 && player.getRidingEntity() == null  && !player.onGround && !dog.isIncapacicated()) {
        		if(!dog.world.isRemote) {
        			//TODO RangedAttacker
        		}
        		return ActionResult.newResult(ActionResultType.SUCCESS, stack);
        	}
        }
		
		return ActionResult.newResult(ActionResultType.PASS, stack);
	}
}
