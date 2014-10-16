package doggytalents.core.handler;

import net.minecraftforge.common.config.Configuration;
import doggytalents.lib.Constants;

/**
 * @author ProPercivalalb
 */
public class ConfigurationHandler {

	public static Configuration configuration;
	
	public static void loadConfig(Configuration config) {
		config.load();
		configuration = config;
		config.addCustomCategoryComment("doggySettings", "Here you can change details about your dog.");
		Constants.allowPermaDeath = config.get("doggySettings", "allowPermaDeath", false, "Determines if dogs die when their health reaches zero. If false, dogs will not die, and will instead become incapacitated.").getBoolean(false);
		Constants.tenDayPuppies = config.get("doggySettings", "tenDayPuppies", true, "Determines if pups take 10 days to mature.").getBoolean(true);
		Constants.isHungerOn = config.get("doggySettings", "isHungerOn", true).getBoolean(true);
		Constants.barkRate = config.get("doggySettings", "barkRate", 10, "Default is 10, higher the number the slower the dogs bark. Lower the number the faster the dogs bark.").getInt(10);
		Constants.direParticalsOff = config.get("doggySettings", "direParticalsOff", false, "Disables the particle effect on Dire Level 30 dogs.").getBoolean(false);
		Constants.isStartingItemEnabled = config.get("doggySettings", "isStartingItemsEnabled", true, "When enabled you will spawn with a guide, Doggy Charm and Command Emblem.").getBoolean(true);
		Constants.bloodWhenIncapacitated = config.get("doggySettings", "bloodWhenIncapacitated", true, "When enabled, Dogs will bleed while incapacitated.").getBoolean(true);
		Constants.versionCheck = config.get("general", "versionCheck", true, "Should check whether there are available updates").getBoolean(true);
		
		config.save();
	 }
}
