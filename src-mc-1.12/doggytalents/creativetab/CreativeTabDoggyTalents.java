package doggytalents.creativetab;

import doggytalents.ModItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ProPercivalalb
 */
public class CreativeTabDoggyTalents extends CreativeTabs {

	public CreativeTabDoggyTalents() {
		super("doggytalents");
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public ItemStack getTabIconItem() {
		return new ItemStack(ModItems.trainingTreat);
	}
}
