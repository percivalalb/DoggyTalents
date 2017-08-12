package doggytalents.base.b;

import doggytalents.base.IGeneralMethods;
import doggytalents.entity.EntityDog;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class GeneralMethods implements IGeneralMethods {

	@Override
	public boolean isTeleportFriendlyBlock(Entity entity, World world, int xBase, int zBase, int y, int xAdd, int zAdd) {
		return false;
	}

	@Override
	public float getBrightness(EntityDog dog, float partialTicks) {
		return 0;
	}

	@Override
	public int getColour(EnumDyeColor dyeColor) {
		return 0;
	}

	@Override
	public float[] getRGB(EnumDyeColor dyeColor) {
		return null;
	}

	@Override
	public void registerEntity(Class<? extends Entity> entityClass, ResourceLocation entityName, int id, Object mod, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates) {
		
	}
	
	@Override
	public String translateToLocal(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String translateToLocalFormatted(String key, Object... args) {
		// TODO Auto-generated method stub
		return null;
	}

}
