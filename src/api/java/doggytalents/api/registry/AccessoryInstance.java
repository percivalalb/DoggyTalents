package doggytalents.api.registry;

import doggytalents.api.DoggyTalentsAPI;
import doggytalents.api.inferface.AbstractDogEntity;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.Comparator;
import java.util.Optional;
import java.util.function.Supplier;

public class AccessoryInstance {

    public static final byte RENDER_TOP = 1;
    public static final byte RENDER_BOTTOM = -1;
    public static final byte RENDER_DEFAULT = 0;
    public static final Comparator<AccessoryInstance> RENDER_SORTER = Comparator.comparing(AccessoryInstance::getRenderIndex);

    @Deprecated // Do not call directly use AccessoryInstance#getAccessory
    private final Accessory accessoryDelegate;

    public AccessoryInstance(Accessory typeIn) {
        this.accessoryDelegate = typeIn;
    }

    public Accessory getAccessory() {
        return this.accessoryDelegate;
    }

    public <T extends Accessory> boolean of(Supplier<T> accessoryIn) {
        return this.of(accessoryIn.get());
    }

    public <T extends Accessory> boolean of(T accessoryIn) {
        return this.accessoryDelegate == accessoryIn;
    }

    public <T extends AccessoryType> boolean ofType(Supplier<T> accessoryTypeIn) {
        return this.ofType(accessoryTypeIn.get());
    }

    public <T extends AccessoryType> boolean ofType(T accessoryTypeIn) {
        return this.ofType(DoggyTalentsAPI.ACCESSORY_TYPE.get().getKey(accessoryTypeIn));
    }

    public <T extends AccessoryType> boolean ofType(ResourceLocation accessoryTypeDelegateIn) {
        return DoggyTalentsAPI.ACCESSORY_TYPE.get().getKey(this.accessoryDelegate.getType()).equals(accessoryTypeDelegateIn);
    }

    public AccessoryInstance copy() {
        return new AccessoryInstance(this.accessoryDelegate);
    }

    public ItemStack getReturnItem() {
        return this.getAccessory().getReturnItem(this);
    }

    public byte getRenderIndex() {
        return this.getAccessory().getRenderLayer();
    }

    public final void writeInstance(CompoundTag compound) {
        ResourceLocation rl = DoggyTalentsAPI.ACCESSORIES.get().getKey(this.accessoryDelegate);
        if (rl != null) {
            compound.putString("type", rl.toString());
        }

        this.getAccessory().write(this, compound);
    }

    /**
     * Reads the accessory from the given NBT data. If the accessory id is not
     * valid or an exception is thrown during loading then an empty optional
     * is returned.
     */
    public static Optional<AccessoryInstance> readInstance(CompoundTag compound) {
        ResourceLocation rl = null;
        try {
            rl = ResourceLocation.tryParse(compound.getString("type"));
            if (DoggyTalentsAPI.ACCESSORIES.get().containsKey(rl)) {
                Accessory type = DoggyTalentsAPI.ACCESSORIES.get().getValue(rl);
                return Optional.of(type.read(compound));
            } else {
                DoggyTalentsAPI.LOGGER.warn("Failed to load accessory {}", compound);
            }
        } catch (Exception e) {
            DoggyTalentsAPI.LOGGER.warn("Failed to load accessory {}", rl);
        }

        return Optional.empty();
    }

    @SuppressWarnings("unchecked")
    public <T extends AccessoryInstance> T cast(Class<T> type) {
        if (type.isAssignableFrom(this.getClass())) {
            return (T) this;
        } else {
            throw new RuntimeException("Could not cast " + this.getClass().getName() + " to " + type.getName());
        }
    }

    public ResourceLocation getModelTexture(AbstractDogEntity dog) {
        return this.getAccessory().getModelTexture();
    }

    public boolean usesRenderer(Class layer) {
        return this.getAccessory().usesRenderer(layer);
    }
}
