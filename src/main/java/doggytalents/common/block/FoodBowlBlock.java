package doggytalents.common.block;

import javax.annotation.Nullable;

import doggytalents.DoggyItems;
import doggytalents.common.Screens;
import doggytalents.common.block.tileentity.FoodBowlTileEntity;
import doggytalents.common.util.InventoryUtil;
import doggytalents.common.util.WorldUtil;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.Containers;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.EmptyHandler;

public class FoodBowlBlock extends Block {

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    protected static final VoxelShape SHAPE = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 8.0D, 15.0D);

    public FoodBowlBlock() {
        super(Block.Properties.of(Material.METAL).strength(5.0F, 5.0F).sound(SoundType.METAL));
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public BlockEntity createTileEntity(BlockState state, BlockGetter world) {
        return new FoodBowlTileEntity();
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext selectionContext) {
        return SHAPE;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
        return Block.canSupportCenter(worldIn, pos.below(), Direction.UP);
    }

    @Override
    public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        FoodBowlTileEntity foodBowlTileEntity = WorldUtil.getTileEntity(worldIn, pos, FoodBowlTileEntity.class);

        if (foodBowlTileEntity != null) {
            foodBowlTileEntity.setPlacer(placer);
        }

        worldIn.setBlock(pos, state, Constants.BlockFlags.BLOCK_UPDATE);
    }

    @Override
    public void entityInside(BlockState state, Level worldIn, BlockPos pos, Entity entityIn) {
        if (entityIn instanceof ItemEntity) {
            FoodBowlTileEntity foodBowl = WorldUtil.getTileEntity(worldIn, pos, FoodBowlTileEntity.class);

            if (foodBowl != null) {
                ItemEntity entityItem = (ItemEntity) entityIn;

                IItemHandler bowlInventory = foodBowl.getInventory();
                ItemStack remaining = InventoryUtil.addItem(bowlInventory, entityItem.getItem());
                if (!remaining.isEmpty()) {
                    entityItem.setItem(remaining);
                } else {
                    entityItem.remove();
                    worldIn.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.NEUTRAL, 0.25F, ((worldIn.random.nextFloat() - worldIn.random.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                }
            }
        }
    }

    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            FoodBowlTileEntity foodBowl = WorldUtil.getTileEntity(worldIn, pos, FoodBowlTileEntity.class);
            if (foodBowl != null) {
                IItemHandler bowlInventory = foodBowl.getInventory();
                for (int i = 0; i < bowlInventory.getSlots(); ++i) {
                    Containers.dropItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), bowlInventory.getStackInSlot(i));
                }
                worldIn.updateNeighbourForOutputSignal(pos, this);
            }

            super.onRemove(state, worldIn, pos, newState, isMoving);
        }
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState, Level worldIn, BlockPos pos) {
        FoodBowlTileEntity foodBowl = WorldUtil.getTileEntity(worldIn, pos, FoodBowlTileEntity.class);

        if (foodBowl != null) {
            IItemHandler bowlInventory = foodBowl.getInventory();
            return InventoryUtil.calcRedstoneFromInventory(bowlInventory);
        }

        return 0;
    }

    @Override
    public InteractionResult use(BlockState blockStateIn, Level worldIn, BlockPos posIn, Player playerIn, InteractionHand handIn, BlockHitResult result) {
        if (worldIn.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        else {
            FoodBowlTileEntity foodBowl = WorldUtil.getTileEntity(worldIn, posIn, FoodBowlTileEntity.class);

            if (foodBowl != null) {
                ItemStack stack = playerIn.getItemInHand(handIn);

                if (!stack.isEmpty() && stack.getItem() == DoggyItems.TREAT_BAG.get()) {
                    IItemHandler bagInventory = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(EmptyHandler.INSTANCE);
                    IItemHandler bowlInventory = foodBowl.getInventory();

                    InventoryUtil.transferStacks((IItemHandlerModifiable) bagInventory, bowlInventory);
                } else if (playerIn instanceof ServerPlayer && !(playerIn instanceof FakePlayer)) {
                    ServerPlayer serverPlayer = (ServerPlayer)playerIn;

                    Screens.openFoodBowlScreen(serverPlayer, foodBowl);
                }
            }

            return InteractionResult.SUCCESS;
        }
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (stateIn.getValue(WATERLOGGED)) {
            worldIn.getLiquidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(worldIn));
        }

        return facing == Direction.DOWN && !stateIn.canSurvive(worldIn, currentPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState FluidState = context.getLevel().getFluidState(context.getClickedPos());

        return this.defaultBlockState().setValue(WATERLOGGED, Boolean.valueOf(FluidState.getType() == Fluids.WATER));
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED);
    }
}
