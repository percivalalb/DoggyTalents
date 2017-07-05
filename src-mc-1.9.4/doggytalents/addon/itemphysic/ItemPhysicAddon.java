package doggytalents.addon.itemphysic;

import doggytalents.ModBlocks;
import doggytalents.ModItems;
import doggytalents.addon.AddonEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author ProPercivalalb
 */
public class ItemPhysicAddon {

	private static ItemPhysicAPI API = new ItemPhysicAPI(ItemPhysicLib.MOD_ID);
	
	@SubscribeEvent
	public void onPre(AddonEvent.Pre event) {
		if(!Loader.isModLoaded(ItemPhysicLib.MOD_ID))
			return;
	}
	
	@SubscribeEvent
	public void onInit(AddonEvent.Init event) {
		if(!Loader.isModLoaded(ItemPhysicLib.MOD_ID))
			return;
	}

	@SubscribeEvent
	public void onPost(AddonEvent.Post event) throws Exception {
		if(!Loader.isModLoaded(ItemPhysicLib.MOD_ID))
			return;
		
		API.addBurnObjects(ModBlocks.DOG_BED, ModItems.BREEDING_BONE, ModItems.DIRE_TREAT, ModItems.MASTER_TREAT, ModItems.SUPER_TREAT, ModItems.TRAINING_TREAT, ModItems.COLLAR_SHEARS, ModItems.THROW_BONE, ModItems.RADAR);
		
		
		API.addSwimObjects(ModItems.BREEDING_BONE, ModItems.DIRE_TREAT, ModItems.MASTER_TREAT, ModItems.SUPER_TREAT, ModItems.TRAINING_TREAT, ModItems.COLLAR_SHEARS, ModItems.THROW_BONE);
	}

}
