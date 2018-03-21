package doggytalents.block;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import doggytalents.DoggyTalents;
import doggytalents.ModItems;
import doggytalents.helper.DogUtil;
import doggytalents.inventory.InventoryTreatBag;
import doggytalents.proxy.CommonProxy;
import doggytalents.tileentity.TileEntityFoodBowl;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.audio.SoundCategory;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * @author ProPercivalalb
 **/
public class BlockFoodBowl extends BlockContainer {
	
	private final Random random = new Random();
	public static IIcon top;
    public static IIcon side;
    public static IIcon bottom;
    
    public BlockFoodBowl() {
        super(Material.iron);
        this.setHardness(5.0F);
		this.setResistance(5.0F);
        this.setTickRandomly(true);
        this.setCreativeTab(DoggyTalents.CREATIVE_TAB);
        this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 1.0F - 0.0625F, 0.5F, 1.0F - 0.0625F);
    }
    
    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean onBlockActivated(World worldIn, int x, int y, int z, EntityPlayer playerIn, int par6, float hitX, float hitY, float hitZ) {
        if(worldIn.isRemote) {
            return true;
        }
        else {
        	ItemStack stack = playerIn.getHeldItem();
        	
        	if(stack != null && stack.getItem() == ModItems.TREAT_BAG) {
        		TileEntityFoodBowl tileentitydogfoodbowl = (TileEntityFoodBowl)worldIn.getTileEntity(x, y, z);
        		InventoryTreatBag treatBag = new InventoryTreatBag(playerIn, playerIn.inventory.currentItem, stack);
        		treatBag.openInventory();
        		
        		for(int i = 0; i < treatBag.getSizeInventory(); i++)
        			treatBag.setInventorySlotContents(i, DogUtil.addItem(tileentitydogfoodbowl.inventory, treatBag.getStackInSlot(i)));
        		
        		treatBag.markDirty();
        		treatBag.closeInventory();
        		
        		return true;
        	}
        	else {
	            TileEntityFoodBowl tileentitydogfoodbowl = (TileEntityFoodBowl)worldIn.getTileEntity(x, y, z);
	            playerIn.openGui(DoggyTalents.INSTANCE, CommonProxy.GUI_ID_FOOD_BOWL, worldIn, x, y, z);
	            return true;
        	}
        }
    }
    
    @Override
    public void onEntityCollidedWithBlock(World worldIn, int x, int y, int z, Entity entityIn) {
    	TileEntityFoodBowl foodBowl = (TileEntityFoodBowl) worldIn.getTileEntity(x, y, z);
        
        if(entityIn instanceof EntityItem) {
            EntityItem entityItem = (EntityItem)entityIn;
            ItemStack itemstack = entityItem.getEntityItem().copy();
            ItemStack itemstack1 = DogUtil.addItem(foodBowl.inventory, entityItem.getEntityItem());

            if(itemstack1 != null && itemstack1.stackSize != 0)
            	entityItem.setEntityItemStack(itemstack1);
            else {
                entityItem.setDead();
                worldIn.playSoundEffect(x + 0.5D, y + 0.5D, z + 0.5D, "random.pop", 0.25F, ((worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
            }
        }
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityFoodBowl();
    }

    @Override
	public boolean canPlaceBlockAt(World worldIn, int x, int y, int z) {
	    return super.canPlaceBlockAt(worldIn, x, y, z) ? this.canBlockStay(worldIn, x, y, z) : false;
	}

    @Override
	public void onNeighborBlockChange(World worldIn, int x, int y, int z, Block block) {
		if(!this.canBlockStay(worldIn, x, y, z)) {
			this.dropBlockAsItem(worldIn, x, y, z, worldIn.getBlockMetadata(x, y, z), 0);
			worldIn.setBlockToAir(x, y, z);
		}
	}

	@Override
	public boolean canBlockStay(World world, int x, int y, int z) {
		Block block = world.getBlock(x, y - 1, z);
		return block.isSideSolid(world, x, y - 1, z, ForgeDirection.UP);
	}
	
	@Override
    public void registerBlockIcons(IIconRegister par1IconRegister) {
        this.top = par1IconRegister.registerIcon("doggytalents:food_top");
        this.bottom = par1IconRegister.registerIcon("doggytalents:food_bottom");
        this.side = par1IconRegister.registerIcon("doggytalents:food_side");
    }
	
	@Override
    public IIcon getIcon(int side, int meta) {
    	return side == 1 ? this.top : side == 0 ? this.bottom : this.side;
    }
	
	@Override
	public void breakBlock(World worldIn, int x, int y, int z, Block block, int side) {
		TileEntity tileentity = worldIn.getTileEntity(x, y, z);
		
		if(tileentity instanceof TileEntityFoodBowl) {
			TileEntityFoodBowl foodBowl = (TileEntityFoodBowl)tileentity;
			
			if (tileentity != null)
	        {
	            for (int i1 = 0; i1 < foodBowl.inventory.getSizeInventory(); ++i1)
	            {
	                ItemStack itemstack = foodBowl.inventory.getStackInSlot(i1);

	                if (itemstack != null)
	                {
	                    float f = this.random.nextFloat() * 0.8F + 0.1F;
	                    float f1 = this.random.nextFloat() * 0.8F + 0.1F;
	                    EntityItem entityitem;

	                    for (float f2 = this.random.nextFloat() * 0.8F + 0.1F; itemstack.stackSize > 0; worldIn.spawnEntityInWorld(entityitem))
	                    {
	                        int j1 = this.random.nextInt(21) + 10;

	                        if (j1 > itemstack.stackSize)
	                        {
	                            j1 = itemstack.stackSize;
	                        }

	                        itemstack.stackSize -= j1;
	                        entityitem = new EntityItem(worldIn, (double)((float)x + f), (double)((float)y + f1), (double)((float)z + f2), new ItemStack(itemstack.getItem(), j1, itemstack.getItemDamage()));
	                        float f3 = 0.05F;
	                        entityitem.motionX = (double)((float)this.random.nextGaussian() * f3);
	                        entityitem.motionY = (double)((float)this.random.nextGaussian() * f3 + 0.2F);
	                        entityitem.motionZ = (double)((float)this.random.nextGaussian() * f3);

	                        if (itemstack.hasTagCompound())
	                        {
	                            entityitem.getEntityItem().setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy());
	                        }
	                    }
	                }
	            }

	            worldIn.func_147453_f(x, y, z, block);
	        }
		}

		super.breakBlock(worldIn, x, y, z, block, side);
	}

	@Override
	public boolean hasComparatorInputOverride() {
		return true;
	}

	@Override
	public int getComparatorInputOverride(World worldIn, int x, int y, int z, int meta) {
		return Container.calcRedstoneFromInventory(((TileEntityFoodBowl)worldIn.getTileEntity(x, y, z)).inventory);
	}
}
