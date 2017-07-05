package doggytalents.creativetab;

import java.util.List;
import java.util.Random;

import doggytalents.ModItems;
import doggytalents.api.registry.DogBedRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ProPercivalalb
 */
public class CreativeTabDogBed extends CreativeTabs {

	private Random random = new Random();
	
    @SideOnly(Side.CLIENT)
    private ItemStack iconItemStack;
	
	public CreativeTabDogBed() {
		super("doggytalents.dogbed");
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public ItemStack getTabIconItem() {
		return new ItemStack(ModItems.TRAINING_TREAT);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public ItemStack getIconItemStack() {
		if(this.iconItemStack == null)
			this.iconItemStack = DogBedRegistry.createItemStack(pickRandomString(DogBedRegistry.CASINGS.getKeys()), pickRandomString(DogBedRegistry.BEDDINGS.getKeys()));

		return this.iconItemStack;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public void displayAllRelevantItems(NonNullList<ItemStack> p_78018_1_) {
		for(String beddingId : DogBedRegistry.BEDDINGS.getKeys())
			for(String casingId : DogBedRegistry.CASINGS.getKeys())
				p_78018_1_.add(DogBedRegistry.createItemStack(casingId, beddingId));
    }
	
	public String pickRandomString(List<String> strs) {
		return strs.get(random.nextInt(strs.size()));
	}
}
