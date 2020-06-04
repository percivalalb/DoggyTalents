package doggytalents.common.util;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

public class ItemUtil {

    public static List<ItemStack> getContentOverview(IItemHandler inventory) {
        List<ItemStack> items = Lists.newArrayList();


        for (int i = 0; i < inventory.getSlots(); i++) {
            ItemStack itemstack = inventory.getStackInSlot(i).copy();

            if (!itemstack.isEmpty()) {
                boolean flag = false;

                for (int j = 0; j < items.size(); j++) {
                    ItemStack stack = items.get(j);
                    if (ItemStack.areItemsEqualIgnoreDurability(stack, itemstack) && ItemStack.areItemStackTagsEqual(stack, itemstack)) {
                        stack.grow(itemstack.getCount());
                        flag = true;
                    }
                }

                if (!flag) {
                    items.add(itemstack);
                }
            }
        }
        return items;
    }
}
