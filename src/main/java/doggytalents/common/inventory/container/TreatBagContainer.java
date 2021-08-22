package doggytalents.common.inventory.container;

import doggytalents.DoggyContainerTypes;
import doggytalents.DoggyItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class TreatBagContainer extends Container {

    public int slot;
    public ItemStack itemstack;
    public IItemHandler bagInventory;

    public TreatBagContainer(int windowId, PlayerInventory playerInventory, int slotIn, ItemStack itemstackIn) {
        super(DoggyContainerTypes.TREAT_BAG.get(), windowId);
        this.slot = slotIn;
        this.itemstack = itemstackIn;
        this.bagInventory = itemstackIn.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElseThrow(() -> new RuntimeException("Item handler not present."));

        checkContainerSize(playerInventory, 3 * 5);

        for (int l = 0; l < 5; l++) {
            this.addSlot(new SlotItemHandler(this.bagInventory, l, 44 + l * 18, 22));
        }

        for (int j = 0; j < 3; j++) {
            for (int i1 = 0; i1 < 9; i1++) {
                this.addSlot(new Slot(playerInventory, i1 + j * 9 + 9, 8 + i1 * 18, 45 + j * 18));
            }
        }

        for (int k = 0; k < 9; k++) {
            this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 103) {
                @Override
                public boolean mayPickup(PlayerEntity playerIn) {
                    return TreatBagContainer.this.slot != this.getSlotIndex();
                }
            });
        }
    }

    @Override
    public ItemStack quickMoveStack(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();

            if (index < 5) {
                if (!moveItemStackTo(itemstack1, 5, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (!moveItemStackTo(itemstack1, 0, 5, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }
        }

        return itemstack;
    }

    @Override
    public boolean stillValid(PlayerEntity playerIn) {
        return playerIn.inventory.getItem(this.slot).getItem() == DoggyItems.TREAT_BAG.get();
    }
}
