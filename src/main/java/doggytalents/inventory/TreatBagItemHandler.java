package doggytalents.inventory;

import javax.annotation.Nonnull;

import doggytalents.ModItems;
import doggytalents.api.inferface.IDogItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.items.ItemStackHandler;

public class TreatBagItemHandler extends ItemStackHandler {

    private ItemStack bag;

    public TreatBagItemHandler(ItemStack bag) {
        super(5);
        this.bag = bag;

        CompoundNBT inventoryNBT = bag.getChildTag("inventory");
        if (inventoryNBT != null) {
            this.deserializeNBT(inventoryNBT);
        }
    }

    @Override
    protected void onContentsChanged(int slot) {
        this.bag.getOrCreateChildTag("inventory").merge(this.serializeNBT());
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return stack.getItem() instanceof IDogItem || stack.getItem() == ModItems.CHEW_STICK.get() || stack.getItem() == Items.ROTTEN_FLESH || (stack.getItem().isFood() && stack.getItem().getFood().isMeat());
    }
}
