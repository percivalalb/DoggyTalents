package doggytalents;

import java.util.function.Supplier;

import doggytalents.lib.Reference;
import doggytalents.serializer.TalentListSerializer;
import net.minecraft.network.datasync.IDataSerializer;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DataSerializerEntry;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModSerializers {

    public static final DeferredRegister<DataSerializerEntry> SERIALIZERS = new DeferredRegister<>(ForgeRegistries.DATA_SERIALIZERS, Reference.MOD_ID);

    public static final RegistryObject<DataSerializerEntry> TALENT_LEVEL_SERIALIZER = register2("talent_level_list", TalentListSerializer::new);

    private static <X extends IDataSerializer<?>> RegistryObject<DataSerializerEntry> register2(final String name, final Supplier<X> factory) {
        return register(name, () -> new DataSerializerEntry(factory.get()));
    }

    private static RegistryObject<DataSerializerEntry> register(final String name, final Supplier<DataSerializerEntry> sup) {
        return SERIALIZERS.register(name, sup);
    }
}