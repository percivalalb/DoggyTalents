package doggytalents;

import cpw.mods.fml.common.registry.GameRegistry;
import doggytalents.item.ItemCommandEmblem;
import doggytalents.item.ItemDogOwnersManual;
import doggytalents.item.ItemThrowBone;
import doggytalents.lib.ItemIds;
import net.minecraft.item.Item;

/**
 * @author ProPercivalalb
 */
public class ModItems {

	public static Item dogOwnersManual;
	public static Item throwBone;
	public static Item commandEmblem;
	
	public static void inti() {
		dogOwnersManual = new ItemDogOwnersManual(ItemIds.ID_DOG_OWNERS_MANUEL).setUnlocalizedName("dt.dogOwnersManual").setTextureName("doggytalents:dogOwnersManual");
		throwBone = new ItemThrowBone(ItemIds.ID_THROW_BONE).setUnlocalizedName("dt.throwBone");
		commandEmblem = new ItemCommandEmblem(ItemIds.ID_COMMAND_EMBLEM).setUnlocalizedName("dt.throwBone");
		
		GameRegistry.registerItem(throwBone, "dt.throwBone");
	}
}
