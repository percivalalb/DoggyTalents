package doggytalents.base.f;

import doggytalents.api.registry.DogBedRegistry;
import doggytalents.block.BlockDogBath;
import doggytalents.block.BlockDogBed;
import doggytalents.block.BlockFoodBowl;
import doggytalents.tileentity.TileEntityDogBed;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * 1.12 Code
 */
public class BlockWrapper {

	public static class BlockDogBathWrapper extends BlockDogBath {

		@Override
		public boolean canBlockStay(World world, BlockPos pos) {
			IBlockState blockstate = world.getBlockState(pos.down());
			return blockstate.getBlockFaceShape(world, pos.down(), EnumFacing.UP) == BlockFaceShape.SOLID;
		}
		
		@Override
		public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing facing) {
			return BlockFaceShape.UNDEFINED;
		}
	}

	public static class BlockDogBedWrapper extends BlockDogBed {

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
	
	public static class BlockFoodBowlWrapper extends BlockFoodBowl {

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
		public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
			return this.onBlockActivatedGENERAL(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
		}
	}
}
