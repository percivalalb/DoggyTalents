package doggytalents.network.client;

import java.util.function.Supplier;

import doggytalents.api.feature.EnumMode;
import doggytalents.entity.EntityDog;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketDogMode {
    
    public int entityId;
    public EnumMode mode;
    
    public PacketDogMode(int entityId, EnumMode modeIn) {
        this.entityId = entityId;
        this.mode = modeIn;
    }
    
    public static void encode(PacketDogMode msg, PacketBuffer buf) {
        buf.writeInt(msg.entityId);
        buf.writeInt(msg.mode.getIndex());
    }
    
    public static PacketDogMode decode(PacketBuffer buf) {
        int entityId = buf.readInt();
        int modeIndex = buf.readInt();
        return new PacketDogMode(entityId, EnumMode.byIndex(modeIndex));
    }
    
    public static class Handler {
        public static void handle(final PacketDogMode message, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                Entity target = ctx.get().getSender().world.getEntityByID(message.entityId);
                if(!(target instanceof EntityDog))
                    return;
                
                EntityDog dog = (EntityDog)target;
                if(!dog.canInteract(ctx.get().getSender()))
                    return;

                dog.setMode(message.mode);
            });

            ctx.get().setPacketHandled(true);
        }
    }
}
