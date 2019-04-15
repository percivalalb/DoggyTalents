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
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

public class ModItems {

	@ObjectHolder(ItemNames.THROW_BONE)
	public static Item THROW_BONE;
	@ObjectHolder(ItemNames.THROW_BONE_WET)
	public static Item THROW_BONE_WET;
	@ObjectHolder(ItemNames.THROW_STICK)
	public static Item THROW_STICK;
	@ObjectHolder(ItemNames.THROW_STICK_WET)
	public static Item THROW_STICK_WET;
	@ObjectHolder(ItemNames.COMMAND_EMBLEM)
	public static Item COMMAND_EMBLEM;
	@ObjectHolder(ItemNames.TRAINING_TREAT)
    public static Item TRAINING_TREAT;
	@ObjectHolder(ItemNames.SUPER_TREAT)
    public static Item SUPER_TREAT;
	@ObjectHolder(ItemNames.MASTER_TREAT)
    public static Item MASTER_TREAT;
	@ObjectHolder(ItemNames.DIRE_TREAT)
    public static Item DIRE_TREAT;
	@ObjectHolder(ItemNames.BREEDING_BONE)
    public static Item BREEDING_BONE;
	@ObjectHolder(ItemNames.COLLAR_SHEARS)
    public static Item COLLAR_SHEARS;
	@ObjectHolder(ItemNames.DOGGY_CHARM)
    public static Item DOGGY_CHARM;
	@ObjectHolder(ItemNames.RADIO_COLLAR)
    public static Item RADIO_COLLAR;
	@ObjectHolder(ItemNames.WOOL_COLLAR)
    public static Item WOOL_COLLAR;
	@ObjectHolder(ItemNames.CREATIVE_COLLAR)
    public static Item CREATIVE_COLLAR;
	@ObjectHolder(ItemNames.SPOTTED_COLLAR)
    public static Item SPOTTED_COLLAR;
	@ObjectHolder(ItemNames.MULTICOLOURED_COLLAR)
    public static Item MULTICOLOURED_COLLAR;
	@ObjectHolder(ItemNames.RADAR)
    public static Item RADAR;
	@ObjectHolder(ItemNames.CREATIVE_RADAR)
    public static Item CREATIVE_RADAR;
	@ObjectHolder(ItemNames.WHISTLE)
    public static Item WHISTLE;
	@ObjectHolder(ItemNames.TREAT_BAG)
    public static Item TREAT_BAG;
	@ObjectHolder(ItemNames.CHEW_STICK)
    public static Item CHEW_STICK;
	@ObjectHolder(ItemNames.CAPE)
    public static Item CAPE;
	@ObjectHolder(ItemNames.CAPE_COLOURED)
    public static Item CAPE_COLOURED;
	@ObjectHolder(ItemNames.SUNGLASSES)
    public static Item SUNGLASSES;
	@ObjectHolder(ItemNames.LEATHER_JACKET)
    public static Item LEATHER_JACKET;
	@ObjectHolder(ItemNames.TINY_BONE)
    public static Item TINY_BONE;
	@ObjectHolder(ItemNames.BIG_BONE)
    public static Item BIG_BONE;
	
	@Mod.EventBusSubscriber(modid = Reference.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class Registration
    {
		
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
	    }
	
	    
	    private static ItemBlock makeItemBlock(Block block) {
	        return (ItemBlock)new ItemBlock(block, new Item.Properties().group(ModCreativeTabs.GENERAL)).setRegistryName(block.getRegistryName());
	    }
    }
}