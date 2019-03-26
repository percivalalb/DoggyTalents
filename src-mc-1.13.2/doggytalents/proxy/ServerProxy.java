package doggytalents.proxy;

import doggytalents.DoggyTalentsMod;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class ServerProxy extends SideProxy {
	
	public ServerProxy() {
		super();
        DoggyTalentsMod.LOGGER.debug("SideProxy.Server init");
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::serverSetup);
    }

    private void serverSetup(FMLDedicatedServerSetupEvent event) {
        DoggyTalentsMod.LOGGER.debug("SideProxy.Server serverSetup");
    }
}