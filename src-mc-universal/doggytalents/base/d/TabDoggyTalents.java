package doggytalents.base.d;

import java.util.List;

import doggytalents.ModItems;
import doggytalents.api.registry.DogBedRegistry;
import doggytalents.creativetab.CreativeTabDogBed;
import doggytalents.creativetab.CreativeTabDoggyTalents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * 1.11.2 & 1.12 Code
 */
public class TabDoggyTalents extends CreativeTabDoggyTalents {
	
	@Override
	public ItemStack getTabIconItem() {
		return new ItemStack(ModItems.TRAINING_TREAT);
	}
}
