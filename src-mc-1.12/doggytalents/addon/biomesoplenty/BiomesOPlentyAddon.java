package doggytalents.addon.biomesoplenty;

import doggytalents.addon.AddonEvent;
import doggytalents.api.registry.DogBedRegistry;
import net.minecraft.block.Block;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author ProPercivalalb
 */
public class BiomesOPlentyAddon {

	private static BiomesOPlentyAPI API = new BiomesOPlentyAPI(BiomesOPlentyLib.MOD_ID);
	
	@SubscribeEvent
	public void onPre(AddonEvent.Pre event) {
		if(!Loader.isModLoaded(BiomesOPlentyLib.MOD_ID))
			return;
	}
	
	@SubscribeEvent
	public void onInit(AddonEvent.Init event) {
		if(!Loader.isModLoaded(BiomesOPlentyLib.MOD_ID))
			return;
	}

	@SubscribeEvent
	public void onPost(AddonEvent.Post event) throws Exception {
		if(!Loader.isModLoaded(BiomesOPlentyLib.MOD_ID))
			return;
		
		Block planks1 = Block.getBlockFromName(BiomesOPlentyLib.ITEM_NAME_1);
		
		for(int i = 0; i < BiomesOPlentyLib.PLANKS_1_TEXURE.length; i++)
			DogBedRegistry.CASINGS.registerMaterial(planks1, i, BiomesOPlentyLib.TEXTURE_LOCATION + BiomesOPlentyLib.PLANKS_1_TEXURE[i]);
		
	}
}
