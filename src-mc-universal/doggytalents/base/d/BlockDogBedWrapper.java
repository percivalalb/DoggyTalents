package doggytalents.base.d;

import java.util.List;
import java.util.Random;

import doggytalents.api.registry.DogBedRegistry;
import doggytalents.block.BlockDogBed;
import doggytalents.tileentity.TileEntityDogBed;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * 1.12 Code
 */
public class BlockDogBedWrapper extends BlockDogBed {

	@Override
	public boolean canBlockStay(World world, BlockPos pos) {
		IBlockState blockstate = world.getBlockState(pos.down());
		return blockstate.getBlockFaceShape(world, pos.down(), EnumFacing.UP) == BlockFaceShape.SOLID;
	}
	
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing facing) {
		return BlockFaceShape.UNDEFINED;
	}
	
	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess worldIn, BlockPos pos, IBlockState state, int fortune) {
		ItemStack ret = this.drops.get();
		this.drops.remove();
		if(ret != null)
			drops.add(ret);
		else {
			TileEntity tileentity = worldIn.getTileEntity(pos);

			if(tileentity instanceof TileEntityDogBed) {
				TileEntityDogBed dogBed = (TileEntityDogBed)tileentity;
				drops.add(DogBedRegistry.createItemStack(dogBed.getCasingId(), dogBed.getBeddingId()));
			}
		}
	}
}
