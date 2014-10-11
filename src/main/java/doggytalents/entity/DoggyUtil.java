package doggytalents.entity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import doggytalents.entity.data.EnumTalents;

/**
 * @author ProPercivalalb
 */
public class DoggyUtil {

	public static void loseStack(EntityPlayer player, ItemStack stack) {
     	if(!player.capabilities.isCreativeMode)
     		stack.stackSize--;

        if (stack.stackSize <= 0)
        	player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
	}
	
    public static int foodValue(EntityDTDoggy dog, ItemStack stack) {
    	if(stack == null || stack.getItem() == null)
    		return 0;
    	
    	Item item = stack.getItem();
    	int level = dog.talents.getTalentLevel(EnumTalents.HAPPYEATER);

        if ((item == Items.fish || item == Items.cooked_fished) && level == 5)
        	return 30 + 3 * level;

        if (stack.getItem() == Items.rotten_flesh && level >= 3)
        	return 30 + 3 * dog.talents.getTalentLevel(EnumTalents.HAPPYEATER);

        if (item == Items.porkchop || item == Items.cooked_porkchop || item == Items.beef || item == Items.cooked_beef || item == Items.chicken || item == Items.cooked_chicken)
        	return 40 + 4 * level;
        else if(stack.getItem() != Items.rotten_flesh && item instanceof ItemFood) {
            ItemFood itemfood = (ItemFood)item;

            if (itemfood.isWolfsFavoriteMeat())
            	return 40 + 4 * level;
        }

        return 0;
    }
}
