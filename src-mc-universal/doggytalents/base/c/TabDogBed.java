package doggytalents.base.c;

import doggytalents.ModItems;
import doggytalents.api.registry.DogBedRegistry;
import doggytalents.creativetab.CreativeTabDogBed;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * 1.11.2 & 1.12 Code
 */
public class TabDogBed extends CreativeTabDogBed {

	@Override
	@SideOnly(Side.CLIENT)
	public ItemStack getTabIconItem() {
		return new ItemStack(ModItems.TRAINING_TREAT);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public void displayAllRelevantItems(NonNullList<ItemStack> p_78018_1_) {
		for(String beddingId : DogBedRegistry.BEDDINGS.getKeys())
			for(String casingId : DogBedRegistry.CASINGS.getKeys())
				p_78018_1_.add(DogBedRegistry.createItemStack(casingId, beddingId));
    }
}
