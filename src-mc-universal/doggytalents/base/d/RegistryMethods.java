package doggytalents.base.d;

import doggytalents.base.IRegistryMethods;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistry;

public class RegistryMethods implements IRegistryMethods {

	@Override
	public void registerBlock(Object registry, Block block) {
		((IForgeRegistry<Block>)registry).register(block);
	}
	
	@Override
	public void registerBlock(Object registry, Block block, Class<? extends ItemBlock> itemBlock) {
		((IForgeRegistry<Block>)registry).register(block);
	}
	
	@Override
	public void registerItem(Object registry, Item item) {
		((IForgeRegistry<Item>)registry).register(item);
	}
	
	@Override
	public void registerTileEntity(Class<? extends TileEntity> tileEntityClass, String key, String... alternatives) {
		GameRegistry.registerTileEntityWithAlternatives(tileEntityClass, key, alternatives);
	}

	@Override
	public void registerEntity(Class<? extends Entity> entityClass, ResourceLocation entityName, int id, Object mod, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates) {
		EntityRegistry.registerModEntity(entityClass, entityName.toString(), id, mod, trackingRange, updateFrequency, sendsVelocityUpdates);
	}
}
