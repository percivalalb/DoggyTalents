package doggytalents.helper;

import java.util.HashMap;
import java.util.function.Supplier;

import doggytalents.ModTalents;
import doggytalents.api.inferface.Talent;
import doggytalents.lib.Reference;
import net.minecraft.util.ResourceLocation;

//@Mod.EventBusSubscriber(modid = Reference.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class Compatibility {

	public static final ResourceLocation DOGGY_BEAM = new ResourceLocation(Reference.MOD_ID, "attackbeam");
	
	private static HashMap<String, String> OLD_NEW_BED = new HashMap<String, String>();
	private static HashMap<String, Supplier<Talent>> OLD_NEW_TALENT = new HashMap<String, Supplier<Talent>>();
	
	public static String getBedOldNamingScheme(String name) {
		return OLD_NEW_BED.containsKey(name) ? OLD_NEW_BED.get(name) : name;
	}
	
	public static Talent getTalentOldNamingScheme(String name) {
		return OLD_NEW_TALENT.containsKey(name) ? OLD_NEW_TALENT.get(name).get() : null;
	}
	
	/**
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
   	}**/
	 
	static {
		OLD_NEW_BED.put("minecraft:wool.0", "minecraft_white_wool");
		OLD_NEW_BED.put("minecraft:wool.1", "minecraft_orange_wool");
		OLD_NEW_BED.put("minecraft:wool.2", "minecraft_magenta_wool");
		OLD_NEW_BED.put("minecraft:wool.3", "minecraft_light_blue_wool");
		OLD_NEW_BED.put("minecraft:wool.4", "minecraft_yellow_wool");
		OLD_NEW_BED.put("minecraft:wool.5", "minecraft_lime_wool");
		OLD_NEW_BED.put("minecraft:wool.6", "minecraft_pink_wool");
		OLD_NEW_BED.put("minecraft:wool.7", "minecraft_gray_wool");
		OLD_NEW_BED.put("minecraft:wool.8", "minecraft_light_gray_wool");
		OLD_NEW_BED.put("minecraft:wool.9", "minecraft_cyan_wool");
		OLD_NEW_BED.put("minecraft:wool.10", "minecraft_purple_wool");
		OLD_NEW_BED.put("minecraft:wool.11", "minecraft_blue_wool");
		OLD_NEW_BED.put("minecraft:wool.12", "minecraft_brown_wool");
		OLD_NEW_BED.put("minecraft:wool.13", "minecraft_green_wool");
		OLD_NEW_BED.put("minecraft:wool.14", "minecraft_red_wool");
		OLD_NEW_BED.put("minecraft:wool.15", "minecraft_black_wool");

		OLD_NEW_BED.put("minecraft:planks.0", "minecraft_oak_planks");
		OLD_NEW_BED.put("minecraft:planks.1", "minecraft_spruce_planks");
		OLD_NEW_BED.put("minecraft:planks.2", "minecraft_birch_planks");
		OLD_NEW_BED.put("minecraft:planks.3", "minecraft_jungle_planks");
		OLD_NEW_BED.put("minecraft:planks.4", "minecraft_acacia_planks");
		OLD_NEW_BED.put("minecraft:planks.5", "minecraft_dark_oak_planks");
		
		OLD_NEW_TALENT.put("bedfinder", ()->ModTalents.BED_FINDER);
		OLD_NEW_TALENT.put("blackpelt", ()->ModTalents.BLACK_PELT);
		OLD_NEW_TALENT.put("creepersweeper", ()->ModTalents.CREEPER_SWEEPER);
		OLD_NEW_TALENT.put("doggydash", ()->ModTalents.DOGGY_DASH);
		OLD_NEW_TALENT.put("fisherdog", ()->ModTalents.FISHER_DOG);
		OLD_NEW_TALENT.put("guarddog", ()->ModTalents.GUARD_DOG);
		OLD_NEW_TALENT.put("happyeater", ()->ModTalents.HAPPY_EATER);
		OLD_NEW_TALENT.put("hellhound", ()->ModTalents.HELL_HOUND);
		OLD_NEW_TALENT.put("hunterdog", ()->ModTalents.HUNTER_DOG);
		OLD_NEW_TALENT.put("packpuppy", ()->ModTalents.PACK_PUPPY);
		OLD_NEW_TALENT.put("pestfighter", ()->ModTalents.PEST_FIGHTER);
		OLD_NEW_TALENT.put("pillowpaw", ()->ModTalents.PILLOW_PAW);
		OLD_NEW_TALENT.put("poisonfang", ()->ModTalents.POISON_FANG);
		OLD_NEW_TALENT.put("puppyeyes", ()->ModTalents.PUPPY_EYES);
		OLD_NEW_TALENT.put("quickhealer", ()->ModTalents.QUICK_HEALER);
		OLD_NEW_TALENT.put("rangedattacker", ()->ModTalents.RANGED_ATTACKER);
		OLD_NEW_TALENT.put("rescuedog", ()->ModTalents.RESCUE_DOG);
		OLD_NEW_TALENT.put("roaringgale", ()->ModTalents.ROARING_GALE);
		OLD_NEW_TALENT.put("shepherddog", ()->ModTalents.SHEPHERD_DOG);
		OLD_NEW_TALENT.put("swimmerdog", ()->ModTalents.SWIMMER_DOG);
		OLD_NEW_TALENT.put("wolfmount", ()->ModTalents.WOLF_MOUNT);

	}
}
