package doggytalents.inventory;

import doggytalents.ModItems;
import doggytalents.api.inferface.IDogInteractItem;
import doggytalents.lib.GuiNames;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IInteractionObject;

public class InventoryTreatBag extends InventoryBasic implements IInteractionObject {

	public EntityPlayer player;
	public int slot;
	public ItemStack itemstack;
	
	public InventoryTreatBag(EntityPlayer playerIn, int slotIn, ItemStack itemstackIn) {
		super(new TextComponentTranslation("doggytalents.treat_bag"), 5);
		this.player = playerIn;
		this.slot = slotIn;
		this.itemstack = itemstackIn.copy();
	}
	
	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
        return stack.getItem() instanceof IDogInteractItem || stack.getItem() == ModItems.CHEW_STICK || stack.getItem() == Items.ROTTEN_FLESH || (stack.getItem() instanceof ItemFood && ((ItemFood)stack.getItem()).isMeat()); 
    }

	@Override
	public void openInventory(EntityPlayer player) {
		if(this.itemstack.hasTag() && this.itemstack.getTag().contains("inventory", 10)) {
			NBTTagList nbttaglist = this.itemstack.getTag().getCompound("inventory").getList("Items", 10);
	
	        for(int i = 0; i < nbttaglist.size(); ++i) {
	            NBTTagCompound nbttagcompound = nbttaglist.getCompound(i);
	            int j = nbttagcompound.getByte("Slot") & 255;
	
	            if(j >= 0 && j < this.getSizeInventory())
	                this.setInventorySlotContents(j, ItemStack.read(nbttagcompound));
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
                nbttagcompound.putByte("Slot", (byte)i);
                itemstack.write(nbttagcompound);
                nbttaglist.add(nbttagcompound);
            }
        }

        if(!this.itemstack.hasTag())
        	this.itemstack.setTag(new NBTTagCompound());
        
        if(!this.itemstack.getTag().contains("inventory"))
        	this.itemstack.getTag().put("inventory", new NBTTagCompound());
        
        this.itemstack.getTag().getCompound("inventory").put("Items", nbttaglist);
        
        this.player.inventory.setInventorySlotContents(this.slot, this.itemstack);
	}

	@Override
	public Container createContainer(InventoryPlayer inventory, EntityPlayer player) {
		return new ContainerTreatBag(player, slot, itemstack);
	}

	@Override
	public String getGuiID() {
		return GuiNames.TREAT_BAG;
	}
}
