package doggytalents.addon.thaumcraft;

import doggytalents.addon.AddonEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

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

		//DogBedRegistry.CASINGS.registerMaterial(ThaumcraftLib.PLANKS_1_ID, 6);
		//DogBedRegistry.CASINGS.registerMaterial(ThaumcraftLib.PLANKS_1_ID, 7);
		
	}
}
