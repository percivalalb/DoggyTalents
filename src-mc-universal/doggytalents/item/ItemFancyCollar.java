package doggytalents.item;

import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;

public class ItemFancyCollar extends ItemDT {

	public static final int NO_COLLAR = 3;
	
	public ItemFancyCollar() {
		super();
		this.setHasSubtypes(true);
	}
	
	@Override
	public EnumRarity getRarity(ItemStack stack) {
        return stack.getItemDamage() == 0 ? EnumRarity.EPIC : super.getRarity(stack);
    }
	
	@Override
	public String getUnlocalizedName(ItemStack par1ItemStack) {
		return this.getUnlocalizedName() + par1ItemStack.getItemDamage();
	}
}
