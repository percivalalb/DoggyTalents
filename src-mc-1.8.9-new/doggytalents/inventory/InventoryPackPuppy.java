package doggytalents.inventory;

import doggytalents.entity.EntityDog;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;

/**
 * @author ProPercivalalb
 */
public class InventoryPackPuppy implements IInventory {
	
	public ItemStack[] inventorySlots;
    private EntityDog dog;

    public InventoryPackPuppy(EntityDog dog) {
        this.dog = dog;
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
	public ItemStack removeStackFromSlot(int index) {
		if (this.inventorySlots[index] != null) {
            ItemStack var2 = this.inventorySlots[index];
            this.inventorySlots[index] = null;
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
	public int getInventoryStackLimit() {
		return 64;
	}
	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return true;
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		return true;
	}
	
	public void readFromNBT(NBTTagCompound tagCompound) {
        NBTTagList nbttaglist = tagCompound.getTagList("packpuppyitems", 10);
        
        for(int i = 0; i < nbttaglist.tagCount(); ++i) {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.getCompoundTagAt(i);
            int j = nbttagcompound1.getByte("Slot") & 255;

            if (j >= 0 && j < this.inventorySlots.length)
                this.inventorySlots[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
        }
    }

    public void writeToNBT(NBTTagCompound tagCompound) {
        NBTTagList nbttaglist = new NBTTagList();

        for(int i = 0; i < this.inventorySlots.length; ++i) {
            if(this.inventorySlots[i] != null) {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte)i);
                this.inventorySlots[i].writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }

        tagCompound.setTag("packpuppyitems", nbttaglist);
    }

	@Override
	public void markDirty() {
		
	}

	@Override
	public void openInventory(EntityPlayer player) {}

	@Override
	public void closeInventory(EntityPlayer player) {}

	@Override
	public String getName() {
		return "container.packpuppy";
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public IChatComponent getDisplayName() {
		return (IChatComponent)(this.hasCustomName() ? new ChatComponentText(this.getName()) : new ChatComponentTranslation(this.getName(), new Object[0]));
	}

	@Override
	public int getField(int id) {
		return 0;
	}

	@Override
	public void setField(int id, int value) {
		
	}

	@Override
	public int getFieldCount() {
		return 0;
	}

	@Override
	public void clear() {
		for(int i = 0; i < this.inventorySlots.length; ++i)
			this.inventorySlots[i] = (ItemStack)null;
	}
}
