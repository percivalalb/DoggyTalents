package doggytalents.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class ContainerTreatBag extends Container {

	public PlayerEntity player;
	public int slot;
	public ItemStack itemstack;
	public InventoryTreatBag inventoryTreatBag;
		
	public ContainerTreatBag(int windowId, PlayerEntity playerIn, int slotIn, ItemStack itemstackIn) {
		super(null, windowId); //TODO
		this.player = playerIn;
		this.slot = slotIn;
		this.itemstack = itemstackIn;
		this.inventoryTreatBag = new InventoryTreatBag(playerIn, slotIn, this.itemstack);
		
        for(int l = 0; l < 5; l++)
        	this.addSlot(new Slot(this.inventoryTreatBag, l, 44 + l * 18, 22) {
        		@Override
        		public boolean isItemValid(ItemStack stack) {
        	        return ContainerTreatBag.this.inventoryTreatBag.isItemValidForSlot(this.getSlotIndex(), stack);
        	    }
        	});

        for(int j = 0; j < 3; j++)
            for(int i1 = 0; i1 < 9; i1++)
            	this.addSlot(new Slot(playerIn.inventory, i1 + j * 9 + 9, 8 + i1 * 18, 45 + j * 18));

        for(int k = 0; k < 9; k++)
        	this.addSlot(new Slot(playerIn.inventory, k, 8 + k * 18, 103) {
        		@Override
        		public boolean canTakeStack(PlayerEntity playerIn) {
        			return this.getSlotIndex() != ContainerTreatBag.this.slot;
        		}
        	});
        
        this.inventoryTreatBag.openInventory(playerIn);
	}

	@Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
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

            if(itemstack1.isEmpty())
            	slot.putStack(ItemStack.EMPTY);
            else
                slot.onSlotChanged();
            
            if(itemstack1.getCount() == itemstack.getCount())
                return ItemStack.EMPTY;
        }

        return itemstack;
    }
	
	@Override
	public boolean canInteractWith(PlayerEntity playerIn) {
		return playerIn.inventory.getStackInSlot(this.slot).isItemEqual(this.itemstack);
	}

	@Override
    public void onContainerClosed(PlayerEntity playerIn) {
		super.onContainerClosed(playerIn);
		this.inventoryTreatBag.closeInventory(playerIn);
    }
}
