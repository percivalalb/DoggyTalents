package doggytalents.addon.extratrees;

import java.util.Locale;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import doggytalents.addon.AddonEvent;
import doggytalents.api.registry.DogBedRegistry;

/**
 * @author ProPercivalalb
 */
public class ExtraTreesAddon {

	private static ExtraTreesAPI API = new ExtraTreesAPI(ExtraTreesLib.MOD_NAME);
	
	@SubscribeEvent
	public void onPre(AddonEvent.Pre event) {
		if(!Loader.isModLoaded(ExtraTreesLib.MOD_NAME))
			return;
	}
	
	@SubscribeEvent
	public void onInit(AddonEvent.Init event) {
		if(!Loader.isModLoaded(ExtraTreesLib.MOD_NAME))
			return;
	}

	@SubscribeEvent
	public void onPost(AddonEvent.Post event) throws Exception {
		if(!Loader.isModLoaded(ExtraTreesLib.MOD_NAME))
			return;
		
		for(int i = 0; i < ExtraTreesLib.PLANKS_1_COUNT; ++i)
			DogBedRegistry.CASINGS.registerMaterial(ExtraTreesLib.PLANKS_1_ID, i);
		
	}
}
