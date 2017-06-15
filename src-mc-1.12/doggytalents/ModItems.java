package doggytalents;

import doggytalents.item.ItemCommandEmblem;
import doggytalents.item.ItemDT;
import doggytalents.item.ItemDireTreat;
import doggytalents.item.ItemDoggyCharm;
import doggytalents.item.ItemRadar;
import doggytalents.item.ItemThrowBone;
import doggytalents.item.ItemTreat;
import doggytalents.lib.Reference;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
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
    public static Item RADAR;
	
	public static void inti() {
		
		//dogOwnersManual = new ItemDogOwnersManual().setUnlocalizedName("dt.dogOwnersManual").setTextureName("doggytalents:dogOwnersManual");
		THROW_BONE = new ItemThrowBone().setUnlocalizedName("doggytalents.throwbone");
		COMMAND_EMBLEM = new ItemCommandEmblem().setUnlocalizedName("doggytalents.commandemblem");
		TRAINING_TREAT = new ItemTreat(20).setUnlocalizedName("doggytalents.trainingtreat");
	    SUPER_TREAT = new ItemTreat(40).setUnlocalizedName("doggytalents.supertreat");
	    MASTER_TREAT = new ItemTreat(60).setUnlocalizedName("doggytalents.mastertreat");
	    DIRE_TREAT = new ItemDireTreat().setUnlocalizedName("doggytalents.diretreat");
	    BREEDING_BONE = new ItemDT().setUnlocalizedName("doggytalents.breedingbone");
	    COLLAR_SHEARS = new ItemDT().setUnlocalizedName("doggytalents.collarshears").setMaxDamage(16);
	    DOGGY_CHARM = new ItemDoggyCharm().setUnlocalizedName("doggytalents.doggycharm");
	    RADIO_COLLAR = new ItemDT().setUnlocalizedName("doggytalents.radiocollar");
	    RADAR = new ItemRadar().setUnlocalizedName("doggytalents.radar");
	    
		GameRegistry.register(THROW_BONE, new ResourceLocation(Reference.MOD_ID, "throw_bone"));
	    GameRegistry.register(TRAINING_TREAT, new ResourceLocation(Reference.MOD_ID, "training_treat"));
	    GameRegistry.register(SUPER_TREAT, new ResourceLocation(Reference.MOD_ID, "super_treat"));
	    GameRegistry.register(MASTER_TREAT, new ResourceLocation(Reference.MOD_ID, "master_treat"));
	    GameRegistry.register(DIRE_TREAT, new ResourceLocation(Reference.MOD_ID, "dire_treat"));
	    GameRegistry.register(BREEDING_BONE, new ResourceLocation(Reference.MOD_ID, "breeding_bone"));
	    GameRegistry.register(COLLAR_SHEARS, new ResourceLocation(Reference.MOD_ID, "collar_shears"));
	    GameRegistry.register(COMMAND_EMBLEM, new ResourceLocation(Reference.MOD_ID, "command_emblem"));
	    GameRegistry.register(DOGGY_CHARM, new ResourceLocation(Reference.MOD_ID, "doggy_charm"));
	    GameRegistry.register(RADIO_COLLAR, new ResourceLocation(Reference.MOD_ID, "radio_collar"));
	    GameRegistry.register(RADAR, new ResourceLocation(Reference.MOD_ID, "radar"));
	}
}
