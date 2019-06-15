package doggytalents;

import doggytalents.client.model.ModelHelper;
import doggytalents.item.ItemBigBone;
import doggytalents.item.ItemCapeColoured;
import doggytalents.item.ItemChewStick;
import doggytalents.item.ItemCreativeCollar;
import doggytalents.item.ItemCreativeOwnerChange;
import doggytalents.item.ItemDT;
import doggytalents.item.ItemDireTreat;
import doggytalents.item.ItemDoggyCharm;
import doggytalents.item.ItemFancyCollar;
import doggytalents.item.ItemRadar;
import doggytalents.item.ItemRadarCreative;
import doggytalents.item.ItemThrowBone;
import doggytalents.item.ItemTinyBone;
import doggytalents.item.ItemTreat;
import doggytalents.item.ItemTreatBag;
import doggytalents.item.ItemWhistle;
import doggytalents.item.ItemWoolCollar;
import doggytalents.lib.ItemNames;
import doggytalents.lib.Reference;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.common.registry.IForgeRegistry;


public class ModItems {

	public static Item THROW_BONE = null;
	public static Item THROW_BONE_WET = null;
	public static Item THROW_STICK = null;
	public static Item THROW_STICK_WET = null;
    public static Item TRAINING_TREAT = null;
    public static Item SUPER_TREAT = null;
    public static Item MASTER_TREAT = null;
    public static Item DIRE_TREAT = null;
    public static Item BREEDING_BONE = null;
    public static Item COLLAR_SHEARS = null;
    public static Item DOGGY_CHARM = null;
    public static Item RADIO_COLLAR = null;
    public static Item WOOL_COLLAR = null;
    public static Item CREATIVE_COLLAR = null;
    public static Item SPOTTED_COLLAR = null;
    public static Item MULTICOLOURED_COLLAR = null;
    public static Item RADAR = null;
    public static Item CREATIVE_RADAR = null;
    public static Item WHISTLE = null;
    public static Item TREAT_BAG = null;
    public static Item CHEW_STICK = null;
    public static Item CAPE = null;
    public static Item CAPE_COLOURED = null;
    public static Item SUNGLASSES = null;
    public static Item LEATHER_JACKET = null;
    public static Item TINY_BONE = null;
    public static Item BIG_BONE = null;
    public static Item OWNER_CHANGE = null;
	
	@Mod.EventBusSubscriber
    public static class Registration {
		
