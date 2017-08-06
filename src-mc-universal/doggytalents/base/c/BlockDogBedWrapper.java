package doggytalents.base.c;

import java.util.List;
import java.util.Random;

import doggytalents.api.registry.DogBedRegistry;
import doggytalents.block.BlockDogBed;
import doggytalents.tileentity.TileEntityDogBed;
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
 * 1.11.2 Code
 */
public class BlockDogBedWrapper extends BlockDogBed {

	@Override
	public boolean canBlockStay(World world, BlockPos pos) {
		IBlockState blockstate = world.getBlockState(pos.down());
		return blockstate.getBlock().isSideSolid(blockstate, world, pos.down(), EnumFacing.UP);
	}
	
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Items.AIR;
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
