package doggytalents.api.inferface;

import net.minecraft.world.item.ItemStack;

@FunctionalInterface
public interface IDogFoodPredicate {

    /**
     * Determines if the stack could ever be food for a dog, i.e
     * the stack could be fed to the dog under certain conditions
     * Used to check if the stack can go in food bowl or treat bag
     * @param stackIn The stack
     * @return If the start could ever be fed to a dog
     */
    public boolean isFood(ItemStack stackIn);
}