	    @SubscribeEvent
	    public static void onItemRegister(RegistryEvent.Register<Item> event) {
	    	IForgeRegistry<Item> itemRegistry = event.getRegistry();
	    	
			DoggyTalents.LOGGER.info("Registering Items");
			itemRegistry.register(THROW_BONE = new ItemThrowBone().setUnlocalizedName(ItemNames.THROW_BONE.replace(":", ".")).setRegistryName(ItemNames.THROW_BONE).setMaxStackSize(2));
	    	itemRegistry.register(THROW_BONE_WET = new ItemThrowBone(ItemThrowBone.Type.WET).setUnlocalizedName(ItemNames.THROW_BONE_WET.replace(":", ".")).setRegistryName(ItemNames.THROW_BONE_WET).setMaxStackSize(1));
	    	itemRegistry.register(THROW_STICK = new ItemThrowBone().setUnlocalizedName(ItemNames.THROW_STICK.replace(":", ".")).setRegistryName(ItemNames.THROW_STICK).setMaxStackSize(8));
	    	itemRegistry.register(THROW_STICK_WET = new ItemThrowBone(ItemThrowBone.Type.WET).setUnlocalizedName(ItemNames.THROW_STICK_WET.replace(":", ".")).setRegistryName(ItemNames.THROW_STICK_WET).setMaxStackSize(1));
	    	itemRegistry.register(TRAINING_TREAT = new ItemTreat(20).setUnlocalizedName(ItemNames.TRAINING_TREAT.replace(":", ".")).setRegistryName(ItemNames.TRAINING_TREAT));
	    	itemRegistry.register(SUPER_TREAT = new ItemTreat(40).setUnlocalizedName(ItemNames.SUPER_TREAT.replace(":", ".")).setRegistryName(ItemNames.SUPER_TREAT));
	    	itemRegistry.register(MASTER_TREAT = new ItemTreat(60).setUnlocalizedName(ItemNames.MASTER_TREAT.replace(":", ".")).setRegistryName(ItemNames.MASTER_TREAT));
	    	itemRegistry.register(DIRE_TREAT = new ItemDireTreat().setUnlocalizedName(ItemNames.DIRE_TREAT.replace(":", ".")).setRegistryName(ItemNames.DIRE_TREAT));
	    	itemRegistry.register(BREEDING_BONE = new ItemDT().setUnlocalizedName(ItemNames.BREEDING_BONE.replace(":", ".")).setRegistryName(ItemNames.BREEDING_BONE));
	    	itemRegistry.register(COLLAR_SHEARS = new ItemDT().setUnlocalizedName(ItemNames.COLLAR_SHEARS.replace(":", ".")).setRegistryName(ItemNames.COLLAR_SHEARS).setMaxStackSize(1));
	    	itemRegistry.register(DOGGY_CHARM = new ItemDoggyCharm().setUnlocalizedName(ItemNames.DOGGY_CHARM.replace(":", ".")).setRegistryName(ItemNames.DOGGY_CHARM).setMaxStackSize(1));
	    	itemRegistry.register(RADIO_COLLAR = new ItemDT().setUnlocalizedName(ItemNames.RADIO_COLLAR.replace(":", ".")).setRegistryName(ItemNames.RADIO_COLLAR));
	    	itemRegistry.register(WOOL_COLLAR = new ItemWoolCollar().setUnlocalizedName(ItemNames.WOOL_COLLAR.replace(":", ".")).setRegistryName(ItemNames.WOOL_COLLAR));
	    	itemRegistry.register(CREATIVE_COLLAR = new ItemCreativeCollar(ItemFancyCollar.Type.CREATIVE).setUnlocalizedName(ItemNames.CREATIVE_COLLAR.replace(":", ".")).setRegistryName(ItemNames.CREATIVE_COLLAR));
	      	itemRegistry.register(SPOTTED_COLLAR = new ItemFancyCollar(ItemFancyCollar.Type.SPOTTED).setUnlocalizedName(ItemNames.SPOTTED_COLLAR.replace(":", ".")).setRegistryName(ItemNames.SPOTTED_COLLAR));
	      	itemRegistry.register(MULTICOLOURED_COLLAR = new ItemFancyCollar(ItemFancyCollar.Type.MULTI_COLOURED).setUnlocalizedName(ItemNames.MULTICOLOURED_COLLAR.replace(":", ".")).setRegistryName(ItemNames.MULTICOLOURED_COLLAR));
	    	itemRegistry.register(CREATIVE_RADAR = new ItemRadarCreative().setUnlocalizedName(ItemNames.CREATIVE_RADAR.replace(":", ".")).setRegistryName(ItemNames.CREATIVE_RADAR).setMaxStackSize(1));
	      	itemRegistry.register(RADAR = new ItemRadar().setUnlocalizedName(ItemNames.RADAR.replace(":", ".")).setRegistryName(ItemNames.RADAR).setMaxStackSize(1));
	    	itemRegistry.register(WHISTLE = new ItemWhistle().setUnlocalizedName(ItemNames.WHISTLE.replace(":", ".")).setRegistryName(ItemNames.WHISTLE).setMaxStackSize(1));
	    	itemRegistry.register(TREAT_BAG = new ItemTreatBag().setUnlocalizedName(ItemNames.TREAT_BAG.replace(":", ".")).setRegistryName(ItemNames.TREAT_BAG).setMaxStackSize(1));
	     	itemRegistry.register(CHEW_STICK = new ItemChewStick().setUnlocalizedName(ItemNames.CHEW_STICK.replace(":", ".")).setRegistryName(ItemNames.CHEW_STICK));
	     	itemRegistry.register(CAPE = new ItemDT().setUnlocalizedName(ItemNames.CAPE.replace(":", ".")).setRegistryName(ItemNames.CAPE));
	     	itemRegistry.register(CAPE_COLOURED = new ItemCapeColoured().setUnlocalizedName(ItemNames.CAPE_COLOURED.replace(":", ".")).setRegistryName(ItemNames.CAPE_COLOURED));
	     	itemRegistry.register(SUNGLASSES = new ItemDT().setUnlocalizedName(ItemNames.SUNGLASSES.replace(":", ".")).setRegistryName(ItemNames.SUNGLASSES));
	     	itemRegistry.register(LEATHER_JACKET = new ItemDT().setUnlocalizedName(ItemNames.LEATHER_JACKET.replace(":", ".")).setRegistryName(ItemNames.LEATHER_JACKET));
	     	itemRegistry.register(TINY_BONE = new ItemTinyBone().setUnlocalizedName(ItemNames.TINY_BONE.replace(":", ".")).setRegistryName(ItemNames.TINY_BONE));
	     	itemRegistry.register(BIG_BONE = new ItemBigBone().setUnlocalizedName(ItemNames.BIG_BONE.replace(":", ".")).setRegistryName(ItemNames.BIG_BONE));
	     	itemRegistry.register(OWNER_CHANGE = new ItemCreativeOwnerChange().setUnlocalizedName(ItemNames.OWNER_CHANGE.replace(":", ".")).setRegistryName(ItemNames.OWNER_CHANGE).setMaxStackSize(1));
	     	DoggyTalents.LOGGER.info("Finished Registering Items");
	    }
	    
