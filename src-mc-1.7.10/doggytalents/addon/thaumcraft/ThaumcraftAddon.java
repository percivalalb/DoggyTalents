package doggytalents.addon.thaumcraft;

import java.util.Locale;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import doggytalents.addon.AddonEvent;
import doggytalents.api.registry.DogBedRegistry;

/**
 * @author ProPercivalalb
 */
public class ThaumcraftAddon {

	private static ThaumcraftAPI API = new ThaumcraftAPI(ThaumcraftLib.MOD_NAME);
	
	@SubscribeEvent
	public void onPre(AddonEvent.Pre event) {
		if(!Loader.isModLoaded(ThaumcraftLib.MOD_NAME))
			return;
	}
	
	@SubscribeEvent
	public void onInit(AddonEvent.Init event) {
		if(!Loader.isModLoaded(ThaumcraftLib.MOD_NAME))
			return;
	}

	@SubscribeEvent
	public void onPost(AddonEvent.Post event) throws Exception {
		if(!Loader.isModLoaded(ThaumcraftLib.MOD_NAME))
			return;

		DogBedRegistry.CASINGS.registerMaterial(ThaumcraftLib.PLANKS_1_ID, 6);
		DogBedRegistry.CASINGS.registerMaterial(ThaumcraftLib.PLANKS_1_ID, 7);
		
	}
}
