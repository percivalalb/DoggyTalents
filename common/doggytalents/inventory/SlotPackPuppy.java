package doggytalents.inventory;

import doggytalents.entity.EntityDTDoggy;
import doggytalents.entity.data.EnumTalents;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * @author ProPercivalalb
 **/
public class SlotPackPuppy extends Slot {
   
	private EntityDTDoggy doggy;

    public SlotPackPuppy(IInventory iinventory, int i, int j, int k, EntityDTDoggy entitydtdoggy) {
        super(iinventory, i, j, k);
        doggy = entitydtdoggy;
    }

    /**
     * Check if the stack is a valid item for this slot. Always true beside for the armor slots.
     */
    @Override
    public boolean isItemValid(ItemStack itemstack) {
        return doggy.talents.getTalentLevel(EnumTalents.PACKPUPPY) != 0;
    }
}
