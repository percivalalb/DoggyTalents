package doggytalents.core.proxy;

import cpw.mods.fml.client.registry.RenderingRegistry;
import doggytalents.client.render.RenderDTDoggy;
import doggytalents.entity.EntityDTDoggy;

public class ClientProxy extends CommonProxy {
	
	public void registerRenders() {
		RenderingRegistry.registerEntityRenderingHandler(EntityDTDoggy.class, new RenderDTDoggy());
	}
	
}
