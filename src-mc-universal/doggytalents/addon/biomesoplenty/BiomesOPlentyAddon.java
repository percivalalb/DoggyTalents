package doggytalents.addon.biomesoplenty;

import doggytalents.addon.AddonEvent;
import doggytalents.addon.AddonManager;
import doggytalents.api.registry.DogBedRegistry;
import net.minecraft.block.Block;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author ProPercivalalb
 */
public class BiomesOPlentyAddon {

	@SubscribeEvent
	public void onPost(AddonEvent.Post event) {
		if(!AddonManager.areModsLoaded(BiomesOPlentyLib.MOD_ID, BiomesOPlentyLib.MOD_ID_194))
			return;
		
		Block planks1 = Block.getBlockFromName(BiomesOPlentyLib.ITEM_NAME_1);
		Block bambooThatching = Block.getBlockFromName(BiomesOPlentyLib.ITEM_BAMBOO_THATCHING);
		
		for(int i = 0; i < BiomesOPlentyLib.PLANKS_1_TEXURE.length; i++)
			DogBedRegistry.CASINGS.registerMaterial(planks1, i, BiomesOPlentyLib.TEXTURE_LOCATION + BiomesOPlentyLib.PLANKS_1_TEXURE[i]);
		
		DogBedRegistry.CASINGS.registerMaterial(bambooThatching, 0, BiomesOPlentyLib.TEXTURE_LOCATION + BiomesOPlentyLib.BAMBOO_THATCHING_TEXURE);
		
	}
}
