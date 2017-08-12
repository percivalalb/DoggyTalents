package doggytalents.base.f;

import doggytalents.base.IRegistryMethods;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

public class RegistryMethods implements IRegistryMethods {

	@Override
	public void registerBlock(Object registry, Block block) {
		((IForgeRegistry<Block>)registry).register(block);
	}
	
	@Override
	public void registerItem(Object registry, Item item) {
		((IForgeRegistry<Item>)registry).register(item);
	}

}
