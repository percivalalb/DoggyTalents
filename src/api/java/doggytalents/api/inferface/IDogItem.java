package doggytalents.api.inferface;

import doggytalents.common.entity.DogEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public interface IDogItem {

    /**
     * Implement on item class called when player interacts with a dog
     * @param dogIn The dog the item is being used on
     * @param worldIn The world the dog is in
     * @param playerIn The player who clicked
     * @param handIn The hand used
     * @return The result of the interaction
     */
    public ActionResultType processInteract(DogEntity dogIn, World worldIn, PlayerEntity playerIn, Hand handIn);

    @Deprecated
    default ActionResultType onInteractWithDog(DogEntity dogIn, World worldIn, PlayerEntity playerIn, Hand handIn) {
        return processInteract(dogIn, worldIn, playerIn, handIn);
    }
}
