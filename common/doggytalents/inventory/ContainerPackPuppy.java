package doggytalents.inventory;

import java.util.List;

import doggytalents.entity.EntityDTDoggy;
import doggytalents.entity.data.EnumTalents;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * @author ProPercivalalb
 */
public class ContainerPackPuppy extends Container {
	
    private EntityDTDoggy wolf;

    public ContainerPackPuppy(InventoryPlayer par1IInventory, EntityDTDoggy entitydtdoggy) {
        wolf = entitydtdoggy;
        int i = 0;
        inventoryItemStacks.clear();
        inventorySlots.clear();

        for (int j = 0; j < 3; j++)
        {
            for (int i1 = 0; i1 < wolf.talents.getTalentLevel(EnumTalents.PACKPUPPY); i1++)
            {
                this.addSlotToContainer(new SlotPackPuppy(entitydtdoggy.inventory, i, 79 + 18 * i1, 1 + 18 * j + 24, wolf));
                i++;
            }
        }

        int var3;
        int var4;
        
        for (var3 = 0; var3 < 3; ++var3)
        {
            for (var4 = 0; var4 < 9; ++var4)
            {
                this.addSlotToContainer(new Slot(par1IInventory, var4 + var3 * 9 + 9, 8 + var4 * 18, 84 + var3 * 18));
            }
        }

        for (var3 = 0; var3 < 9; ++var3)
        {
            this.addSlotToContainer(new Slot(par1IInventory, var3, 8 + var3 * 18, 142));
        }
    }

    /**
     * Called to transfer a stack from one inventory to the other eg. when shift clicking.
     */
    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int i)
    {
        ItemStack itemstack = null;
        Slot slot = (Slot)inventorySlots.get(i);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (i < 3 * wolf.talents.getTalentLevel(EnumTalents.PACKPUPPY))
            {
                if (!mergeItemStack(itemstack1, 3 * wolf.talents.getTalentLevel(EnumTalents.PACKPUPPY), inventorySlots.size(), true))
                {
                    return null;
                }
            }
            else if (!mergeItemStack(itemstack1, 0, 3 * wolf.talents.getTalentLevel(EnumTalents.PACKPUPPY), false))
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

    @Override
    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        return wolf.inventory.isUseableByPlayer(entityplayer);
    }
}
