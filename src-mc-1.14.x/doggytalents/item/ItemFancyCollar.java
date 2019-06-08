package doggytalents.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;

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
	public Rarity getRarity(ItemStack stack) {
        return type == Type.CREATIVE ? Rarity.EPIC : super.getRarity(stack);
    }
}
