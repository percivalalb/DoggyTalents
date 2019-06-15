package doggytalents;

import java.util.Map;

import com.google.common.base.Optional;

import doggytalents.api.inferface.Talent;
import doggytalents.lib.Reference;
import doggytalents.serializers.OptionalTextComponentSerializer;
import doggytalents.serializers.TalentListSerializer;
import net.minecraft.network.datasync.DataSerializer;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.fml.common.registry.IForgeRegistry;

@ObjectHolder(Reference.MOD_ID)
public class ModSerializers {
	
	public static DataSerializer<Map<Talent, Integer>> TALENT_LEVEL_SERIALIZER = new TalentListSerializer();
	public static DataSerializer<Optional<ITextComponent>> OPTIONAL_TEXT_COMPONENT_SERIALIZER = new OptionalTextComponentSerializer();
	
    public static class Registration {
	    
	    public static void registerSerializers() {
	    	DoggyTalents.LOGGER.info("Registering Serializers");
	    	DataSerializers.registerSerializer(TALENT_LEVEL_SERIALIZER);
	    	DataSerializers.registerSerializer(OPTIONAL_TEXT_COMPONENT_SERIALIZER);
	        
	        DoggyTalents.LOGGER.info("Finished Registering Serializers");
	    }
    }
}