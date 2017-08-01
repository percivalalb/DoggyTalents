package doggytalents.addon.dendrology;

import doggytalents.addon.AddonEvent;
import doggytalents.addon.AddonManager;
import doggytalents.api.registry.DogBedRegistry;
import net.minecraft.block.Block;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

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
