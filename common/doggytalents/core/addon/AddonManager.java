package doggytalents.core.addon;

import doggytalents.core.addon.forestry.ForestryAddon;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.event.EventBus;

/**
 * @author ProPercivalalb
 */
public class AddonManager {

	private static final EventBus EVENT_BUS	= new EventBus();
	
	public static void registerAddons() {
		EVENT_BUS.register(new ForestryAddon());
	}

	public static void runRegisteredAddons(Configuration config) {
		EVENT_BUS.post(new AddonEvent.Pre(config));
		EVENT_BUS.post(new AddonEvent.Init(config));
		EVENT_BUS.post(new AddonEvent.Post(config));
	}
}
