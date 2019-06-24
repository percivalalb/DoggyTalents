package doggytalents.block;

import java.util.List;

import javax.annotation.Nullable;

import doggytalents.DoggyTalentsMod;
import doggytalents.api.inferface.IBedMaterial;
import doggytalents.api.registry.BedMaterial;
import doggytalents.api.registry.DogBedRegistry;
import doggytalents.client.model.block.IStateParticleModel;
import doggytalents.client.renderer.particle.ParticleCustomDigging;
import doggytalents.network.PacketHandler;
import doggytalents.network.client.PacketCustomParticle;
import doggytalents.tileentity.TileEntityDogBed;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ContainerBlock;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.ServerWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.PacketDistributor;

/**
 * @author ProPercivalalb
 */
public class BlockDogBed extends ContainerBlock implements IWaterLoggable {
	
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	protected static final VoxelShape SHAPE = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 9.0D, 16.0D);
	protected static final VoxelShape SHAPE_COLLISION = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D);
	
	public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
	
	public BlockDogBed() {
		super(Block.Properties.create(Material.WOOD).hardnessAndResistance(3.0F, 5.0F).sound(SoundType.WOOD));
		this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH).with(WATERLOGGED, Boolean.valueOf(false)));
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		
		CompoundNBT tag = stack.getChildTag("doggytalents");
		if(tag != null) {
			IBedMaterial casingId = DogBedRegistry.CASINGS.get(tag.getString("casingId"));
	    	IBedMaterial beddingId = DogBedRegistry.BEDDINGS.get(tag.getString("beddingId"));
		    
		    if(casingId.isValid())
		    	tooltip.add(new TranslationTextComponent(casingId.getTranslationKey()));
		    else
		    	tooltip.add(new TranslationTextComponent(casingId.getTranslationKey(), casingId.getSaveId()).applyTextStyle(TextFormatting.RED));
		    	
		    if(beddingId.isValid())
		    	tooltip.add(new TranslationTextComponent(beddingId.getTranslationKey()));	
		    else
		    	tooltip.add(new TranslationTextComponent(beddingId.getTranslationKey(), beddingId.getSaveId()).applyTextStyle(TextFormatting.RED));
		}
	}
	
	@Override
	public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
		for(BedMaterial beddingId : DogBedRegistry.BEDDINGS.getKeys())
			for(BedMaterial casingId : DogBedRegistry.CASINGS.getKeys())
				items.add(DogBedRegistry.createItemStack(casingId, beddingId));
	}
	
	@Override
	public TileEntity createNewTileEntity(IBlockReader world) {
		return new TileEntityDogBed();
	}
	
	@Override
	public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
		return SHAPE;
	}
	
	@Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext selectionContext) {
        return SHAPE_COLLISION;
    }
	
	@Override
	public BlockRenderType getRenderType(BlockState state) {
	    return BlockRenderType.MODEL;
	}
	
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		IFluidState ifluidstate = context.getWorld().getFluidState(context.getPos());
		return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite()).with(WATERLOGGED, Boolean.valueOf(ifluidstate.getFluid() == Fluids.WATER));
	}
	
	@Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
	    state = state.with(FACING, placer.getHorizontalFacing().getOpposite());
        if(stack != null && stack.hasTag() && stack.getTag().contains("doggytalents")) {
	    	CompoundNBT tag = stack.getTag().getCompound("doggytalents");
	    	
	    	TileEntity tile = worldIn.getTileEntity(pos);
	    	
	    	if(tile instanceof TileEntityDogBed) {
	    	    TileEntityDogBed dogBed = (TileEntityDogBed)tile;
	    	    dogBed.setBeddingId(DogBedRegistry.BEDDINGS.get(tag.getString("beddingId")));
	    	    dogBed.setCasingId(DogBedRegistry.CASINGS.get(tag.getString("casingId")));
	    	}
	    }
        worldIn.setBlockState(pos, state, 2);
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(FACING, WATERLOGGED);
	}
	
	@Override
	public boolean isSolid(BlockState state) {
		return false;
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.SOLID;
	}
	
	@Override
	public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player) {
	    
	    TileEntity tile = world.getTileEntity(pos);
        if(tile instanceof TileEntityDogBed) {
            IBedMaterial casing = ((TileEntityDogBed)tile).getCasingId();
            IBedMaterial bedding = ((TileEntityDogBed)tile).getBeddingId();
            return DogBedRegistry.createItemStack(casing, bedding);
        }
	    
        DoggyTalentsMod.LOGGER.debug("Unable to get data from TileEntityDogBed. This should never happen.");
		return ItemStack.EMPTY;
	}
	
	@Override
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
		if(stateIn.get(WATERLOGGED)) {
			worldIn.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn));
		}
		return facing == Direction.DOWN && !stateIn.isValidPosition(worldIn, currentPos) ? Blocks.AIR.getDefaultState() : super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
	}
	
	@Override
	public IFluidState getFluidState(BlockState state) {
		return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
	}
	
	@Override
	public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
		return func_220055_a(worldIn, pos.down(), Direction.UP);
	}
	
	@Override
	public BlockState rotate(BlockState state, Rotation rot) {
		return state.with(FACING, rot.rotate(state.get(FACING)));
	}

	@Override
	public BlockState mirror(BlockState state, Mirror mirrorIn) {
		return state.rotate(mirrorIn.toRotation(state.get(FACING)));
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean hasCustomBreakingProgress(BlockState state) {
		return true;
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean addDestroyEffects(BlockState state, World world, BlockPos pos, ParticleManager manager) {
		IBakedModel model = Minecraft.getInstance().getBlockRendererDispatcher().getModelForState(state);
		if(model instanceof IStateParticleModel) {
		    TileEntity tile = world.getTileEntity(pos);
	        if(tile instanceof TileEntityDogBed) {
	            IBedMaterial casing = ((TileEntityDogBed)tile).getCasingId();
	            IBedMaterial bedding = ((TileEntityDogBed)tile).getBeddingId();
	            TextureAtlasSprite sprite = ((IStateParticleModel)model).getParticleTexture(casing, bedding, state.get(FACING));
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
		}

		return false;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean addHitEffects(BlockState state, World world, RayTraceResult target, ParticleManager manager) {
		IBakedModel model = Minecraft.getInstance().getBlockRendererDispatcher().getModelForState(state);
		if(model instanceof IStateParticleModel && target.getType() == RayTraceResult.Type.BLOCK) {
			BlockRayTraceResult result = ((BlockRayTraceResult)target);
			BlockPos pos = result.getPos();
			Direction side = result.getFace();

			TileEntity tile = world.getTileEntity(pos);
            if(tile instanceof TileEntityDogBed) {
                IBedMaterial casing = ((TileEntityDogBed)tile).getCasingId();
                IBedMaterial bedding = ((TileEntityDogBed)tile).getBeddingId();
                TextureAtlasSprite sprite = ((IStateParticleModel)model).getParticleTexture(casing, bedding, state.get(FACING));
    			if(sprite != null) {
    				int x = pos.getX();
    				int y = pos.getY();
    				int z = pos.getZ();
    				AxisAlignedBB axisalignedbb = state.getShape(world, pos).getBoundingBox();
    				double d0 = (double)x + RANDOM.nextDouble() * (axisalignedbb.maxX - axisalignedbb.minX - (double)0.2F) + (double)0.1F + axisalignedbb.minX;
    				double d1 = (double)y + RANDOM.nextDouble() * (axisalignedbb.maxY - axisalignedbb.minY - (double)0.2F) + (double)0.1F + axisalignedbb.minY;
    				double d2 = (double)z + RANDOM.nextDouble() * (axisalignedbb.maxZ - axisalignedbb.minZ - (double)0.2F) + (double)0.1F + axisalignedbb.minZ;
    
    				
    				
    				if(side == Direction.DOWN)
    					d1 = (double)y + axisalignedbb.minY - 0.1F;
    				
    				if(side == Direction.UP)
    					d1 = (double)y + axisalignedbb.maxY + 0.1F;
    
    				if(side == Direction.NORTH)
    					d2 = (double)z + axisalignedbb.minZ - 0.1F;
    
    				if(side == Direction.SOUTH)
    					d2 = (double)z + axisalignedbb.maxZ + 0.1F;
    
    				if(side == Direction.WEST)
    					d0 = (double)x + axisalignedbb.minX - 0.1F;
    
    				if(side == Direction.EAST)
    					d0 = (double)x + axisalignedbb.maxX + 0.1F;
    
    				manager.addEffect(new ParticleCustomDigging(world, d0, d1, d2, 0.0D, 0.0D, 0.0D, state, pos, sprite).multiplyVelocity(0.2F).multipleParticleScaleBy(0.6F));
    
    				return true;
    			}
            }
		}

		return false;
	}

	@Override
	public boolean addLandingEffects(BlockState state, ServerWorld world, BlockPos pos, BlockState stateAgain, LivingEntity entity, int numberOfParticles) {
		PacketCustomParticle packet = new PacketCustomParticle(pos, entity.posX, entity.posY, entity.posZ, numberOfParticles, 0.15F);
		PacketHandler.send(PacketDistributor.TRACKING_CHUNK.with(() -> world.getChunkAt(pos)), packet);
		return true;
	}
}
