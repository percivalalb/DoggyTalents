package doggytalents.addon;

import doggytalents.addon.biomesoplenty.BiomesOPlentyAddon;
import doggytalents.addon.extratrees.ExtraTreesAddon;
import doggytalents.addon.extrautilites.ExtraUtilitesAddon;
import doggytalents.addon.forestry.ForestryAddon;
import doggytalents.addon.thaumcraft.ThaumcraftAddon;
import doggytalents.addon.tropicraft.TropicraftAddon;
import doggytalents.addon.twilightforest.TwilightForestAddon;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.eventhandler.EventBus;

/**
 * @author ProPercivalalb
 */
public class AddonManager {

	private static final EventBus EVENT_BUS	= new EventBus();
	
	public static void registerAddons() {
		EVENT_BUS.register(new ForestryAddon());
		EVENT_BUS.register(new ExtraTreesAddon());
		EVENT_BUS.register(new TropicraftAddon());
		EVENT_BUS.register(new BiomesOPlentyAddon());
		EVENT_BUS.register(new ExtraUtilitesAddon());
		EVENT_BUS.register(new ThaumcraftAddon());
		EVENT_BUS.register(new TwilightForestAddon());
	}

	public static void runRegisteredAddons(Configuration config) {
		EVENT_BUS.post(new AddonEvent.Pre(config));
		EVENT_BUS.post(new AddonEvent.Init(config));
		EVENT_BUS.post(new AddonEvent.Post(config));
	}
}
