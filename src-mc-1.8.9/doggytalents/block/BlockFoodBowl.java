package doggytalents.block;

import doggytalents.DoggyTalents;
import doggytalents.ModItems;
import doggytalents.helper.DogUtil;
import doggytalents.inventory.InventoryTreatBag;
import doggytalents.proxy.CommonProxy;
import doggytalents.tileentity.TileEntityFoodBowl;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * @author ProPercivalalb
 **/
public class BlockFoodBowl extends BlockContainer {
	
    public BlockFoodBowl() {
        super(Material.iron);
        this.setHardness(5.0F);
        this.setTickRandomly(true);
        this.setCreativeTab(DoggyTalents.CREATIVE_TAB);
		this.setResistance(5.0F);
		this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 1.0F - 0.0625F, 0.5F, 1.0F - 0.0625F);
    }
    
    @Override
    public int getRenderType() {
        return 3;
    }
    
	@Override
	public boolean isFullCube() {
	    return false;
	}

	@Override
	public boolean isOpaqueCube() {
	    return false;
	}

	@Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(worldIn.isRemote) {
            return true;
        }
        else {
        	ItemStack stack = playerIn.getHeldItem();
        	
        	if(stack != null && stack.getItem() == ModItems.TREAT_BAG) {
        		TileEntityFoodBowl tileentitydogfoodbowl = (TileEntityFoodBowl)worldIn.getTileEntity(pos);
        		InventoryTreatBag treatBag = new InventoryTreatBag(playerIn, playerIn.inventory.currentItem, stack);
        		treatBag.openInventory(playerIn);
        		
        		for(int i = 0; i < treatBag.getSizeInventory(); i++)
        			treatBag.setInventorySlotContents(i, DogUtil.addItem(tileentitydogfoodbowl.inventory, treatBag.getStackInSlot(i)));
        		
        		treatBag.closeInventory(playerIn);
        		
        		return true;
        	}
        	else {
	            TileEntityFoodBowl tileentitydogfoodbowl = (TileEntityFoodBowl)worldIn.getTileEntity(pos);
	            playerIn.openGui(DoggyTalents.INSTANCE, CommonProxy.GUI_ID_FOOD_BOWL, worldIn, pos.getX(), pos.getY(), pos.getZ());
	            return true;
        	}
        }
    }
    
    @Override
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
    	TileEntityFoodBowl foodBowl = (TileEntityFoodBowl) worldIn.getTileEntity(pos);
        
        if(entityIn instanceof EntityItem) {
            EntityItem entityItem = (EntityItem)entityIn;
            ItemStack itemstack = entityItem.getEntityItem().copy();
            ItemStack itemstack1 = DogUtil.addItem(foodBowl.inventory, entityItem.getEntityItem());

            if(itemstack1 != null && itemstack1.stackSize != 0)
            	entityItem.setEntityItemStack(itemstack1);
            else {
                entityItem.setDead();
                worldIn.playSoundEffect(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, "random.pop", 0.25F, ((worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
            }
        }
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityFoodBowl();
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
		return blockstate.getBlock().isSideSolid(world, pos.down(), EnumFacing.UP);
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
	public boolean hasComparatorInputOverride() {
		return true;
	}

	@Override
	public int getComparatorInputOverride(World worldIn, BlockPos pos) {
		return Container.calcRedstoneFromInventory(((TileEntityFoodBowl)worldIn.getTileEntity(pos)).inventory);
	}
}
