package doggytalents;

import doggytalents.lib.Reference;
import doggytalents.lib.SoundNames;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.fml.common.registry.IForgeRegistry;

public class ModSounds {
	
	public static SoundEvent WHISTLE_SHORT = null;
	public static SoundEvent WHISTLE_LONG = null;
	
	@Mod.EventBusSubscriber
    public static class Registration {
	    
	    @SubscribeEvent
	    public static void registerSoundEvents(final RegistryEvent.Register<SoundEvent> event){
	    	IForgeRegistry<SoundEvent> soundRegistry = event.getRegistry();
	    	
	    	DoggyTalents.LOGGER.info("Registering Sounds");
	        soundRegistry.register(WHISTLE_SHORT = new SoundEvent(SoundNames.WHISTLE_SHORT).setRegistryName(SoundNames.WHISTLE_SHORT));
	        soundRegistry.register(WHISTLE_LONG = new SoundEvent(SoundNames.WHISTLE_LONG).setRegistryName(SoundNames.WHISTLE_LONG));
	        DoggyTalents.LOGGER.info("Finished Registering Sounds");
	    }
    }
}