package doggytalents.base.d;

import doggytalents.base.IGeneralMethods;
import doggytalents.entity.EntityDog;
import net.minecraft.block.Block;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.registries.IForgeRegistry;

/**
 * 1.12 Code
 */
public class GeneralMethods implements IGeneralMethods {

	@Override
	public boolean isTeleportFriendlyBlock(Entity entity, World world, int xBase, int zBase, int y, int xAdd, int zAdd) {
		BlockPos blockpos = new BlockPos(xBase + xAdd, y - 1, zBase + zAdd);
		IBlockState iblockstate = world.getBlockState(blockpos);
		return iblockstate.getBlockFaceShape(world, blockpos, EnumFacing.DOWN) == BlockFaceShape.SOLID && iblockstate.canEntitySpawn(entity) && world.isAirBlock(blockpos.up()) && world.isAirBlock(blockpos.up(2));
	}
	
	@Override
	public float getBrightness(EntityDog dog, float partialTicks) {
		return dog.getBrightness();
	}
	
	@Override
	public int getColour(EnumDyeColor colour) {
		return colour.getColorValue();
	}
	
	@Override
	public float[] getRGB(EnumDyeColor dyeColor) {
		return dyeColor.getColorComponentValues();
	}
	
	@Override
	public void registerEntity(Class<? extends Entity> entityClass, ResourceLocation entityName, int id, Object mod, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates) {
		EntityRegistry.registerModEntity(entityName, entityClass, entityName.toString(), id, mod, trackingRange, updateFrequency, sendsVelocityUpdates);
	}
	
	@Override
	public void registerBlock(Object registry, Block block) {
		((IForgeRegistry<Block>)registry).register(block);
	}
	
	@Override
	public void registerItem(Object registry, Item item) {
		((IForgeRegistry<Item>)registry).register(item);
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
