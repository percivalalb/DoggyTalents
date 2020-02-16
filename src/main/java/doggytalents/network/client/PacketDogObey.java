package doggytalents.network.client;

import java.util.function.Supplier;

import doggytalents.entity.EntityDog;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketDogObey {
    
    public int entityId;
    public boolean obey;
    
    public PacketDogObey(int entityId, boolean obey) {
        this.entityId = entityId;
        this.obey = obey;
    }
    
    public static void encode(PacketDogObey msg, PacketBuffer buf) {
        buf.writeInt(msg.entityId);
        buf.writeBoolean(msg.obey);
    }
    
    public static PacketDogObey decode(PacketBuffer buf) {
        int entityId = buf.readInt();
        boolean obey = buf.readBoolean();
        return new PacketDogObey(entityId, obey);
    }
    
    
    public static class Handler {
        public static void handle(final PacketDogObey message, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                Entity target = ctx.get().getSender().world.getEntityByID(message.entityId);
                if(!(target instanceof EntityDog))
                    return;
                
                EntityDog dog = (EntityDog)target;
                if(!dog.canInteract(ctx.get().getSender()))
                    return;
                
                dog.setWillObeyOthers(message.obey);
            });

            ctx.get().setPacketHandled(true);
        }
    }
}
