package doggytalents.inventory;

import doggytalents.ModItems;
import doggytalents.api.IDogTreat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class InventoryTreatBag extends InventoryBasic {

	public ItemStack itemstack;
	
	public InventoryTreatBag(ItemStack itemstackIn) {
		super("doggytalents:coin_bag", false, 5);
		this.itemstack = itemstackIn;
	}
	
	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
        return stack.getItem() instanceof IDogTreat || stack.getItem() == ModItems.CHEW_STICK || stack.getItem() == Items.ROTTEN_FLESH || (stack.getItem() instanceof ItemFood && ((ItemFood)stack.getItem()).isWolfsFavoriteMeat()); 
    }

	@Override
	public void openInventory(EntityPlayer player) {
		if(this.itemstack.hasTagCompound() && this.itemstack.getTagCompound().hasKey("inventory", 10)) {
			NBTTagList nbttaglist = this.itemstack.getSubCompound("inventory").getTagList("Items", 10);
	
	        for(int i = 0; i < nbttaglist.tagCount(); ++i) {
	            NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
	            int j = nbttagcompound.getByte("Slot") & 255;
	
	            if(j >= 0 && j < this.getSizeInventory())
	                this.setInventorySlotContents(j, new ItemStack(nbttagcompound));
	        }
		}
	}

	@Override
	public void closeInventory(EntityPlayer player) {
		NBTTagList nbttaglist = new NBTTagList();

        for(int i = 0; i < this.getSizeInventory(); ++i) {
            ItemStack itemstack = this.getStackInSlot(i);

            if(!itemstack.isEmpty()) {
                NBTTagCompound nbttagcompound = new NBTTagCompound();
                nbttagcompound.setByte("Slot", (byte)i);
                itemstack.writeToNBT(nbttagcompound);
                nbttaglist.appendTag(nbttagcompound);
            }
        }

        if(!this.itemstack.hasTagCompound())
        	this.itemstack.setTagCompound(new NBTTagCompound());
        
        this.itemstack.getOrCreateSubCompound("inventory").setTag("Items", nbttaglist);
       
	}
	
	@Override
	 public ItemStack addItem(ItemStack stack)
	    {
	        ItemStack itemstack = stack;

	        for (int i = 0; i < this.getSizeInventory(); ++i)
	        {
	            ItemStack itemstack1 = this.getStackInSlot(i);

	            if (itemstack1.isEmpty())
	            {
	                this.setInventorySlotContents(i, itemstack);
	                this.markDirty();
	                return ItemStack.EMPTY;
	            }

	            if (ItemStack.areItemsEqual(itemstack1, itemstack))
	            {
	                int j = Math.min(this.getInventoryStackLimit(), itemstack1.getMaxStackSize());
	                int k = Math.min(itemstack.getCount(), j - itemstack1.getCount());

	                if (k > 0)
	                {
	                    itemstack1.grow(k);
	                    itemstack.shrink(k);

	                    if (itemstack.isEmpty())
	                    {
	                        this.markDirty();
	                        return ItemStack.EMPTY;
	                    }
	                }
	            }
	        }

	        if (itemstack.getCount() != stack.getCount())
	        {
	            this.markDirty();
	        }

	        return itemstack;
	    }
}
