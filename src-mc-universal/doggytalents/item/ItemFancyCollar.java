package doggytalents.item;

import net.minecraft.item.ItemStack;

public class ItemFancyCollar extends ItemDT {

	public final int NO_COLLAR = 2;
	
	@Override
	public String getUnlocalizedName(ItemStack par1ItemStack) {
		return this.getUnlocalizedName() + par1ItemStack.getItemDamage();
	}
}
