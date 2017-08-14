package doggytalents.item;

import net.minecraft.item.ItemStack;

public class ItemFancyCollar extends ItemDT {

	public static final int NO_COLLAR = 2;
	
	public ItemFancyCollar() {
		super();
		this.setHasSubtypes(true);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack par1ItemStack) {
		return this.getUnlocalizedName() + par1ItemStack.getItemDamage();
	}
}
