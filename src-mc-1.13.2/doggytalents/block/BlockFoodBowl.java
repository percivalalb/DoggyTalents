package doggytalents.block;

import javax.annotation.Nullable;

import doggytalents.ModItems;
import doggytalents.helper.DogUtil;
import doggytalents.inventory.InventoryTreatBag;
import doggytalents.lib.BlockNames;
import doggytalents.tileentity.TileEntityFoodBowl;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReaderBase;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.network.NetworkHooks;

public class BlockFoodBowl extends BlockContainer {

	protected static final VoxelShape SHAPE = Block.makeCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 8.0D, 15.0D);
	
	public BlockFoodBowl() {
		super(Block.Properties.create(Material.IRON).hardnessAndResistance(5.0F, 5.0F).sound(SoundType.METAL));
		this.setRegistryName(BlockNames.FOOD_BOWL);
	}
	
	@Override
	public VoxelShape getShape(IBlockState state, IBlockReader worldIn, BlockPos pos) {
		return SHAPE;
	}
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
	    return EnumBlockRenderType.MODEL;
	}
	
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
	
	@Override
	public IBlockState updatePostPlacement(IBlockState stateIn, EnumFacing facing, IBlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
		return facing == EnumFacing.DOWN && !stateIn.isValidPosition(worldIn, currentPos) ? Blocks.AIR.getDefaultState() : super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
	}
	
	@Override
	public boolean isValidPosition(IBlockState state, IWorldReaderBase worldIn, BlockPos pos) {
		IBlockState down = worldIn.getBlockState(pos.down());
		return down.isTopSolid() || down.getBlockFaceShape(worldIn, pos.down(), EnumFacing.UP) == BlockFaceShape.SOLID;
	}
	
	@Override
	public TileEntity createNewTileEntity(IBlockReader world) {
		return new TileEntityFoodBowl();
	}

	@Override
    public void onEntityCollision(IBlockState state, World worldIn, BlockPos pos, Entity entityIn) {
    	TileEntityFoodBowl foodBowl = (TileEntityFoodBowl) worldIn.getTileEntity(pos);
     
        if(entityIn instanceof EntityItem) {
            EntityItem entityItem = (EntityItem)entityIn;
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
	public void onReplaced(IBlockState state, World worldIn, BlockPos pos, IBlockState newState, boolean isMoving) {
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
	public boolean hasComparatorInputOverride(IBlockState state) {
		return true;
	}
	
	@Override
	public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos) {
		return Container.calcRedstone(worldIn.getTileEntity(pos));
	}

	@Override
	public boolean onBlockActivated(IBlockState state, World worldIn, BlockPos pos, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if(worldIn.isRemote) {
            return true;
        }
        else {
        	TileEntityFoodBowl foodBowl = this.getTileEntity(state, worldIn, pos);

            if(foodBowl != null) {
            	ItemStack stack = player.getHeldItem(hand);
            	
            	if(!stack.isEmpty() && stack.getItem() == ModItems.TREAT_BAG) {
            		InventoryTreatBag treatBag = new InventoryTreatBag(player, player.inventory.currentItem, stack);
            		treatBag.openInventory(player);
            		
            		for(int i = 0; i < treatBag.getSizeInventory(); i++)
            			treatBag.setInventorySlotContents(i, DogUtil.addItem(foodBowl, treatBag.getStackInSlot(i)));
            		
            		treatBag.closeInventory(player);
            		
            		return true;
            	}
            	
            	else if(player instanceof EntityPlayerMP && !(player instanceof FakePlayer)) {
                    EntityPlayerMP entityPlayerMP = (EntityPlayerMP) player;

                    NetworkHooks.openGui(entityPlayerMP, foodBowl, buf -> buf.writeBlockPos(pos));
                }
            }

            return true;
        }
    }
	
	@Nullable
    public TileEntityFoodBowl getTileEntity(IBlockState state, World worldIn, BlockPos pos) {
        TileEntity tileentity = worldIn.getTileEntity(pos);

        if (!(tileentity instanceof TileEntityFoodBowl)) {
            return null;
        }
        else {
            return (TileEntityFoodBowl)tileentity;
        }
    }
}
