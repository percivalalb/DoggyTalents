package doggytalents.common.block;

import javax.annotation.Nullable;

import doggytalents.DoggyItems;
import doggytalents.common.Screens;
import doggytalents.common.block.tileentity.FoodBowlTileEntity;
import doggytalents.common.util.InventoryUtil;
import doggytalents.common.util.WorldUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.EmptyHandler;

public class FoodBowlBlock extends Block {

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    protected static final VoxelShape SHAPE = Block.makeCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 8.0D, 15.0D);

    public FoodBowlBlock() {
        super(Block.Properties.create(Material.IRON).hardnessAndResistance(5.0F, 5.0F).sound(SoundType.METAL));
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new FoodBowlTileEntity();
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext selectionContext) {
        return SHAPE;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
        return Block.hasEnoughSolidSide(worldIn, pos.down(), Direction.UP);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        FoodBowlTileEntity foodBowlTileEntity = WorldUtil.getTileEntity(worldIn, pos, FoodBowlTileEntity.class);

        if (foodBowlTileEntity != null) {
            foodBowlTileEntity.setPlacer(placer);
        }

        worldIn.setBlockState(pos, state, Constants.BlockFlags.BLOCK_UPDATE);
    }

    @Override
    public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
        FoodBowlTileEntity foodBowl = WorldUtil.getTileEntity(worldIn, pos, FoodBowlTileEntity.class);

        if(foodBowl != null && entityIn instanceof ItemEntity) {
            ItemEntity entityItem = (ItemEntity) entityIn;

            IItemHandler bowlInventory = foodBowl.getInventory();
            ItemStack remaining = InventoryUtil.addItem(bowlInventory, entityItem.getItem());
            if(!remaining.isEmpty()) {
                entityItem.setItem(remaining);
            } else {
                entityItem.remove();
                worldIn.playSound(null, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.NEUTRAL, 0.25F, ((worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
            }
        }
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if(state.getBlock() != newState.getBlock()) {
            FoodBowlTileEntity foodBowl = WorldUtil.getTileEntity(worldIn, pos, FoodBowlTileEntity.class);
            if(foodBowl != null) {
                IItemHandler bowlInventory = foodBowl.getInventory();
                for(int i = 0; i < bowlInventory.getSlots(); ++i) {
                    InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), bowlInventory.getStackInSlot(i));
                }
                worldIn.updateComparatorOutputLevel(pos, this);
            }

            super.onReplaced(state, worldIn, pos, newState, isMoving);
        }
    }

    @Override
    public boolean hasComparatorInputOverride(BlockState state) {
        return true;
    }

    @Override
    public int getComparatorInputOverride(BlockState blockState, World worldIn, BlockPos pos) {
        FoodBowlTileEntity foodBowl = WorldUtil.getTileEntity(worldIn, pos, FoodBowlTileEntity.class);

        if (foodBowl != null) {
            IItemHandler bowlInventory = foodBowl.getInventory();
            return InventoryUtil.calcRedstoneFromInventory(bowlInventory);
        }

        return 0;
    }

    @Override
    public ActionResultType onBlockActivated(BlockState blockStateIn, World worldIn, BlockPos posIn, PlayerEntity playerIn, Hand handIn, BlockRayTraceResult result) {
        if(worldIn.isRemote) {
            return ActionResultType.SUCCESS;
        }
        else {
            FoodBowlTileEntity foodBowl = WorldUtil.getTileEntity(worldIn, posIn, FoodBowlTileEntity.class);

            if(foodBowl != null) {
                ItemStack stack = playerIn.getHeldItem(handIn);

                if(!stack.isEmpty() && stack.getItem() == DoggyItems.TREAT_BAG.get()) {
                    IItemHandler bagInventory = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(EmptyHandler.INSTANCE);
                    IItemHandler bowlInventory = foodBowl.getInventory();

                    InventoryUtil.transferStacks((IItemHandlerModifiable) bagInventory, bowlInventory);
                } else if(playerIn instanceof ServerPlayerEntity && !(playerIn instanceof FakePlayer)) {
                    ServerPlayerEntity serverPlayer = (ServerPlayerEntity)playerIn;

                    Screens.openFoodBowlScreen(serverPlayer, foodBowl);
                }
            }

            return ActionResultType.SUCCESS;
        }
    }

    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        if(stateIn.get(WATERLOGGED)) {
            worldIn.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn));
        }

        return facing == Direction.DOWN && !stateIn.isValidPosition(worldIn, currentPos) ? Blocks.AIR.getDefaultState() : super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        IFluidState ifluidstate = context.getWorld().getFluidState(context.getPos());

        return this.getDefaultState().with(WATERLOGGED, Boolean.valueOf(ifluidstate.getFluid() == Fluids.WATER));
    }

    @Override
    public IFluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED);
    }
}
