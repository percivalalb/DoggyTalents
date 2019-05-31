package doggytalents.handler;

import doggytalents.talent.HunterDogTalent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class LivingDrops {
	
	@SubscribeEvent
	public void onLootDrop(LivingDropsEvent event) {
		HunterDogTalent.onLootDrop(event.getEntityLiving(), event.getSource().getTrueSource(), event.getDrops());
	}
}
