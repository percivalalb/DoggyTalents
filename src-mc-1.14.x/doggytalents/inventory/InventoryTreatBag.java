package doggytalents.inventory;

import doggytalents.ModItems;
import doggytalents.api.inferface.IDogInteractItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class InventoryTreatBag extends Inventory implements INamedContainerProvider {

	public PlayerEntity player;
	public int slot;
	public ItemStack itemstack;
	
	public InventoryTreatBag(PlayerEntity playerIn, int slotIn, ItemStack itemstackIn) {
		super(5);
		this.player = playerIn;
		this.slot = slotIn;
		this.itemstack = itemstackIn.copy();
	}
	
	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
        return stack.getItem() instanceof IDogInteractItem || stack.getItem() == ModItems.CHEW_STICK || stack.getItem() == Items.ROTTEN_FLESH || (stack.getItem().func_219971_r() && stack.getItem().func_219971_r()); 
    }

	@Override
	public void openInventory(PlayerEntity player) {
		if(this.itemstack.hasTag() && this.itemstack.getTag().contains("inventory", 10)) {
			ListNBT nbttaglist = this.itemstack.getTag().getCompound("inventory").getList("Items", 10);
	
	        for(int i = 0; i < nbttaglist.size(); ++i) {
	            CompoundNBT nbttagcompound = nbttaglist.getCompound(i);
	            int j = nbttagcompound.getByte("Slot") & 255;
	
	            if(j >= 0 && j < this.getSizeInventory())
	                this.setInventorySlotContents(j, ItemStack.read(nbttagcompound));
	        }
		}
	}

	@Override
	public void closeInventory(PlayerEntity player) {
		ListNBT nbttaglist = new ListNBT();

        for(int i = 0; i < this.getSizeInventory(); ++i) {
            ItemStack itemstack = this.getStackInSlot(i);

            if(!itemstack.isEmpty()) {
                CompoundNBT nbttagcompound = new CompoundNBT();
                nbttagcompound.putByte("Slot", (byte)i);
                itemstack.write(nbttagcompound);
                nbttaglist.add(nbttagcompound);
            }
        }

        if(!this.itemstack.hasTag())
        	this.itemstack.setTag(new CompoundNBT());
        
        if(!this.itemstack.getTag().contains("inventory"))
        	this.itemstack.getTag().put("inventory", new CompoundNBT());
        
        this.itemstack.getTag().getCompound("inventory").put("Items", nbttaglist);
        
        this.player.inventory.setInventorySlotContents(this.slot, this.itemstack);
	}

	@Override
	public Container createMenu(int windowId, PlayerInventory inventory, PlayerEntity player) {
		return new ContainerTreatBag(windowId, player, slot, itemstack);
	}

	@Override
	public ITextComponent getDisplayName() {
		return new TranslationTextComponent("doggytalents.treat_bag");
	}
}
