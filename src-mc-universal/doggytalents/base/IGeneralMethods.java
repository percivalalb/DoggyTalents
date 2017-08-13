package doggytalents.base;

import doggytalents.entity.EntityDog;
import net.minecraft.entity.Entity;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.world.World;

public interface IGeneralMethods {
	
	public boolean isTeleportFriendlyBlock(Entity entity, World world, int xBase, int zBase, int y, int xAdd, int zAdd);
	
	public float getBrightness(EntityDog dog, float partialTicks);
	
	public int getColour(EnumDyeColor dyeColor);
	public float[] getRGB(EnumDyeColor dyeColor);
}
