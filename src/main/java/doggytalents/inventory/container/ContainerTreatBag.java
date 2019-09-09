package doggytalents.inventory.container;

import doggytalents.ModContainerTypes;
import doggytalents.ModItems;
import doggytalents.helper.CapabilityHelper;
import doggytalents.item.ItemTreatBag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerTreatBag extends Container {

    public int slot;
    public ItemStack itemstack;
    public ItemStackHandler bagInventory;
    
    public ContainerTreatBag(int windowId, PlayerInventory playerInventory, int slotIn, ItemStack itemstackIn) {
        super(ModContainerTypes.TREAT_BAG, windowId);
        this.slot = slotIn;
        this.itemstack = itemstackIn;
        this.bagInventory = CapabilityHelper.getOrThrow(itemstackIn, ItemTreatBag.TREAT_BAG_CAPABILITY);

        assertInventorySize(playerInventory, 3 * 5);

        for(int l = 0; l < 5; l++) {
            this.addSlot(new SlotItemHandler(this.bagInventory, l, 44 + l * 18, 22));
        }

        for(int j = 0; j < 3; j++) {
            for(int i1 = 0; i1 < 9; i1++) {
                this.addSlot(new Slot(playerInventory, i1 + j * 9 + 9, 8 + i1 * 18, 45 + j * 18));
            }
        }

        for(int k = 0; k < 9; k++) {
            this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 103) {
                @Override
                public boolean canTakeStack(PlayerEntity playerIn) {
                    return ContainerTreatBag.this.slot != this.getSlotIndex();
                }
            });
        }
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = (Slot)this.inventorySlots.get(index);

        if(slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if(index < 5) {
                if(!mergeItemStack(itemstack1, 5, this.inventorySlots.size(), true)) {
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
            
           // if(itemstack1.getCount() == itemstack.getCount())
           //     return ItemStack.EMPTY;
        }

        return itemstack;
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return playerIn.inventory.getStackInSlot(this.slot).getItem() == ModItems.TREAT_BAG;
    }
}
