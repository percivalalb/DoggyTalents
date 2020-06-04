package doggytalents.api.inferface;

import javax.annotation.Nullable;

import doggytalents.common.entity.DogEntity;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;

public interface IDogFoodHandler {

    public boolean isFood(ItemStack stackIn);

    /**
     * Callback when a food item is consumed by a dog, as example
     * used by the treat stick to apply potion effects
     * @param dogIn The dog eating the item
     * @param stackIn The stack that is being eaten, currently the itemstack has not been changed
     * @param entityIn The entity who fed the dog, usually the player. Can be null probably meaning the dog ate on its own
     */
    public boolean canConsume(@Nullable DogEntity dogIn, ItemStack stackIn, @Nullable Entity entityIn);

    public boolean consume(DogEntity dogIn, ItemStack stackIn, @Nullable Entity entityIn);
}
