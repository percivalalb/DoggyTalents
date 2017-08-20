package doggytalents.base.b;

import doggytalents.base.IRegistryMethods;
import doggytalents.base.ObjectLib;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class RegistryMethods implements IRegistryMethods {

	@Override
	public void registerBlock(Object registry, Block block) {
		GameRegistry.registerBlock(block);
	}
	
	@Override
	public void registerBlock(Object registry, Block block, Class<? extends ItemBlock> itemBlock) {
		GameRegistry.registerBlock(block, itemBlock);
	}

	@Override
	public void registerItem(Object registry, Item item) {
		GameRegistry.registerItem(item);
	}

	@Override
	public void registerTileEntity(Class<? extends TileEntity> tileEntityClass, String key, String... alternatives) {
		GameRegistry.registerTileEntityWithAlternatives(tileEntityClass, key, alternatives);
	}
	
	@Override
	public void registerEntity(Class<? extends Entity> entityClass, ResourceLocation entityName, int id, Object mod, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates) {
		EntityRegistry.registerModEntity(entityClass, entityName.getResourcePath(), id, mod, trackingRange, updateFrequency, sendsVelocityUpdates);
		EntityList.stringToClassMapping.put("doggytalents." + entityName, entityClass);
	}
}
