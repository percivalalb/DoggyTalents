package doggytalents.base.a;

import doggytalents.block.BlockFoodBowl;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * 1.9.4 Code
 */
public class BlockFoodBowlWrapper extends BlockFoodBowl {

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
