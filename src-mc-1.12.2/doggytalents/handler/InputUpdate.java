package doggytalents.handler;

import doggytalents.DoggyTalents;
import doggytalents.entity.EntityDog;
import doggytalents.network.packet.client.DogJumpMessage;
import net.minecraft.entity.Entity;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class InputUpdate {

	@SubscribeEvent
	public void event(InputUpdateEvent event) {
		if(event.getMovementInput().jump) {
			Entity entity = event.getEntityPlayer().getRidingEntity();
			if(event.getEntityPlayer().getRidingEntity() != null && entity instanceof EntityDog) {
				EntityDog dog = (EntityDog)entity;
				
				if(dog.canJump()) {
					dog.setJumpPower(100);
					DogJumpMessage jumpMessage = new DogJumpMessage(dog.getEntityId());
				}
			}
		}
	}
}
