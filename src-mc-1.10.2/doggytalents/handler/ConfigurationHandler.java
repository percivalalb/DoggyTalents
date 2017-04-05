package doggytalents.handler;

import doggytalents.client.gui.ConfigGuiFactory;
import doggytalents.lib.Constants;
import doggytalents.lib.Reference;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ProPercivalalb, NovaViper
 */
public class ConfigurationHandler {

	public static Configuration config;
	public static final String CATEGORY_DOGGYSETTINGS = "doggySettings";
	public static final String CATEGORY_DT_GENERAL = "general";
	
	public static void init(Configuration configuration) {
		config = configuration;
		loadConfig();
		MinecraftForge.EVENT_BUS.register(new ConfigurationHandler());
	}
	
	 /** TODO NOTE from NovaViper
	  *     I did indeed test out these configurations in game and they work perfectly within the world!
	  *     I haven't tested tenDayPuupys or isStartingItemsEnabled (though this one will most likely
	  *     require a world restart for sure). However, restarting Minecraft (not just the world) might
	  *     be required for the immortal configuration.
	  *
	  *     If you have any questions, you can look at the links in {@link ConfigGuiFactory}
	  *     or contact me again at my email <nova.gamez15+code@gmail.com> or in our conversation on Minecraft
	  *     Forums. Thanks!
	  */
	public static void loadConfig() {
		config.addCustomCategoryComment(CATEGORY_DOGGYSETTINGS, "Here you can change details about your dog.");
		config.addCustomCategoryComment(CATEGORY_DT_GENERAL, "Other settings");
		
		//Creates list for doggy settings
		List<String> orderDSetting = new ArrayList<String>();
		 
		Constants.DOGS_IMMORTAL = config.get(CATEGORY_DOGGYSETTINGS, "isDogImmortal", true, "Determines if dogs die when their health reaches zero. If true, dogs will not die, and will instead become incapacitated.").setRequiresMcRestart(true).getBoolean(true);
		Constants.TEN_DAY_PUPS = config.get(CATEGORY_DOGGYSETTINGS, "tenDayPuppies", true, "Determines if pups take 10 days to mature.").getBoolean(true);
		Constants.IS_HUNGER_ON = config.get(CATEGORY_DOGGYSETTINGS, "isHungerOn", true, "Enables hunger mode for the dog").getBoolean(true);
		//Constants.barkRate = config.get("doggySettings", "barkRate", 10, "Default is 10, higher the number the slower the dogs bark. Lower the number the faster the dogs bark.").getInt(10);
		Constants.DIRE_PARTICLES = config.get(CATEGORY_DOGGYSETTINGS, "direParticles", true, "Enables the particle effect on Dire Level 30 dogs.").getBoolean(true);
		Constants.STARTING_ITEMS = config.get(CATEGORY_DOGGYSETTINGS, "isStartingItemsEnabled", true, "When enabled you will spawn with a guide, Doggy Charm and Command Emblem.").getBoolean(true);
		Constants.RENDER_BLOOD = config.get(CATEGORY_DOGGYSETTINGS, "bloodWhenIncapacitated", true, "When enabled, Dogs will bleed while incapacitated.").getBoolean(true);
		
		//Add Everything in the current list in whatever way you want
		orderDSetting.add("isDogImmortal");
		orderDSetting.add("tenDayPuppies");
		orderDSetting.add("isHungerOn");
		orderDSetting.add("direParticles");
		orderDSetting.add("isStartingItemsEnabled");
		orderDSetting.add("bloodWhenIncapacitated");
		
		//Sets the category property order to that of which you have set the list above
		config.setCategoryPropertyOrder(CATEGORY_DOGGYSETTINGS, orderDSetting);
		
		
		//Creates list for general settings
		List<String> orderDTGeneral = new ArrayList<String>();
		
		config.setCategoryPropertyOrder(CATEGORY_DT_GENERAL, orderDTGeneral);
		
		if(config.hasChanged())
			config.save();
	}
	
	@SubscribeEvent
	public void onConfigurationChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event) {
		if(event.getModID().equalsIgnoreCase(Reference.MOD_ID))
			loadConfig();
	}
}