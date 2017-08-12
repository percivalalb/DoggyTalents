package doggytalents.base.c;

import java.util.List;

import doggytalents.api.registry.DogBedRegistry;
import doggytalents.block.BlockDogBath;
import doggytalents.block.BlockDogBed;
import doggytalents.block.BlockFoodBowl;
import doggytalents.tileentity.TileEntityDogBed;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockWrapper {

	public static class BlockDogBathWrapper extends BlockDogBath {
		
		@Override
		public boolean canBlockStay(World world, BlockPos pos) {
			IBlockState blockstate = world.getBlockState(pos.down());
			return blockstate.getBlock().isSideSolid(blockstate, world, pos.down(), EnumFacing.UP);
		}
	}
	
	public static class BlockDogBedWrapper extends BlockDogBed {

		@Override
		public boolean canBlockStay(World world, BlockPos pos) {
			IBlockState blockstate = world.getBlockState(pos.down());
			return blockstate.getBlock().isSideSolid(blockstate, world, pos.down(), EnumFacing.UP);
		}
		
		@Override
		public List<ItemStack> getDrops(IBlockAccess worldIn, BlockPos pos, IBlockState state, int fortune) {
			List<ItemStack> ret = new java.util.ArrayList<ItemStack>();
			
			ItemStack cache = this.drops.get();
			this.drops.remove();
			if(cache != null)
				ret.add(cache);
			else {
				TileEntity tileentity = worldIn.getTileEntity(pos);

				if(tileentity instanceof TileEntityDogBed) {
					TileEntityDogBed dogBed = (TileEntityDogBed)tileentity;
					ret.add(DogBedRegistry.createItemStack(dogBed.getCasingId(), dogBed.getBeddingId()));
				}
			}
			
			return ret;
		}
	}

	public static class BlockFoodBowlWrapper extends BlockFoodBowl {

		@Override
		public boolean canBlockStay(World world, BlockPos pos) {
			IBlockState blockstate = world.getBlockState(pos.down());
			return blockstate.getBlock().isSideSolid(blockstate, world, pos.down(), EnumFacing.UP);
		}
		
		@Override
		public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack stack, EnumFacing facing, float hitX, float hitY, float hitZ) {
			return this.onBlockActivatedGENERAL(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
		}
	}
}


