package doggytalents;

import doggytalents.common.entity.serializers.*;
import doggytalents.common.lib.Constants;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DataSerializerEntry;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class DoggySerializers {

    public static final DeferredRegister<DataSerializerEntry> SERIALIZERS = DeferredRegister.create(ForgeRegistries.DATA_SERIALIZERS, Constants.MOD_ID);

    public static final RegistryObject<DataSerializerEntry> TALENT_SERIALIZER = register2("talents", TalentListSerializer::new);
    public static final RegistryObject<DataSerializerEntry> COLLAR_TYPE_SERIALIZER = register2("collar", CollarSerializer::new);
    public static final RegistryObject<DataSerializerEntry> ACCESSORY_SERIALIZER = register2("accessories", AccessorySerializer::new);
    public static final RegistryObject<DataSerializerEntry> GENDER_SERIALIZER = register2("gender", GenderSerializer::new);
    public static final RegistryObject<DataSerializerEntry> MODE_SERIALIZER = register2("mode", ModeSerializer::new);
    public static final RegistryObject<DataSerializerEntry> DOG_LEVEL_SERIALIZER = register2("dog_level", DogLevelSerializer::new);
    public static final RegistryObject<DataSerializerEntry> BED_LOC_SERIALIZER = register2("dog_bed_location", BedLocationsSerializer::new);

    private static <X extends EntityDataSerializer<?>> RegistryObject<DataSerializerEntry> register2(final String name, final Supplier<X> factory) {
        return register(name, () -> new DataSerializerEntry(factory.get()));
    }

    private static RegistryObject<DataSerializerEntry> register(final String name, final Supplier<DataSerializerEntry> sup) {
        return SERIALIZERS.register(name, sup);
    }
}
