package doggytalents.addon.extratrees;

import doggytalents.addon.AddonEvent;
import doggytalents.addon.AddonManager;
import doggytalents.api.registry.DogBedRegistry;
import net.minecraft.block.Block;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author ProPercivalalb
 */
public class ExtraTreesAddon {

	@SubscribeEvent
	public void onPost(AddonEvent.Post event) throws Exception {
		if(!AddonManager.areModsLoaded(ExtraTreesLib.MOD_NAME))
			return;
		
		Block planks1 = Block.getBlockFromName(ExtraTreesLib.ITEM_NAME_1);
		Block planks2 = Block.getBlockFromName(ExtraTreesLib.ITEM_NAME_2);
		Block planks3 = Block.getBlockFromName(ExtraTreesLib.ITEM_NAME_2);
		
		for(int i = 0; i < ExtraTreesLib.PLANKS_1_TEXURE.length; i++)
			DogBedRegistry.CASINGS.registerMaterial(planks1, i, ExtraTreesLib.TEXTURE_LOCATION + ExtraTreesLib.PLANKS_1_TEXURE[i]);
		
		for(int i = 0; i < ExtraTreesLib.PLANKS_2_TEXURE.length; i++)
			DogBedRegistry.CASINGS.registerMaterial(planks2, i, ExtraTreesLib.TEXTURE_LOCATION + ExtraTreesLib.PLANKS_2_TEXURE[i]);
		
		for(int i = 0; i < ExtraTreesLib.PLANKS_3_TEXURE.length; i++)
			DogBedRegistry.CASINGS.registerMaterial(planks3, i, ExtraTreesLib.TEXTURE_LOCATION + ExtraTreesLib.PLANKS_3_TEXURE[i]);
		
	}
}
