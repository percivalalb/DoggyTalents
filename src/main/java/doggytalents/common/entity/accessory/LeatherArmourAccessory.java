package doggytalents.common.entity.accessory;

import doggytalents.DoggyAccessories;
import doggytalents.api.inferface.IColoredObject;
import doggytalents.api.inferface.IDogAlteration;
import doggytalents.api.registry.AccessoryInstance;
import doggytalents.api.registry.AccessoryType;
import doggytalents.common.util.ColourCache;
import net.minecraft.item.IDyeableArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.IItemProvider;

import java.util.function.Supplier;

public class LeatherArmourAccessory extends ArmourAccessory {

    public LeatherArmourAccessory(Supplier<? extends AccessoryType> typeIn, Supplier<? extends IItemProvider> itemIn) {
        super(typeIn, itemIn);
    }

    @Override
    public AccessoryInstance create(ItemStack armourStack) {
        if (armourStack.isEmpty()) {
            if (this.delegate.equals(DoggyAccessories.IRON_HELMET.get().delegate)) {
                armourStack = new ItemStack(Items.IRON_HELMET);
            } else if (this.delegate.equals(DoggyAccessories.DIAMOND_HELMET.get().delegate)) {
                armourStack = new ItemStack(Items.DIAMOND_HELMET);
            } // TODO

            //else throw Illegi Arguemnt
        }

        return new LeatherArmourAccessory.Instance(armourStack.copy());
    }

    public class Instance extends ArmourAccessory.Instance implements IColoredObject {

        private ColourCache color = ColourCache.WHITE;

        public Instance(ItemStack armourStack) {
            super(armourStack);

            if (armourStack.getItem() instanceof IDyeableArmorItem) {
                this.color = ColourCache.make(((IDyeableArmorItem) armourStack.getItem()).getColor(armourStack));
            }
        }

        @Override
        public AccessoryInstance copy() {
            return new LeatherArmourAccessory.Instance(this.armourStack.copy());
        }

        @Override
        public float[] getColor() {
            return this.color.getFloatArray();
        }
    }
}
