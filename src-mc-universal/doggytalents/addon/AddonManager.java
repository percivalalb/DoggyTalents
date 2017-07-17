package doggytalents.addon;

import doggytalents.addon.biomesoplenty.BiomesOPlentyAddon;
import doggytalents.addon.forestry.ForestryAddon;
import doggytalents.addon.itemphysic.ItemPhysicAddon;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.eventhandler.EventBus;

/**
 * @author ProPercivalalb
 */
public class AddonManager {

	private static final EventBus EVENT_BUS	= new EventBus();
	
	public static void registerAddons() {
		EVENT_BUS.register(new ForestryAddon());
		EVENT_BUS.register(new ItemPhysicAddon());
		EVENT_BUS.register(new BiomesOPlentyAddon());
	}

	public static void runRegisteredAddons(Configuration config) {
		EVENT_BUS.post(new AddonEvent.Pre(config));
		EVENT_BUS.post(new AddonEvent.Init(config));
		EVENT_BUS.post(new AddonEvent.Post(config));
	}
}
