package doggytalents.common.item;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

public interface IDyeableArmorItem extends net.minecraft.item.IDyeableArmorItem {

    @Override
    default int getColor(ItemStack stack) {
        CompoundNBT compoundnbt = stack.getTagElement("display");
        return compoundnbt != null && compoundnbt.contains("color", 99) ? compoundnbt.getInt("color") : this.getDefaultColor(stack);
    }

    default int getDefaultColor(ItemStack stack) {
        return 0xFFFFFF;
    }
}
