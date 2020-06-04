package doggytalents.common.network.packet;

import java.util.function.Supplier;

import doggytalents.common.entity.DogEntity;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class FriendlyFirePacket {

    private final int entityId;
    private final boolean attackPlayers;

    public FriendlyFirePacket(int entityId, boolean attackPlayers) {
        this.entityId = entityId;
        this.attackPlayers = attackPlayers;
    }

    public static void encode(FriendlyFirePacket msg, PacketBuffer buf) {
        buf.writeInt(msg.entityId);
        buf.writeBoolean(msg.attackPlayers);
    }

    public static FriendlyFirePacket decode(PacketBuffer buf) {
        int entityId = buf.readInt();
        boolean obeyOthers = buf.readBoolean();
        return new FriendlyFirePacket(entityId, obeyOthers);
    }


    public static class Handler {
        public static void handle(final FriendlyFirePacket msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                Entity target = ctx.get().getSender().world.getEntityByID(msg.entityId);

                if(!(target instanceof DogEntity)) {
                    return;
                }

                DogEntity dog = (DogEntity)target;
                if(!dog.canInteract(ctx.get().getSender())) {
                    return;
                }

                dog.setCanPlayersAttack(msg.attackPlayers);
            });

            ctx.get().setPacketHandled(true);
        }
    }
}
