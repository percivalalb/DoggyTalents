package doggytalents;

import doggytalents.lib.Reference;
import doggytalents.serializer.TalentListSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DataSerializerEntry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(Reference.MOD_ID)
public class ModSerializers {
	
	public static final DataSerializerEntry TALENT_LEVEL_LIST = null;
	
	@Mod.EventBusSubscriber(modid = Reference.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class Registration {
	    
	    @SubscribeEvent
	    public static void registerTileEntities(final RegistryEvent.Register<DataSerializerEntry> event){
	    	IForgeRegistry<DataSerializerEntry> serializerRegistry = event.getRegistry();
	    	DoggyTalentsMod.LOGGER.info("Registering Serializers");
	        serializerRegistry.register(new DataSerializerEntry(new TalentListSerializer()).setRegistryName(Reference.MOD_ID, "talent_level_list"));
	        DoggyTalentsMod.LOGGER.info("Finished Registering Serializers");
	    }
    }
}