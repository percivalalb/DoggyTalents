package doggytalents.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerTreatBag extends Container {

	public EntityPlayer player;
	public int slot;
	public ItemStack itemstack;
	public InventoryTreatBag inventoryTreatBag;
		
	public ContainerTreatBag(EntityPlayer playerIn, int slotIn, ItemStack itemstackIn) {
		this.player = playerIn;
		this.slot = slotIn;
		this.itemstack = itemstackIn;
		this.inventoryTreatBag = new InventoryTreatBag(this.itemstack);
		
        for(int l = 0; l < 5; l++)
        	this.addSlotToContainer(new Slot(this.inventoryTreatBag, l, 44 + l * 18, 22) {
        		@Override
        		public boolean isItemValid(ItemStack stack) {
        	        return ContainerTreatBag.this.inventoryTreatBag.isItemValidForSlot(this.getSlotIndex(), stack);
        	    }
        	});

        for(int j = 0; j < 3; j++)
            for(int i1 = 0; i1 < 9; i1++)
            	this.addSlotToContainer(new Slot(playerIn.inventory, i1 + j * 9 + 9, 8 + i1 * 18, 45 + j * 18));

        for(int k = 0; k < 9; k++)
        	this.addSlotToContainer(new Slot(playerIn.inventory, k, 8 + k * 18, 103) {
        		@Override
        		public boolean canTakeStack(EntityPlayer playerIn) {
        			return this.getSlotIndex() != ContainerTreatBag.this.slot;
        		}
        	});
        
        this.inventoryTreatBag.openInventory(playerIn);
	}

	@Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = (Slot)this.inventorySlots.get(index);

        if(slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if(index < 5) {
                if(!mergeItemStack(itemstack1, 5, inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            }
            else if(!mergeItemStack(itemstack1, 0, 5, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty())
            	slot.putStack(ItemStack.EMPTY);
            else
                slot.onSlotChanged();
            
            if(itemstack1.getCount() == itemstack.getCount())
                return ItemStack.EMPTY;
        }

        return itemstack;
    }
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return playerIn.inventory.getStackInSlot(this.slot).isItemEqual(this.itemstack);
	}

	@Override
    public void onContainerClosed(EntityPlayer playerIn) {
		super.onContainerClosed(playerIn);
		this.inventoryTreatBag.closeInventory(playerIn);
    }
}
