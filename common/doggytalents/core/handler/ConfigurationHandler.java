package doggytalents.core.handler;

import doggytalents.lib.BlockIds;
import doggytalents.lib.Constants;
import doggytalents.lib.ItemIds;
import net.minecraftforge.common.Configuration;

/**
 * @author ProPercivalalb
 */
public class ConfigurationHandler {

	public static Configuration configuration;
	
	public static void loadConfig(Configuration config) {
		config.load();
		configuration = config;
		
		ItemIds.ID_DOG_OWNERS_MANUEL = config.getItem("dogOwnersManuel", 13550).getInt(13550);
		ItemIds.ID_THROW_BONE = config.getItem("throwBone", 13551).getInt(13551);
		ItemIds.ID_COMMAND_EMBLEM = config.getItem("commandEmblem", 13552).getInt(13552);
		ItemIds.ID_TRAINING_TREAT = config.getItem("trainingTreat", 13553).getInt(13553);
		ItemIds.ID_SUPER_TREAT = config.getItem("superTreat", 13554).getInt(13554);
		ItemIds.ID_MASTER_TREAT = config.getItem("masterTreat", 13555).getInt(13555);
		ItemIds.ID_DIRE_TREAT = config.getItem("direTreat", 13556).getInt(13556);
		ItemIds.ID_BREEDING_BONE = config.getItem("breedingBone", 13557).getInt(13557);
		ItemIds.ID_COLLAR_SHEARS = config.getItem("collarShears", 13558).getInt(13558);
		ItemIds.ID_DOGGY_CHARM = config.getItem("doggyCharm", 13559).getInt(13559);
		
		BlockIds.ID_DOG_BED = config.getBlock("dogBed", 3550).getInt(3550);
		BlockIds.ID_DOG_BOWL = config.getBlock("bogBowl", 3551).getInt(3551);
		
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
