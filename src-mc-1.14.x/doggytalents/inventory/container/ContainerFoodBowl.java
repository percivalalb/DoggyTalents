package doggytalents.inventory.container;

import doggytalents.ModContainerTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

/**
 * @author ProPercivalalb
 */
public class ContainerFoodBowl extends Container {
   
    private IInventory tileEntityFoodBowl;

    //CLient method
    public ContainerFoodBowl(int windowId, PlayerInventory playerInventory) {
        this(windowId, playerInventory, new Inventory(5));
    }
    
    //Server method
    public ContainerFoodBowl(int windowId, PlayerInventory playerInventory, IInventory bowlInventory) {
        super(ModContainerTypes.FOOD_BOWL, windowId);
        this.tileEntityFoodBowl = bowlInventory;
        assertInventorySize(bowlInventory, 5);
        bowlInventory.openInventory(playerInventory.player);
        
        for(int i = 0; i < 1; i++)
            for (int l = 0; l < 5; l++)
                this.addSlot(new Slot(bowlInventory, l + i * 9, 44 + l * 18, 22 + i * 18));

        for(int j = 0; j < 3; j++)
            for (int i1 = 0; i1 < 9; i1++)
                this.addSlot(new Slot(playerInventory, i1 + j * 9 + 9, 8 + i1 * 18, 45 + j * 18));

        for(int k = 0; k < 9; k++)
            this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 103));
    }

    @Override
    public boolean canInteractWith(PlayerEntity player) {
        return this.tileEntityFoodBowl.isUsableByPlayer(player);
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity player, int i) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = (Slot)this.inventorySlots.get(i);

        if(slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if(i < 5) {
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
}
