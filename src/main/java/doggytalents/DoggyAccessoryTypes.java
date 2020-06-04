package doggytalents;

import doggytalents.api.registry.AccessoryType;
import doggytalents.common.lib.Constants;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(Constants.MOD_ID)
public class DoggyAccessoryTypes {

    public static final AccessoryType COLLAR = null;
    public static final AccessoryType CLOTHING = null;
    public static final AccessoryType GLASSES = null;
    public static final AccessoryType BAND = null;
    public static final AccessoryType HEAD = null;

    public static final void registerAccessoryTypes(final RegistryEvent.Register<AccessoryType> event) {
        IForgeRegistry<AccessoryType> accessoryTypeRegistry = event.getRegistry();

        accessoryTypeRegistry.register(new AccessoryType().setRegistryName("collar"));
        accessoryTypeRegistry.register(new AccessoryType().setRegistryName("clothing"));
        accessoryTypeRegistry.register(new AccessoryType().setRegistryName("glasses"));
        accessoryTypeRegistry.register(new AccessoryType().setRegistryName("band"));
        accessoryTypeRegistry.register(new AccessoryType().setRegistryName("head"));
    }
}