package doggytalents.handler;

import doggytalents.entity.EntityDog;
import doggytalents.network.packet.client.DogJumpMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class InputUpdate {

	@SubscribeEvent
	public void event(KeyInputEvent event) {
		EntityPlayerSP player = Minecraft.getMinecraft().player;
		if(player == null) return;
		if(player.movementInput.jump) {
			Entity entity = player.getRidingEntity();
			if(player.getRidingEntity() != null && entity instanceof EntityDog) {
				EntityDog dog = (EntityDog)entity;
				
				if(dog.canJump()) {
					dog.setJumpPower(100);
					DogJumpMessage jumpMessage = new DogJumpMessage(dog.getEntityId());
				}
			}
		}
	}
}
