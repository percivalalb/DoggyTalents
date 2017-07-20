package doggytalents.handler;

import doggytalents.DoggyTalents;
import doggytalents.base.ObjectLib;
import net.minecraftforge.client.event.ModelBakeEvent;

public class ModelBake {

	public static void onModelBakeEvent(ModelBakeEvent event) {
	    
	    try {
		    ObjectLib.METHODS.onModelBakeEvent(event);
	    }
	    catch(Exception e) {
	    	DoggyTalents.LOGGER.warn("Could not get base Dog Bed model. Reverting to default textures...");
	    }
	}
}
