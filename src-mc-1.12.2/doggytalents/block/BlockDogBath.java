package doggytalents.block;

import doggytalents.ModCreativeTabs;
import doggytalents.entity.EntityDog;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**;
 * @author ProPercivalalb
 */
public class BlockDogBath extends Block {

	protected static final AxisAlignedBB SHAPE = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D);
	protected static final AxisAlignedBB SHAPE_COLLISION = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.25D, 1.0D);
	
	public BlockDogBath() {
		super(Material.IRON);
		this.setHardness(3.0F);
		this.setResistance(5.0F);
		this.setCreativeTab(ModCreativeTabs.GENERAL);
		this.setSoundType(SoundType.METAL);
		this.setHarvestLevel("pickaxe", 0);
	}
	
	@Override
	public void onEntityCollision(World world, BlockPos pos, IBlockState state, Entity entity) {
		if(entity instanceof EntityDog) {
			EntityDog dog = (EntityDog)entity;
			dog.isWet = true;
		}
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
		return blockstate.getBlockFaceShape(world, pos.down(), EnumFacing.UP) == BlockFaceShape.SOLID;
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return SHAPE;
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
		return SHAPE_COLLISION;
	}
	
	@Override
	public boolean isFullCube(IBlockState state) {
	    return false;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
	    return false;
	}
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
	    return EnumBlockRenderType.MODEL;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }
	
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing facing) {
		if(facing == EnumFacing.DOWN) 
			return BlockFaceShape.SOLID;
		return BlockFaceShape.UNDEFINED;
	}
}
