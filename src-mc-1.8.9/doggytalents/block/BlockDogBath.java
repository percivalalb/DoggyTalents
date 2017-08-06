package doggytalents.block;

import doggytalents.DoggyTalents;
import doggytalents.entity.EntityDog;
import doggytalents.tileentity.TileEntityDogBath;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**;
 * @author ProPercivalalb
 */
public class BlockDogBath extends BlockContainer {

	protected static final AxisAlignedBB AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D);
	
	public BlockDogBath() {
		super(Material.iron);
		this.setHardness(3.0F);
		this.setResistance(5.0F);
		this.setStepSound(Block.soundTypeMetal);
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
		this.setCreativeTab(DoggyTalents.CREATIVE_TAB);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityDogBath();
	}	
	
	@Override
	public int getRenderType() {
	    return 3;
	}
	
	@Override
	public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {
		if(entity instanceof EntityDog) {
			EntityDog dog = (EntityDog)entity;
			dog.isShaking = true;
		}
	}
	
	@Override
	public boolean isOpaqueCube() {
        return false;
    }
	
	@Override
	public boolean isFullCube() {
	    return false;
	}
	
	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
	    return super.canPlaceBlockAt(worldIn, pos) ? this.canBlockStay(worldIn, pos) : false;
	}

	@Override
	public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
		if(!this.canBlockStay((World)world, pos)) {
			this.dropBlockAsItem((World)world, pos, world.getBlockState(pos), 0);
			((World)world).setBlockToAir(pos);
		}
	}
	
	public boolean canBlockStay(World world, BlockPos pos) {
		IBlockState blockstate = world.getBlockState(pos.down());
		return blockstate.getBlock().isSideSolid(world, pos.down(), EnumFacing.UP);
	}
}
