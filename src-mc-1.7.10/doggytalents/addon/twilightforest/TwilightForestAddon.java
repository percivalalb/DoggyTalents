package doggytalents.addon.twilightforest;

import java.util.Locale;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import doggytalents.addon.AddonEvent;
import doggytalents.api.registry.DogBedRegistry;

/**
 * @author ProPercivalalb
 */
public class TwilightForestAddon {

	private static TwilightForestAPI API = new TwilightForestAPI(TwilightForestLib.MOD_NAME);
	
	@SubscribeEvent
	public void onPre(AddonEvent.Pre event) {
		if(!Loader.isModLoaded(TwilightForestLib.MOD_NAME))
			return;
	}
	
	@SubscribeEvent
	public void onInit(AddonEvent.Init event) {
		if(!Loader.isModLoaded(TwilightForestLib.MOD_NAME))
			return;
	}

	@SubscribeEvent
	public void onPost(AddonEvent.Post event) throws Exception {
		if(!Loader.isModLoaded(TwilightForestLib.MOD_NAME))
			return;

		DogBedRegistry.CASINGS.registerMaterial(TwilightForestLib.PLANKS_1_ID, 0);
		DogBedRegistry.CASINGS.registerMaterial(TwilightForestLib.PLANKS_1_ID, 2);
		DogBedRegistry.CASINGS.registerMaterial(TwilightForestLib.PLANKS_1_ID, 3);
		DogBedRegistry.CASINGS.registerMaterial(TwilightForestLib.PLANKS_1_ID, 4);
	}
}
