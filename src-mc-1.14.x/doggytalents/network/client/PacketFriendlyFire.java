package doggytalents.network.client;

import java.util.function.Supplier;

import doggytalents.entity.EntityDog;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketFriendlyFire {
	
	public int entityId;
	public boolean friendlyFire;
	
    public PacketFriendlyFire(int entityId, boolean obey) {
        this.entityId = entityId;
        this.friendlyFire = obey;
    }
    
	public static void encode(PacketFriendlyFire msg, PacketBuffer buf) {
		buf.writeInt(msg.entityId);
		buf.writeBoolean(msg.friendlyFire);
	}
	
	public static PacketFriendlyFire decode(PacketBuffer buf) {
		int entityId = buf.readInt();
		boolean friendlyFire = buf.readBoolean();
		return new PacketFriendlyFire(entityId, friendlyFire);
	}
	
	
	public static class Handler {
        public static void handle(final PacketFriendlyFire message, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
            	Entity target = ctx.get().getSender().world.getEntityByID(message.entityId);
                if(!(target instanceof EntityDog))
                	return;
                
                EntityDog dog = (EntityDog)target;
                
        		dog.setFriendlyFire(message.friendlyFire);
            });

            ctx.get().setPacketHandled(true);
        }
    }
}
