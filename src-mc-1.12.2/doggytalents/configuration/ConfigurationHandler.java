package doggytalents.configuration;

import java.util.ArrayList;
import java.util.List;

import doggytalents.lib.ConfigValues;
import doggytalents.lib.TalentNames;
import net.minecraftforge.common.config.Configuration;

/**
 * @author ProPercivalalb, NovaViper
 */
public class ConfigurationHandler {

    public static Configuration CONFIG;
    public static final String CATEGORY_DOGGYSETTINGS = "doggySettings";
    public static final String CATEGORY_TALENT = "talents";
    public static final String CATEGORY_GENERAL = "general";
    
    public static void init(Configuration configuration) {
        CONFIG = configuration;
        loadConfig();
    }
    
     /** TODO NOTE from NovaViper
      *     I did indeed test out these configurations in game and they work perfectly within the world!
      *     I haven't tested tenDayPuupys or isStartingItemsEnabled (though this one will most likely
      *     require a world restart for sure). However, restarting Minecraft (not just the world) might
      *     be required for the immortal configuration.
      *
      *     If you have any questions, you can look at the links in {@link DoggyTalentsGuiFactory}
      *     or contact me again at my email <nova.gamez15+code@gmail.com> or in our conversation on Minecraft
      *     Forums. Thanks!
      */
    public static void loadConfig() {
        CONFIG.addCustomCategoryComment(CATEGORY_GENERAL, "General settings for the mod");
        CONFIG.addCustomCategoryComment(CATEGORY_DOGGYSETTINGS, "Change certain behaviors of dogs");
        CONFIG.addCustomCategoryComment(CATEGORY_TALENT, "Enable and disable talents here as you wish");

        //Creates list for general settings
        List<String> orderDTGeneral = new ArrayList<String>();

        ConfigValues.DEBUG_MODE = CONFIG.get(CATEGORY_GENERAL, "debugMode", false, "Enables debugging mode, which would output values for the sake of finding issues in the mod.").setRequiresMcRestart(false).setRequiresWorldRestart(false).getBoolean(false);

        orderDTGeneral.add("debugMode");

        //Sets the category property order to that of which you have set the list above
        CONFIG.setCategoryPropertyOrder(CATEGORY_GENERAL, orderDTGeneral);

        //Creates list for dog behavior settings
        List<String> orderDSetting = new ArrayList<String>();
         
        ConfigValues.DOGS_IMMORTAL = CONFIG.get(CATEGORY_DOGGYSETTINGS, "isDogImmortal", true, "Determines if dogs die when their health reaches zero. If true, dogs will not die, and will instead become incapacitated.").setRequiresMcRestart(true).getBoolean(true);
        ConfigValues.TIME_TO_MATURE = CONFIG.getInt(CATEGORY_DOGGYSETTINGS, "timeToMature", 48000, 0, Integer.MAX_VALUE, "The time in ticks it takes for a baby dog to become an adult, default 48000 (2 Minecraft days) and minimum 0");
        ConfigValues.IS_HUNGER_ON = CONFIG.get(CATEGORY_DOGGYSETTINGS, "isHungerOn", true, "Enables hunger mode for the dog").getBoolean(true);
        //Constants.barkRate = config.get("doggySettings", "barkRate", 10, "Default is 10, higher the number the slower the dogs bark. Lower the number the faster the dogs bark.").getInt(10);
        ConfigValues.DIRE_PARTICLES = CONFIG.get(CATEGORY_DOGGYSETTINGS, "direParticles", true, "Enables the particle effect on Dire Level 30 dogs.").getBoolean(true);
        ConfigValues.STARTING_ITEMS = CONFIG.get(CATEGORY_DOGGYSETTINGS, "isStartingItemsEnabled", false, "When enabled you will spawn with a guide, Doggy Charm and Command Emblem.").getBoolean(false);
        ConfigValues.DOG_GENDER = CONFIG.get(CATEGORY_DOGGYSETTINGS, "dogGender", true, "When enabled, dogs will be randomly assigned genders and will only mate and produce children with the opposite gender").setRequiresMcRestart(true).getBoolean(false);
        ConfigValues.DOG_WHINE_WHEN_HUNGER_LOW = CONFIG.get(CATEGORY_DOGGYSETTINGS, "shouldWhineWhenStarving", true, "Determines if dogs should whine when hunger reaches below 20 DP").getBoolean(true);
        ConfigValues.USE_DT_TEXTURES = CONFIG.get(CATEGORY_DOGGYSETTINGS, "useDTTextures", true, "If disabled will use the default minecraft wolf skin for all dog textures.").getBoolean(true);
        ConfigValues.RENDER_CHEST = CONFIG.get(CATEGORY_DOGGYSETTINGS, "doggyChest", true, "When enabled, dogs with points in pack puppy will have chests on their side.").getBoolean(true);
        ConfigValues.RENDER_SADDLE = CONFIG.get(CATEGORY_DOGGYSETTINGS, "doggySaddle", true, "When enabled, dogs with points in wolf mount will have a saddle on.").getBoolean(true);
        ConfigValues.RENDER_BLOOD = CONFIG.get(CATEGORY_DOGGYSETTINGS, "bloodWhenIncapacitated", true, "When enabled, Dogs will show blood texture while incapacitated.").getBoolean(true);
        ConfigValues.RENDER_WINGS = CONFIG.get(CATEGORY_DOGGYSETTINGS, "doggyWings", false, "When enabled, Dogs will have wings when at level 5 pillow paw").getBoolean(false);
        ConfigValues.RENDER_ARMOUR = CONFIG.get(CATEGORY_DOGGYSETTINGS, "doggyArmour", false, "When enabled, dogs with points in guard dog will have armour.").getBoolean(false);
        ConfigValues.MOD_BED_STUFF = CONFIG.get(CATEGORY_DOGGYSETTINGS, "modBedStuff", true, "When enabled, some mods that add new planks will be able to be used for dog beds.").getBoolean(true);
        ConfigValues.PUPS_GET_PARENT_LEVELS = CONFIG.get(CATEGORY_DOGGYSETTINGS, "pupsGetParentLevel", false, "When enabled, puppies get some levels from parents. When disabled, puppies start at 0 points").getBoolean(false);
        
        //Add Everything in the current list in whatever way you want
        orderDSetting.add("isDogImmortal");
        orderDSetting.add("tenDayPuppies");
        orderDSetting.add("isHungerOn");
        orderDSetting.add("dogGender");
        orderDSetting.add("shouldWhineWhenStarving");
        orderDSetting.add("pupsGetParentLevel");
        orderDSetting.add("isStartingItemsEnabled");
        orderDSetting.add("modBedStuff");
        
        orderDSetting.add("direParticles");
        orderDSetting.add("bloodWhenIncapacitated");
        orderDSetting.add("doggyChest");
        orderDSetting.add("doggySaddle");
        orderDSetting.add("doggyWings");
        orderDSetting.add("doggyArmour");
        orderDSetting.add("dogGender");
        
        
        //Sets the category property order to that of which you have set the list above
        CONFIG.setCategoryPropertyOrder(CATEGORY_DOGGYSETTINGS, orderDSetting);
        
        ConfigValues.DISABLED_TALENTS.clear();
        
        String[] talentIds = new String[] {TalentNames.BED_FINDER, TalentNames.BLACK_PELT, TalentNames.CREEPER_SWEEPER, TalentNames.DOGGY_DASH, TalentNames.FISHER_DOG, 
                TalentNames.GUARD_DOG, TalentNames.HAPPY_EATER, TalentNames.HELL_HOUND, TalentNames.HUNTER_DOG, TalentNames.PACK_PUPPY, TalentNames.PEST_FIGHTER, 
                TalentNames.PILLOW_PAW, TalentNames.POISON_FANG, TalentNames.PUPPY_EYES, TalentNames.QUICK_HEALER, TalentNames.RESCUE_DOG, TalentNames.ROARING_GALE,
                TalentNames.SHEPHERD_DOG, TalentNames.SWIMMER_DOG, TalentNames.WOLF_MOUNT};
        for(String talentId : talentIds) {
            boolean enabled = CONFIG.get(CATEGORY_TALENT, talentId, true).getBoolean(true);
            if(!enabled)
                ConfigValues.DISABLED_TALENTS.add(talentId);
        }
        
        if(CONFIG.hasChanged())
            CONFIG.save();
    }
}