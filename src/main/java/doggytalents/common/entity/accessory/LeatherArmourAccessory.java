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
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.common.util.Constants;

import java.util.function.Supplier;

public class LeatherArmourAccessory extends ArmourAccessory {

    public LeatherArmourAccessory(Supplier<? extends AccessoryType> typeIn, Supplier<? extends IItemProvider> itemIn) {
        super(typeIn, itemIn);
    }

    @Override
    public AccessoryInstance create(ItemStack armourStack) {
        if (armourStack.isEmpty()) {
            if (this.of(DoggyAccessories.LEATHER_HELMET)) {
                armourStack = new ItemStack(Items.LEATHER_HELMET);
            } else {
                throw new IllegalArgumentException();
            }
        }

        return new LeatherArmourAccessory.Instance(armourStack.copy());
    }

    @Override
    public AccessoryInstance read(CompoundNBT compound) {
        AccessoryInstance inst = super.read(compound);

        if (this.of(DoggyAccessories.LEATHER_HELMET)) {
            // Backwards compatibility
            if (compound.contains("color", Constants.NBT.TAG_ANY_NUMERIC)) {
                int color = compound.getInt("color");

                Instance def = inst.cast(Instance.class);
                if (def.armourStack.getItem() instanceof IDyeableArmorItem) {
                    ((IDyeableArmorItem) def.armourStack.getItem()).setColor(def.armourStack, color);
                }

                def.color = ColourCache.make(color);
            }
        }

        return inst;
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
