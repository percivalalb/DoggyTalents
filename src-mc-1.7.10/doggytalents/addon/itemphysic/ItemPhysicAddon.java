package doggytalents.addon.itemphysic;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import doggytalents.ModBlocks;
import doggytalents.ModItems;
import doggytalents.addon.AddonEvent;
import doggytalents.addon.AddonManager;
import net.minecraftforge.common.MinecraftForge;

/**
 * @author ProPercivalalb
 */
public class ItemPhysicAddon {

	private static ItemPhysicAPI API;

	@SubscribeEvent
	public void onPost(AddonEvent.Post event) throws Exception {
		if(!AddonManager.areModsLoaded(ItemPhysicLib.MOD_ID, ItemPhysicLib.LIB_ID))
			return;
		
		API = new ItemPhysicAPI();
		API.addBurnObjects(ModBlocks.DOG_BED, ModItems.BREEDING_BONE, ModItems.DIRE_TREAT, ModItems.MASTER_TREAT, ModItems.SUPER_TREAT, ModItems.TRAINING_TREAT, ModItems.COLLAR_SHEARS, ModItems.THROW_BONE, ModItems.RADAR);
			
			
		API.addSwimObjects(ModItems.BREEDING_BONE, ModItems.DIRE_TREAT, ModItems.MASTER_TREAT, ModItems.SUPER_TREAT, ModItems.TRAINING_TREAT, ModItems.COLLAR_SHEARS, ModItems.THROW_BONE);
	}

}
