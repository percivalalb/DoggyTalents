package doggytalents.api.inferface;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;

/**
 * Implement in Item class
 */
public interface IDogFedCallback {

    /**
     * Callback when a food item is consumed by a dog, as example
     * used by the treat stick to apply potion effects
     * @param dogIn The dog eating the item
     * @param stackIn The stack that is being eaten, currently the itemstack has not been changed
     * @param entityIn The entity who fed the dog, usually the player. Can be null probably meaning the dog ate on its own
     */
    public void onItemConsumed(IDogEntity dogIn, ItemStack stackIn, @Nullable Entity entityIn);
}
