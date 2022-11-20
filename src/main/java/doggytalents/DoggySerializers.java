package doggytalents;

import doggytalents.common.entity.serializers.*;
import doggytalents.common.lib.Constants;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class DoggySerializers {

    public static final DeferredRegister<EntityDataSerializer<?>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.Keys.ENTITY_DATA_SERIALIZERS, Constants.MOD_ID);

    public static final RegistryObject<EntityDataSerializer> TALENT_SERIALIZER = register2("talents", TalentListSerializer::new);
    public static final RegistryObject<EntityDataSerializer> COLLAR_TYPE_SERIALIZER = register2("collar", CollarSerializer::new);
    public static final RegistryObject<EntityDataSerializer> ACCESSORY_SERIALIZER = register2("accessories", AccessorySerializer::new);
    public static final RegistryObject<EntityDataSerializer> GENDER_SERIALIZER = register2("gender", GenderSerializer::new);
    public static final RegistryObject<EntityDataSerializer> MODE_SERIALIZER = register2("mode", ModeSerializer::new);
    public static final RegistryObject<EntityDataSerializer> DOG_LEVEL_SERIALIZER = register2("dog_level", DogLevelSerializer::new);
    public static final RegistryObject<EntityDataSerializer> BED_LOC_SERIALIZER = register2("dog_bed_location", BedLocationsSerializer::new);

    private static <X extends EntityDataSerializer<?>> RegistryObject<EntityDataSerializer> register2(final String name, final Supplier<X> factory) {
        return register(name, () -> factory.get());
    }

    private static RegistryObject<EntityDataSerializer> register(final String name, final Supplier<EntityDataSerializer> sup) {
        return SERIALIZERS.register(name, sup);
    }
}
