package doggytalents;

import net.minecraft.item.Item;
import cpw.mods.fml.common.registry.GameRegistry;
import doggytalents.item.ItemCommandEmblem;
import doggytalents.item.ItemDT;
import doggytalents.item.ItemDireTreat;
import doggytalents.item.ItemDogOwnersManual;
import doggytalents.item.ItemDoggyCharm;
import doggytalents.item.ItemRadar;
import doggytalents.item.ItemThrowBone;
import doggytalents.item.ItemTreat;

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
    public static Item radioCollar;
    public static Item radar;
	
	public static void inti() {
		dogOwnersManual = new ItemDogOwnersManual().setUnlocalizedName("dt.dogOwnersManual").setTextureName("doggytalents:dogOwnersManual");
		throwBone = new ItemThrowBone().setUnlocalizedName("dt.throwBone");
		commandEmblem = new ItemCommandEmblem().setUnlocalizedName("dt.commandEmblem");
		trainingTreat = new ItemTreat("trainingtreat", 20).setUnlocalizedName("dt.trainingTreat");
	    superTreat = new ItemTreat("supertreat", 40).setUnlocalizedName("dt.superTreat");
	    masterTreat = new ItemTreat("mastertreat", 60).setUnlocalizedName("dt.masterTreat");
	    direTreat = new ItemDireTreat("diretreat").setUnlocalizedName("dt.direTreat");
	    breedingBone = new ItemDT("breedingbone").setUnlocalizedName("dt.breedingBone");
	    collarShears = new ItemDT("collarshears").setUnlocalizedName("dt.collarShears").setMaxDamage(16);
	    doggyCharm = new ItemDoggyCharm("doggycharm").setUnlocalizedName("dt.doggyCharm");
	    radioCollar = new ItemDT("radiocollar").setUnlocalizedName("dt.radioCollar");
	    radar = new ItemRadar().setUnlocalizedName("dt.radar");
	    
		GameRegistry.registerItem(throwBone, "dt.throwBone");
	    GameRegistry.registerItem(trainingTreat, "dt.trainingTreat");
	    GameRegistry.registerItem(superTreat, "dt.superTreat");
	    GameRegistry.registerItem(masterTreat, "dt.masterTreat");
	    GameRegistry.registerItem(direTreat, "dt.direTreat");
	    GameRegistry.registerItem(breedingBone, "dt.breedingBone");
	    GameRegistry.registerItem(collarShears, "dt.collarShears");
	    GameRegistry.registerItem(commandEmblem, "dt.commandEmblem");
	    GameRegistry.registerItem(doggyCharm, "dt.doggyCharm");
	    GameRegistry.registerItem(radioCollar, "dt.radioCollar");
	    GameRegistry.registerItem(radar, "dt.radar");
	}
}
