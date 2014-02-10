package doggytalents.creativetab;

import doggytalents.ModItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

/**
 * @author ProPercivalalb
 */
public class CreativeTabDoggyTalents extends CreativeTabs {

	public CreativeTabDoggyTalents() {
		super("doggytalents");
	}

	@Override
	public ItemStack getIconItemStack() {
	    return new ItemStack(ModItems.trainingTreat);
	}
}
