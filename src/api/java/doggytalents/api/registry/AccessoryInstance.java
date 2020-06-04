package doggytalents.api.registry;

import java.util.Comparator;
import java.util.Optional;

import doggytalents.api.DoggyTalentsAPI;
import doggytalents.common.util.NBTUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;

public class AccessoryInstance {

    public static final byte RENDER_TOP = 1;
    public static final byte RENDER_BOTTOM = -1;
    public static final byte RENDER_DEFAULT = 0;
    public static final Comparator<AccessoryInstance> RENDER_SORTER = Comparator.comparing(AccessoryInstance::getRenderIndex);

    @Deprecated // Do not call directly use AccessoryInstance#getAccessory
    private Accessory accessory;

    public AccessoryInstance(Accessory typeIn) {
        this.accessory = typeIn;
    }

    public Accessory getAccessory() {
        return this.accessory;
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

    public final void writeInstance(CompoundNBT compound) {
        NBTUtil.putResourceLocation(compound, "type", this.getAccessory().getRegistryName());
        this.getAccessory().write(this, compound);
    }

    public static Optional<AccessoryInstance> readInstance(CompoundNBT compound) {
        ResourceLocation rl = NBTUtil.getResourceLocation(compound, "type");
        if (DoggyTalentsAPI.ACCESSORIES.containsKey(rl)) {
            Accessory type = DoggyTalentsAPI.ACCESSORIES.getValue(rl);
            return Optional.of(type.read(compound));
        } else {
            return Optional.empty();
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends AccessoryInstance> T cast(Class<T> type) {
        if (this.getClass().isAssignableFrom(type)) {
            return (T) this;
        } else {
            throw new RuntimeException("Could not cast " + this.getClass().getName() + " to " + type.getName());
        }
    }

}
