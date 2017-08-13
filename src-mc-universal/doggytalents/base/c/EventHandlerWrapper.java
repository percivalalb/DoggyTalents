package doggytalents.base.c;

import doggytalents.configuration.ConfigurationHandler;
import doggytalents.handler.EntityInteract;
import doggytalents.handler.GameOverlay;
import doggytalents.lib.Reference;
import doggytalents.talent.HunterDog;
import net.minecraft.util.EnumHand;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventHandlerWrapper {

	@SubscribeEvent
	public void rightClickEntity(PlayerInteractEvent.EntityInteract event) {
		if(event.getHand() == EnumHand.MAIN_HAND)
			EntityInteract.rightClickEntity(event.getWorld(), event.getEntityPlayer(), event.getEntityPlayer().getHeldItem(event.getHand()), event.getTarget());
	}
	
	@SubscribeEvent
	public void onConfigurationChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event) {
		if(event.getModID().equals(Reference.MOD_ID))
			ConfigurationHandler.loadConfig();
	}
	
	@SubscribeEvent
	public void onLootDrop(LivingDropsEvent event) {
		HunterDog.onLootDrop(event.getEntityLiving(), event.getSource().getTrueSource(), event.getDrops());
	}
	
	@SubscribeEvent
	public void onPreRenderGameOverlay(RenderGameOverlayEvent.Post event) {
		GameOverlay.onPreRenderGameOverlay(event.getType(), event.getResolution());
	}
}
