package doggytalents.api.inferface;

import javax.annotation.Nullable;

import doggytalents.common.entity.DogEntity;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;

public interface IDogFoodHandler {

    /**
     * Determines if the stack could ever be food for a dog, i.e
     * the stack could be fed to the dog under certain conditions
     * Used to check if the stack can go in food bowl or treat bag
     * @param stackIn The stack
     * @return If the start could ever be fed to a dog
     */
    public boolean isFood(ItemStack stackIn);

    /**
     * Checks if the dog can eat the start
     * used by the treat stick to apply potion effects
     * @param dogIn The dog eating the item
     * @param stackIn The stack that is being eaten, DO NOT alter the start in this method
     * @param entityIn The entity who fed the dog, usually the player. Can be null probably meaning the dog ate on its own
     * @return If the dog can eat the stack, {@link #consume} is called to eat the stack
     */
    public boolean canConsume(@Nullable DogEntity dogIn, ItemStack stackIn, @Nullable Entity entityIn);

    /**
     * Actually eat the stack,
     * @param dogIn The dog eating the item
     * @param stackIn The stack that is being eaten
     * @param entityIn The entity who fed the dog, usually the player. Can be null probably meaning the dog ate on its own
     * @return
     */
    public boolean consume(DogEntity dogIn, ItemStack stackIn, @Nullable Entity entityIn);
}
