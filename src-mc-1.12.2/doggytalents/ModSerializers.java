package doggytalents;

import java.util.Map;
import java.util.function.Supplier;

import doggytalents.api.inferface.Talent;
import doggytalents.lib.Reference;
import doggytalents.serializers.TalentListSerializer;
import net.minecraft.network.datasync.DataSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.registries.DataSerializerEntry;
import net.minecraftforge.registries.IForgeRegistry;

@ObjectHolder(Reference.MOD_ID)
public class ModSerializers {
	
	public static final DataSerializerEntry TALENT_LEVEL_LIST = null;
	
	@SuppressWarnings("unchecked")
	public static Supplier<DataSerializer<Map<Talent, Integer>>> TALENT_LEVEL_SERIALIZER = () -> (DataSerializer<Map<Talent, Integer>>)TALENT_LEVEL_LIST.getSerializer();
	
	@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
    public static class Registration {
	    
	    @SubscribeEvent
	    public static void registerTileEntities(final RegistryEvent.Register<DataSerializerEntry> event){
	    	IForgeRegistry<DataSerializerEntry> serializerRegistry = event.getRegistry();
	    	DoggyTalents.LOGGER.info("Registering Serializers");
	        serializerRegistry.register(new DataSerializerEntry(new TalentListSerializer()).setRegistryName(Reference.MOD_ID, "talent_level_list"));
	        DoggyTalents.LOGGER.info("Finished Registering Serializers");
	    }
    }
}