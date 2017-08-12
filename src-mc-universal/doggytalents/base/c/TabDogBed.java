package doggytalents.base.c;

import java.util.List;

import doggytalents.ModItems;
import doggytalents.api.registry.DogBedRegistry;
import doggytalents.creativetab.CreativeTabDogBed;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * 1.9.4 & 1.10.2 Code
 */
public class TabDogBed extends CreativeTabDogBed {
	
	@Override
	public Item getTabIconItem() {
		return ModItems.TRAINING_TREAT;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public void displayAllRelevantItems(List<ItemStack> p_78018_1_) {
		for(String beddingId : DogBedRegistry.BEDDINGS.getKeys())
			for(String casingId : DogBedRegistry.CASINGS.getKeys())
				p_78018_1_.add(DogBedRegistry.createItemStack(casingId, beddingId));
    }
}
