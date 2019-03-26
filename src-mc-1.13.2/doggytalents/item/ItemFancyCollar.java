package doggytalents.item;

import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemFancyCollar extends Item {
	
	public enum Type {
		CREATIVE,
		SPOTTED,
		MULTI_COLOURED
	}
	
	public Type type;
	
	public ItemFancyCollar(Type type, Properties properties) {
		super(properties);
		this.type = type;
	}
	
	@Override
	public EnumRarity getRarity(ItemStack stack) {
        return type == Type.CREATIVE ? EnumRarity.EPIC : super.getRarity(stack);
    }
	
	@Override
	public String getTranslationKey(ItemStack par1ItemStack) {
		return this.getTranslationKey();
	}
}
