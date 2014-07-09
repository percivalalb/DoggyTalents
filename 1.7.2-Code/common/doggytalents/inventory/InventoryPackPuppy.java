package doggytalents.inventory;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import doggytalents.entity.EntityDTDoggy;

/**
 * @author ProPercivalalb
 */
public class InventoryPackPuppy implements IInventory {
	
	public ItemStack[] inventorySlots;
    private EntityDTDoggy dog;

    public InventoryPackPuppy(EntityDTDoggy entitydtdoggy) {
        this.dog = entitydtdoggy;
        this.inventorySlots = new ItemStack[this.getSizeInventory()];
    }

    @Override
	public int getSizeInventory()  {
		return 15;
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
                this.markDirty();
                return var3;
            }
            else {
                var3 = this.inventorySlots[par1].splitStack(par2);

                if (this.inventorySlots[par1].stackSize == 0)
                {
                    this.inventorySlots[par1] = null;
                }

                this.markDirty();
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

        this.markDirty();
	}

	@Override
	public String getInventoryName()  {
		return "container.packpuppy";
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}
	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return true;
	}

	public boolean insertStackFromEntity(EntityItem entityItem) {
	    boolean succesful = false;

	    if (entityItem == null || entityItem.isDead)
	        return false;
	    else {
	        ItemStack itemstack = entityItem.getEntityItem().copy();
	        ItemStack itemstack1 = this.insertStack(itemstack);

	        if (itemstack1 != null && itemstack1.stackSize != 0)
	        	entityItem.setEntityItemStack(itemstack1);
	        else {
	        	succesful = true;
	        	entityItem.setDead();
	        }

	        return succesful;
	    }
	}
	
    public ItemStack insertStack(ItemStack stack) {
    	int j = this.getSizeInventory();

        for (int k = 0; k < j && stack != null && stack.stackSize > 0; ++k)
        	stack = tryInsertStackToSlot(stack, k);

        if (stack != null && stack.stackSize == 0)
            stack = null;

        return stack;
    }
    
    public ItemStack tryInsertStackToSlot(ItemStack stack, int slot) {
        ItemStack slotStack = this.getStackInSlot(slot);

        if (this.isItemValidForSlot(slot, stack)) {
            boolean changed = false;

            if (slotStack == null) {
                int max = Math.min(stack.getMaxStackSize(), this.getInventoryStackLimit());
                if (max >= stack.stackSize) {
                	this.setInventorySlotContents(slot, stack);
                    stack = null;
                }
                else
                	this.setInventorySlotContents(slot, stack.splitStack(max));
                changed = true;
            }
            else if (this.areItemStacksEqualItem(slotStack, stack)) {
                int max = Math.min(stack.getMaxStackSize(), this.getInventoryStackLimit());
                if (max > slotStack.stackSize) {
                    int l = Math.min(stack.stackSize, max - slotStack.stackSize);
                    stack.stackSize -= l;
                    slotStack.stackSize += l;
                    changed = l > 0;
                }
            }

            if (changed)
                this.markDirty();
        }

        return stack;
    }
    
    private boolean areItemStacksEqualItem(ItemStack p_145894_0_, ItemStack p_145894_1_) {
        return p_145894_0_.getItem() != p_145894_1_.getItem() ? false : (p_145894_0_.getItemDamage() != p_145894_1_.getItemDamage() ? false : (p_145894_0_.stackSize > p_145894_0_.getMaxStackSize() ? false : ItemStack.areItemStackTagsEqual(p_145894_0_, p_145894_1_)));
    }
    
    @Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		return false;
	}
	
	public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
        NBTTagList nbttaglist = par1NBTTagCompound.getTagList("Items", 10);
        
        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.getCompoundTagAt(i);
            int j = nbttagcompound1.getByte("Slot") & 255;

            if (j >= 0 && j < this.inventorySlots.length)
            {
                this.inventorySlots[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
        }
    }

    public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.inventorySlots.length; ++i)
        {
            if (this.inventorySlots[i] != null)
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte)i);
                this.inventorySlots[i].writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }

        par1NBTTagCompound.setTag("Items", nbttaglist);
    }

	@Override
	public void markDirty() {
		
	}

	@Override
	public void openInventory() {}

	@Override
	public void closeInventory() {}
}
