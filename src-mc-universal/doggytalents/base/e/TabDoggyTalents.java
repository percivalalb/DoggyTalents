package doggytalents.base.e;

import doggytalents.ModItems;
import doggytalents.creativetab.CreativeTabDoggyTalents;
import net.minecraft.item.ItemStack;

/**
 * 1.11.2 & 1.12 Code
 */
public class TabDoggyTalents extends CreativeTabDoggyTalents {
	
	@Override
	public ItemStack getTabIconItem() {
		return new ItemStack(ModItems.TRAINING_TREAT);
	}
}
