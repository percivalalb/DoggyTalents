package doggytalents;

import doggytalents.lib.Reference;
import doggytalents.lib.SoundNames;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(Reference.MOD_ID)
public class ModSounds {
	
	public static final SoundEvent WHISTLE_SHORT = null;
	public static final SoundEvent WHISTLE_LONG = null;
	
	@Mod.EventBusSubscriber(modid = Reference.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class Registration {
	    
	    @SubscribeEvent
	    public static void registerSoundEvents(final RegistryEvent.Register<SoundEvent> event){
	    	IForgeRegistry<SoundEvent> soundRegistry = event.getRegistry();
	    	
	    	DoggyTalentsMod.LOGGER.info("Registering Sounds");
	        soundRegistry.register(new SoundEvent(SoundNames.WHISTLE_SHORT).setRegistryName(SoundNames.WHISTLE_SHORT));
	        soundRegistry.register(new SoundEvent(SoundNames.WHISTLE_LONG).setRegistryName(SoundNames.WHISTLE_LONG));
	        DoggyTalentsMod.LOGGER.info("Finished Registering Sounds");
	    }
    }
}