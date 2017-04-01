package doggytalents.handler;

import doggytalents.lib.Constants;
import net.minecraftforge.common.config.Configuration;

/**
 * @author ProPercivalalb
 */
public class ConfigurationHandler {

	public static Configuration configuration;
	
	public static void loadConfig(Configuration config) {
		config.load();
		configuration = config;
		config.addCustomCategoryComment("doggySettings", "Here you can change details about your dog.");
		Constants.DOGS_IMMORTAL = config.get("doggySettings", "isDogImmortal", true, "Determines if dogs die when their health reaches zero. If true, dogs will not die, and will instead become incapacitated.").getBoolean(true);
		Constants.TEN_DAY_PUPS = config.get("doggySettings", "tenDayPuppies", true, "Determines if pups take 10 days to mature.").getBoolean(true);
		Constants.IS_HUNGER_ON = config.get("doggySettings", "isHungerOn", true).getBoolean(true);
		//Constants.barkRate = config.get("doggySettings", "barkRate", 10, "Default is 10, higher the number the slower the dogs bark. Lower the number the faster the dogs bark.").getInt(10);
		Constants.DIRE_PARTICLES = config.get("doggySettings", "direParticals", true, "Enables the particle effect on Dire Level 30 dogs.").getBoolean(true);
		Constants.STARTING_ITEMS = config.get("doggySettings", "isStartingItemsEnabled", true, "When enabled you will spawn with a guide, Doggy Charm and Command Emblem.").getBoolean(true);
		Constants.RENDER_BLOOD = config.get("doggySettings", "bloodWhenIncapacitated", true, "When enabled, Dogs will bleed while incapacitated.").getBoolean(true);
		Constants.VERSION_CHECK = config.get("general", "versionCheck", true, "Should check whether there are available updates").getBoolean(true);
		
		config.save();
	 }
}
