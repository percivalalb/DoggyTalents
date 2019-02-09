package doggytalents.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class ItemFancyCollar extends ItemDT {

	public static final int NO_COLLAR = 3;
	
	public ItemFancyCollar() {
		super();
		this.setHasSubtypes(true);
	}
	
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems) {
		if(this.isInCreativeTab(tab))
			for(int i = 0; i < this.NO_COLLAR; i++)
				subItems.add(new ItemStack(this, 1, i));
	}
	
	@Override
	public EnumRarity getRarity(ItemStack stack) {
        return stack.getItemDamage() == 0 ? EnumRarity.EPIC : super.getRarity(stack);
    }
	
	@Override
	public String getTranslationKey(ItemStack par1ItemStack) {
		return this.getTranslationKey() + par1ItemStack.getItemDamage();
	}
}
