package doggytalents.item;

import java.util.function.Supplier;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

/**
 * @author ProPercivalalb
 **/
public class ItemDroolBone extends Item {
    
    public Supplier<Item> altBone;
    
    public ItemDroolBone(Supplier<Item> altBone, Properties properties) {
        super(properties);
        this.altBone = altBone;
    }

    
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemStackIn = playerIn.getHeldItem(handIn);

        if(itemStackIn.getItem() == this) {
            
            ItemStack returnStack = new ItemStack(this.altBone.get());
            if(itemStackIn.hasTag()) {
                returnStack.setTag(itemStackIn.getTag().copy());
            }
            
            playerIn.swingArm(handIn);
            return new ActionResult<ItemStack>(ActionResultType.SUCCESS, returnStack);
        }
            
        return new ActionResult<ItemStack>(ActionResultType.FAIL, itemStackIn);
    }
}
