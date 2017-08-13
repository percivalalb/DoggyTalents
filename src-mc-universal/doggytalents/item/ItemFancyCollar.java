package doggytalents.item;

import net.minecraft.item.ItemStack;

public class ItemFancyCollar extends ItemDT {

	@Override
	public String getUnlocalizedName(ItemStack par1ItemStack) {
		return this.getUnlocalizedName() + par1ItemStack.getItemDamage();
	}
}
