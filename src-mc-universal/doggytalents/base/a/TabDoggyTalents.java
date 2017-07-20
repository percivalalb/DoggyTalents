package doggytalents.base.a;

import doggytalents.ModItems;
import doggytalents.creativetab.CreativeTabDoggyTalents;
import net.minecraft.item.Item;

/**
 * 1.9.4 & 1.10.2 Code
 */
public class TabDoggyTalents extends CreativeTabDoggyTalents {
	
	@Override
	public Item getTabIconItem() {
		return ModItems.TRAINING_TREAT;
	}
}
