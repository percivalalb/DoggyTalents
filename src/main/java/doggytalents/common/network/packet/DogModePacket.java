package doggytalents.common.network.packet;

import java.util.function.Supplier;

import doggytalents.api.feature.EnumMode;
import doggytalents.common.entity.DogEntity;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class DogModePacket {

    public int entityId;
    public EnumMode mode;

    public DogModePacket(int entityId, EnumMode modeIn) {
        this.entityId = entityId;
        this.mode = modeIn;
    }

    public static void encode(DogModePacket msg, PacketBuffer buf) {
        buf.writeInt(msg.entityId);
        buf.writeInt(msg.mode.getIndex());
    }

    public static DogModePacket decode(PacketBuffer buf) {
        int entityId = buf.readInt();
        int modeIndex = buf.readInt();
        return new DogModePacket(entityId, EnumMode.byIndex(modeIndex));
    }

    public static class Handler {
        public static void handle(final DogModePacket message, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                Entity target = ctx.get().getSender().world.getEntityByID(message.entityId);
                if(!(target instanceof DogEntity)) {
                    return;
                }

                DogEntity dog = (DogEntity)target;
                if(!dog.canInteract(ctx.get().getSender())) {
                    return;
                }

                dog.setMode(message.mode);
            });

            ctx.get().setPacketHandled(true);
        }
    }
}
