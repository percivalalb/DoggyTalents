package doggytalents.base;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public interface IRegistryMethods {

	public void registerBlock(Object registry, Block block);		
	public void registerItem(Object registry, Item item);
	
	public void registerEntity(Class<? extends Entity> entityClass, ResourceLocation entityName, int id, Object mod, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates);
		
}
