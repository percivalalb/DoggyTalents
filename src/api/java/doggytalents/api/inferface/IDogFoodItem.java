package doggytalents.api.inferface;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;

/**
 * Implement in Item class
 */
public interface IDogFoodItem extends IDogFedCallback {

    /**
     * @param dogIn The dog
     * @param stackIn The stack
     * @param entityIn The entity who is querying the items food value, null if no entity is involved
     * @return The food value of the item, returning non-positive value will class the item not as food
     */
    public int getFoodValue(IDogEntity dogIn, ItemStack stackIn, @Nullable Entity entityIn);
}
