package doggytalents;

import net.minecraft.item.Item;
import cpw.mods.fml.common.registry.GameRegistry;
import doggytalents.item.ItemCommandEmblem;
import doggytalents.item.ItemDT;
import doggytalents.item.ItemDireTreat;
import doggytalents.item.ItemDoggyCharm;
import doggytalents.item.ItemRadar;
import doggytalents.item.ItemThrowBone;
import doggytalents.item.ItemTreat;

/**
 * @author ProPercivalalb
 */
public class ModItems {

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
		
		//dogOwnersManual = new ItemDogOwnersManual().setUnlocalizedName("dt.dogOwnersManual").setTextureName("doggytalents:dogOwnersManual");
		throwBone = new ItemThrowBone().setUnlocalizedName("doggytalents.throwbone");
		commandEmblem = new ItemCommandEmblem().setUnlocalizedName("doggytalents.commandemblem");
		trainingTreat = new ItemTreat("trainingtreat", 20).setUnlocalizedName("doggytalents.trainingtreat");
	    superTreat = new ItemTreat("supertreat", 40).setUnlocalizedName("doggytalents.supertreat");
	    masterTreat = new ItemTreat("mastertreat", 60).setUnlocalizedName("doggytalents.mastertreat");
	    direTreat = new ItemDireTreat("diretreat").setUnlocalizedName("doggytalents.diretreat");
	    breedingBone = new ItemDT("breedingbone").setUnlocalizedName("doggytalents.breedingbone");
	    collarShears = new ItemDT("collarshears").setUnlocalizedName("doggytalents.collarshears").setMaxDamage(16);
	    doggyCharm = new ItemDoggyCharm("doggycharm").setUnlocalizedName("doggytalents.doggycharm");
	    radioCollar = new ItemDT("radiocollar").setUnlocalizedName("doggytalents.radiocollar");
	    radar = new ItemRadar().setUnlocalizedName("doggytalents.radar");
	    
		GameRegistry.registerItem(throwBone, "throw_bone");
	    GameRegistry.registerItem(trainingTreat, "training_treat");
	    GameRegistry.registerItem(superTreat, "super_treat");
	    GameRegistry.registerItem(masterTreat, "master_treat");
	    GameRegistry.registerItem(direTreat, "dire_treat");
	    GameRegistry.registerItem(breedingBone, "breeding_bone");
	    GameRegistry.registerItem(collarShears, "collar_shears");
	    GameRegistry.registerItem(commandEmblem, "command_emblem");
	    GameRegistry.registerItem(doggyCharm, "doggy_charm");
	    GameRegistry.registerItem(radioCollar, "radio_collar");
	    GameRegistry.registerItem(radar, "radar");
	}
}
