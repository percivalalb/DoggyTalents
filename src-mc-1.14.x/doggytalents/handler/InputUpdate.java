package doggytalents.handler;

import doggytalents.entity.EntityDog;
import doggytalents.network.PacketHandler;
import doggytalents.network.client.PacketJump;
import net.minecraft.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.network.PacketDistributor;

@OnlyIn(Dist.CLIENT)
public class InputUpdate {

    @SubscribeEvent
    public void event(InputUpdateEvent event) {
        if(event.getMovementInput().jump) {
            Entity entity = event.getEntityPlayer().getRidingEntity();
            if(event.getEntityPlayer().isPassenger() && entity instanceof EntityDog) {
                EntityDog dog = (EntityDog)entity;
                
                if(dog.canJump()) {
                    dog.setJumpPower(100);
                    PacketHandler.send(PacketDistributor.SERVER.noArg(), new PacketJump(dog.getEntityId()));
                }
            }
        }
    }
}
