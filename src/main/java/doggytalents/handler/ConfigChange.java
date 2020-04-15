package doggytalents.handler;

import doggytalents.api.lib.Reference;
import doggytalents.configuration.ConfigurationHandler;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ConfigChange {

    @SubscribeEvent
    public void onConfigurationChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event) {
        if(event.getModID().equals(Reference.MOD_ID))
            ConfigurationHandler.loadConfig();
    }
    
}
