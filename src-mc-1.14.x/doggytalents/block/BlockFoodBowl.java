package doggytalents.block;

import javax.annotation.Nullable;

import doggytalents.ModItems;
import doggytalents.helper.DogUtil;
import doggytalents.inventory.InventoryTreatBag;
import doggytalents.tileentity.TileEntityFoodBowl;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ContainerBlock;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.network.NetworkHooks;

public class BlockFoodBowl extends ContainerBlock implements IWaterLoggable {

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    protected static final VoxelShape SHAPE = Block.makeCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 8.0D, 15.0D);
    
    public BlockFoodBowl() {
        super(Block.Properties.create(Material.IRON).hardnessAndResistance(5.0F, 5.0F).sound(SoundType.METAL));
        this.setDefaultState(this.stateContainer.getBaseState().with(WATERLOGGED, Boolean.valueOf(false)));
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
    public boolean isSolid(BlockState state) {
        return false;
    }
    
    @Override
    @OnlyIn(Dist.CLIENT)
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.SOLID;
    }
    
    @Override
    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
        return func_220055_a(worldIn, pos.down(), Direction.UP);
    }
    
    @Override
    public TileEntity createNewTileEntity(IBlockReader world) {
        return new TileEntityFoodBowl();
    }

    @Override
    public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
        TileEntityFoodBowl foodBowl = (TileEntityFoodBowl) worldIn.getTileEntity(pos);
     
        if(entityIn instanceof ItemEntity) {
            ItemEntity entityItem = (ItemEntity)entityIn;
            ItemStack itemstack = entityItem.getItem().copy();
            
            ItemStack itemstack1 = DogUtil.addItem(foodBowl, entityItem.getItem());

            if(!itemstack1.isEmpty())
                entityItem.setItem(itemstack1);
            else {
                entityItem.remove();
                worldIn.playSound(null, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.NEUTRAL, 0.25F, ((worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
            }
        }
    }
    
    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if(state.getBlock() != newState.getBlock()) {
            TileEntity tileentity = worldIn.getTileEntity(pos);
            if(tileentity instanceof TileEntityFoodBowl) {
                InventoryHelper.dropInventoryItems(worldIn, pos, (TileEntityFoodBowl)tileentity);
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
        return Container.calcRedstone(worldIn.getTileEntity(pos));
    }

    @Override
    public boolean onBlockActivated(BlockState blockStateIn, World worldIn, BlockPos posIn, PlayerEntity playerIn, Hand handIn, BlockRayTraceResult result) {
        if(worldIn.isRemote) {
            return true;
        }
        else {
            TileEntityFoodBowl foodBowl = this.getTileEntity(blockStateIn, worldIn, posIn);

            if(foodBowl != null) {
                ItemStack stack = playerIn.getHeldItem(handIn);
                
                if(!stack.isEmpty() && stack.getItem() == ModItems.TREAT_BAG) {
                    InventoryTreatBag treatBag = new InventoryTreatBag(playerIn.inventory, playerIn.inventory.currentItem, stack);
                    treatBag.openInventory(playerIn);
                    
                    for(int i = 0; i < treatBag.getSizeInventory(); i++)
                        treatBag.setInventorySlotContents(i, DogUtil.addItem(foodBowl, treatBag.getStackInSlot(i)));
                    
                    treatBag.closeInventory(playerIn);
                    
                    return true;
                }
                
                else if(playerIn instanceof ServerPlayerEntity && !(playerIn instanceof FakePlayer)) {
                    ServerPlayerEntity entityPlayerMP = (ServerPlayerEntity)playerIn;
                    NetworkHooks.openGui(entityPlayerMP, foodBowl);
                }
            }

            return true;
        }
    }
    
    @Nullable
    public TileEntityFoodBowl getTileEntity(BlockState state, World worldIn, BlockPos pos) {
        TileEntity tileentity = worldIn.getTileEntity(pos);

        if (!(tileentity instanceof TileEntityFoodBowl)) {
            return null;
        }
        else {
            return (TileEntityFoodBowl)tileentity;
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
