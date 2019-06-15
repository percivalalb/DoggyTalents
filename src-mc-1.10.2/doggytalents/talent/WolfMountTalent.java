package doggytalents.talent;

import doggytalents.api.inferface.Talent;
import doggytalents.entity.EntityDog;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * @author ProPercivalalb
 */
public class WolfMountTalent extends Talent {

	@Override
	public ActionResult<ItemStack> onInteract(EntityDog dog, EntityPlayer player, ItemStack stack) { 
		if(stack.isEmpty() && dog.canInteract(player)) {
        	if(dog.TALENTS.getLevel(this) > 0 && player.getRidingEntity() == null && !player.onGround && !dog.isIncapacicated()) {
        		if(!dog.world.isRemote) {
        			dog.getAISit().setSitting(false);
        			dog.mountTo(player);
        		}
        		return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
        	}
        }
		
		return ActionResult.newResult(EnumActionResult.PASS, stack);
	}
	
	@Override
	public void livingTick(EntityDog dog) {
		if(dog.isBeingRidden() && (dog.getDogHunger() <= 0 || dog.isIncapacicated())) {
			dog.getControllingPassenger().sendMessage(new TextComponentTranslation("talent.doggytalents.wolf_mount.exhausted", dog.getName()));
			
			dog.removePassengers();
		}
	}
	
	@Override
	public int onHungerTick(EntityDog dog, int totalInTick) { 
		if(dog.getControllingPassenger() instanceof EntityPlayer) {
			totalInTick += dog.TALENTS.getLevel(this) < 5 ? 3 : 1;
		}
		
		return totalInTick;
	}
	
	@Override
	public ActionResult<Integer> fallProtection(EntityDog dog) { 
		if(dog.TALENTS.getLevel(this) == 5)
			return ActionResult.newResult(EnumActionResult.SUCCESS, 1);
		
		return ActionResult.newResult(EnumActionResult.PASS, 0);
	}
	
	@Override
	public boolean attackEntityFrom(EntityDog dog, DamageSource damageSource, float damage) {
		Entity entity = damageSource.getTrueSource();
		return dog.isBeingRidden() && entity != null && dog.isRidingOrBeingRiddenBy(entity) ? false : true; 
	}
}
