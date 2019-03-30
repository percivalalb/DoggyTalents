package doggytalents.helper;

import java.util.HashMap;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import doggytalents.DoggyTalentsMod;
import doggytalents.lib.Reference;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent.MissingMappings;
import net.minecraftforge.event.RegistryEvent.MissingMappings.Mapping;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLModIdMappingEvent;
import net.minecraftforge.fml.event.lifecycle.FMLModIdMappingEvent.ModRemapping;
import net.minecraftforge.registries.GameData;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class Compatibility {

	private static HashMap<String, String> OLD_NEW = new HashMap<String, String>();
	
	public static String getMapOldNamingScheme(String name) {
		return OLD_NEW.containsKey(name) ? OLD_NEW.get(name) : name;
	}
	
    @SubscribeEvent
	public static void remapBlocks(MissingMappings<Block> event) {
    	ImmutableList<Mapping<Block>> mappings = event.getAllMappings();
    	
    	for(Mapping<Block> mapping : mappings) {
    		
    		DoggyTalentsMod.LOGGER.info("Block: " + mapping.key.toString());
    	}
	}
    
    @SubscribeEvent
	public static void remapTile(MissingMappings<TileEntityType<?>> event) {
    	ImmutableList<Mapping<TileEntityType<?>>> mappings = event.getAllMappings();
    	
    	for(Mapping<TileEntityType<?>> mapping : mappings) {
    		
    		DoggyTalentsMod.LOGGER.info("Tile: " + mapping.key.toString());
    	}
	}
    
	@SubscribeEvent
	public static void remapBlocks(FMLModIdMappingEvent event) {
		ImmutableSet<ResourceLocation> registries = event.getRegistries();
		for(ResourceLocation registry : registries) {
	    	ImmutableList<ModRemapping> mappings = event.getRemaps(registry);
	    	DoggyTalentsMod.LOGGER.info(registry.toString());
	    	for(ModRemapping mapping : mappings) {
	    		
	    		DoggyTalentsMod.LOGGER.info("     : " + mapping.key.toString());
	    	}
		}
	}
	
    @SubscribeEvent
   	public static void remapItems(MissingMappings<Item> event) {
    	ImmutableList<Mapping<Item>> mappings = event.getAllMappings();
    	for(Mapping<Item> mapping : mappings) {
    		
    		DoggyTalentsMod.LOGGER.info("Item: " + mapping.key.toString());
    	}
   	}
    
    @SubscribeEvent
   	public static void remapEntities(MissingMappings<EntityType<?>> event) {
    	ImmutableList<Mapping<EntityType<?>>> mappings = event.getAllMappings();
    	
    	for(Mapping<EntityType<?>> mapping : mappings) {
    		
    		DoggyTalentsMod.LOGGER.info("Entity: " + mapping.key.toString());
    	}
   	}
	 
	static {
		OLD_NEW.put("minecraft:wool.0", "minecraft_white_wool");
		OLD_NEW.put("minecraft:wool.1", "minecraft_orange_wool");
		OLD_NEW.put("minecraft:wool.2", "minecraft_magenta_wool");
		OLD_NEW.put("minecraft:wool.3", "minecraft_light_blue_wool");
		OLD_NEW.put("minecraft:wool.4", "minecraft_yellow_wool");
		OLD_NEW.put("minecraft:wool.5", "minecraft_lime_wool");
		OLD_NEW.put("minecraft:wool.6", "minecraft_pink_wool");
		OLD_NEW.put("minecraft:wool.7", "minecraft_gray_wool");
		OLD_NEW.put("minecraft:wool.8", "minecraft_light_gray_wool");
		OLD_NEW.put("minecraft:wool.9", "minecraft_cyan_wool");
		OLD_NEW.put("minecraft:wool.10", "minecraft_purple_wool");
		OLD_NEW.put("minecraft:wool.11", "minecraft_blue_wool");
		OLD_NEW.put("minecraft:wool.12", "minecraft_brown_wool");
		OLD_NEW.put("minecraft:wool.13", "minecraft_green_wool");
		OLD_NEW.put("minecraft:wool.14", "minecraft_red_wool");
		OLD_NEW.put("minecraft:wool.15", "minecraft_black_wool");

		OLD_NEW.put("minecraft:planks.0", "minecraft_oak_planks");
		OLD_NEW.put("minecraft:planks.1", "minecraft_spruce_planks");
		OLD_NEW.put("minecraft:planks.2", "minecraft_birch_planks");
		OLD_NEW.put("minecraft:planks.3", "minecraft_jungle_planks");
		OLD_NEW.put("minecraft:planks.4", "minecraft_acacia_planks");
		OLD_NEW.put("minecraft:planks.5", "minecraft_dark_oak_planks");
	}
}
