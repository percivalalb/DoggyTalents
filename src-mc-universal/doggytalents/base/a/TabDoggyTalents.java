package doggytalents.base.a;

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
 * 1.9.4 & 1.10.2 Code
 */
public class TabDoggyTalents extends CreativeTabDoggyTalents {
	
	@Override
	public Item getTabIconItem() {
		return ModItems.TRAINING_TREAT;
	}
}
