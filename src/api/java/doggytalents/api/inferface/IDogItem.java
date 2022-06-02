package doggytalents.api.inferface;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;

public interface IDogItem {

    /**
     * Implement on item class called when player interacts with a dog
     * @param dogIn The dog the item is being used on
     * @param worldIn The world the dog is in
     * @param playerIn The player who clicked
     * @param handIn The hand used
     * @return The result of the interaction
     */
    public InteractionResult processInteract(AbstractDog dogIn, Level worldIn, Player playerIn, InteractionHand handIn);

    @Deprecated
    default InteractionResult onInteractWithDog(AbstractDog dogIn, Level worldIn, Player playerIn, InteractionHand handIn) {
        return processInteract(dogIn, worldIn, playerIn, handIn);
    }
}
