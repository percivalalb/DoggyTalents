package doggytalents.addon.tropicraft;

import doggytalents.addon.AddonEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author ProPercivalalb
 */
public class TropicraftAddon {

	private static TropicraftAPI API = new TropicraftAPI(TropicraftLib.MOD_NAME);
	
	@SubscribeEvent
	public void onPre(AddonEvent.Pre event) {
		if(!Loader.isModLoaded(TropicraftLib.MOD_NAME))
			return;
	}
	
	@SubscribeEvent
	public void onInit(AddonEvent.Init event) {
		if(!Loader.isModLoaded(TropicraftLib.MOD_NAME))
			return;
	}

	@SubscribeEvent
	public void onPost(AddonEvent.Post event) throws Exception {
		if(!Loader.isModLoaded(TropicraftLib.MOD_NAME))
			return;
		
		//DogBedRegistry.CASINGS.registerMaterial(TropicraftLib.PLANKS_ID, 0);
		//DogBedRegistry.CASINGS.registerMaterial(TropicraftLib.BAMBOO_ID, 0);
		
	}
}
