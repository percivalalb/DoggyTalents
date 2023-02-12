package doggytalents.common.item;

import doggytalents.api.inferface.AbstractDogEntity;
import doggytalents.api.registry.AccessoryInstance;
import doggytalents.common.entity.accessory.DyeableAccessory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public class DyeableAccessoryItem extends AccessoryItem implements IDyeableArmorItem {

    private Supplier<? extends DyeableAccessory> accessory;

    public DyeableAccessoryItem(Supplier<? extends DyeableAccessory> accessoryIn, Properties properties) {
        super(accessoryIn, properties);
        this.accessory = accessoryIn;
    }

    @Override
    public AccessoryInstance createInstance(AbstractDogEntity dogIn, ItemStack stack, Player playerIn) {
        return this.accessory.get().create(this.getColor(stack));
    }

}
