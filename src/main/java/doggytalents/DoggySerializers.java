package doggytalents;

import java.util.function.Supplier;

import doggytalents.common.entity.serializers.AccessorySerializer;
import doggytalents.common.entity.serializers.CollarSerializer;
import doggytalents.common.entity.serializers.DogLevelSerializer;
import doggytalents.common.entity.serializers.GenderSerializer;
import doggytalents.common.entity.serializers.ModeSerializer;
import doggytalents.common.entity.serializers.TalentListSerializer;
import doggytalents.common.lib.Constants;
import net.minecraft.network.datasync.IDataSerializer;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DataSerializerEntry;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class DoggySerializers {

    public static final DeferredRegister<DataSerializerEntry> SERIALIZERS = DeferredRegister.create(ForgeRegistries.DATA_SERIALIZERS, Constants.MOD_ID);

    public static final RegistryObject<DataSerializerEntry> TALENT_LEVEL_SERIALIZER = register2("talent_level_list", TalentListSerializer::new);
    public static final RegistryObject<DataSerializerEntry> COLLAR_TYPE_SERIALIZER = register2("collar", CollarSerializer::new);
    public static final RegistryObject<DataSerializerEntry> ACCESSORY_SERIALIZER = register2("accessories", AccessorySerializer::new);
    public static final RegistryObject<DataSerializerEntry> GENDER_SERIALIZER = register2("gender", GenderSerializer::new);
    public static final RegistryObject<DataSerializerEntry> MODE_SERIALIZER = register2("mode", ModeSerializer::new);
    public static final RegistryObject<DataSerializerEntry> DOG_LEVEL_SERIALIZER = register2("dog_level", DogLevelSerializer::new);

    private static <X extends IDataSerializer<?>> RegistryObject<DataSerializerEntry> register2(final String name, final Supplier<X> factory) {
        return register(name, () -> new DataSerializerEntry(factory.get()));
    }

    private static RegistryObject<DataSerializerEntry> register(final String name, final Supplier<DataSerializerEntry> sup) {
        return SERIALIZERS.register(name, sup);
    }
}