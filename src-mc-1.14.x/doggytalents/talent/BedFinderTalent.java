package doggytalents.talent;

import doggytalents.api.inferface.Talent;
import doggytalents.entity.EntityDog;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;

/**
 * @author ProPercivalalb
 */
public class BedFinderTalent extends Talent {
	
	@Override
	public void livingTick(EntityDog dog) {
		int level = dog.TALENTS.getLevel(this);
		
		Entity entityRidden = dog.getRidingEntity();
		
		if(entityRidden instanceof PlayerEntity && !dog.world.isRemote) {
			
			PlayerEntity player = (PlayerEntity)entityRidden;
			if(player != null && player.getBedLocation(player.dimension) != null) {
	            dog.COORDS.setBedPos(player.getBedLocation(player.dimension));
	        }
		}
	}
	
	
	@Override
	public ActionResult<ItemStack> onInteract(EntityDog dog, PlayerEntity player, ItemStack stack) {
		int level = dog.TALENTS.getLevel(this);
		if(level > 0 && stack.getItem() == Items.BONE && dog.canInteract(player)) {
			dog.startRiding(player);
			if(!dog.world.isRemote) {
				if(!dog.isSitting()) {
					dog.getAISit().setSitting(true);
				}
			}
			return ActionResult.newResult(ActionResultType.SUCCESS, stack);
        }
		return ActionResult.newResult(ActionResultType.PASS, stack);
	}
}