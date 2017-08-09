package doggytalents;

import cpw.mods.fml.common.registry.GameRegistry;
import doggytalents.item.ItemCape2;
import doggytalents.item.ItemCommandEmblem;
import doggytalents.item.ItemDT;
import doggytalents.item.ItemDireTreat;
import doggytalents.item.ItemDoggyCharm;
import doggytalents.item.ItemRadar;
import doggytalents.item.ItemThrowBone;
import doggytalents.item.ItemTreat;
import doggytalents.item.ItemTreatBag;
import doggytalents.item.ItemWhistle;
import doggytalents.item.ItemWoolCollar;
import net.minecraft.item.Item;

/**
 * @author ProPercivalalb
 */
public class ModItems {

	public static Item THROW_BONE;
	public static Item COMMAND_EMBLEM;
	public static Item TRAINING_TREAT;
    public static Item SUPER_TREAT;
    public static Item MASTER_TREAT;
    public static Item DIRE_TREAT;
    public static Item BREEDING_BONE;
    public static Item COLLAR_SHEARS;
    public static Item DOGGY_CHARM;
    public static Item RADIO_COLLAR;
    public static Item WOOL_COLLAR;
    public static Item RADAR;
    public static Item WHISTLE;
    public static Item TREAT_BAG;
    public static Item CHEW_STICK;
    public static Item CAPE;
    public static Item CAPE1;
	public static Item SUNGLASSES;
	public static Item LEATHER_JACKET;
	
	public static void init() {
		DoggyTalents.LOGGER.info("Registering Items");
		THROW_BONE = new ItemThrowBone().setUnlocalizedName("doggytalents.throwbone");
		COMMAND_EMBLEM = new ItemCommandEmblem().setUnlocalizedName("doggytalents.commandemblem");
		TRAINING_TREAT = new ItemTreat("training_treat", 20).setUnlocalizedName("doggytalents.trainingtreat");
	    SUPER_TREAT = new ItemTreat("super_treat", 40).setUnlocalizedName("doggytalents.supertreat");
	    MASTER_TREAT = new ItemTreat("master_treat", 60).setUnlocalizedName("doggytalents.mastertreat");
	    DIRE_TREAT = new ItemDireTreat("dire_treat").setUnlocalizedName("doggytalents.diretreat");
	    BREEDING_BONE = new ItemDT("breeding_bone").setUnlocalizedName("doggytalents.breedingbone");
	    COLLAR_SHEARS = new ItemDT("collar_shears").setUnlocalizedName("doggytalents.collarshears").setMaxDamage(16);
	    DOGGY_CHARM = new ItemDoggyCharm().setUnlocalizedName("doggytalents.doggycharm");
	    RADIO_COLLAR = new ItemDT("radio_collar").setUnlocalizedName("doggytalents.radiocollar");
	    WOOL_COLLAR = new ItemWoolCollar().setUnlocalizedName("doggytalents.woolcollar");
	    RADAR = new ItemRadar().setUnlocalizedName("doggytalents.radar");
	    WHISTLE = new ItemWhistle().setUnlocalizedName("doggytalents.whistle");
	    TREAT_BAG =  new ItemTreatBag().setUnlocalizedName("doggytalents.treatbag");
	    CHEW_STICK = new ItemDT("chew_stick").setUnlocalizedName("doggytalents.chewstick");
	    CAPE = new ItemDT("cape").setUnlocalizedName("doggytalents.cape");
	    SUNGLASSES = new ItemDT("sunglasses").setUnlocalizedName("doggytalents.sunglasses");
	    CAPE1 = new ItemCape2().setUnlocalizedName("doggytalents.cape1");
	    LEATHER_JACKET = new ItemDT("leather_jacket").setUnlocalizedName("doggytalents.leather_jacket");
	    
		GameRegistry.registerItem(THROW_BONE, "throw_bone");
	    GameRegistry.registerItem(TRAINING_TREAT, "training_treat");
	    GameRegistry.registerItem(SUPER_TREAT, "super_treat");
	    GameRegistry.registerItem(MASTER_TREAT, "master_treat");
	    GameRegistry.registerItem(DIRE_TREAT, "dire_treat");
	    GameRegistry.registerItem(BREEDING_BONE, "breeding_bone");
	    GameRegistry.registerItem(COLLAR_SHEARS, "collar_shears");
	    GameRegistry.registerItem(COMMAND_EMBLEM, "command_emblem");
	    GameRegistry.registerItem(DOGGY_CHARM, "doggy_charm");
	    GameRegistry.registerItem(RADIO_COLLAR, "radio_collar");
	    GameRegistry.registerItem(WOOL_COLLAR, "wool_collar");
	    GameRegistry.registerItem(RADAR, "radar");
	    GameRegistry.registerItem(WHISTLE, "whistle");
	    GameRegistry.registerItem(TREAT_BAG, "treat_bag");
	    GameRegistry.registerItem(CHEW_STICK, "chew_stick");
	    GameRegistry.registerItem(CAPE, "cape");
	    GameRegistry.registerItem(SUNGLASSES, "sunglasses");
	    GameRegistry.registerItem(CAPE1, "cape1");
	    GameRegistry.registerItem(LEATHER_JACKET, "leather_jacket");
	}
}
