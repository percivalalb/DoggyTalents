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
    public static final ResourceLocation COMMAND_EMBLEM = new ResourceLocation(Reference.MOD_ID, "command_emblem");
    
    private static HashMap<String, String> OLD_NEW_BED = new HashMap<String, String>();
    private static HashMap<String, Supplier<Talent>> OLD_NEW_TALENT = new HashMap<String, Supplier<Talent>>();
    
    public static String getBedOldNamingScheme(String name) {
        return OLD_NEW_BED.containsKey(name) ? OLD_NEW_BED.get(name) : name;
    }
    
    public static Talent getTalentOldNamingScheme(String name) {
        return OLD_NEW_TALENT.containsKey(name) ? OLD_NEW_TALENT.get(name).get() : null;
    }
    
    static {
        // Update to 1.13
        OLD_NEW_BED.put("minecraft:wool.0", "minecraft:white_wool");
        OLD_NEW_BED.put("minecraft:wool.1", "minecraft:orange_wool");
        OLD_NEW_BED.put("minecraft:wool.2", "minecraft:magenta_wool");
        OLD_NEW_BED.put("minecraft:wool.3", "minecraft:light_blue_wool");
        OLD_NEW_BED.put("minecraft:wool.4", "minecraft:yellow_wool");
        OLD_NEW_BED.put("minecraft:wool.5", "minecraft:lime_wool");
        OLD_NEW_BED.put("minecraft:wool.6", "minecraft:pink_wool");
        OLD_NEW_BED.put("minecraft:wool.7", "minecraft:gray_wool");
        OLD_NEW_BED.put("minecraft:wool.8", "minecraft:light_gray_wool");
        OLD_NEW_BED.put("minecraft:wool.9", "minecraft:cyan_wool");
        OLD_NEW_BED.put("minecraft:wool.10", "minecraft:purple_wool");
        OLD_NEW_BED.put("minecraft:wool.11", "minecraft:blue_wool");
        OLD_NEW_BED.put("minecraft:wool.12", "minecraft:brown_wool");
        OLD_NEW_BED.put("minecraft:wool.13", "minecraft:green_wool");
        OLD_NEW_BED.put("minecraft:wool.14", "minecraft:red_wool");
        OLD_NEW_BED.put("minecraft:wool.15", "minecraft:black_wool");
        OLD_NEW_BED.put("minecraft:planks.0", "minecraft:oak_planks");
        OLD_NEW_BED.put("minecraft:planks.1", "minecraft:spruce_planks");
        OLD_NEW_BED.put("minecraft:planks.2", "minecraft:birch_planks");
        OLD_NEW_BED.put("minecraft:planks.3", "minecraft:jungle_planks");
        OLD_NEW_BED.put("minecraft:planks.4", "minecraft:acacia_planks");
        OLD_NEW_BED.put("minecraft:planks.5", "minecraft:dark_oak_planks");
        
        // Update to 1.14
        OLD_NEW_BED.put("minecraft_white_wool", "minecraft:white_wool");
        OLD_NEW_BED.put("minecraft_orange_wool", "minecraft:orange_wool");
        OLD_NEW_BED.put("minecraft_magenta_wool", "minecraft:magenta_wool");
        OLD_NEW_BED.put("minecraft_light_blue_wool", "minecraft:light_blue_wool");
        OLD_NEW_BED.put("minecraft_yellow_wool", "minecraft:yellow_wool");
        OLD_NEW_BED.put("minecraft_lime_wool", "minecraft:lime_wool");
        OLD_NEW_BED.put("minecraft_pink_wool", "minecraft:pink_wool");
        OLD_NEW_BED.put("minecraft_gray_wool", "minecraft:gray_wool");
        OLD_NEW_BED.put("minecraft_light_gray_wool", "minecraft:light_gray_wool");
        OLD_NEW_BED.put("minecraft_cyan_wool", "minecraft:cyan_wool");
        OLD_NEW_BED.put("minecraft_purple_wool", "minecraft:purple_wool");
        OLD_NEW_BED.put("minecraft_blue_wool", "minecraft:blue_wool");
        OLD_NEW_BED.put("minecraft_brown_wool", "minecraft:brown_wool");
        OLD_NEW_BED.put("minecraft_green_wool", "minecraft:green_wool");
        OLD_NEW_BED.put("minecraft_red_wool", "minecraft:red_wool");
        OLD_NEW_BED.put("minecraft_black_wool", "minecraft:black_wool");
        OLD_NEW_BED.put("minecraft_oak_planks", "minecraft:oak_planks");
        OLD_NEW_BED.put("minecraft_spruce_planks", "minecraft:spruce_planks");
        OLD_NEW_BED.put("minecraft_birch_planks", "minecraft:birch_planks");
        OLD_NEW_BED.put("minecraft_jungle_planks", "minecraft:jungle_planks");
        OLD_NEW_BED.put("minecraft_acacia_planks", "minecraft:acacia_planks");
        OLD_NEW_BED.put("minecraft_dark_oak_planks", "minecraft:dark_oak_planks");
        
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
        //OLD_NEW_TALENT.put("rangedattacker", ()->ModTalents.RANGED_ATTACKER);
        OLD_NEW_TALENT.put("rescuedog", ()->ModTalents.RESCUE_DOG);
        OLD_NEW_TALENT.put("roaringgale", ()->ModTalents.ROARING_GALE);
        OLD_NEW_TALENT.put("shepherddog", ()->ModTalents.SHEPHERD_DOG);
        OLD_NEW_TALENT.put("swimmerdog", ()->ModTalents.SWIMMER_DOG);
        OLD_NEW_TALENT.put("wolfmount", ()->ModTalents.WOLF_MOUNT);

    }
}
