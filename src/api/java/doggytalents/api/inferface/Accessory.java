package doggytalents.api.inferface;

import java.util.function.Supplier;

import javax.annotation.Nullable;

import doggytalents.api.DoggyTalentsAPI;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.Util;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class Accessory extends ForgeRegistryEntry<Accessory> {

    @Nullable
    private String translationKey;
    @Deprecated // Do not call directly use Accessory#getReturnItem
    private Supplier<ItemStack> stack;

    @Deprecated // Do not call directly use Accessory#getType
    private final Supplier<? extends AccessoryType> type;

    public Accessory(Supplier<? extends AccessoryType> typeIn, Supplier<ItemStack> stackIn, int x) {
        this.type = typeIn;
        this.stack = stackIn;
    }

    public Accessory(Supplier<? extends AccessoryType> typeIn, Supplier<? extends IItemProvider> itemIn) {
        this(typeIn, () -> new ItemStack(itemIn.get()), 0);
    }

    public String getTranslationKey() {
        if(this.translationKey == null) {
            this.translationKey = Util.makeTranslationKey("accessory", DoggyTalentsAPI.ACCESSORIES.getKey(this));
        }
        return this.translationKey;
    }

    public byte getRenderLayer() {
        return AccessoryInstance.RENDER_DEFAULT;
    }

    public AccessoryInstance createInstance(PacketBuffer buf) {
        return this.getDefault();
    }

    public AccessoryInstance getDefault() {
        return new AccessoryInstance(this);
    }

    public void write(AccessoryInstance instance, PacketBuffer buf) {

    }

    public void write(AccessoryInstance instance, CompoundNBT compound) {

    }

    public AccessoryInstance read(CompoundNBT compound) {
        return this.getDefault();
    }

    public ItemStack getReturnItem(AccessoryInstance instance) {
        return this.stack.get();
    }

    public final AccessoryType getType() {
        return this.type.get();
    }
}
