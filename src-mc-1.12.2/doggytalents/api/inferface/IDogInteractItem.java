package doggytalents.api.inferface;

import doggytalents.entity.EntityDog;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public interface IDogInteractItem {

    /**
     * Implement on item class called when player interacts with a dog
     * @param dogIn The dog the item is being used on
     * @param worldIn The world the dog is in
     * @param playerIn
     * @param handIn The hand
     * @return The result
     */
    public EnumActionResult onInteractWithDog(EntityDog dogIn, World worldIn, EntityPlayer playerIn, EnumHand handIn);
}
