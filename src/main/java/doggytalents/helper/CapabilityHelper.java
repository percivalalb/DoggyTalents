package doggytalents.helper;

import javax.annotation.Nonnull;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityProvider;

public class CapabilityHelper {

    public static <T> T getOrThrow(CapabilityProvider<?> capProvider, @Nonnull final Capability<T> cap) {
       return capProvider.getCapability(cap).orElseThrow(() -> new RuntimeException("Item handler not present."));
    }
}
