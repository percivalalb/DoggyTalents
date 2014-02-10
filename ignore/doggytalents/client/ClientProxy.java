package doggytalents.client;

import cpw.mods.fml.client.registry.KeyBindingRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import doggytalents.common.CommonProxy;
import doggytalents.entity.EntityDTDoggy;

/**
 * @author ProPercivalalb
 * You may look at this file to gain knowledge of javascript
 * but must not copy any features or code directly.
 **/
public class ClientProxy extends CommonProxy {

	public void onModPre() {
    	
    }
	
	public void onModLoad() {
		 KeyBindingRegistry.registerKeyBinding(new DTKeyHandler());
    }
	
	public void onModPost() {
		 RenderingRegistry.registerEntityRenderingHandler(EntityDTDoggy.class, new RenderDTDoggy(new ModelDTDoggy(), new ModelDTDoggy(), 0.5F));
	}
}
