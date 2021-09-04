package doggytalents.common.util;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import java.util.ArrayList;
import java.util.List;

public class ItemUtil {

    public static List<ItemStack> getContentOverview(IItemHandler inventory) {
        List<ItemStack> items = new ArrayList<>(inventory.getSlots());

        SLOT: for (int i = 0; i < inventory.getSlots(); i++) {
            ItemStack itemstack = inventory.getStackInSlot(i).copy();

            if (!itemstack.isEmpty()) {
                for (int j = 0; j < items.size(); j++) {
                    ItemStack stack = items.get(j);
                    if (ItemStack.isSameIgnoreDurability(stack, itemstack) && ItemStack.tagMatches(stack, itemstack)) {
                        stack.grow(itemstack.getCount());
                        continue SLOT;
                    }
                }
            }
        }

        return items;
    }
}
