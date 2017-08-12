package doggytalents.base.c;

import doggytalents.handler.ModelBake;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ModelBakeWrapper {

	@SubscribeEvent
	public void onModelBakeEvent(ModelBakeEvent event) {
		ModelBake.onModelBakeEvent(event);
	}
}
