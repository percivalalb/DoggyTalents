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
		trainingTreat = new ItemTreat(20).setUnlocalizedName("doggytalents.trainingtreat");
	    superTreat = new ItemTreat(40).setUnlocalizedName("doggytalents.supertreat");
	    masterTreat = new ItemTreat(60).setUnlocalizedName("doggytalents.mastertreat");
	    direTreat = new ItemDireTreat().setUnlocalizedName("doggytalents.diretreat");
	    breedingBone = new ItemDT().setUnlocalizedName("doggytalents.breedingbone");
	    collarShears = new ItemDT().setUnlocalizedName("doggytalents.collarshears").setMaxDamage(16);
	    doggyCharm = new ItemDoggyCharm().setUnlocalizedName("doggytalents.doggycharm");
	    radioCollar = new ItemDT().setUnlocalizedName("doggytalents.radiocollar");
	    radar = new ItemRadar().setUnlocalizedName("doggytalents.radar");
	    
		GameRegistry.register(throwBone, new ResourceLocation(Reference.MOD_ID, "throw_bone"));
	    GameRegistry.register(trainingTreat, new ResourceLocation(Reference.MOD_ID, "training_treat"));
	    GameRegistry.register(superTreat, new ResourceLocation(Reference.MOD_ID, "super_treat"));
	    GameRegistry.register(masterTreat, new ResourceLocation(Reference.MOD_ID, "master_treat"));
	    GameRegistry.register(direTreat, new ResourceLocation(Reference.MOD_ID, "dire_treat"));
	    GameRegistry.register(breedingBone, new ResourceLocation(Reference.MOD_ID, "breeding_bone"));
	    GameRegistry.register(collarShears, new ResourceLocation(Reference.MOD_ID, "collar_shears"));
	    GameRegistry.register(commandEmblem, new ResourceLocation(Reference.MOD_ID, "command_emblem"));
	    GameRegistry.register(doggyCharm, new ResourceLocation(Reference.MOD_ID, "doggy_charm"));
	    GameRegistry.register(radioCollar, new ResourceLocation(Reference.MOD_ID, "radio_collar"));
	    GameRegistry.register(radar, new ResourceLocation(Reference.MOD_ID, "radar"));
	}
}
