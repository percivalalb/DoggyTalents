package doggytalents;

import java.util.Map;

import doggytalents.api.inferface.Talent;
import doggytalents.lib.Reference;
import doggytalents.serializer.TalentListSerializer;
import net.minecraft.network.datasync.IDataSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.DataSerializerEntry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(Reference.MOD_ID)
public class ModSerializers {
    
    public static final DataSerializerEntry TALENT_LEVEL_LIST = null;
    
    public static IDataSerializer<Map<Talent, Integer>> TALENT_LEVEL_SERIALIZER = new TalentListSerializer();
    
    public static void registerSerializers(final RegistryEvent.Register<DataSerializerEntry> event) {
        IForgeRegistry<DataSerializerEntry> serializerRegistry = event.getRegistry();
        DoggyTalentsMod.LOGGER.debug("Registering Serializers");
        serializerRegistry.register(new DataSerializerEntry(TALENT_LEVEL_SERIALIZER).setRegistryName(Reference.MOD_ID, "talent_level_list"));
        DoggyTalentsMod.LOGGER.debug("Finished Registering Serializers");
    }
}