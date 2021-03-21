package doggytalents.common.item;

import java.util.function.Supplier;

import doggytalents.api.inferface.AbstractDogEntity;
import doggytalents.api.registry.AccessoryInstance;
import doggytalents.common.entity.accessory.DyeableAccessory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import net.minecraft.item.Item.Properties;

public class DyeableAccessoryItem extends AccessoryItem implements IDyeableArmorItem {

    private Supplier<? extends DyeableAccessory> accessory;

    public DyeableAccessoryItem(Supplier<? extends DyeableAccessory> accessoryIn, Properties properties) {
        super(accessoryIn, properties);
        this.accessory = accessoryIn;
    }

    @Override
    public AccessoryInstance createInstance(AbstractDogEntity dogIn, ItemStack stack, PlayerEntity playerIn) {
        return this.accessory.get().create(this.getColor(stack));
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        if (this.isInGroup(group)) {
            ItemStack stack = new ItemStack(this);
            this.setColor(stack, this.getDefaultColor(stack));
            items.add(stack);
        }
    }
}
