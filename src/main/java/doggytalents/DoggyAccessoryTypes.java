package doggytalents;

import doggytalents.api.registry.AccessoryType;
import doggytalents.common.lib.Constants;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class DoggyAccessoryTypes {

    public static final DeferredRegister<AccessoryType> ACCESSORY_TYPES = DeferredRegister.create(DoggyRegistries.Keys.ACCESSORY_TYPE_REGISTRY, Constants.MOD_ID);

    public static final RegistryObject<AccessoryType> COLLAR = register("collar");
    public static final RegistryObject<AccessoryType> CLOTHING = register("clothing");
    public static final RegistryObject<AccessoryType> GLASSES = register("glasses");
    public static final RegistryObject<AccessoryType> BAND = register("band");
    public static final RegistryObject<AccessoryType> HEAD = register("head");
    public static final RegistryObject<AccessoryType> FEET = register("feet");
    public static final RegistryObject<AccessoryType> TAIL = register("tail");

    private static RegistryObject<AccessoryType> register(final String name) {
        return register(name, () -> new AccessoryType());
    }

    private static <T extends AccessoryType> RegistryObject<T> register(final String name, final Supplier<T> sup) {
        return ACCESSORY_TYPES.register(name, sup);
    }
}
