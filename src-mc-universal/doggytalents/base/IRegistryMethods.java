package doggytalents.base;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

public interface IRegistryMethods {

	public void registerBlock(Object registry, Block block);		
	public void registerItem(Object registry, Item item);
		
}
