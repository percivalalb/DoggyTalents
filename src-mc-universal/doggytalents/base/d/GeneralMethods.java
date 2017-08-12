package doggytalents.base.d;

import doggytalents.base.IGeneralMethods;
import doggytalents.entity.EntityDog;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.EntityRegistry;

/**
 * 1.10.2 Code
 */
public class GeneralMethods implements IGeneralMethods {

	@Override
	public boolean isTeleportFriendlyBlock(Entity entity, World world, int xBase, int zBase, int y, int xAdd, int zAdd) {
		return world.getBlockState(new BlockPos(xBase + xAdd, y - 1, zBase + zAdd)).isTopSolid() && this.isEmptyBlock(world, new BlockPos(xBase + xAdd, y, zBase + zAdd)) && this.isEmptyBlock(world, new BlockPos(xBase + xAdd, y + 1, zBase + zAdd));
	}

	private boolean isEmptyBlock(World world, BlockPos pos) {
        IBlockState iblockstate = world.getBlockState(pos);
        return iblockstate.getMaterial() == Material.AIR ? true : !iblockstate.isFullCube();
    }
	
	@Override
	public float getBrightness(EntityDog dog, float partialTicks) {
		return dog.getBrightness(partialTicks);
	}
	
	@Override
	public int getColour(EnumDyeColor dyeColor) {
		return dyeColor.getMapColor().colorValue;
	}
	
	@Override
	public float[] getRGB(EnumDyeColor dyeColor) {
		return EntitySheep.getDyeRgb(dyeColor);
	}
	
	@Override
	public void registerEntity(Class<? extends Entity> entityClass, ResourceLocation entityName, int id, Object mod, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates) {
		EntityRegistry.registerModEntity(entityClass, entityName.toString(), id, mod, trackingRange, updateFrequency, sendsVelocityUpdates);
	}
	
	@Override
	public String translateToLocal(String key) {
		return I18n.translateToLocal(key);
	}

	@Override
	public String translateToLocalFormatted(String key, Object... format) {
		return I18n.translateToLocalFormatted(key);
	}
}
