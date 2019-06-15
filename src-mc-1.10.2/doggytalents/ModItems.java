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


@ObjectHolder(Reference.MOD_ID)
public class ModItems {

	public static final Item THROW_BONE = null;
	public static final Item THROW_BONE_WET = null;
	public static final Item THROW_STICK = null;
	public static final Item THROW_STICK_WET = null;
    public static final Item TRAINING_TREAT = null;
    public static final Item SUPER_TREAT = null;
    public static final Item MASTER_TREAT = null;
    public static final Item DIRE_TREAT = null;
    public static final Item BREEDING_BONE = null;
    public static final Item COLLAR_SHEARS = null;
    public static final Item DOGGY_CHARM = null;
    public static final Item RADIO_COLLAR = null;
    public static final Item WOOL_COLLAR = null;
    public static final Item CREATIVE_COLLAR = null;
    public static final Item SPOTTED_COLLAR = null;
    public static final Item MULTICOLOURED_COLLAR = null;
    public static final Item RADAR = null;
    public static final Item CREATIVE_RADAR = null;
    public static final Item WHISTLE = null;
    public static final Item TREAT_BAG = null;
    public static final Item CHEW_STICK = null;
    public static final Item CAPE = null;
    public static final Item CAPE_COLOURED = null;
    public static final Item SUNGLASSES = null;
    public static final Item LEATHER_JACKET = null;
    public static final Item TINY_BONE = null;
    public static final Item BIG_BONE = null;
    public static final Item OWNER_CHANGE = null;
	
	@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
    public static class Registration {
		
