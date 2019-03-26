package doggytalents.addon;

import doggytalents.addon.itemphysic.ItemPhysicAddon;
import net.minecraftforge.eventbus.EventBus;
import net.minecraftforge.eventbus.api.BusBuilder;
import net.minecraftforge.fml.ModList;

/**
 * @author ProPercivalalb
 */
public class AddonManager {

	private static final EventBus EVENT_BUS	= new EventBus(BusBuilder.builder());
	
	public static void registerAddons() {
		//EVENT_BUS.register(new ForestryAddon());
		EVENT_BUS.register(new ItemPhysicAddon());
		//EVENT_BUS.register(new BiomesOPlentyAddon());
		//EVENT_BUS.register(new ExtraTreesAddon());
		//EVENT_BUS.register(new TerraqueousAddon());
		//EVENT_BUS.register(new NaturaAddon());
		//EVENT_BUS.register(new BotaniaAddon());
	}

	public static void runRegisteredAddons() {
		EVENT_BUS.post(new AddonEvent.Pre());
		EVENT_BUS.post(new AddonEvent.Init());
		EVENT_BUS.post(new AddonEvent.Post());
	}
	
	public static boolean areModsLoaded(String... modIds) {
		for(String modId : modIds)
			if(!ModList.get().isLoaded(modId))
				return false;
		return true;
	}
}
