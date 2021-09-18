package doggytalents.common.item;

import java.util.function.Supplier;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import net.minecraft.item.Item.Properties;

public class DroolBoneItem extends Item {

    public Supplier<? extends Item> altBone;

    public DroolBoneItem(Supplier<? extends Item> altBone, Properties properties) {
        super(properties);
        this.altBone = altBone;
    }

    @Override
    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemStackIn = playerIn.getItemInHand(handIn);

        if (itemStackIn.getItem() == this) {

            ItemStack returnStack = new ItemStack(this.altBone.get());
            if (itemStackIn.hasTag()) {
                returnStack.setTag(itemStackIn.getTag().copy());
            }

            playerIn.swing(handIn);
            return new ActionResult<ItemStack>(ActionResultType.SUCCESS, returnStack);
        }

        return new ActionResult<ItemStack>(ActionResultType.FAIL, itemStackIn);
    }
}
