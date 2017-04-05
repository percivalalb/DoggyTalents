package doggytalents.addon.twilightforest;

import doggytalents.addon.AddonEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

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

		//DogBedRegistry.CASINGS.registerMaterial(TwilightForestLib.PLANKS_1_ID, 0);
		//DogBedRegistry.CASINGS.registerMaterial(TwilightForestLib.PLANKS_1_ID, 2);
		//DogBedRegistry.CASINGS.registerMaterial(TwilightForestLib.PLANKS_1_ID, 3);
		//DogBedRegistry.CASINGS.registerMaterial(TwilightForestLib.PLANKS_1_ID, 4);
	}
}
