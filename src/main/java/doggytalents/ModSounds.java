package doggytalents;

import doggytalents.api.lib.Reference;
import doggytalents.lib.SoundNames;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.registries.IForgeRegistry;

@ObjectHolder(Reference.MOD_ID)
public class ModSounds {
    
    public static final SoundEvent WHISTLE_SHORT = null;
    public static final SoundEvent WHISTLE_LONG = null;
    
    @Mod.EventBusSubscriber(modid = Reference.MOD_ID)
    public static class Registration {
        
        @SubscribeEvent
        public static void registerSoundEvents(final RegistryEvent.Register<SoundEvent> event){
            IForgeRegistry<SoundEvent> soundRegistry = event.getRegistry();
            
            DoggyTalents.LOGGER.debug("Registering Sounds");
            soundRegistry.register(new SoundEvent(SoundNames.WHISTLE_SHORT).setRegistryName(SoundNames.WHISTLE_SHORT));
            soundRegistry.register(new SoundEvent(SoundNames.WHISTLE_LONG).setRegistryName(SoundNames.WHISTLE_LONG));
            DoggyTalents.LOGGER.debug("Finished Registering Sounds");
        }
    }
}