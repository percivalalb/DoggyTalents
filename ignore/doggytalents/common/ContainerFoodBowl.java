package doggytalents.common;

import java.util.List;

import doggytalents.tileentity.TileEntityFoodBowl;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerFoodBowl extends Container
{
    private TileEntityFoodBowl thisdogfoodbowl;

    public ContainerFoodBowl(IInventory iinventory, TileEntityFoodBowl tileentitydogfoodbowl)
    {
        thisdogfoodbowl = tileentitydogfoodbowl;

        for (int i = 0; i < 1; i++)
        {
            for (int l = 0; l < 5; l++)
            {
                this.addSlotToContainer(new Slot(tileentitydogfoodbowl, l + i * 9, 44 + l * 18, 22 + i * 18));
            }
        }

        for (int j = 0; j < 3; j++)
        {
            for (int i1 = 0; i1 < 9; i1++)
            {
            	this.addSlotToContainer(new Slot(iinventory, i1 + j * 9 + 9, 8 + i1 * 18, 45 + j * 18));
            }
        }

        for (int k = 0; k < 9; k++)
        {
        	this.addSlotToContainer(new Slot(iinventory, k, 8 + k * 18, 103));
        }
    }

    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        return true;
    }

    /**
     * Called to transfer a stack from one inventory to the other eg. when shift clicking.
     */
    public ItemStack transferStackInSlot(EntityPlayer player, int i)
    {
        ItemStack itemstack = null;
        Slot slot = (Slot)inventorySlots.get(i);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (i < 5)
            {
                if (!mergeItemStack(itemstack1, 5, inventorySlots.size(), true))
                {
                    return null;
                }
            }
            else if (!mergeItemStack(itemstack1, 0, 5, false))
            {
                return null;
            }

            if (itemstack1.stackSize == 0)
            {
                slot.putStack(null);
            }
            else
            {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }
}
