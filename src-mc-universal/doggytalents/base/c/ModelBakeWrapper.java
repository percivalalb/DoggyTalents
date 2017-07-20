package doggytalents.base.c;

import doggytalents.handler.ModelBake;
import doggytalents.lib.Reference;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@EventBusSubscriber(value = Side.CLIENT, modid = Reference.MOD_ID)
public class ModelBakeWrapper {

	@SubscribeEvent
	public static void onModelBakeEvent(ModelBakeEvent event) {
		ModelBake.onModelBakeEvent(event);
	}
}
