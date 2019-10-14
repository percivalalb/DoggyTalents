package doggytalents;

import java.util.Map;

import com.google.common.base.Optional;

import doggytalents.api.inferface.Talent;
import doggytalents.lib.Reference;
import doggytalents.serializers.OptionalTextComponentSerializer;
import doggytalents.serializers.TalentListSerializer;
import net.minecraft.network.datasync.DataSerializer;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.registries.DataSerializerEntry;
import net.minecraftforge.registries.IForgeRegistry;

@ObjectHolder(Reference.MOD_ID)
public class ModSerializers {
    
    public static final DataSerializerEntry TALENT_LEVEL_LIST = null;
    public static final DataSerializerEntry OPTIONAL_TEXT_COMPONENT = null;
    
    public static DataSerializer<Map<Talent, Integer>> TALENT_LEVEL_SERIALIZER = new TalentListSerializer();
    public static DataSerializer<Optional<ITextComponent>> OPTIONAL_TEXT_COMPONENT_SERIALIZER = new OptionalTextComponentSerializer();
    
    @Mod.EventBusSubscriber(modid = Reference.MOD_ID)
    public static class Registration {
        
        @SubscribeEvent
        public static void registerTileEntities(final RegistryEvent.Register<DataSerializerEntry> event){
            IForgeRegistry<DataSerializerEntry> serializerRegistry = event.getRegistry();
            DoggyTalents.LOGGER.debug("Registering Serializers");
            serializerRegistry.register(new DataSerializerEntry(TALENT_LEVEL_SERIALIZER).setRegistryName(Reference.MOD_ID, "talent_level_list"));
            serializerRegistry.register(new DataSerializerEntry(OPTIONAL_TEXT_COMPONENT_SERIALIZER).setRegistryName(Reference.MOD_ID, "optional_text_component"));
            
            DoggyTalents.LOGGER.debug("Finished Registering Serializers");
        }
    }
}