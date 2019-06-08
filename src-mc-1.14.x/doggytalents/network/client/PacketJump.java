package doggytalents.network.client;

import java.util.function.Supplier;

import doggytalents.entity.EntityDog;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
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
            	PlayerEntity player = ctx.get().getSender();
                if(!(target instanceof EntityDog))
                	return;
                
                EntityDog dog = (EntityDog)target;
                //dog.onJump();
        		
            });

            ctx.get().setPacketHandled(true);
        }
    }
}
