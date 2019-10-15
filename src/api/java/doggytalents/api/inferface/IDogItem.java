package doggytalents.api.inferface;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
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
    public EnumActionResult onInteractWithDog(IDogEntity dogIn, World worldIn, EntityPlayer playerIn, EnumHand handIn);
}
