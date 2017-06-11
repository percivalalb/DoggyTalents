package doggytalents.addon.extrautilites;

import doggytalents.addon.AddonEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author ProPercivalalb
 */
public class ExtraUtilitesAddon {

	private static ExtraUtilitesAPI API = new ExtraUtilitesAPI(ExtraUtilitesLib.MOD_NAME);
	
	@SubscribeEvent
	public void onPre(AddonEvent.Pre event) {
		if(!Loader.isModLoaded(ExtraUtilitesLib.MOD_NAME))
			return;
	}
	
	@SubscribeEvent
	public void onInit(AddonEvent.Init event) {
		if(!Loader.isModLoaded(ExtraUtilitesLib.MOD_NAME))
			return;
	}

	@SubscribeEvent
	public void onPost(AddonEvent.Post event) throws Exception {
		if(!Loader.isModLoaded(ExtraUtilitesLib.MOD_NAME))
			return;
		
		//for(int i = 0; i < ExtraUtilitesLib.PLANKS_1_COUNT; ++i)
		//	DogBedRegistry.CASINGS.registerMaterial(ExtraUtilitesLib.PLANKS_1_ID, i);
		
	}
}
