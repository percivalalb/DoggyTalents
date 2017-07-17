package doggytalents.addon.forestry;

import doggytalents.addon.AddonEvent;
import doggytalents.api.registry.DogBedRegistry;
import net.minecraft.block.Block;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

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
		
		Block planks1 = Block.getBlockFromName(ForestryLib.ITEM_NAME_1);
		Block planks2 = Block.getBlockFromName(ForestryLib.ITEM_NAME_2);
		
		for(int i = 0; i < ForestryLib.PLANKS_1_TEXURE.length; i++)
			DogBedRegistry.CASINGS.registerMaterial(planks1, i, ForestryLib.TEXTURE_LOCATION + ForestryLib.PLANKS_1_TEXURE[i]);
		
		for(int i = 0; i < ForestryLib.PLANKS_2_TEXURE.length; i++)
			DogBedRegistry.CASINGS.registerMaterial(planks2, i, ForestryLib.TEXTURE_LOCATION + ForestryLib.PLANKS_2_TEXURE[i]);
		
	}
}
