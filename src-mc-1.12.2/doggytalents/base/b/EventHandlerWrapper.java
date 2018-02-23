package doggytalents.base.b;

import doggytalents.configuration.ConfigurationHandler;
import doggytalents.handler.EntityInteract;
import doggytalents.handler.GameOverlay;
import doggytalents.lib.Reference;
import doggytalents.talent.HunterDog;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventHandlerWrapper {

	@SubscribeEvent
	public void rightClickEntity(EntityInteractEvent event) {
		EntityInteract.rightClickEntity(event.entityPlayer.world, event.entityPlayer, event.entityPlayer.getHeldItem(), event.target);
	}
	
	@SubscribeEvent
	public void onConfigurationChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event) {
		if(event.modID.equals(Reference.MOD_ID))
			ConfigurationHandler.loadConfig();
	}
	
	@SubscribeEvent
	public void onLootDrop(LivingDropsEvent event) {
		HunterDog.onLootDrop(event.entityLiving, event.source.getTrueSource(), event.drops);
	}
	
	@SubscribeEvent
	public void onPreRenderGameOverlay(RenderGameOverlayEvent.Post event) {
		GameOverlay.onPreRenderGameOverlay(event.type, event.resolution);
	}
}
