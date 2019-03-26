package doggytalents.network.client;

import doggytalents.DoggyTalentsMod;
import doggytalents.entity.EntityDog;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketDogMode {
	
	public int entityId, doggyMode;
	
    public PacketDogMode(int entityId, int dogMode) {
        this.entityId = entityId;
        this.doggyMode = dogMode;
    }
    
	public static void encode(PacketDogMode msg, PacketBuffer buf) {
		buf.writeInt(msg.entityId);
		buf.writeInt(msg.doggyMode);
	}
	
	public static PacketDogMode decode(PacketBuffer buf) {
		int entityId = buf.readInt();
		int doggyMode = buf.readInt();
		return new PacketDogMode(entityId, doggyMode);
	}
	
	
	public static class Handler {
        public static void handle(final PacketDogMode message, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
            	Entity target = ctx.get().getSender().world.getEntityByID(message.entityId);
                if(!(target instanceof EntityDog))
                	return;
                
                EntityDog dog = (EntityDog)target;
                
        		dog.MODE.setMode(message.doggyMode);
            });

            ctx.get().setPacketHandled(true);
        }
    }
}
