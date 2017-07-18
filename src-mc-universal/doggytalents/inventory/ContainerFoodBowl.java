package doggytalents.inventory;

import doggytalents.base.ObjectLib;
import doggytalents.tileentity.TileEntityFoodBowl;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * @author ProPercivalalb
 */
public class ContainerFoodBowl extends Container {
   
	private TileEntityFoodBowl tileEntityFoodBowl;

    public ContainerFoodBowl(IInventory playerInventory, TileEntityFoodBowl tileEntityFoodBowl) {
        this.tileEntityFoodBowl = tileEntityFoodBowl;

        for(int i = 0; i < 1; i++)
            for (int l = 0; l < 5; l++)
                this.addSlotToContainer(new Slot(tileEntityFoodBowl.inventory, l + i * 9, 44 + l * 18, 22 + i * 18));

        for(int j = 0; j < 3; j++)
            for (int i1 = 0; i1 < 9; i1++)
            	this.addSlotToContainer(new Slot(playerInventory, i1 + j * 9 + 9, 8 + i1 * 18, 45 + j * 18));

        for(int k = 0; k < 9; k++)
        	this.addSlotToContainer(new Slot(playerInventory, k, 8 + k * 18, 103));
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return this.tileEntityFoodBowl.inventory.isUsableByPlayer(player);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int i) {
        ItemStack itemstack = ObjectLib.STACK_UTIL.getEmpty();
        Slot slot = (Slot)this.inventorySlots.get(i);

        if(slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if(i < 5) {
                if(!mergeItemStack(itemstack1, 5, inventorySlots.size(), true)) {
                    return ObjectLib.STACK_UTIL.getEmpty();
                }
            }
            else if(!mergeItemStack(itemstack1, 0, 5, false)) {
                return ObjectLib.STACK_UTIL.getEmpty();
            }

            if(ObjectLib.STACK_UTIL.isEmpty(itemstack1))
            	slot.putStack(ObjectLib.STACK_UTIL.getEmpty());
            else
                slot.onSlotChanged();
            
            if(ObjectLib.STACK_UTIL.getCount(itemstack1) == ObjectLib.STACK_UTIL.getCount(itemstack))
                return ObjectLib.STACK_UTIL.getEmpty();
        }

        return itemstack;
    }
}
