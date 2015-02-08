package doggytalents.addon.nei;

import doggytalents.lib.Reference;
import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;

/**
 * @author ProPercivalalb
 */
public class NEIDoggyTalentsConfig implements IConfigureNEI {

	@Override
	public void loadConfig() {	
		API.registerNEIGuiHandler(new NEIGuiHandler());
	}

	@Override
	public String getName() {
		return Reference.MOD_NAME;
	}

	@Override
	public String getVersion() {
		return Reference.MOD_VERSION;
	}

}
