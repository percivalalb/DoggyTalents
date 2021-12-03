package doggytalents.common.entity.accessory;

import doggytalents.DoggyAccessories;
import doggytalents.api.inferface.IColoredObject;
import doggytalents.api.registry.AccessoryInstance;
import doggytalents.api.registry.AccessoryType;
import doggytalents.common.util.ColourCache;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

import java.util.function.Supplier;

public class LeatherArmourAccessory extends ArmourAccessory {

    public LeatherArmourAccessory(Supplier<? extends AccessoryType> typeIn, Supplier<? extends ItemLike> itemIn) {
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
    public AccessoryInstance read(CompoundTag compound) {
        AccessoryInstance inst = super.read(compound);

        if (this.of(DoggyAccessories.LEATHER_HELMET)) {
            // Backwards compatibility
            if (compound.contains("color", Tag.TAG_ANY_NUMERIC)) {
                int color = compound.getInt("color");

                Instance def = inst.cast(Instance.class);
                if (def.armourStack.getItem() instanceof DyeableLeatherItem) {
                    ((DyeableLeatherItem) def.armourStack.getItem()).setColor(def.armourStack, color);
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

            if (armourStack.getItem() instanceof DyeableLeatherItem) {
                this.color = ColourCache.make(((DyeableLeatherItem) armourStack.getItem()).getColor(armourStack));
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
