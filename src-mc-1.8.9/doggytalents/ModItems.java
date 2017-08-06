package doggytalents;

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
import doggytalents.lib.Reference;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

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
	
	public static void init() {
		DoggyTalents.LOGGER.info("Registering Items");
		THROW_BONE = new ItemThrowBone().setUnlocalizedName("doggytalents.throwbone").setRegistryName(Reference.MOD_ID + ":throw_bone");
		COMMAND_EMBLEM = new ItemCommandEmblem().setUnlocalizedName("doggytalents.commandemblem").setRegistryName(Reference.MOD_ID + ":command_emblem");
		TRAINING_TREAT = new ItemTreat(20).setUnlocalizedName("doggytalents.trainingtreat").setRegistryName(Reference.MOD_ID + ":training_treat");
	    SUPER_TREAT = new ItemTreat(40).setUnlocalizedName("doggytalents.supertreat").setRegistryName(Reference.MOD_ID + ":super_treat");
	    MASTER_TREAT = new ItemTreat(60).setUnlocalizedName("doggytalents.mastertreat").setRegistryName(Reference.MOD_ID + ":master_treat");
	    DIRE_TREAT = new ItemDireTreat().setUnlocalizedName("doggytalents.diretreat").setRegistryName(Reference.MOD_ID + ":dire_treat");
	    BREEDING_BONE = new ItemDT().setUnlocalizedName("doggytalents.breedingbone").setRegistryName(Reference.MOD_ID + ":breeding_bone");
	    COLLAR_SHEARS = new ItemDT().setUnlocalizedName("doggytalents.collarshears").setMaxDamage(16).setRegistryName(Reference.MOD_ID + ":collar_shears");
	    DOGGY_CHARM = new ItemDoggyCharm().setUnlocalizedName("doggytalents.doggycharm").setRegistryName(Reference.MOD_ID + ":doggy_charm");
	    RADIO_COLLAR = new ItemDT().setUnlocalizedName("doggytalents.radiocollar").setRegistryName(Reference.MOD_ID + ":radio_collar");
	    WOOL_COLLAR = new ItemWoolCollar().setUnlocalizedName("doggytalents.woolcollar").setRegistryName(Reference.MOD_ID + ":wool_collar");
	    RADAR = new ItemRadar().setUnlocalizedName("doggytalents.radar").setRegistryName(Reference.MOD_ID + ":radar");
	    WHISTLE = new ItemWhistle().setUnlocalizedName("doggytalents.whistle").setRegistryName(Reference.MOD_ID + ":whistle");
	    TREAT_BAG = new ItemTreatBag().setUnlocalizedName("doggytalents.treatbag").setRegistryName(Reference.MOD_ID + ":treat_bag");
	    CHEW_STICK = new ItemDT().setUnlocalizedName("doggytalents.chewstick").setRegistryName(Reference.MOD_ID + ":chew_stick");
	    
		GameRegistry.registerItem(THROW_BONE);
	    GameRegistry.registerItem(TRAINING_TREAT);
	    GameRegistry.registerItem(SUPER_TREAT);
	    GameRegistry.registerItem(MASTER_TREAT);
	    GameRegistry.registerItem(DIRE_TREAT);
	    GameRegistry.registerItem(BREEDING_BONE);
	    GameRegistry.registerItem(COLLAR_SHEARS);
	    GameRegistry.registerItem(COMMAND_EMBLEM);
	    GameRegistry.registerItem(DOGGY_CHARM);
	    GameRegistry.registerItem(RADIO_COLLAR);
	    GameRegistry.registerItem(WOOL_COLLAR);
	    GameRegistry.registerItem(RADAR);
	    GameRegistry.registerItem(WHISTLE);
	    GameRegistry.registerItem(TREAT_BAG);
	    GameRegistry.registerItem(CHEW_STICK);
	}
}
