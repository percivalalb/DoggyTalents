package doggytalents.addon.forestry;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import doggytalents.addon.AddonEvent;
import doggytalents.addon.AddonManager;
import doggytalents.api.registry.DogBedRegistry;
import net.minecraft.block.Block;

/**
 * @author ProPercivalalb
 */
public class ForestryAddon {

	@SubscribeEvent
	public void onPost(AddonEvent.Post event) throws Exception {
		if(!AddonManager.areModsLoaded(ForestryLib.MOD_NAME))
			return;
		
		Block planks1 = Block.getBlockFromName(ForestryLib.ITEM_NAME_1);
		Block planks2 = Block.getBlockFromName(ForestryLib.ITEM_NAME_2);
		
		for(int i = 0; i < ForestryLib.PLANKS_1_TEXURE.length; i++)
			DogBedRegistry.CASINGS.registerMaterial(planks1, i, ForestryLib.TEXTURE_LOCATION + ForestryLib.PLANKS_1_TEXURE[i]);
		
		for(int i = 0; i < ForestryLib.PLANKS_2_TEXURE.length; i++)
			DogBedRegistry.CASINGS.registerMaterial(planks2, i, ForestryLib.TEXTURE_LOCATION + ForestryLib.PLANKS_2_TEXURE[i]);
		
	}
}
