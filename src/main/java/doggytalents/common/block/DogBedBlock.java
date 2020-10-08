package doggytalents.common.block;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import org.apache.commons.lang3.tuple.Pair;

import doggytalents.DoggyEntityTypes;
import doggytalents.DoggyTalents2;
import doggytalents.api.DoggyTalentsAPI;
import doggytalents.api.registry.IBeddingMaterial;
import doggytalents.api.registry.ICasingMaterial;
import doggytalents.common.block.tileentity.DogBedTileEntity;
import doggytalents.common.entity.DogEntity;
import doggytalents.common.storage.DogRespawnData;
import doggytalents.common.storage.DogRespawnStorage;
import doggytalents.common.util.DogBedUtil;
import doggytalents.common.util.EntityUtil;
import doggytalents.common.util.NBTUtil;
import doggytalents.common.util.WorldUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.Hand;
import net.minecraft.util.Mirror;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Rotation;
import net.minecraft.util.Util;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;

public class DogBedBlock extends Block {

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;

    protected static final VoxelShape SHAPE = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 9.0D, 16.0D);
    protected static final VoxelShape SHAPE_COLLISION = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 7.0D, 16.0D);

    public DogBedBlock() {
        super(Block.Properties.create(Material.WOOD).hardnessAndResistance(3.0F, 5.0F).sound(SoundType.WOOD));
        this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH).with(WATERLOGGED, false));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext selectionContext) {
        return SHAPE_COLLISION;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new DogBedTileEntity();
    }

    @Override
    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
        return Block.hasEnoughSolidSide(worldIn, pos.down(), Direction.UP);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        state = state.with(FACING, placer.getHorizontalFacing().getOpposite());

        DogBedTileEntity dogBedTileEntity = WorldUtil.getTileEntity(worldIn, pos, DogBedTileEntity.class);

        if (dogBedTileEntity != null) {
            DogBedUtil.setBedVariant(dogBedTileEntity, stack);

            dogBedTileEntity.setPlacer(placer);
            CompoundNBT tag = stack.getChildTag("doggytalents");
            if (tag != null) {
                ITextComponent name = NBTUtil.getTextComponent(tag, "name");
                ITextComponent ownerName = NBTUtil.getTextComponent(tag, "ownerName");
                UUID ownerId = NBTUtil.getUniqueId(tag, "ownerId");
                dogBedTileEntity.setBedName(name);
                dogBedTileEntity.setOwnerName(ownerName);
                dogBedTileEntity.setOwner(ownerId);
            }
        }

        worldIn.setBlockState(pos, state, Constants.BlockFlags.BLOCK_UPDATE);
    }

    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (stateIn.get(WATERLOGGED)) {
            worldIn.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn));
        }
        return facing == Direction.DOWN && !stateIn.isValidPosition(worldIn, currentPos) ? Blocks.AIR.getDefaultState() : super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
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
    @Deprecated
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (worldIn.isRemote) {
            return ActionResultType.SUCCESS;
        } else if (handIn == Hand.MAIN_HAND) {
            DogBedTileEntity dogBedTileEntity = WorldUtil.getTileEntity(worldIn, pos, DogBedTileEntity.class);

            if (dogBedTileEntity != null) {

                ItemStack stack = player.getHeldItem(handIn);
                if (stack.getItem() == Items.NAME_TAG && stack.hasDisplayName()) {
                    dogBedTileEntity.setBedName(stack.getDisplayName());

                    if (!player.abilities.isCreativeMode) {
                        stack.shrink(1);
                    }

                    worldIn.notifyBlockUpdate(pos, state, state, Constants.BlockFlags.DEFAULT);
                    return ActionResultType.SUCCESS;
                } else if (player.isSneaking() && dogBedTileEntity.getOwnerUUID() == null) {
                    List<DogEntity> dogs = worldIn.getEntitiesWithinAABB(DoggyEntityTypes.DOG.get(), new AxisAlignedBB(pos).grow(10D), (dog) -> dog.isAlive() && dog.isOwner(player));
                    Collections.sort(dogs, new EntityUtil.Sorter(new Vector3d(pos.getX(), pos.getY(), pos.getZ())));

                    DogEntity closestStanding = null;
                    DogEntity closestSitting = null;
                    for (DogEntity dog : dogs) {
                        if (closestSitting != null && closestSitting != null) {
                            break;
                        }

                        if (closestSitting == null && dog.isSitting()) {
                            closestSitting = dog;
                        } else if (closestStanding == null && !dog.isSitting()) {
                            closestStanding = dog;
                        }
                    }

                    DogEntity closests = closestStanding != null ? closestStanding : closestSitting;
                    if (closests != null) {
                        closests.setTargetBlock(pos);
                    }
                } else if (dogBedTileEntity.getOwnerUUID() != null) {
                    DogRespawnData storage = DogRespawnStorage.get(worldIn).remove(dogBedTileEntity.getOwnerUUID());

                    if (storage != null) {
                        DogEntity dog = storage.respawn((ServerWorld) worldIn, player, pos.up());

                        dogBedTileEntity.setOwner(dog);
                        dog.setBedPos(dog.world.getDimensionKey(), pos);
                        return ActionResultType.SUCCESS;
                    } else {
                        ITextComponent name = dogBedTileEntity.getOwnerName();
                        player.sendMessage(new TranslationTextComponent("block.doggytalents.dog_bed.owner", name != null ? name : "someone"), Util.DUMMY_UUID);
                        return ActionResultType.FAIL;
                    }
                } else {
                    player.sendMessage(new TranslationTextComponent("block.doggytalents.dog_bed.set_owner_help"), Util.DUMMY_UUID);
                    return ActionResultType.SUCCESS;
                }
            }
            return ActionResultType.SUCCESS;
        }

        return ActionResultType.FAIL;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);

        Pair<ICasingMaterial, IBeddingMaterial> materials = DogBedUtil.getMaterials(stack);

        tooltip.add(materials.getLeft().getTooltip());
        tooltip.add(materials.getRight().getTooltip());

        CompoundNBT tag = stack.getChildTag("doggytalents");
        if (tag != null) {
            UUID ownerId = NBTUtil.getUniqueId(tag, "ownerId");
            ITextComponent name = NBTUtil.getTextComponent(tag, "name");
            ITextComponent ownerName = NBTUtil.getTextComponent(tag, "ownerName");

            if (name != null) {
                tooltip.add(new StringTextComponent("Bed Name: ").mergeStyle(TextFormatting.WHITE).append(name));
            }

            if (ownerName != null) {
                tooltip.add(new StringTextComponent("Name: ").mergeStyle(TextFormatting.DARK_AQUA).append(ownerName));

            }

            if (ownerId != null && (flagIn.isAdvanced() || Screen.hasShiftDown())) {
                tooltip.add(new StringTextComponent("UUID: ").mergeStyle(TextFormatting.AQUA).append(new StringTextComponent(ownerId.toString())));
            }
        }
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        for (IBeddingMaterial beddingId : DoggyTalentsAPI.BEDDING_MATERIAL.getValues()) {
            for (ICasingMaterial casingId : DoggyTalentsAPI.CASING_MATERIAL.getValues()) {
                items.add(DogBedUtil.createItemStack(casingId, beddingId));
            }
        }
    }

    @Override
    public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player) {
        DogBedTileEntity dogBedTileEntity = WorldUtil.getTileEntity(world, pos, DogBedTileEntity.class);

        if (dogBedTileEntity != null) {
            return DogBedUtil.createItemStack(dogBedTileEntity.getCasing(), dogBedTileEntity.getBedding());
        }

        DoggyTalents2.LOGGER.debug("Unable to pick block on dog bed.");
        return ItemStack.EMPTY;
    }
}
