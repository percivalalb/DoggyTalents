package doggytalents.entity;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class InventoryPackPuppy implements IInventory
{
    public ItemStack inventorySlots[];
    private EntityDTDoggy dog;

    public InventoryPackPuppy(EntityDTDoggy entitydtdoggy, int i)
    {
        this.dog = entitydtdoggy;
        this.inventorySlots = new ItemStack[15];
    }

	public int getSizeInventory() 
	{
		return inventorySlots.length;
	}

	public ItemStack getStackInSlot(int var1) 
	{
		return this.inventorySlots[var1];
	}

	public ItemStack decrStackSize(int par1, int par2) 
	{
		if (this.inventorySlots[par1] != null)
        {
            ItemStack var3;

            if (this.inventorySlots[par1].stackSize <= par2)
            {
                var3 = this.inventorySlots[par1];
                this.inventorySlots[par1] = null;
                this.onInventoryChanged();
                return var3;
            }
            else
            {
                var3 = this.inventorySlots[par1].splitStack(par2);

                if (this.inventorySlots[par1].stackSize == 0)
                {
                    this.inventorySlots[par1] = null;
                }

                this.onInventoryChanged();
                return var3;
            }
        }
        else
        {
            return null;
        }
	}

	public ItemStack getStackInSlotOnClosing(int par1) 
	{
		if (this.inventorySlots[par1] != null)
        {
            ItemStack var2 = this.inventorySlots[par1];
            this.inventorySlots[par1] = null;
            return var2;
        }
        else
        {
            return null;
        }
	}

	public void setInventorySlotContents(int par1, ItemStack par2ItemStack) 
	{
		this.inventorySlots[par1] = par2ItemStack;

        if (par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit())
        {
            par2ItemStack.stackSize = this.getInventoryStackLimit();
        }

        this.onInventoryChanged();
	}

	public String getInvName() 
	{
		return "Doggy Backpack";
	}

	public int getInventoryStackLimit()
	{
		return 64;
	}

	@Override
	public void onInventoryChanged()
	{
		
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer var1) 
	{
		return true;
	}

	public void openChest() {}
	
	public void closeChest() {}

	 public static boolean addItem(IInventory par0IInventory, EntityItem par1EntityItem)
	    {
	        boolean flag = false;

	        if (par1EntityItem == null)
	        {
	            return false;
	        }
	        else
	        {
	            ItemStack itemstack = par1EntityItem.getEntityItem().copy();
	            ItemStack itemstack1 = func_94117_a(par0IInventory, itemstack, -1);

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
	    
	    public static ItemStack func_94117_a(IInventory par1IInventory, ItemStack par2ItemStack, int par3)
	    {
	        int j = 0;
	        int k = par1IInventory.getSizeInventory();

	        for (int l = j; l < k && par2ItemStack != null && par2ItemStack.stackSize > 0; ++l)
	        {
	            ItemStack itemstack1 = par1IInventory.getStackInSlot(l);

	            //if (par1IInventory.func_94041_b(l, par2ItemStack))
	            if (true)
	            {
	                if (itemstack1 == null)
	                {
	                    par1IInventory.setInventorySlotContents(l, par2ItemStack);
	                    par2ItemStack = null;
	                }
	                else if (areStacksEqual(itemstack1, par2ItemStack))
	                {
	                    int i1 = par2ItemStack.getMaxStackSize() - itemstack1.stackSize;
	                    int j1 = Math.min(par2ItemStack.stackSize, i1);
	                    par2ItemStack.stackSize -= j1;
	                    itemstack1.stackSize += j1;
	                }
	            }
	        }

	        if (par2ItemStack != null && par2ItemStack.stackSize == 0)
	        {
	            par2ItemStack = null;
	        }

	        return par2ItemStack;
	    }
	    
	    private static boolean areStacksEqual(ItemStack par1ItemStack, ItemStack par2ItemStack)
	    {
	        return par1ItemStack.itemID != par2ItemStack.itemID ? false : (par1ItemStack.getItemDamage() != par2ItemStack.getItemDamage() ? false : (par1ItemStack.stackSize > par1ItemStack.getMaxStackSize() ? false : ItemStack.areItemStackTagsEqual(par1ItemStack, par2ItemStack)));
	    }

		@Override
		public boolean func_94042_c() {
			return false;
		}

		@Override
		public boolean func_94041_b(int i, ItemStack itemstack) {
			return false;
		}
}