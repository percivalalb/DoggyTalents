package doggytalents.base.b;

import java.util.List;

import doggytalents.item.ItemFancyCollar;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemFancyCollarWrapper extends ItemFancyCollar {

	@Override
	public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
		for(int i = 0; i < 2; i++)
			subItems.add(new ItemStack(itemIn, 1, i));
	}
}
