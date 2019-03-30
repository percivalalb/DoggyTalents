package doggytalents.handler;

import doggytalents.lib.Reference;
import doggytalents.talent.HunterDog;
import net.minecraft.util.EnumHand;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DTEventHandler {

	@SubscribeEvent
	public static void rightClickEntity(final PlayerInteractEvent.EntityInteract event) {
		if(event.getHand() == EnumHand.MAIN_HAND)
			EntityInteract.rightClickEntity(event.getWorld(), event.getEntityPlayer(), event.getEntityPlayer().getHeldItem(event.getHand()), event.getTarget());
	}
	
	//TODO @SubscribeEvent
	//public static void onConfigurationChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event) {
	//	if(event.getModID().equals(Reference.MOD_ID))
	//		ConfigurationHandler.loadConfig();
	//}
	
	@SubscribeEvent
	public static void onLootDrop(final LivingDropsEvent event) {
		HunterDog.onLootDrop(event.getEntityLiving(), event.getSource().getTrueSource(), event.getDrops());
	}
	
	@SubscribeEvent
	public static void onPreRenderGameOverlay(final RenderGameOverlayEvent.Post event) {
		GameOverlay.onPreRenderGameOverlay(event.getType());
	}
}
