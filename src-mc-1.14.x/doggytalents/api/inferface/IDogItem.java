package doggytalents.api.inferface;

import doggytalents.entity.EntityDog;
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
    public ActionResultType onInteractWithDog(EntityDog dogIn, World worldIn, PlayerEntity playerIn, Hand handIn);
}
