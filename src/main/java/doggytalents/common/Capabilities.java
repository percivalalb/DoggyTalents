package doggytalents.common;

import doggytalents.common.inventory.PackPuppyItemHandler;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;

public class Capabilities {

    public static void registerCaps(final RegisterCapabilitiesEvent event) {
        event.register(PackPuppyItemHandler.class);
    }
}
