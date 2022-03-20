package doggytalents.api.registry;

import java.util.Comparator;
import java.util.Optional;
import java.util.function.Supplier;

import doggytalents.api.DoggyTalentsAPI;
import doggytalents.api.inferface.AbstractDogEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.IRegistryDelegate;

public class AccessoryInstance {

    public static final byte RENDER_TOP = 1;
    public static final byte RENDER_BOTTOM = -1;
    public static final byte RENDER_DEFAULT = 0;
    public static final Comparator<AccessoryInstance> RENDER_SORTER = Comparator.comparing(AccessoryInstance::getRenderIndex);

    @Deprecated // Do not call directly use AccessoryInstance#getAccessory
    private final Accessory accessory;

    public AccessoryInstance(Accessory typeIn) {
        this.accessory = typeIn;
    }

    public Accessory getAccessory() {
        return this.accessory;
    }

    public <T extends Accessory> boolean of(Supplier<T> accessoryIn) {
        return this.accessory.of(accessoryIn);
    }

    public <T extends Accessory> boolean of(T accessoryIn) {
        return this.accessory.of(accessoryIn);
    }

    public <T extends Accessory> boolean of(IRegistryDelegate<T> accessoryDelegateIn) {
        return this.accessory.of(accessoryDelegateIn);
    }

    public <T extends AccessoryType> boolean ofType(Supplier<T> accessoryTypeIn) {
        return this.ofType(accessoryTypeIn.get());
    }

    public <T extends AccessoryType> boolean ofType(T accessoryTypeIn) {
        return this.ofType(accessoryTypeIn.delegate);
    }

    public <T extends AccessoryType> boolean ofType(IRegistryDelegate<T> accessoryTypeDelegateIn) {
        return accessoryTypeDelegateIn.equals(this.accessory.getType().delegate);
    }

    public AccessoryInstance copy() {
        return new AccessoryInstance(this.accessory);
    }

    public ItemStack getReturnItem() {
        return this.getAccessory().getReturnItem(this);
    }

    public byte getRenderIndex() {
        return this.getAccessory().getRenderLayer();
    }

    public final void writeInstance(CompoundTag compound) {
        ResourceLocation rl = this.getAccessory().getRegistryName();
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
