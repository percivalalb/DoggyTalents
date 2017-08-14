package doggytalents.base.f;

import java.util.List;

import doggytalents.item.ItemFancyCollar;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class ItemFancyCollarWrapper extends ItemFancyCollar {

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems) {
		for(int i = 0; i < this.NO_COLLAR; i++)
			subItems.add(new ItemStack(this, 1, i));
	}
}
