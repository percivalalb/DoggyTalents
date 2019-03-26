package doggytalents.network.client;

import java.util.function.Supplier;

import doggytalents.entity.EntityDog;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketDogTexture {
	
	public int entityId, doggyTexture;
	
    public PacketDogTexture(int entityId, int doggyTexture) {
        this.entityId = entityId;
        this.doggyTexture = doggyTexture;
    }
    
	public static void encode(PacketDogTexture msg, PacketBuffer buf) {
		buf.writeInt(msg.entityId);
		buf.writeInt(msg.doggyTexture);
	}
	
	public static PacketDogTexture decode(PacketBuffer buf) {
		int entityId = buf.readInt();
		int doggyTexture = buf.readInt();
		return new PacketDogTexture(entityId, doggyTexture);
	}
	
	
	public static class Handler {
        public static void handle(final PacketDogTexture message, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
            	Entity target = ctx.get().getSender().world.getEntityByID(message.entityId);
                if(!(target instanceof EntityDog))
                	return;
                
                EntityDog dog = (EntityDog)target;
                
        		dog.setTameSkin(message.doggyTexture);
            });

            ctx.get().setPacketHandled(true);
        }
    }
}
