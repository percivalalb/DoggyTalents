package doggytalents.core.handler;

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
		config.save();
	 }
}
