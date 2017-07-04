package doggytalents.creativetab;

import doggytalents.ModItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
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
	public Item getTabIconItem() {
		return ModItems.TRAINING_TREAT;
	}
}
