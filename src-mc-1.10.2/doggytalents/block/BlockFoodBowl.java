package doggytalents.block;

import doggytalents.DoggyTalents;
import doggytalents.ModCreativeTabs;
import doggytalents.ModItems;
import doggytalents.inventory.InventoryTreatBag;
import doggytalents.lib.GuiNames;
import doggytalents.tileentity.TileEntityFoodBowl;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
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
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ProPercivalalb
 **/
public class BlockFoodBowl extends BlockContainer {
	
	protected static final AxisAlignedBB AABB = new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 1.0D - 0.0625D, 0.5D, 1.0D - 0.0625D);
	
    public BlockFoodBowl() {
        super(Material.IRON);
        this.setHardness(5.0F);
        this.setTickRandomly(true);
        this.setCreativeTab(ModCreativeTabs.GENERAL);
		this.setResistance(5.0F);
		this.setHarvestLevel("pickaxe", 0);
    }
    
    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
    	return new TileEntityFoodBowl();
    }
    
    
    @Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return AABB;
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
	public EnumBlockRenderType getRenderType(IBlockState state) {
	    return EnumBlockRenderType.MODEL;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }
	
    @Override
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
    	TileEntityFoodBowl foodBowl = (TileEntityFoodBowl) worldIn.getTileEntity(pos);
        
        if(entityIn instanceof EntityItem) {
            EntityItem entityItem = (EntityItem)entityIn;
            ItemStack itemstack1 = foodBowl.inventory.addItem(entityItem.getEntityItem());

            if(itemstack1 != null)
            	entityItem.setEntityItemStack(itemstack1);
            else {
                entityItem.setDead();
                worldIn.playSound(null, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.NEUTRAL, 0.25F, ((worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
            }
        }
    }

    @Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
	    return super.canPlaceBlockAt(worldIn, pos) ? this.canBlockStay(worldIn, pos) : false;
	}

	@Override
	public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
		if(!this.canBlockStay((World)world, pos)) {
			this.dropBlockAsItem((World)world, pos, world.getBlockState(pos), 0);
			((World)world).setBlockToAir(pos);
		}
	}

	public boolean canBlockStay(World world, BlockPos pos) {
		IBlockState blockstate = world.getBlockState(pos.down());
		return blockstate.getBlock().isSideSolid(blockstate, world, pos.down(), EnumFacing.UP);
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack stack, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if(worldIn.isRemote) {
            return true;
        }
        else {
        	if(stack != null && stack.getItem() == ModItems.TREAT_BAG) {
        		TileEntityFoodBowl tileentitydogfoodbowl = (TileEntityFoodBowl)worldIn.getTileEntity(pos);
        		InventoryTreatBag treatBag = new InventoryTreatBag(playerIn, playerIn.inventory.currentItem, stack);
        		treatBag.openInventory(playerIn);
        		
        		for(int i = 0; i < treatBag.getSizeInventory(); i++)
        			if(treatBag.getStackInSlot(i) != null)
        				treatBag.setInventorySlotContents(i, tileentitydogfoodbowl.inventory.addItem(treatBag.getStackInSlot(i)));
        		
        		treatBag.closeInventory(playerIn);
        		
        		return true;
        	}
        	else {
	            playerIn.openGui(DoggyTalents.INSTANCE, GuiNames.GUI_ID_FOOD_BOWL, worldIn, pos.getX(), pos.getY(), pos.getZ());
	            return true;
        	}
        }
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		TileEntity tileentity = worldIn.getTileEntity(pos);

		if(tileentity instanceof TileEntityFoodBowl) {
			InventoryHelper.dropInventoryItems(worldIn, pos, ((TileEntityFoodBowl)tileentity).inventory);
			worldIn.updateComparatorOutputLevel(pos, this);
		}

		super.breakBlock(worldIn, pos, state);
	}

	@Override
	public boolean hasComparatorInputOverride(IBlockState state) {
		return true;
	}

	@Override
	public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos) {
		return Container.calcRedstone(worldIn.getTileEntity(pos));
	}
}
