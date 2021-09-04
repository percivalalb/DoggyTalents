package doggytalents.common.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public interface IDyeableArmorItem extends net.minecraft.world.item.DyeableLeatherItem {

    @Override
    default int getColor(ItemStack stack) {
        CompoundTag compoundnbt = stack.getTagElement("display");
        return compoundnbt != null && compoundnbt.contains("color", 99) ? compoundnbt.getInt("color") : this.getDefaultColor(stack);
    }

    default int getDefaultColor(ItemStack stack) {
        return 0xFFFFFF;
    }
}
