package doggytalents.inventory;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityHopper;
import doggytalents.entity.EntityDTDoggy;

/**
 * @author ProPercivalalb
 */
public class InventoryPackPuppy implements IInventory {
	
	public ItemStack inventorySlots[];
    private EntityDTDoggy dog;

    public InventoryPackPuppy(EntityDTDoggy entitydtdoggy) {
        this.dog = entitydtdoggy;
        this.inventorySlots = new ItemStack[15];
    }

    @Override
	public int getSizeInventory()  {
		return inventorySlots.length;
	}

	@Override
	public ItemStack getStackInSlot(int var1)  {
		return this.inventorySlots[var1];
	}

	@Override
	public ItemStack decrStackSize(int par1, int par2) {
		if (this.inventorySlots[par1] != null) {
            ItemStack var3;

            if (this.inventorySlots[par1].stackSize <= par2) {
                var3 = this.inventorySlots[par1];
                this.inventorySlots[par1] = null;
                this.onInventoryChanged();
                return var3;
            }
            else {
                var3 = this.inventorySlots[par1].splitStack(par2);

                if (this.inventorySlots[par1].stackSize == 0)
                {
                    this.inventorySlots[par1] = null;
                }

                this.onInventoryChanged();
                return var3;
            }
        }
        else {
            return null;
        }
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int par1) {
		if (this.inventorySlots[par1] != null) {
            ItemStack var2 = this.inventorySlots[par1];
            this.inventorySlots[par1] = null;
            return var2;
        }
        else {
            return null;
        }
	}

	@Override
	public void setInventorySlotContents(int par1, ItemStack par2ItemStack)  {
		this.inventorySlots[par1] = par2ItemStack;

        if (par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit()) {
            par2ItemStack.stackSize = this.getInventoryStackLimit();
        }

        this.onInventoryChanged();
	}

	@Override
	public String getInvName()  {
		return "container.packpuppy";
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public void onInventoryChanged() {}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return true;
	}

	public void openChest() {}
	
	public void closeChest() {}

	public boolean insertStackFromEntity(EntityItem par1EntityItem)
    {
        boolean flag = false;

        if (par1EntityItem == null || par1EntityItem.isDead) {
            return false;
        }
        else {
            ItemStack itemstack = par1EntityItem.getEntityItem().copy();
            ItemStack itemstack1 = insertStack(this, itemstack, -1);

            if (itemstack1 != null && itemstack1.stackSize != 0)
            {
                par1EntityItem.setEntityItemStack(itemstack1);
            }
            else
            {
                flag = true;
                par1EntityItem.setDead();
            }

            return flag;
        }
    }

    /**
     * Inserts a stack into an inventory. Args: Inventory, stack, side. Returns leftover items.
     */
    public ItemStack insertStack(IInventory par0IInventory, ItemStack par1ItemStack, int par2)
    {
        if (par0IInventory instanceof ISidedInventory && par2 > -1)
        {
            ISidedInventory isidedinventory = (ISidedInventory)par0IInventory;
            int[] aint = isidedinventory.getAccessibleSlotsFromSide(par2);

            for (int j = 0; j < aint.length && par1ItemStack != null && par1ItemStack.stackSize > 0; ++j)
            {
                par1ItemStack = func_102014_c(par0IInventory, par1ItemStack, aint[j], par2);
            }
        }
        else
        {
            int k = par0IInventory.getSizeInventory();

            for (int l = 0; l < k && par1ItemStack != null && par1ItemStack.stackSize > 0; ++l)
            {
                par1ItemStack = func_102014_c(par0IInventory, par1ItemStack, l, par2);
            }
        }

        if (par1ItemStack != null && par1ItemStack.stackSize == 0)
        {
            par1ItemStack = null;
        }

        return par1ItemStack;
    }
    
    private ItemStack func_102014_c(IInventory par0IInventory, ItemStack par1ItemStack, int par2, int par3)
    {
        ItemStack itemstack1 = par0IInventory.getStackInSlot(par2);
            
        boolean flag = false;

        if (itemstack1 == null)
        {
            par0IInventory.setInventorySlotContents(par2, par1ItemStack);
            par1ItemStack = null;
            flag = true;
        }
        else if (areItemStacksEqualItem(itemstack1, par1ItemStack))
        {
            int k = par1ItemStack.getMaxStackSize() - itemstack1.stackSize;
            int l = Math.min(par1ItemStack.stackSize, k);
            par1ItemStack.stackSize -= l;
            itemstack1.stackSize += l;
            flag = l > 0;
        }

        if (flag)
        {
            if (par0IInventory instanceof TileEntityHopper)
            {
                ((TileEntityHopper)par0IInventory).setTransferCooldown(8);
                par0IInventory.onInventoryChanged();
            }

            par0IInventory.onInventoryChanged();
        }

        return par1ItemStack;
    }
    
    private boolean areItemStacksEqualItem(ItemStack par0ItemStack, ItemStack par1ItemStack) {
        return par0ItemStack.itemID != par1ItemStack.itemID ? false : (par0ItemStack.getItemDamage() != par1ItemStack.getItemDamage() ? false : (par0ItemStack.stackSize > par0ItemStack.getMaxStackSize() ? false : ItemStack.areItemStackTagsEqual(par0ItemStack, par1ItemStack)));
    }
    
    @Override
	public boolean isInvNameLocalized() {
		return false;
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		return false;
	}
}
