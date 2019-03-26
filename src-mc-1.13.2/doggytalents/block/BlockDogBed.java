package doggytalents.block;

import java.util.List;

import javax.annotation.Nullable;

import doggytalents.api.registry.DogBedRegistry;
import doggytalents.lib.BlockNames;
import doggytalents.tileentity.PropertyString;
import doggytalents.tileentity.TileEntityDogBed;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.IProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IExtendedState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * @author ProPercivalalb
 */
public class BlockDogBed extends BlockContainer {
	
	protected static final VoxelShape SHAPE = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 9.0D, 16.0D);
	
	public static final DirectionProperty FACING = BlockHorizontal.HORIZONTAL_FACING;
	public static final PropertyString CASING = PropertyString.create("casing");
	public static final PropertyString BEDDING = PropertyString.create("bedding");
	
	public BlockDogBed() {
		super(Block.Properties.create(Material.WOOD).hardnessAndResistance(3.0F, 5.0F).sound(SoundType.WOOD));
		this.setRegistryName(BlockNames.DOG_BED);
		this.setDefaultState(this.stateContainer.getBaseState().with(FACING, EnumFacing.NORTH));
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		if(stack.hasTag() && stack.getTag().hasKey("doggytalents")) {
			NBTTagCompound tag = stack.getTag().getCompound("doggytalents");
		    
		    String casingId = tag.getString("casingId");
		    String beddingId = tag.getString("beddingId");
		    
		    if(DogBedRegistry.CASINGS.isValidId(casingId))
		    	tooltip.add(new TextComponentTranslation(DogBedRegistry.CASINGS.getLookUpValue(casingId)));
		    else
		    	tooltip.add(new TextComponentTranslation("dogBed.woodError")); // TODO RED
		    	
		    if(DogBedRegistry.BEDDINGS.isValidId(beddingId))
		    	tooltip.add(new TextComponentTranslation(DogBedRegistry.BEDDINGS.getLookUpValue(beddingId)));	
		    else
		    	tooltip.add(new TextComponentTranslation("dogBed.woolError"));  // TODO RED
		}
	}
	
	@Override
	public TileEntity createNewTileEntity(IBlockReader world) {
		return new TileEntityDogBed();
	}
	
	@Override
	public VoxelShape getShape(IBlockState state, IBlockReader worldIn, BlockPos pos) {
		Vec3d vec3d = state.getOffset(worldIn, pos);
		return SHAPE.withOffset(vec3d.x, vec3d.y, vec3d.z);
	}
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
	    return EnumBlockRenderType.MODEL;
	}
	
	@Override
	public IBlockState getStateForPlacement(BlockItemUseContext context) {
		return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
	}
	
	@Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        worldIn.setBlockState(pos, state.with(FACING, placer.getHorizontalFacing().getOpposite()), 2);
	    
        if(stack != null && stack.hasTag() && stack.getTag().hasKey("doggytalents")) {
	    	NBTTagCompound tag = stack.getTag().getCompound("doggytalents");
	    	
	    	String casingId = tag.getString("casingId");
	    	if(DogBedRegistry.CASINGS.isValidId(casingId)) 
	    		((TileEntityDogBed)worldIn.getTileEntity(pos)).setCasingId(casingId);
	    	
	    	String beddingId = tag.getString("beddingId");
	    	if(DogBedRegistry.BEDDINGS.isValidId(beddingId))
	    		((TileEntityDogBed)worldIn.getTileEntity(pos)).setBeddingId(beddingId);
	    }
	}
	
	//@Override
	//@OnlyIn(Dist.CLIENT)
	//public IBlockState getStateForEntityRender(IBlockState state) {
	//    return this.getDefaultState().with(FACING, EnumFacing.SOUTH);
	//}
	
	//@Override
	//protected BlockStateContainer createBlockState() {
	//	return new ExtendedBlockState(this, new IProperty[] {FACING}, new IUnlistedProperty[] {CASING, BEDDING});
	//}
	
	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, IBlockState> builder) {
		builder.add(FACING);
	}
	
	@Override
    public IBlockState getExtendedState(IBlockState state, IBlockReader world, BlockPos pos) {
        TileEntity te = world.getTileEntity(pos);
        if(te instanceof TileEntityDogBed && state instanceof IExtendedBlockState) {
        	IExtendedBlockState stateExtended = (IExtendedBlockState)state;
        	TileEntityDogBed dogBed = (TileEntityDogBed) te;
            return ((IExtendedState<IBlockState>)stateExtended.withProperty(CASING, dogBed.getCasingId())).withProperty(BEDDING, dogBed.getBeddingId());
        }
        return super.getExtendedState(state, world, pos);
    }
	
	//@OnlyIn(Dist.CLIENT)
    //public int getMixedBrightnessForBlock(IBlockAccess worldIn, BlockPos pos) {
    //    return worldIn.getCombinedLight(pos, 0);
    //}
	
	@Override
	public boolean isFullCube(IBlockState state) {
	    return false;
	}
	
	@Override
	public boolean isSolid(IBlockState state) {
		return false;
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.SOLID;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public BlockFaceShape getBlockFaceShape(IBlockReader worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
		return BlockFaceShape.UNDEFINED;
	}

	//@Override
	//public boolean isOpaqueCube(IBlockState state) {
	//    return false;
	//}
	
	public final ThreadLocal<ItemStack> drops = new ThreadLocal<>();

	@Override
	public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn) {
		TileEntity tileentity = worldIn.getTileEntity(pos);

		if(tileentity instanceof TileEntityDogBed) {
			TileEntityDogBed dogBed = (TileEntityDogBed)tileentity;
			//if(!playerIn.isCreative())
				//this.drops.set(DogBedRegistry.createItemStack(dogBed.getCasingId(), dogBed.getBeddingId()));
		}
	}
	
	
	/**
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
	**/
	
	@Override
	public IBlockState rotate(IBlockState state, Rotation rot) {
		return state.with(FACING, rot.rotate(state.get(FACING)));
	}

	@Override
	public IBlockState mirror(IBlockState state, Mirror mirrorIn) {
		return state.rotate(mirrorIn.toRotation(state.get(FACING)));
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean hasCustomBreakingProgress(IBlockState state) {
		return true;
	}
	
	@OnlyIn(Dist.CLIENT)
	public boolean addDestroyEffects(World world, BlockPos pos, ParticleManager manager) {
		/**
		IBlockState state = world.getBlockState(pos);
		IBakedModel model = Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(state);
		if(model instanceof IStateParticleModel) {
			state = this.getExtendedState(state.getActualState(world, pos), world, pos);
			TextureAtlasSprite sprite = ((IStateParticleModel)model).getParticleTexture(state);
			if(sprite != null) {
				for(int j = 0; j < 4; ++j) {
					for(int k = 0; k < 4; ++k) {
						for(int l = 0; l < 4; ++l) {
							double d0 = ((double)j + 0.5D) / 4.0D;
							double d1 = ((double)k + 0.5D) / 4.0D;
							double d2 = ((double)l + 0.5D) / 4.0D;
							manager.addEffect(new ParticleCustomDigging(world, (double)pos.getX() + d0, (double)pos.getY() + d1, (double)pos.getZ() + d2, d0 - 0.5D, d1 - 0.5D, d2 - 0.5D, state, pos, sprite));
						}
					}
				}

				return true;
			}
		}
**/
		return false;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean addHitEffects(IBlockState state, World world, RayTraceResult target, ParticleManager manager) {
		/**
		IBakedModel model = Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(state);
		if(model instanceof IStateParticleModel) {
			BlockPos pos = target.getBlockPos();
			EnumFacing side = target.sideHit;

			state = this.getExtendedState(state.getActualState(world, pos), world, pos);
			TextureAtlasSprite sprite = ((IStateParticleModel) model).getParticleTexture(state);
			if(sprite != null) {
				int x = pos.getX();
				int y = pos.getY();
				int z = pos.getZ();
				AxisAlignedBB axisalignedbb = state.getBoundingBox(world, pos);
				double d0 = (double)x + RANDOM.nextDouble() * (axisalignedbb.maxX - axisalignedbb.minX - 0.20000000298023224D) + 0.10000000149011612D + axisalignedbb.minX;
				double d1 = (double)y + RANDOM.nextDouble() * (axisalignedbb.maxY - axisalignedbb.minY - 0.20000000298023224D) + 0.10000000149011612D + axisalignedbb.minY;
				double d2 = (double)z + RANDOM.nextDouble() * (axisalignedbb.maxZ - axisalignedbb.minZ - 0.20000000298023224D) + 0.10000000149011612D + axisalignedbb.minZ;

				if(side == EnumFacing.DOWN)
					d1 = (double)y + axisalignedbb.minY - 0.10000000149011612D;
				
				if(side == EnumFacing.UP)
					d1 = (double)y + axisalignedbb.maxY + 0.10000000149011612D;

				if(side == EnumFacing.NORTH)
					d2 = (double)z + axisalignedbb.minZ - 0.10000000149011612D;

				if(side == EnumFacing.SOUTH)
					d2 = (double)z + axisalignedbb.maxZ + 0.10000000149011612D;

				if(side == EnumFacing.WEST)
					d0 = (double)x + axisalignedbb.minX - 0.10000000149011612D;

				if(side == EnumFacing.EAST)
					d0 = (double)x + axisalignedbb.maxX + 0.10000000149011612D;

				Particle particle = new ParticleCustomDigging(world, d0, d1, d2, 0.0D, 0.0D, 0.0D, state, pos, sprite).multiplyVelocity(0.2F).multipleParticleScaleBy(0.6F);
				manager.addEffect(particle);

				return true;
			}
		}
**/
		return false;
	}

	@Override
	public boolean addLandingEffects(IBlockState state, WorldServer world, BlockPos pos, IBlockState stateAgain, EntityLivingBase entity, int numberOfParticles) {
		/**
		CustomParticleMessage packet = new CustomParticleMessage(world, pos, entity.posX, entity.posY, entity.posZ, numberOfParticles, 0.15f);
		PacketDispatcher.sendToDimension(packet, world.provider.getDimension());**/
		return true;
	}
}
