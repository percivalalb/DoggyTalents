package doggytalents.common.network.packet;

import java.util.function.Supplier;

import doggytalents.common.entity.DogEntity;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.network.NetworkEvent;

public class DogNamePacket {

    private final int entityId;
    private final String name;

    public DogNamePacket(int entityId, String name) {
        this.entityId = entityId;
        this.name = name;
    }

    public static void encode(DogNamePacket msg, PacketBuffer buf) {
        buf.writeInt(msg.entityId);
        buf.writeString(msg.name, 64);
    }

    public static DogNamePacket decode(PacketBuffer buf) {
        int entityId = buf.readInt();
        String name = buf.readString(64);
        return new DogNamePacket(entityId, name);
    }


    public static class Handler {
        public static void handle(final DogNamePacket message, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                Entity target = ctx.get().getSender().world.getEntityByID(message.entityId);

                if(!(target instanceof DogEntity)) {
                    return;
                }

                DogEntity dog = (DogEntity)target;
//                if(!dog.canInteract(ctx.get().getSender())) {
//                    return;
//                }

                if(message.name.isEmpty()) {
                    dog.setCustomName(null);
                }
                else {
                    dog.setCustomName(new StringTextComponent(message.name));
                }
            });

            ctx.get().setPacketHandled(true);
        }
    }
}
