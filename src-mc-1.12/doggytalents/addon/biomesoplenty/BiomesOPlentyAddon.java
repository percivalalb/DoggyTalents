package doggytalents.addon.biomesoplenty;

import doggytalents.addon.AddonEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

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
		
		//for(int i = 0; i < BiomesOPlentyLib.PLANKS_1_COUNT; ++i)
			//DogBedRegistry.CASINGS.registerMaterial(BiomesOPlentyLib.PLANKS_1_ID, i);
		
	}
}
