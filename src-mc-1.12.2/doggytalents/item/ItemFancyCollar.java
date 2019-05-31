package doggytalents.item;

import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;

public class ItemFancyCollar extends ItemDT {
	
	public enum Type {
		CREATIVE,
		SPOTTED,
		MULTI_COLOURED
	}
	
	public Type type;
	
	public ItemFancyCollar(Type type) {
		super();
		this.type = type;
	}
	
	@Override
	public EnumRarity getRarity(ItemStack stack) {
        return type == Type.CREATIVE ? EnumRarity.EPIC : super.getRarity(stack);
    }
}
