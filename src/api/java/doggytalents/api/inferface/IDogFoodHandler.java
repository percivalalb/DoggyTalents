package doggytalents.api.inferface;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;

public interface IDogFoodHandler extends IDogFoodPredicate {

    /**
     * Checks if the dog can eat the start
     * used by the treat stick to apply potion effects
     * @param dogIn The dog eating the item
     * @param stackIn The stack that is being eaten, DO NOT alter the start in this method
     * @param entityIn The entity who fed the dog, usually the player. Can be null probably meaning the dog ate on its own
     * @return If the dog can eat the stack, {@link #consume} is called to eat the stack
     */
    public boolean canConsume(AbstractDogEntity dogIn, ItemStack stackIn, @Nullable Entity entityIn);

    /**
     * Actually eat the stack,
     * @param dogIn The dog eating the item
     * @param stackIn The stack that is being eaten
     * @param entityIn The entity who fed the dog, usually the player. Can be null probably meaning the dog ate on its own
     * @return
     */
    public boolean consume(AbstractDogEntity dogIn, ItemStack stackIn, @Nullable Entity entityIn);
}
