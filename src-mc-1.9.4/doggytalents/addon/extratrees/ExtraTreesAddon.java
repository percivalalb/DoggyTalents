package doggytalents.addon.extratrees;

import doggytalents.addon.AddonEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

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
		
		//for(int i = 0; i < ExtraTreesLib.PLANKS_1_COUNT; ++i)
		//	DogBedRegistry.CASINGS.registerMaterial(ExtraTreesLib.PLANKS_1_ID, i);
		
	}
}
