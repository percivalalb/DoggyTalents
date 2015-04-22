package doggytalents.inventory;

import doggytalents.entity.EntityDog;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * @author ProPercivalalb
 **/
public class SlotPackPuppy extends Slot {
   
	private EntityDog dog;

    public SlotPackPuppy(IInventory iinventory, int i, int j, int k, EntityDog dog) {
        super(iinventory, i, j, k);
        this.dog = dog;
    }

    @Override
    public boolean isItemValid(ItemStack itemstack) {
        return this.dog.talents.getLevel("packpuppy") != 0;
    }
}
