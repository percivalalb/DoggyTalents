package doggytalents.common.item;

import doggytalents.api.inferface.AbstractDog;
import doggytalents.api.registry.AccessoryInstance;
import doggytalents.common.entity.accessory.DyeableAccessory;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public class DyeableAccessoryItem extends AccessoryItem implements IDyeableArmorItem {

    private Supplier<? extends DyeableAccessory> accessory;

    public DyeableAccessoryItem(Supplier<? extends DyeableAccessory> accessoryIn, Properties properties) {
        super(accessoryIn, properties);
        this.accessory = accessoryIn;
    }

    @Override
    public AccessoryInstance createInstance(AbstractDog dogIn, ItemStack stack, Player playerIn) {
        return this.accessory.get().create(this.getColor(stack));
    }

    @Override
    public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
        if (this.allowdedIn(group)) {
            ItemStack stack = new ItemStack(this);
            this.setColor(stack, this.getDefaultColor(stack));
            items.add(stack);
        }
    }
}