	    @SubscribeEvent
	    public static void onItemRegister(final RegistryEvent.Register<Item> event) {
	    	IForgeRegistry<Item> itemRegistry = event.getRegistry();
	    	
			DoggyTalents.LOGGER.info("Registering Items");
			itemRegistry.register(new ItemThrowBone().setUnlocalizedName(ItemNames.THROW_BONE.replace(":", ".")).setRegistryName(ItemNames.THROW_BONE).setMaxStackSize(2));
	    	itemRegistry.register(new ItemThrowBone(ItemThrowBone.Type.WET).setUnlocalizedName(ItemNames.THROW_BONE_WET.replace(":", ".")).setRegistryName(ItemNames.THROW_BONE_WET).setMaxStackSize(1));
	    	itemRegistry.register(new ItemThrowBone().setUnlocalizedName(ItemNames.THROW_STICK.replace(":", ".")).setRegistryName(ItemNames.THROW_STICK).setMaxStackSize(8));
	    	itemRegistry.register(new ItemThrowBone(ItemThrowBone.Type.WET).setUnlocalizedName(ItemNames.THROW_STICK_WET.replace(":", ".")).setRegistryName(ItemNames.THROW_STICK_WET).setMaxStackSize(1));
	    	itemRegistry.register(new ItemTreat(20).setUnlocalizedName(ItemNames.TRAINING_TREAT.replace(":", ".")).setRegistryName(ItemNames.TRAINING_TREAT));
	    	itemRegistry.register(new ItemTreat(40).setUnlocalizedName(ItemNames.SUPER_TREAT.replace(":", ".")).setRegistryName(ItemNames.SUPER_TREAT));
	    	itemRegistry.register(new ItemTreat(60).setUnlocalizedName(ItemNames.MASTER_TREAT.replace(":", ".")).setRegistryName(ItemNames.MASTER_TREAT));
	    	itemRegistry.register(new ItemDireTreat().setUnlocalizedName(ItemNames.DIRE_TREAT.replace(":", ".")).setRegistryName(ItemNames.DIRE_TREAT));
	    	itemRegistry.register(new ItemDT().setUnlocalizedName(ItemNames.BREEDING_BONE.replace(":", ".")).setRegistryName(ItemNames.BREEDING_BONE));
	    	itemRegistry.register(new ItemDT().setUnlocalizedName(ItemNames.COLLAR_SHEARS.replace(":", ".")).setRegistryName(ItemNames.COLLAR_SHEARS).setMaxStackSize(1));
	    	itemRegistry.register(new ItemDoggyCharm().setUnlocalizedName(ItemNames.DOGGY_CHARM.replace(":", ".")).setRegistryName(ItemNames.DOGGY_CHARM).setMaxStackSize(1));
	    	itemRegistry.register(new ItemDT().setUnlocalizedName(ItemNames.RADIO_COLLAR.replace(":", ".")).setRegistryName(ItemNames.RADIO_COLLAR));
	    	itemRegistry.register(new ItemWoolCollar().setUnlocalizedName(ItemNames.WOOL_COLLAR.replace(":", ".")).setRegistryName(ItemNames.WOOL_COLLAR));
	    	itemRegistry.register(new ItemCreativeCollar(ItemFancyCollar.Type.CREATIVE).setUnlocalizedName(ItemNames.CREATIVE_COLLAR.replace(":", ".")).setRegistryName(ItemNames.CREATIVE_COLLAR));
	      	itemRegistry.register(new ItemFancyCollar(ItemFancyCollar.Type.SPOTTED).setUnlocalizedName(ItemNames.SPOTTED_COLLAR.replace(":", ".")).setRegistryName(ItemNames.SPOTTED_COLLAR));
	      	itemRegistry.register(new ItemFancyCollar(ItemFancyCollar.Type.MULTI_COLOURED).setUnlocalizedName(ItemNames.MULTICOLOURED_COLLAR.replace(":", ".")).setRegistryName(ItemNames.MULTICOLOURED_COLLAR));
	    	itemRegistry.register(new ItemRadarCreative().setUnlocalizedName(ItemNames.CREATIVE_RADAR.replace(":", ".")).setRegistryName(ItemNames.CREATIVE_RADAR).setMaxStackSize(1));
	      	itemRegistry.register(new ItemRadar().setUnlocalizedName(ItemNames.RADAR.replace(":", ".")).setRegistryName(ItemNames.RADAR).setMaxStackSize(1));
	    	itemRegistry.register(new ItemWhistle().setUnlocalizedName(ItemNames.WHISTLE.replace(":", ".")).setRegistryName(ItemNames.WHISTLE).setMaxStackSize(1));
	    	itemRegistry.register(new ItemTreatBag().setUnlocalizedName(ItemNames.TREAT_BAG.replace(":", ".")).setRegistryName(ItemNames.TREAT_BAG).setMaxStackSize(1));
	     	itemRegistry.register(new ItemChewStick().setUnlocalizedName(ItemNames.CHEW_STICK.replace(":", ".")).setRegistryName(ItemNames.CHEW_STICK));
	     	itemRegistry.register(new ItemDT().setUnlocalizedName(ItemNames.CAPE.replace(":", ".")).setRegistryName(ItemNames.CAPE));
	     	itemRegistry.register(new ItemCapeColoured().setUnlocalizedName(ItemNames.CAPE_COLOURED.replace(":", ".")).setRegistryName(ItemNames.CAPE_COLOURED));
	     	itemRegistry.register(new ItemDT().setUnlocalizedName(ItemNames.SUNGLASSES.replace(":", ".")).setRegistryName(ItemNames.SUNGLASSES));
	     	itemRegistry.register(new ItemDT().setUnlocalizedName(ItemNames.LEATHER_JACKET.replace(":", ".")).setRegistryName(ItemNames.LEATHER_JACKET));
	     	itemRegistry.register(new ItemTinyBone().setUnlocalizedName(ItemNames.TINY_BONE.replace(":", ".")).setRegistryName(ItemNames.TINY_BONE));
	     	itemRegistry.register(new ItemBigBone().setUnlocalizedName(ItemNames.BIG_BONE.replace(":", ".")).setRegistryName(ItemNames.BIG_BONE));
	     	itemRegistry.register(new ItemCreativeOwnerChange().setUnlocalizedName(ItemNames.OWNER_CHANGE.replace(":", ".")).setRegistryName(ItemNames.OWNER_CHANGE).setMaxStackSize(1));
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
