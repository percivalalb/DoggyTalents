package doggytalents.addon.terraqueous;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import doggytalents.addon.AddonEvent;
import doggytalents.addon.AddonManager;
import doggytalents.api.registry.DogBedRegistry;
import net.minecraft.block.Block;

/**
 * @author ProPercivalalb
 */
public class TerraqueousAddon {

	@SubscribeEvent
	public void onPost(AddonEvent.Post event) throws Exception {
		if(!AddonManager.areModsLoaded(TerraqueousLib.MOD_NAME))
			return;
		
		Block planks1 = Block.getBlockFromName(TerraqueousLib.ITEM_NAME);
		
		for(int i = 0; i < TerraqueousLib.PLANKS_TEXURE.length; i++)
			DogBedRegistry.CASINGS.registerMaterial(planks1, i, TerraqueousLib.TEXTURE_LOCATION + TerraqueousLib.PLANKS_TEXURE[i]);
	}
}
