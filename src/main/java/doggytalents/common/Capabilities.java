package doggytalents.common;

import doggytalents.common.inventory.PackPuppyItemHandler;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.ListTag;
import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;

public class Capabilities {

    public static void registerCaps(final RegisterCapabilitiesEvent event) {
        event.register(PackPuppyItemHandler.class);
    }
}
