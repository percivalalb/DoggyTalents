package doggytalents.network.client;

import java.util.function.Supplier;

import doggytalents.entity.EntityDog;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketDogName {
	
	private final int entityId;
	private final String name;
	
    public PacketDogName(int entityId, String name) {
        this.entityId = entityId;
        this.name = name;
    }
    
	public static void encode(PacketDogName msg, PacketBuffer buf) {
		buf.writeInt(msg.entityId);
		buf.writeString(msg.name, 64);
	}
	
	public static PacketDogName decode(PacketBuffer buf) {
		int entityId = buf.readInt();
		String name = buf.readString(64);
		return new PacketDogName(entityId, name);
	}
	
	
	public static class Handler {
        public static void handle(final PacketDogName message, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
            	Entity target = ctx.get().getSender().world.getEntityByID(message.entityId);
        		
            	if(!(target instanceof EntityDog))
                	return;
                
        		EntityDog dog = (EntityDog)target;
                
        		if(message.name.isEmpty())
        			dog.setCustomName(null);
        		else
        			dog.setCustomName(new StringTextComponent(message.name));
            });

            ctx.get().setPacketHandled(true);
        }
    }
}
