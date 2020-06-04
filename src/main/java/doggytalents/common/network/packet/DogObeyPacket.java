package doggytalents.common.network.packet;

import java.util.function.Supplier;

import doggytalents.common.entity.DogEntity;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class DogObeyPacket {

    private final int entityId;
    private final boolean obeyOthers;

    public DogObeyPacket(int entityId, boolean obeyOthers) {
        this.entityId = entityId;
        this.obeyOthers = obeyOthers;
    }

    public static void encode(DogObeyPacket msg, PacketBuffer buf) {
        buf.writeInt(msg.entityId);
        buf.writeBoolean(msg.obeyOthers);
    }

    public static DogObeyPacket decode(PacketBuffer buf) {
        int entityId = buf.readInt();
        boolean obeyOthers = buf.readBoolean();
        return new DogObeyPacket(entityId, obeyOthers);
    }


    public static class Handler {
        public static void handle(final DogObeyPacket msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                Entity target = ctx.get().getSender().world.getEntityByID(msg.entityId);

                if(!(target instanceof DogEntity)) {
                    return;
                }

                DogEntity dog = (DogEntity)target;
                if(!dog.canInteract(ctx.get().getSender())) {
                    return;
                }

                dog.setWillObeyOthers(msg.obeyOthers);
            });

            ctx.get().setPacketHandled(true);
        }
    }
}
