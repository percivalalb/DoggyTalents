package doggytalents.network.client;

import java.util.function.Supplier;

import doggytalents.entity.EntityDog;
import net.minecraft.entity.Entity;
import net.minecraft.init.MobEffects;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketJump {
	
	public int entityId;
	
    public PacketJump(int entityId) {
        this.entityId = entityId;
    }
    
	public static void encode(PacketJump msg, PacketBuffer buf) {
		buf.writeInt(msg.entityId);
	}
	
	public static PacketJump decode(PacketBuffer buf) {
		int entityId = buf.readInt();
		return new PacketJump(entityId);
	}
	
	
	public static class Handler {
        public static void handle(final PacketJump message, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
            	Entity target = ctx.get().getSender().world.getEntityByID(message.entityId);
                if(!(target instanceof EntityDog))
                	return;
                
                EntityDog dog = (EntityDog)target;
        		if(dog.onGround) {

        			double verticalVelocity = 0.27D + 0.1D * dog.TALENTS.getLevel("wolfmount");
        			if(dog.TALENTS.getLevel("wolfmount") == 5) verticalVelocity += 0.1D;
        			
        			dog.addVelocity(0D, verticalVelocity, 0D);
        			if(dog.isPotionActive(MobEffects.JUMP_BOOST))
        				dog.motionY += (double)((float)(dog.getActivePotionEffect(MobEffects.JUMP_BOOST).getAmplifier() + 1) * 0.1F);
        		}
        		else if(dog.isInWater() && dog.TALENTS.getLevel("swimmerdog") > 0) {
        			dog.motionY = 0.2F;
        		}
            });

            ctx.get().setPacketHandled(true);
        }
    }
}
