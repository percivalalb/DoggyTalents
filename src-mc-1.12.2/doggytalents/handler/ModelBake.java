package doggytalents.handler;

import doggytalents.DoggyTalents;
import doggytalents.base.ObjectLibClient;
import net.minecraftforge.client.event.ModelBakeEvent;

public class ModelBake {

	public static void onModelBakeEvent(ModelBakeEvent event) {
	    
	    try {
	    	ObjectLibClient.METHODS.onModelBakeEvent(event);
	    }
	    catch(Exception e) {
	    	DoggyTalents.LOGGER.warn("Could not get base Dog Bed model. Reverting to default textures...");
	    }
	}
}
