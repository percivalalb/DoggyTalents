package doggytalents.configuration;

import java.util.ArrayList;
import java.util.List;

import doggytalents.lib.Constants;
import doggytalents.lib.Reference;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

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
		CONFIG.addCustomCategoryComment(CATEGORY_DOGGYSETTINGS, "Here you can change details about your dog.");
		CONFIG.addCustomCategoryComment(CATEGORY_TALENT, "Enable and disable talents here as you wish");
		CONFIG.addCustomCategoryComment(CATEGORY_GENERAL, "Other settings");
		
		//Creates list for doggy settings
		List<String> orderDSetting = new ArrayList<String>();
		 
		Constants.DOGS_IMMORTAL = CONFIG.get(CATEGORY_DOGGYSETTINGS, "isDogImmortal", true, "Determines if dogs die when their health reaches zero. If true, dogs will not die, and will instead become incapacitated.").setRequiresMcRestart(true).getBoolean(true);
		Constants.TEN_DAY_PUPS = CONFIG.get(CATEGORY_DOGGYSETTINGS, "tenDayPuppies", true, "Determines if pups take 10 days to mature.").getBoolean(true);
		Constants.IS_HUNGER_ON = CONFIG.get(CATEGORY_DOGGYSETTINGS, "isHungerOn", true, "Enables hunger mode for the dog").getBoolean(true);
		//Constants.barkRate = config.get("doggySettings", "barkRate", 10, "Default is 10, higher the number the slower the dogs bark. Lower the number the faster the dogs bark.").getInt(10);
		Constants.DIRE_PARTICLES = CONFIG.get(CATEGORY_DOGGYSETTINGS, "direParticles", true, "Enables the particle effect on Dire Level 30 dogs.").getBoolean(true);
		Constants.STARTING_ITEMS = CONFIG.get(CATEGORY_DOGGYSETTINGS, "isStartingItemsEnabled", true, "When enabled you will spawn with a guide, Doggy Charm and Command Emblem.").getBoolean(true);
		Constants.RENDER_BLOOD = CONFIG.get(CATEGORY_DOGGYSETTINGS, "bloodWhenIncapacitated", true, "When enabled, Dogs will bleed while incapacitated.").getBoolean(true);
		
		//Add Everything in the current list in whatever way you want
		orderDSetting.add("isDogImmortal");
		orderDSetting.add("tenDayPuppies");
		orderDSetting.add("isHungerOn");
		orderDSetting.add("direParticles");
		orderDSetting.add("isStartingItemsEnabled");
		orderDSetting.add("bloodWhenIncapacitated");
		
		//Sets the category property order to that of which you have set the list above
		CONFIG.setCategoryPropertyOrder(CATEGORY_DOGGYSETTINGS, orderDSetting);
		
		Constants.DISABLED_TALENTS.clear();
		
		String[] talentIds = new String[] {"bedfinder", "blackpelt", "creepersweeper", "doggydash", "fisherdog", "guarddog", "happyeater", "hellhound", 
											"hunterdog", "packpuppy", "pestfighter", "pillowpaw", "poisonfang", "puppyeyes", "quickhealer", 
											"rescuedog", "shepherddog", "swimmerdog", "wolfmount"};
		for(String talentId : talentIds) {
			boolean enabled = CONFIG.get(CATEGORY_TALENT, talentId, true).getBoolean(true);
			if(!enabled)
				Constants.DISABLED_TALENTS.add(talentId);
		}
		
		//Creates list for general settings
		List<String> orderDTGeneral = new ArrayList<String>();
		
		CONFIG.setCategoryPropertyOrder(CATEGORY_GENERAL, orderDTGeneral);
		
		if(CONFIG.hasChanged())
			CONFIG.save();
	}
}