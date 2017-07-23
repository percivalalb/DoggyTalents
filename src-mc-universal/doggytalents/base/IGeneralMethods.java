package doggytalents.base;

import doggytalents.entity.EntityDog;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public interface IGeneralMethods {
	
	public boolean isTeleportFriendlyBlock(Entity entity, World world, int xBase, int zBase, int y, int xAdd, int zAdd);
	
	public float getBrightness(EntityDog dog, float partialTicks);
	
	public int getColour(EnumDyeColor dyeColor);
	public float[] getRGB(EnumDyeColor dyeColor);
	
	public void registerCraftingRecipes();
	
	public void registerEntity(Class<? extends Entity> entityClass, ResourceLocation entityName, int id, Object mod, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates);
	
	public void registerBlock(Object registry, Block block);
	public void registerItem(Object registry, Item item);
}
