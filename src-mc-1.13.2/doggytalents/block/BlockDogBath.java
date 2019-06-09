package doggytalents.block;

import doggytalents.entity.EntityDog;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtils;
import net.minecraft.stats.StatList;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReaderBase;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BlockDogBath extends Block {

	protected static final VoxelShape SHAPE = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D);
	protected static final VoxelShape SHAPE_COLLISION = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 4.0D, 16.0D);
	
	public BlockDogBath() {
		super(Block.Properties.create(Material.IRON).hardnessAndResistance(3.0F, 5.0F).sound(SoundType.METAL));
	}
	
	@Override
	public void onEntityCollision(IBlockState state, World worldIn, BlockPos pos, Entity entityIn) {
		if(entityIn instanceof EntityDog) {
			EntityDog dog = (EntityDog)entityIn;
			dog.isWet = true;
		}
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
	public VoxelShape getShape(IBlockState state, IBlockReader worldIn, BlockPos pos) {
		return SHAPE;
	}
	
	@Override
	public VoxelShape getCollisionShape(IBlockState state, IBlockReader worldIn, BlockPos pos) {
		return SHAPE_COLLISION;
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
	public BlockFaceShape getBlockFaceShape(IBlockReader worldIn, IBlockState state, BlockPos pos, EnumFacing facing) {
		if(facing == EnumFacing.DOWN) 
			return BlockFaceShape.SOLID;
		return BlockFaceShape.UNDEFINED;
	}

	@Override
	public boolean onBlockActivated(IBlockState state, World worldIn, BlockPos pos, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		ItemStack itemstack = player.getHeldItem(hand);
		if(itemstack.isEmpty()) {
			return true;
		} else {
			Item item = itemstack.getItem();
			if(item == Items.GLASS_BOTTLE) {
				if(!worldIn.isRemote) {
					if(!player.abilities.isCreativeMode) {
						ItemStack itemstack4 = PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), PotionTypes.WATER);
						itemstack.shrink(1);
						if(itemstack.isEmpty()) {
							player.setHeldItem(hand, itemstack4);
						} else if(!player.inventory.addItemStackToInventory(itemstack4)) {
							player.dropItem(itemstack4, false);
						} else if(player instanceof EntityPlayerMP) {
							((EntityPlayerMP)player).sendContainerToPlayer(player.inventoryContainer);
						}
					}

					worldIn.playSound((EntityPlayer)null, pos, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
				}

				return true;
			} else {
				return false;
			}
		} 
	}
}
