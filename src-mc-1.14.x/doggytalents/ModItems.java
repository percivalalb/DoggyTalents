package doggytalents;

import doggytalents.item.ItemBigBone;
import doggytalents.item.ItemCapeColoured;
import doggytalents.item.ItemChewStick;
import doggytalents.item.ItemCreativeCollar;
import doggytalents.item.ItemCreativeOwnerChange;
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
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

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
	
	@Mod.EventBusSubscriber(modid = Reference.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class Registration {
		
	    @SubscribeEvent
	    public static void onItemRegister(final RegistryEvent.Register<Item> event) {
	    	IForgeRegistry<Item> itemRegistry = event.getRegistry();
	    	DoggyTalentsMod.LOGGER.debug("Registering Items");
	    	itemRegistry.register(new ItemThrowBone(ItemThrowBone.Type.DRY, ()->THROW_BONE_WET, new Item.Properties().group(ModCreativeTabs.GENERAL).maxStackSize(2)).setRegistryName(ItemNames.THROW_BONE));
	    	itemRegistry.register(new ItemThrowBone(ItemThrowBone.Type.WET, ()->THROW_BONE, new Item.Properties().group(ModCreativeTabs.GENERAL).maxStackSize(1)).setRegistryName(ItemNames.THROW_BONE_WET));
	    	itemRegistry.register(new ItemThrowBone(ItemThrowBone.Type.DRY, ()->THROW_STICK_WET, new Item.Properties().group(ModCreativeTabs.GENERAL).maxStackSize(8)).setRegistryName(ItemNames.THROW_STICK));
	    	itemRegistry.register(new ItemThrowBone(ItemThrowBone.Type.WET, ()->THROW_STICK, new Item.Properties().group(ModCreativeTabs.GENERAL).maxStackSize(1)).setRegistryName(ItemNames.THROW_STICK_WET));
	    	itemRegistry.register(new ItemTreat(20, new Item.Properties().group(ModCreativeTabs.GENERAL)).setRegistryName(ItemNames.TRAINING_TREAT));
	    	itemRegistry.register(new ItemTreat(40, new Item.Properties().group(ModCreativeTabs.GENERAL)).setRegistryName(ItemNames.SUPER_TREAT));
	    	itemRegistry.register(new ItemTreat(60, new Item.Properties().group(ModCreativeTabs.GENERAL)).setRegistryName(ItemNames.MASTER_TREAT));
	    	itemRegistry.register(new ItemDireTreat(new Item.Properties().group(ModCreativeTabs.GENERAL)).setRegistryName(ItemNames.DIRE_TREAT));
	    	itemRegistry.register(new Item(new Item.Properties().group(ModCreativeTabs.GENERAL)).setRegistryName(ItemNames.BREEDING_BONE));
	    	itemRegistry.register(new Item(new Item.Properties().group(ModCreativeTabs.GENERAL).maxStackSize(1)).setRegistryName(ItemNames.COLLAR_SHEARS));
	    	itemRegistry.register(new ItemDoggyCharm(new Item.Properties().group(ModCreativeTabs.GENERAL).maxStackSize(1)).setRegistryName(ItemNames.DOGGY_CHARM));
	    	itemRegistry.register(new Item(new Item.Properties().group(ModCreativeTabs.GENERAL)).setRegistryName(ItemNames.RADIO_COLLAR));
	    	itemRegistry.register(new ItemWoolCollar(new Item.Properties().group(ModCreativeTabs.GENERAL)).setRegistryName(ItemNames.WOOL_COLLAR));
	    	itemRegistry.register(new ItemCreativeCollar(ItemFancyCollar.Type.CREATIVE, new Item.Properties().group(ModCreativeTabs.GENERAL)).setRegistryName(ItemNames.CREATIVE_COLLAR));
	      	itemRegistry.register(new ItemFancyCollar(ItemFancyCollar.Type.SPOTTED, new Item.Properties().group(ModCreativeTabs.GENERAL)).setRegistryName(ItemNames.SPOTTED_COLLAR));
	      	itemRegistry.register(new ItemFancyCollar(ItemFancyCollar.Type.MULTI_COLOURED, new Item.Properties().group(ModCreativeTabs.GENERAL)).setRegistryName(ItemNames.MULTICOLOURED_COLLAR));
	    	itemRegistry.register(new ItemRadarCreative(new Item.Properties().group(ModCreativeTabs.GENERAL).maxStackSize(1)).setRegistryName(ItemNames.CREATIVE_RADAR));
	      	itemRegistry.register(new ItemRadar(new Item.Properties().group(ModCreativeTabs.GENERAL).maxStackSize(1)).setRegistryName(ItemNames.RADAR));
	    	itemRegistry.register(new ItemWhistle(new Item.Properties().group(ModCreativeTabs.GENERAL).maxStackSize(1)).setRegistryName(ItemNames.WHISTLE));
	    	itemRegistry.register(new ItemTreatBag(new Item.Properties().group(ModCreativeTabs.GENERAL).maxStackSize(1)).setRegistryName(ItemNames.TREAT_BAG));
	     	itemRegistry.register(new ItemChewStick(new Item.Properties().group(ModCreativeTabs.GENERAL)).setRegistryName(ItemNames.CHEW_STICK));
	     	itemRegistry.register(new Item(new Item.Properties().group(ModCreativeTabs.GENERAL)).setRegistryName(ItemNames.CAPE));
	     	itemRegistry.register(new ItemCapeColoured(new Item.Properties().group(ModCreativeTabs.GENERAL)).setRegistryName(ItemNames.CAPE_COLOURED));
	     	itemRegistry.register(new Item(new Item.Properties().group(ModCreativeTabs.GENERAL)).setRegistryName(ItemNames.SUNGLASSES));
	     	itemRegistry.register(new Item(new Item.Properties().group(ModCreativeTabs.GENERAL)).setRegistryName(ItemNames.LEATHER_JACKET));
	     	itemRegistry.register(new ItemTinyBone(new Item.Properties().group(ModCreativeTabs.GENERAL)).setRegistryName(ItemNames.TINY_BONE));
	     	itemRegistry.register(new ItemBigBone(new Item.Properties().group(ModCreativeTabs.GENERAL)).setRegistryName(ItemNames.BIG_BONE));
	     	itemRegistry.register(new ItemCreativeOwnerChange(new Item.Properties().group(ModCreativeTabs.GENERAL).maxStackSize(1)).setRegistryName(ItemNames.OWNER_CHANGE));
	     	DoggyTalentsMod.LOGGER.debug("Finished Registering Items");
	    }
    }
}