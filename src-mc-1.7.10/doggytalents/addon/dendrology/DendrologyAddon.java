package doggytalents.addon.dendrology;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import doggytalents.addon.AddonEvent;
import doggytalents.addon.AddonManager;
import doggytalents.api.registry.DogBedRegistry;
import net.minecraft.block.Block;

/**
 * @author ProPercivalalb
 */
public class DendrologyAddon {

	@SubscribeEvent
	public void onPost(AddonEvent.Post event) throws Exception {
		if(!AddonManager.areModsLoaded(DendrologyLib.MOD_NAME))
			return;
		
		Block planks1 = Block.getBlockFromName(DendrologyLib.ITEM_NAME);
		
		for(int i = 0; i < DendrologyLib.PLANKS_TEXURE.length; i++)
			DogBedRegistry.CASINGS.registerMaterial(planks1, i, DendrologyLib.TEXTURE_LOCATION + DendrologyLib.PLANKS_TEXURE[i]);
		
	}
}
