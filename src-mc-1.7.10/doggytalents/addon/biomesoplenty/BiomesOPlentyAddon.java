package doggytalents.addon.biomesoplenty;

import java.util.Locale;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import doggytalents.addon.AddonEvent;
import doggytalents.api.registry.DogBedRegistry;

/**
 * @author ProPercivalalb
 */
public class BiomesOPlentyAddon {

	private static BiomesOPlentyAPI API = new BiomesOPlentyAPI(BiomesOPlentyLib.MOD_NAME);
	
	@SubscribeEvent
	public void onPre(AddonEvent.Pre event) {
		if(!Loader.isModLoaded(BiomesOPlentyLib.MOD_NAME))
			return;
	}
	
	@SubscribeEvent
	public void onInit(AddonEvent.Init event) {
		if(!Loader.isModLoaded(BiomesOPlentyLib.MOD_NAME))
			return;
	}

	@SubscribeEvent
	public void onPost(AddonEvent.Post event) throws Exception {
		if(!Loader.isModLoaded(BiomesOPlentyLib.MOD_NAME))
			return;
		
		for(int i = 0; i < BiomesOPlentyLib.PLANKS_1_COUNT; ++i)
			DogBedRegistry.CASINGS.registerMaterial(BiomesOPlentyLib.PLANKS_1_ID, i);
		
	}
}
