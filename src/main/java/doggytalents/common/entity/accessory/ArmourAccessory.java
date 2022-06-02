package doggytalents.common.entity.accessory;

import doggytalents.DoggyAccessories;
import doggytalents.api.inferface.AbstractDog;
import doggytalents.api.inferface.IDogAlteration;
import doggytalents.api.registry.Accessory;
import doggytalents.api.registry.AccessoryInstance;
import doggytalents.api.registry.AccessoryType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class ArmourAccessory extends Accessory {

    public ArmourAccessory(Supplier<? extends AccessoryType> typeIn, Supplier<? extends ItemLike> itemIn) {
        super(typeIn, itemIn);
    }

    public AccessoryInstance create(ItemStack armourStack) {
        if (armourStack.isEmpty()) {
            if (this.of(DoggyAccessories.IRON_HELMET)) {
                armourStack = new ItemStack(Items.IRON_HELMET);
            } else if (this.of(DoggyAccessories.DIAMOND_HELMET)) {
                armourStack = new ItemStack(Items.DIAMOND_HELMET);
            } else if (this.of(DoggyAccessories.GOLDEN_HELMET)) {
                armourStack = new ItemStack(Items.GOLDEN_HELMET);
            } else if (this.of(DoggyAccessories.CHAINMAIL_HELMET)) {
                armourStack = new ItemStack(Items.CHAINMAIL_HELMET);
            } else if (this.of(DoggyAccessories.TURTLE_HELMET)) {
                armourStack = new ItemStack(Items.TURTLE_HELMET);
            } else if (this.of(DoggyAccessories.NETHERITE_HELMET)) {
                armourStack = new ItemStack(Items.NETHERITE_HELMET);
            } else {
                throw new IllegalArgumentException();
            }
        }

        return new ArmourAccessory.Instance(armourStack.copy());
    }

    @Override
    public AccessoryInstance getDefault() {
        return new ArmourAccessory.Instance(ItemStack.EMPTY);
    }

    @Override
    public AccessoryInstance createFromStack(ItemStack stackIn) {
        return this.create(stackIn.copy());
    }

    @Override
    public AccessoryInstance createInstance(FriendlyByteBuf buf) {
        return this.create(buf.readItem());
    }

    @Override
    public void write(AccessoryInstance instance, FriendlyByteBuf buf) {
        ArmourAccessory.Instance exact = instance.cast(ArmourAccessory.Instance.class);
        buf.writeItem(exact.armourStack);
    }

    @Override
    public void write(AccessoryInstance instance, CompoundTag compound) {
        ArmourAccessory.Instance exact = instance.cast(ArmourAccessory.Instance.class);
        CompoundTag itemTag = new CompoundTag();
        exact.armourStack.save(itemTag);
        compound.put("item", itemTag);
    }

    @Override
    public AccessoryInstance read(CompoundTag compound) {
        return this.create(ItemStack.of(compound.getCompound("item")));
    }

    public class Instance extends AccessoryInstance implements IDogAlteration {

        @Nonnull
        protected final ItemStack armourStack;

        public Instance(ItemStack armourStack) {
            super(ArmourAccessory.this);
            this.armourStack = armourStack;
        }

        @Override
        public void init(AbstractDog dogIn) {
            EquipmentSlot slotType = null;

            if (this.armourStack.getItem() instanceof ArmorItem) {
                slotType = ((ArmorItem) this.armourStack.getItem()).getSlot();
            }

            dogIn.getAttributes().addTransientAttributeModifiers(this.armourStack.getAttributeModifiers(slotType));
        }

        @Override
        public void remove(AbstractDog dogIn) {
            EquipmentSlot slotType = null;

            if (this.armourStack.getItem() instanceof ArmorItem) {
                slotType = ((ArmorItem) this.armourStack.getItem()).getSlot();
            }

            dogIn.getAttributes().removeAttributeModifiers(this.armourStack.getAttributeModifiers(slotType));
        }

        @Override
        public AccessoryInstance copy() {
            return new ArmourAccessory.Instance(this.armourStack.copy());
        }

        public boolean hasEffect() {
            return this.armourStack.hasFoil();
        }

        @Override
        public ItemStack getReturnItem() {
            return this.armourStack;
        }
    }
}
