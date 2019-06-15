package doggytalents.talent;

import doggytalents.api.inferface.Talent;
import doggytalents.entity.EntityDog;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;

/**
 * @author ProPercivalalb
 */
public class BedFinderTalent extends Talent {
	
	@Override
	public void livingTick(EntityDog dog) {
		int level = dog.TALENTS.getLevel(this);
		
		Entity entityRidden = dog.getRidingEntity();
		
		if(entityRidden instanceof EntityPlayer && !dog.world.isRemote) {
			
			EntityPlayer player = (EntityPlayer)entityRidden;
			if(player != null && player.getBedLocation(player.dimension) != null) {
	            dog.COORDS.setBedPos(player.getBedLocation(player.dimension));
	        }
		}
	}
	
	
	@Override
	public ActionResult<ItemStack> onInteract(EntityDog dog, EntityPlayer player, ItemStack stack) {
		int level = dog.TALENTS.getLevel(this);
		if(level > 0 && stack != null && stack.getItem() == Items.BONE && dog.canInteract(player)) {
			dog.startRiding(player);
			if(!dog.world.isRemote) {
				if(!dog.isSitting()) {
					dog.getAISit().setSitting(true);
				}
			}
			return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
        }
		return ActionResult.newResult(EnumActionResult.PASS, stack);
	}
}