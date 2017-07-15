package doggytalents.block;

import java.util.Random;

import doggytalents.api.registry.DogBedRegistry;
import doggytalents.tileentity.TileEntityDogBed;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ProPercivalalb
 */
public class BlockDogBed extends BlockContainer {

	public static final PropertyDirection FACING = BlockHorizontal.FACING;
	public static final PropertyString CASING = PropertyString.create("casing");
	public static final PropertyString BEDDING = PropertyString.create("bedding");
	protected static final AxisAlignedBB AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.6D, 1.0D);
	
	public BlockDogBed() {
		super(Material.WOOD);
		this.setHardness(3.0F);
		this.setResistance(5.0F);
		this.setSoundType(SoundType.WOOD);
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
	}
	
	@Override
    @SideOnly(Side.CLIENT)
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return AABB;
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityDogBed();
	}
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
	    return EnumBlockRenderType.MODEL;
	}
	
	@Override
	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
	}
	
	@Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        worldIn.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()), 2);
	    
        if(stack != null && stack.hasTagCompound() && stack.getTagCompound().hasKey("doggytalents")) {
	    	NBTTagCompound tag = stack.getTagCompound().getCompoundTag("doggytalents");
	    	
	    	String casingId = tag.getString("casingId");
	    	if(DogBedRegistry.CASINGS.isValidId(casingId)) 
	    		((TileEntityDogBed)worldIn.getTileEntity(pos)).setCasingId(casingId);
	    	
	    	String beddingId = tag.getString("beddingId");
	    	if(DogBedRegistry.BEDDINGS.isValidId(beddingId))
	    		((TileEntityDogBed)worldIn.getTileEntity(pos)).setBeddingId(beddingId);
	    }
	}
	
	@SideOnly(Side.CLIENT)
	public IBlockState getStateForEntityRender(IBlockState state) {
	    return this.getDefaultState().withProperty(FACING, EnumFacing.SOUTH);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		EnumFacing enumfacing = EnumFacing.getFront(meta);

	    if (enumfacing.getAxis() == EnumFacing.Axis.Y)
	        enumfacing = EnumFacing.NORTH;

	    return this.getDefaultState().withProperty(FACING, enumfacing);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
	    return ((EnumFacing)state.getValue(FACING)).getIndex();
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new ExtendedBlockState(this, new IProperty[] {FACING}, new IUnlistedProperty[] {CASING, BEDDING});
	}
	
	@Override
    public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntity te = world.getTileEntity(pos);
        if(te instanceof TileEntityDogBed && state instanceof IExtendedBlockState) {
        	IExtendedBlockState stateExtended = (IExtendedBlockState)state;
        	TileEntityDogBed dogBed = (TileEntityDogBed) te;
            return stateExtended.withProperty(CASING, dogBed.getCasingId()).withProperty(BEDDING, dogBed.getBeddingId());
        }
        return super.getExtendedState(state, world, pos);
    }
	
	@SideOnly(Side.CLIENT)
    public int getMixedBrightnessForBlock(IBlockAccess worldIn, BlockPos pos) {
        return worldIn.getCombinedLight(pos, 0);
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
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		TileEntity tileentity = worldIn.getTileEntity(pos);

		if(tileentity instanceof TileEntityDogBed) {
			TileEntityDogBed dogBed = (TileEntityDogBed)tileentity;
			this.spawnAsEntity(worldIn, pos, DogBedRegistry.createItemStack(dogBed.getCasingId(), dogBed.getBeddingId()));
		}

		super.breakBlock(worldIn, pos, state);
	}
	
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Items.AIR;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }
	
	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        TileEntity tile = world.getTileEntity(pos);
		
		if(!(tile instanceof TileEntityDogBed))
			return ItemStack.EMPTY;
		TileEntityDogBed dogBed = (TileEntityDogBed)tile;
		
		return DogBedRegistry.createItemStack(dogBed.getCasingId(), dogBed.getBeddingId());
	}
	
	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
	    return super.canPlaceBlockAt(worldIn, pos) ? this.canBlockStay(worldIn, pos) : false;
	}

	@Override
	public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
		if(!this.canBlockStay((World)world, pos)) {
		    TileEntity tile = world.getTileEntity(pos);
			if(tile instanceof TileEntityDogBed) {
					
				TileEntityDogBed dogBed = (TileEntityDogBed)tile;
				
		        this.spawnAsEntity((World)world, pos, DogBedRegistry.createItemStack(dogBed.getCasingId(), dogBed.getBeddingId()));
		        ((World) world).setBlockToAir(pos);
			}
		}
	}

	public boolean canBlockStay(World world, BlockPos pos) {
		IBlockState blockstate = world.getBlockState(pos.down());
		return blockstate.getBlockFaceShape(world, pos.down(), EnumFacing.UP) == BlockFaceShape.SOLID;
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> tab) {
		for(String casingId : DogBedRegistry.CASINGS.getKeys())
			tab.add(DogBedRegistry.createItemStack(casingId, Block.REGISTRY.getNameForObject(Blocks.WOOL) + ".0"));
    }
	
	@Override
	public IBlockState withRotation(IBlockState state, Rotation rot) {
		return state.withProperty(FACING, rot.rotate((EnumFacing)state.getValue(FACING)));
	}

	@Override
	public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
		return state.withRotation(mirrorIn.toRotation((EnumFacing)state.getValue(FACING)));
	}
	
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing facing) {
		return BlockFaceShape.UNDEFINED;
	}
}
