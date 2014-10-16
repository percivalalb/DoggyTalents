package doggytalents.core.addon.forestry;

import java.util.Locale;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import doggytalents.api.DogBedManager;
import doggytalents.core.addon.AddonEvent;

/**
 * @author ProPercivalalb
 */
public class ForestryAddon {

	private static ForestryAPI API = new ForestryAPI(ForestryLib.MOD_NAME);
	
	@SubscribeEvent
	public void onPre(AddonEvent.Pre event) {
		if(!Loader.isModLoaded(ForestryLib.MOD_NAME))
			return;
	}
	
	@SubscribeEvent
	public void onInit(AddonEvent.Init event) {
		if(!Loader.isModLoaded(ForestryLib.MOD_NAME))
			return;
	}

	@SubscribeEvent
	public void onPost(AddonEvent.Post event) throws Exception {
		if(!Loader.isModLoaded(ForestryLib.MOD_NAME))
			return;
		
		DogBedManager.registerBedWood("for.planks", 0);
		DogBedManager.registerBedWood("for.planks", 1);
		DogBedManager.registerBedWood("for.planks", 2);
		DogBedManager.registerBedWood("for.planks", 3);
		DogBedManager.registerBedWood("for.planks", 4);
		DogBedManager.registerBedWood("for.planks", 5);
		DogBedManager.registerBedWood("for.planks", 6);
		DogBedManager.registerBedWood("for.planks", 7);
		DogBedManager.registerBedWood("for.planks", 8);
		DogBedManager.registerBedWood("for.planks", 9);
		DogBedManager.registerBedWood("for.planks", 10);
		DogBedManager.registerBedWood("for.planks", 11);
		DogBedManager.registerBedWood("for.planks", 12);
		DogBedManager.registerBedWood("for.planks", 13);
		DogBedManager.registerBedWood("for.planks", 14);
		DogBedManager.registerBedWood("for.planks", 15);
		
		DogBedManager.registerBedWood("for.planks2", 0);
		DogBedManager.registerBedWood("for.planks2", 1);
		DogBedManager.registerBedWood("for.planks2", 2);
		DogBedManager.registerBedWood("for.planks2", 3);
		DogBedManager.registerBedWood("for.planks2", 4);
		DogBedManager.registerBedWood("for.planks2", 5);
		DogBedManager.registerBedWood("for.planks2", 6);
		DogBedManager.registerBedWood("for.planks2", 7);
		
		
	}
}