	    @SubscribeEvent
		@SideOnly(Side.CLIENT)
		public static void setItemModels(ModelRegistryEvent event) {
			ModelHelper.setDefaultModel(THROW_BONE);
			ModelHelper.setDefaultModel(THROW_BONE_WET);
			ModelHelper.setDefaultModel(THROW_STICK);
			ModelHelper.setDefaultModel(THROW_STICK_WET);
			ModelHelper.setDefaultModel(TRAINING_TREAT);
			ModelHelper.setDefaultModel(SUPER_TREAT);
			ModelHelper.setDefaultModel(MASTER_TREAT);
			ModelHelper.setDefaultModel(DIRE_TREAT);
			ModelHelper.setDefaultModel(BREEDING_BONE);
			ModelHelper.setDefaultModel(COLLAR_SHEARS);
			ModelHelper.setDefaultModel(DOGGY_CHARM);
			ModelHelper.setDefaultModel(RADAR);
			ModelHelper.setDefaultModel(CREATIVE_RADAR);
			ModelHelper.setDefaultModel(RADIO_COLLAR);
			ModelHelper.setDefaultModel(WOOL_COLLAR);
			ModelHelper.setDefaultModel(CREATIVE_COLLAR);
			ModelHelper.setDefaultModel(MULTICOLOURED_COLLAR);
			ModelHelper.setDefaultModel(SPOTTED_COLLAR);
			ModelHelper.setDefaultModel(WHISTLE);
			ModelHelper.setDefaultModel(TREAT_BAG);
			ModelHelper.setDefaultModel(CHEW_STICK);
			ModelHelper.setDefaultModel(CAPE);
			ModelHelper.setDefaultModel(CAPE_COLOURED);
			ModelHelper.setDefaultModel(SUNGLASSES);
			ModelHelper.setDefaultModel(LEATHER_JACKET);
			ModelHelper.setDefaultModel(TINY_BONE);
			ModelHelper.setDefaultModel(BIG_BONE);
			ModelHelper.setDefaultModel(OWNER_CHANGE);
		}
	}
}
