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
		Constants.allowPermaDeath = config.get("doggySettings", "allowPermaDeath", false, "If set to false it means you dog can never die and will be in incpasitated mode. If set to true you dogs will die one there health = 0.").getBoolean(false);
		Constants.tenDayPuppies = config.get("doggySettings", "tenDayPuppies", true, "Do you pups take 10 days the grow.").getBoolean(true);
		Constants.isHungerOn = config.get("doggySettings", "isHungerOn", true).getBoolean(true);
		Constants.barkRate = config.get("doggySettings", "barkRate", 10, "Default is 10, higher the number the slow the dogs bark. Lower the number the faster the dogs bark.").getInt(10);
		Constants.direParticalsOff = config.get("doggySettings", "direParticalsOff", false, "When false there will be portal particles if your dog is dire level 30, otherwise no dire particals.").getBoolean(false);
		Constants.isStartingItemEnabled = config.get("doggySettings", "isStartingItemsEnabled", true, "When enabled you will spawn with a guide, Doggy Charm and Command Emblem.").getBoolean(true);
		Constants.bloodWhenIncapacitated = config.get("doggySettings", "bloodWhenIncapacitated", true, "If true then a dog is Incapacitated they will have blood on them").getBoolean(true);
		
		config.save();
	 }
}
