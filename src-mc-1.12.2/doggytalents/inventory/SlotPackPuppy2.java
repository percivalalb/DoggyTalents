package doggytalents.inventory;

import doggytalents.entity.EntityDog;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * @author ProPercivalalb
 **/
public class SlotPackPuppy2 extends Slot {
   
	private EntityDog dog;

    public SlotPackPuppy2(IInventory iinventory, int index, int xPosition, int yPosition, EntityDog dog) {
        super(iinventory, index, xPosition, yPosition);
        this.dog = dog;
    }

    @Override
    public boolean isItemValid(ItemStack itemstack) {
        return this.dog.talents.getLevel("farmdog") != 0;
    }
}
