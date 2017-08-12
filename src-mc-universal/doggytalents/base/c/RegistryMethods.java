package doggytalents.base.c;

import doggytalents.base.IRegistryMethods;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class RegistryMethods implements IRegistryMethods {

	@Override
	public void registerBlock(Object registry, Block block) {
		GameRegistry.register(block);
	}
	
	@Override
	public void registerItem(Object registry, Item item) {
		GameRegistry.register(item);
	}

	@Override
	public void registerEntity(Class<? extends Entity> entityClass, ResourceLocation entityName, int id, Object mod, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates) {
		EntityRegistry.registerModEntity(entityClass, entityName.toString(), id, mod, trackingRange, updateFrequency, sendsVelocityUpdates);
	}
}
