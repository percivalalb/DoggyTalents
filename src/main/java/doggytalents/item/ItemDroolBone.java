package doggytalents.item;

import java.util.function.Supplier;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

/**
 * @author ProPercivalalb
 **/
public class ItemDroolBone extends ItemDT {
    
    public Supplier<Item> altBone;
    
    public ItemDroolBone(Supplier<Item> altBone) {
        super();
        this.altBone = altBone;
    }

    
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack itemStackIn = playerIn.getHeldItem(handIn);

        if(itemStackIn.getItem() == this) {
            
            ItemStack returnStack = new ItemStack(this.altBone.get());
            if(itemStackIn.hasTagCompound()) {
                returnStack.setTagCompound(itemStackIn.getTagCompound().copy());
            }
            
            playerIn.swingArm(handIn);
            return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, returnStack);
        }
            
        return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemStackIn);
    }
}
