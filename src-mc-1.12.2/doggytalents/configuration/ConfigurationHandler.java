package doggytalents.configuration;

import java.util.ArrayList;
import java.util.List;

import doggytalents.lib.Constants;
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

		Constants.DEBUG_MODE = CONFIG.get(CATEGORY_GENERAL, "debugMode", false, "Enables debugging mode, which would output values for the sake of finding issues in the mod.").setRequiresMcRestart(false).setRequiresWorldRestart(false).getBoolean(false);

		orderDTGeneral.add("debugMode");

		//Sets the category property order to that of which you have set the list above
		CONFIG.setCategoryPropertyOrder(CATEGORY_GENERAL, orderDTGeneral);

		//Creates list for dog behavior settings
		List<String> orderDSetting = new ArrayList<String>();
		 
		Constants.DOGS_IMMORTAL = CONFIG.get(CATEGORY_DOGGYSETTINGS, "isDogImmortal", true, "Determines if dogs die when their health reaches zero. If true, dogs will not die, and will instead become incapacitated.").setRequiresMcRestart(true).getBoolean(true);
		Constants.TEN_DAY_PUPS = CONFIG.get(CATEGORY_DOGGYSETTINGS, "tenDayPuppies", true, "Determines if pups take 10 days to mature.").getBoolean(true);
		Constants.IS_HUNGER_ON = CONFIG.get(CATEGORY_DOGGYSETTINGS, "isHungerOn", true, "Enables hunger mode for the dog").getBoolean(true);
		//Constants.barkRate = config.get("doggySettings", "barkRate", 10, "Default is 10, higher the number the slower the dogs bark. Lower the number the faster the dogs bark.").getInt(10);
		Constants.DIRE_PARTICLES = CONFIG.get(CATEGORY_DOGGYSETTINGS, "direParticles", true, "Enables the particle effect on Dire Level 30 dogs.").getBoolean(true);
		Constants.STARTING_ITEMS = CONFIG.get(CATEGORY_DOGGYSETTINGS, "isStartingItemsEnabled", true, "When enabled you will spawn with a guide, Doggy Charm and Command Emblem.").getBoolean(true);
		Constants.RENDER_BLOOD = CONFIG.get(CATEGORY_DOGGYSETTINGS, "bloodWhenIncapacitated", true, "When enabled, Dogs will show blood texture while incapacitated.").getBoolean(true);
		Constants.DOGGY_WINGS = CONFIG.get(CATEGORY_DOGGYSETTINGS, "doggyWings", true, "When enabled, Dogs will have wings when at level 5 pillow paw").getBoolean(true);
		Constants.DOG_GENDER = CONFIG.get(CATEGORY_DOGGYSETTINGS, "dogGender", false, "When enabled, dogs will be randomly assigned genders and will only mate and produce children with the opposite gender").setRequiresMcRestart(true).getBoolean(false);
		Constants.DOG_WHINE_WHEN_HUNGER_LOW = CONFIG.get(CATEGORY_DOGGYSETTINGS, "shouldWhineWhenStarving", true, "Determines if dogs should whine when hunger reaches below 20 DP").getBoolean(true);
		Constants.USE_DT_TEXTURES = CONFIG.get(CATEGORY_DOGGYSETTINGS, "useDTTextures", true, "If disabled will use the default minecraft wolf skin for all dog textures.").getBoolean(true);
		Constants.RENDER_PACK_PUPPY_CHEST = CONFIG.get(CATEGORY_DOGGYSETTINGS, "doggyChest", true, "When enabled, dogs with points in pack puppy will have chests on their side.").getBoolean(true);
		Constants.RENDER_SADDLE = CONFIG.get(CATEGORY_DOGGYSETTINGS, "doggySaddle", true, "When enabled, dogs with points in wolf mount will have a saddle on.").getBoolean(true);
		
		//Add Everything in the current list in whatever way you want
		orderDSetting.add("isDogImmortal");
		orderDSetting.add("tenDayPuppies");
		orderDSetting.add("isHungerOn");
		orderDSetting.add("shouldWhineWhenStarving");
		orderDSetting.add("direParticles");
		orderDSetting.add("isStartingItemsEnabled");
		orderDSetting.add("bloodWhenIncapacitated");
		orderDSetting.add("doggyWings");
		orderDSetting.add("dogGender");
		
		//Sets the category property order to that of which you have set the list above
		CONFIG.setCategoryPropertyOrder(CATEGORY_DOGGYSETTINGS, orderDSetting);
		
		Constants.DISABLED_TALENTS.clear();
		
		String[] talentIds = new String[] {"bedfinder", "blackpelt", "creepersweeper", "doggydash", "fisherdog", "guarddog", "happyeater", "hellhound", 
											"hunterdog", "packpuppy", "pestfighter", "pillowpaw", "poisonfang", "puppyeyes", "quickhealer", 
											"rescuedog", "roaringgale", "shepherddog", "swimmerdog", "wolfmount"};
		for(String talentId : talentIds) {
			boolean enabled = CONFIG.get(CATEGORY_TALENT, talentId, true).getBoolean(true);
			if(!enabled)
				Constants.DISABLED_TALENTS.add(talentId);
		}
		
		if(CONFIG.hasChanged())
			CONFIG.save();
	}
}