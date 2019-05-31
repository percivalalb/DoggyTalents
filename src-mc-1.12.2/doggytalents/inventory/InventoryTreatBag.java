package doggytalents.inventory;

import doggytalents.ModItems;
import doggytalents.api.inferface.IDogInteractItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class InventoryTreatBag extends InventoryBasic {

	public EntityPlayer player;
	public int slot;
	public ItemStack itemstack;
	
	public InventoryTreatBag(EntityPlayer playerIn, int slotIn, ItemStack itemstackIn) {
		super("doggytalents.treatbag", false, 5);
		this.player = playerIn;
		this.slot = slotIn;
		this.itemstack = itemstackIn.copy();
	}
	
	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
        return stack.getItem() instanceof IDogInteractItem || stack.getItem() == ModItems.CHEW_STICK || stack.getItem() == Items.ROTTEN_FLESH || (stack.getItem() instanceof ItemFood && ((ItemFood)stack.getItem()).isWolfsFavoriteMeat()); 
    }

	@Override
	public void openInventory(EntityPlayer player) {
		if(this.itemstack.hasTagCompound() && this.itemstack.getTagCompound().hasKey("inventory", 10)) {
			NBTTagList nbttaglist = this.itemstack.getTagCompound().getCompoundTag("inventory").getTagList("Items", 10);
	
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
        
        if(!this.itemstack.getTagCompound().hasKey("inventory"))
        	this.itemstack.getTagCompound().setTag("inventory", new NBTTagCompound());
        
        this.itemstack.getTagCompound().getCompoundTag("inventory").setTag("Items", nbttaglist);
        
        this.player.inventory.setInventorySlotContents(this.slot, this.itemstack);
	}
}
