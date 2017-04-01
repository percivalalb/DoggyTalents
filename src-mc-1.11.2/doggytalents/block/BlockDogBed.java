package doggytalents.block;

import java.util.ArrayList;
import java.util.List;

import doggytalents.api.DoggyTalentsAPI;
import doggytalents.api.registry.DogBedRegistry;
import doggytalents.tileentity.TileEntityDogBed;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
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
		this.setHardness(2.0F);
		this.setResistance(5.0F);
		this.setSoundType(SoundType.WOOD);
		this.setCreativeTab(DoggyTalentsAPI.CREATIVE_TAB);
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
	
	/**
	@Override
	@SideOnly(Side.CLIENT)
	public boolean addHitEffects(IBlockState state, World world, RayTraceResult target, ParticleManager manager) {
		BlockPos pos = target.getBlockPos();
		IBlockState iblockstate = world.getBlockState(pos);
        Block block = iblockstate.getBlock();
        EnumFacing side = target.sideHit;

        int i = pos.getX();
        int j = pos.getY();
        int k = pos.getZ();
        float f = 0.1F;
        AxisAlignedBB axisalignedbb = iblockstate.getBoundingBox(world, pos);
        double d0 = (double)i + world.rand.nextDouble() * (axisalignedbb.maxX - axisalignedbb.minX - 0.20000000298023224D) + 0.10000000149011612D + axisalignedbb.minX;
        double d1 = (double)j + world.rand.nextDouble() * (axisalignedbb.maxY - axisalignedbb.minY - 0.20000000298023224D) + 0.10000000149011612D + axisalignedbb.minY;
        double d2 = (double)k + world.rand.nextDouble() * (axisalignedbb.maxZ - axisalignedbb.minZ - 0.20000000298023224D) + 0.10000000149011612D + axisalignedbb.minZ;

        if (side == EnumFacing.DOWN)
        {
            d1 = (double)j + axisalignedbb.minY - 0.10000000149011612D;
        }

        if (side == EnumFacing.UP)
        {
            d1 = (double)j + axisalignedbb.maxY + 0.10000000149011612D;
        }

        if (side == EnumFacing.NORTH)
        {
            d2 = (double)k + axisalignedbb.minZ - 0.10000000149011612D;
        }

        if (side == EnumFacing.SOUTH)
        {
            d2 = (double)k + axisalignedbb.maxZ + 0.10000000149011612D;
        }

        if (side == EnumFacing.WEST)
        {
            d0 = (double)i + axisalignedbb.minX - 0.10000000149011612D;
        }

        if (side == EnumFacing.EAST)
        {
            d0 = (double)i + axisalignedbb.maxX + 0.10000000149011612D;
        }

        this.addEffect((new ParticleDigging(world, d0, d1, d2, 0.0D, 0.0D, 0.0D, iblockstate)).setBlockPos(pos).multiplyVelocity(0.2F).multipleParticleScaleBy(0.6F));
           
		return true;
	}
	**/
	@SideOnly(Side.CLIENT)
    public int getMixedBrightnessForBlock(IBlockAccess worldIn, BlockPos pos)
    {
        Block block = worldIn.getBlockState(pos).getBlock();
        int i = worldIn.getCombinedLight(pos, 0);

            return i;
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
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        List<ItemStack> ret = new ArrayList<ItemStack>();
        
        TileEntity target = world.getTileEntity(pos);
		if(!(target instanceof TileEntityDogBed))
			return ret;
		TileEntityDogBed dogBed = (TileEntityDogBed)target;
		
		ItemStack stack = DogBedRegistry.createItemStack(dogBed.getCasingId(), dogBed.getBeddingId());
		ret.add(stack);
		
        return ret;
    }
	
	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        TileEntity tile = world.getTileEntity(pos);
		
		if(!(tile instanceof TileEntityDogBed))
			return null;
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
		return blockstate.getBlock().isSideSolid(blockstate, world, pos.down(), EnumFacing.UP);
	}
	
	@Override
    @SideOnly(Side.CLIENT)
	public void getSubBlocks(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> stackList) {
		for(String casingId : DogBedRegistry.CASINGS.getKeys())
			stackList.add(DogBedRegistry.createItemStack(casingId, Block.REGISTRY.getNameForObject(Blocks.WOOL) + ".0"));
    }
}
