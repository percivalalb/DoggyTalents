package doggytalents.common.network.packet;

import java.util.function.Supplier;

import doggytalents.DoggyTalents2;
import doggytalents.common.entity.texture.DogTextureLoader;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;

public class RequestSkinPacket {

    public String hash;

    public RequestSkinPacket(String hash) {
        this.hash = hash;
    }

    public static void encode(RequestSkinPacket msg, PacketBuffer buf) {
        buf.writeString(msg.hash, 128);
    }

    public static RequestSkinPacket decode(PacketBuffer buf) {
        return new RequestSkinPacket(buf.readString(128));
    }

    public static class Handler {
        public static void handle(final RequestSkinPacket msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                LogicalSide side = ctx.get().getDirection().getReceptionSide();

                if (side.isServer()) {
                    byte[] stream = DogTextureLoader.getCachedBytes(DogTextureLoader.getServerFolder(), msg.hash);
                    if (stream != null) {
                        DoggyTalents2.HANDLER.reply(new SendSkinPacket(0, stream), ctx.get());

                        DoggyTalents2.LOGGER.debug("Client requested skin for hash  {}", msg.hash);
                    } else {
                        DoggyTalents2.LOGGER.warn("Client requested skin but no cache was available {}", msg.hash);
                    }
                }

            });

            ctx.get().setPacketHandled(true);
        }
    }
}