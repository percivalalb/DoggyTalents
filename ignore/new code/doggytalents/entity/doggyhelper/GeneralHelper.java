package doggytalents.entity.doggyhelper;

import doggytalents.entity.EntityDTDoggy;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * @author ProPercivalalb
 */
public class GeneralHelper {

	public static boolean hasPlayerGotBoneInHand(EntityPlayer player, EntityDTDoggy dog) {
		 return player == null ? false : hasPlayerGotBoneInHand(dog, player);
	}
	
	private static boolean hasPlayerGotBoneInHand(EntityDTDoggy dog, EntityPlayer player) {
		ItemStack itemstack = player.inventory.getCurrentItem();
	    return itemstack == null ? false : (!dog.isTamed() && itemstack.itemID == Item.bone.itemID ? true : dog.isBreedingItem(itemstack));
	}
	
	public static boolean shouldContinueBegging(EntityPlayer player, EntityDTDoggy dog) {
        return player == null || !player.isEntityAlive() ? false : (dog.getDistanceSqToEntity(player) > (double)(8.0F * 8.0F) ? false : dog.BEG_TIMER > 0 && hasPlayerGotBoneInHand(player, dog));
    }
}
