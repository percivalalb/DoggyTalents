package doggytalents;

import doggytalents.item.ItemBigBone;
import doggytalents.item.ItemCapeColoured;
import doggytalents.item.ItemChewStick;
import doggytalents.item.ItemCommandEmblem;
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
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(Reference.MOD_ID)
public class ModItems {

	public static final Item THROW_BONE = null;
	public static final Item THROW_BONE_WET = null;
	public static final Item THROW_STICK = null;
	public static final Item THROW_STICK_WET = null;
	public static final Item COMMAND_EMBLEM = null;
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
	
	@Mod.EventBusSubscriber(modid = Reference.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class Registration {
		
	    @SubscribeEvent
	    public static void onItemRegister(final RegistryEvent.Register<Item> event){
	    	DoggyTalentsMod.LOGGER.info("Registering Items");
	    	event.getRegistry().register(new ItemThrowBone(new Item.Properties().group(ModCreativeTabs.GENERAL).maxStackSize(1)).setRegistryName(ItemNames.THROW_BONE));
	    	event.getRegistry().register(new ItemThrowBone(ItemThrowBone.Type.WET, new Item.Properties().group(ModCreativeTabs.GENERAL).maxStackSize(1)).setRegistryName(ItemNames.THROW_BONE_WET));
	    	event.getRegistry().register(new ItemThrowBone(new Item.Properties().group(ModCreativeTabs.GENERAL).maxStackSize(16)).setRegistryName(ItemNames.THROW_STICK));
	    	event.getRegistry().register(new ItemThrowBone(ItemThrowBone.Type.WET, new Item.Properties().group(ModCreativeTabs.GENERAL).maxStackSize(1)).setRegistryName(ItemNames.THROW_STICK_WET));
	    	event.getRegistry().register(new ItemCommandEmblem(new Item.Properties().group(ModCreativeTabs.GENERAL).maxStackSize(1)).setRegistryName(ItemNames.COMMAND_EMBLEM));
	    	event.getRegistry().register(new ItemTreat(20, new Item.Properties().group(ModCreativeTabs.GENERAL)).setRegistryName(ItemNames.TRAINING_TREAT));
	    	event.getRegistry().register(new ItemTreat(40, new Item.Properties().group(ModCreativeTabs.GENERAL)).setRegistryName(ItemNames.SUPER_TREAT));
	    	event.getRegistry().register(new ItemTreat(60, new Item.Properties().group(ModCreativeTabs.GENERAL)).setRegistryName(ItemNames.MASTER_TREAT));
	    	event.getRegistry().register(new ItemDireTreat(new Item.Properties().group(ModCreativeTabs.GENERAL)).setRegistryName(ItemNames.DIRE_TREAT));
	    	event.getRegistry().register(new Item(new Item.Properties().group(ModCreativeTabs.GENERAL)).setRegistryName(ItemNames.BREEDING_BONE));
	    	event.getRegistry().register(new Item(new Item.Properties().group(ModCreativeTabs.GENERAL)).setRegistryName(ItemNames.COLLAR_SHEARS));
	    	event.getRegistry().register(new ItemDoggyCharm(new Item.Properties().group(ModCreativeTabs.GENERAL).maxStackSize(1)).setRegistryName(ItemNames.DOGGY_CHARM));
	    	event.getRegistry().register(new Item(new Item.Properties().group(ModCreativeTabs.GENERAL)).setRegistryName(ItemNames.RADIO_COLLAR));
	    	event.getRegistry().register(new ItemWoolCollar(new Item.Properties().group(ModCreativeTabs.GENERAL)).setRegistryName(ItemNames.WOOL_COLLAR));
	    	event.getRegistry().register(new ItemFancyCollar(ItemFancyCollar.Type.CREATIVE, new Item.Properties().group(ModCreativeTabs.GENERAL)).setRegistryName(ItemNames.CREATIVE_COLLAR));
	      	event.getRegistry().register(new ItemFancyCollar(ItemFancyCollar.Type.SPOTTED, new Item.Properties().group(ModCreativeTabs.GENERAL)).setRegistryName(ItemNames.SPOTTED_COLLAR));
	      	event.getRegistry().register(new ItemFancyCollar(ItemFancyCollar.Type.MULTI_COLOURED, new Item.Properties().group(ModCreativeTabs.GENERAL)).setRegistryName(ItemNames.MULTICOLOURED_COLLAR));
	    	event.getRegistry().register(new ItemRadarCreative(new Item.Properties().group(ModCreativeTabs.GENERAL)).setRegistryName(ItemNames.CREATIVE_RADAR));
	      	event.getRegistry().register(new ItemRadar(new Item.Properties().group(ModCreativeTabs.GENERAL).maxStackSize(1)).setRegistryName(ItemNames.RADAR));
	    	event.getRegistry().register(new ItemWhistle(new Item.Properties().group(ModCreativeTabs.GENERAL).maxStackSize(1)).setRegistryName(ItemNames.WHISTLE));
	    	event.getRegistry().register(new ItemTreatBag(new Item.Properties().group(ModCreativeTabs.GENERAL).maxStackSize(1)).setRegistryName(ItemNames.TREAT_BAG));
	     	event.getRegistry().register(new ItemChewStick(new Item.Properties().group(ModCreativeTabs.GENERAL)).setRegistryName(ItemNames.CHEW_STICK));
	     	event.getRegistry().register(new Item(new Item.Properties().group(ModCreativeTabs.GENERAL)).setRegistryName(ItemNames.CAPE));
	     	event.getRegistry().register(new ItemCapeColoured(new Item.Properties().group(ModCreativeTabs.GENERAL)).setRegistryName(ItemNames.CAPE_COLOURED));
	     	event.getRegistry().register(new Item(new Item.Properties().group(ModCreativeTabs.GENERAL)).setRegistryName(ItemNames.SUNGLASSES));
	     	event.getRegistry().register(new Item(new Item.Properties().group(ModCreativeTabs.GENERAL)).setRegistryName(ItemNames.LEATHER_JACKET));
	     	event.getRegistry().register(new ItemTinyBone(new Item.Properties().group(ModCreativeTabs.GENERAL)).setRegistryName(ItemNames.TINY_BONE));
	     	event.getRegistry().register(new ItemBigBone(new Item.Properties().group(ModCreativeTabs.GENERAL)).setRegistryName(ItemNames.BIG_BONE));
	     	DoggyTalentsMod.LOGGER.info("Finished Registering Items");
	    }
    }
}