package doggytalents.base.b;

import doggytalents.base.IGeneralMethods;
import doggytalents.entity.EntityDog;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class GeneralMethods implements IGeneralMethods {

	@Override
	public boolean isTeleportFriendlyBlock(Entity entity, World world, int xBase, int zBase, int y, int xAdd, int zAdd) {
		return World.doesBlockHaveSolidTopSurface(world, new BlockPos(xBase + xAdd, y - 1, zBase + zAdd)) && isEmptyBlock(world, new BlockPos(xBase + xAdd, y, zBase + zAdd)) && isEmptyBlock(world, new BlockPos(xBase + xAdd, y + 1, zBase + zAdd));
	}

	private boolean isEmptyBlock(World world, BlockPos pos) {
		IBlockState iblockstate = world.getBlockState(pos);
        Block block = iblockstate.getBlock();
        return block == Blocks.air ? true : !block.isFullCube();
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
}
