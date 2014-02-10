package doggytalents;

import cpw.mods.fml.common.registry.GameRegistry;
import doggytalents.item.ItemCommandEmblem;
import doggytalents.item.ItemDT;
import doggytalents.item.ItemDireTreat;
import doggytalents.item.ItemDogOwnersManual;
import doggytalents.item.ItemDoggyCharm;
import doggytalents.item.ItemThrowBone;
import doggytalents.item.ItemTreat;
import doggytalents.lib.ItemIds;
import net.minecraft.item.Item;

/**
 * @author ProPercivalalb
 */
public class ModItems {

	public static Item dogOwnersManual;
	
	public static Item throwBone;
	public static Item commandEmblem;
	public static Item trainingTreat;
    public static Item superTreat;
    public static Item masterTreat;
    public static Item direTreat;
    public static Item breedingBone;
    public static Item collarShears;
    public static Item doggyCharm;
	
	public static void inti() {
		dogOwnersManual = new ItemDogOwnersManual(ItemIds.ID_DOG_OWNERS_MANUEL).setUnlocalizedName("dt.dogOwnersManual").setTextureName("doggytalents:dogOwnersManual");
		throwBone = new ItemThrowBone(ItemIds.ID_THROW_BONE).setUnlocalizedName("dt.throwBone");
		commandEmblem = new ItemCommandEmblem(ItemIds.ID_COMMAND_EMBLEM).setUnlocalizedName("dt.commandEmblem");
		
		trainingTreat = new ItemTreat(ItemIds.ID_TRAINING_TREAT, "trainingtreat", 20).setUnlocalizedName("dt.trainingTreat");
	    superTreat = new ItemTreat(ItemIds.ID_SUPER_TREAT, "supertreat", 40).setUnlocalizedName("dt.superTreat");
	    masterTreat = new ItemTreat(ItemIds.ID_MASTER_TREAT, "mastertreat", 60).setUnlocalizedName("dt.masterTreat");
	    direTreat = new ItemDireTreat(ItemIds.ID_DIRE_TREAT, "diretreat").setUnlocalizedName("dt.direTreat");
	    breedingBone = new ItemDT(ItemIds.ID_BREEDING_BONE, "breedingbone").setUnlocalizedName("dt.breedingBone");
	    collarShears = new ItemDT(ItemIds.ID_COLLAR_SHEARS, "collarshears").setUnlocalizedName("dt.collarShears").setMaxDamage(16);
	    doggyCharm = new ItemDoggyCharm(ItemIds.ID_DOGGY_CHARM, "doggycharm").setUnlocalizedName("dt.doggyCharm");
		
		GameRegistry.registerItem(throwBone, "dt.throwBone");
	    GameRegistry.registerItem(trainingTreat, "dt.trainingTreat");
	    GameRegistry.registerItem(superTreat, "dt.superTreat");
	    GameRegistry.registerItem(masterTreat, "dt.masterTreat");
	    GameRegistry.registerItem(direTreat, "dt.direTreat");
	    GameRegistry.registerItem(breedingBone, "dt.breedingBone");
	    GameRegistry.registerItem(collarShears, "dt.collarShears");
	    GameRegistry.registerItem(commandEmblem, "dt.commandEmblem");
	    GameRegistry.registerItem(doggyCharm, "dt.doggyCharm");
	}
}
