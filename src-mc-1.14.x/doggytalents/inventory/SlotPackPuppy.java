package doggytalents.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

/**
 * @author ProPercivalalb
 **/
public class SlotPackPuppy extends Slot {
   
	private int level;

    public SlotPackPuppy(IInventory iinventory, int i, int j, int k, int level) {
        super(iinventory, i, j, k);
        this.level = level;
    }

    @Override
    public boolean isItemValid(ItemStack itemstack) {
        return this.level != 0;
    }
}
