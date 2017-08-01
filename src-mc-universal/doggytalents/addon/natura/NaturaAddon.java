package doggytalents.addon.natura;

import doggytalents.addon.AddonEvent;
import doggytalents.addon.AddonManager;
import doggytalents.api.registry.DogBedRegistry;
import net.minecraft.block.Block;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author ProPercivalalb
 */
public class NaturaAddon {

	@SubscribeEvent
	public void onPost(AddonEvent.Post event) throws Exception {
		if(!AddonManager.areModsLoaded(NaturaLib.MOD_NAME))
			return;
		
		Block planks1 = Block.getBlockFromName(NaturaLib.ITEM_NAME_1);
		Block planks2 = Block.getBlockFromName(NaturaLib.ITEM_NAME_2);
		
		for(int i = 0; i < NaturaLib.PLANKS_1_TEXURE.length; i++)
			DogBedRegistry.CASINGS.registerMaterial(planks1, i, NaturaLib.TEXTURE_LOCATION_1 + NaturaLib.PLANKS_1_TEXURE[i] + "_planks");
		
		for(int i = 0; i < NaturaLib.PLANKS_2_TEXURE.length; i++)
			DogBedRegistry.CASINGS.registerMaterial(planks2, i, NaturaLib.TEXTURE_LOCATION_2 + NaturaLib.PLANKS_2_TEXURE[i] + "_planks");
		
	}
}
