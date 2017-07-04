package doggytalents;

import doggytalents.client.model.ModelHelper;
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
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ProPercivalalb
 */
@EventBusSubscriber
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
	
	@SubscribeEvent
	public static void onRegister(RegistryEvent.Register<Item> event) {
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
	    RADAR = new ItemRadar().setUnlocalizedName("doggytalents.radar").setRegistryName(Reference.MOD_ID + ":radar");
		
		event.getRegistry().register(ModItems.THROW_BONE);
	    event.getRegistry().register(ModItems.TRAINING_TREAT);
	    event.getRegistry().register(ModItems.SUPER_TREAT);
	    event.getRegistry().register(ModItems.MASTER_TREAT);
	    event.getRegistry().register(ModItems.DIRE_TREAT);
	    event.getRegistry().register(ModItems.BREEDING_BONE);
	    event.getRegistry().register(ModItems.COLLAR_SHEARS);
	    event.getRegistry().register(ModItems.COMMAND_EMBLEM);
	    event.getRegistry().register(ModItems.DOGGY_CHARM);
	    event.getRegistry().register(ModItems.RADIO_COLLAR);
	    event.getRegistry().register(ModItems.RADAR);
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void setItemModels(ModelRegistryEvent event) {
		ModelHelper.setModel(ModItems.THROW_BONE, 0, "doggytalents:throw_bone");
		ModelHelper.setModel(ModItems.THROW_BONE, 1, "doggytalents:throw_bone_wet");
		ModelHelper.setModel(ModItems.COMMAND_EMBLEM, 0, "doggytalents:command_emblem");
		ModelHelper.setModel(ModItems.TRAINING_TREAT, 0, "doggytalents:training_treat");
	    ModelHelper.setModel(ModItems.SUPER_TREAT, 0, "doggytalents:super_treat");
	    ModelHelper.setModel(ModItems.MASTER_TREAT, 0, "doggytalents:master_treat");
	    ModelHelper.setModel(ModItems.DIRE_TREAT, 0, "doggytalents:dire_treat");
	    ModelHelper.setModel(ModItems.BREEDING_BONE, 0, "doggytalents:breeding_bone");
	    ModelHelper.setModel(ModItems.COLLAR_SHEARS, 0, "doggytalents:collar_shears");
	    ModelHelper.setModel(ModItems.DOGGY_CHARM, 0, "doggytalents:doggy_charm");
	    ModelHelper.setModel(ModItems.RADAR, 0, "doggytalents:radar");
	    ModelHelper.setModel(ModItems.RADIO_COLLAR, 0, "doggytalents:radio_collar");
	}
}
