package doggytalents.common.network.packet;

import java.util.function.Supplier;

import doggytalents.DoggyTalents2;
import doggytalents.common.entity.texture.DogTextureServer;
import doggytalents.common.network.IPacket;
import doggytalents.common.network.packet.data.RequestSkinData;
import doggytalents.common.network.packet.data.SendSkinData;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class RequestSkinPacket implements IPacket<RequestSkinData> {

    @Override
    public void encode(RequestSkinData data, PacketBuffer buf) {
        buf.writeString(data.hash, 128);
    }

    @Override
    public RequestSkinData decode(PacketBuffer buf) {
        return new RequestSkinData(buf.readString(128));
    }

    @Override
    public void handle(RequestSkinData data, Supplier<Context> ctx) {
        ctx.get().enqueueWork(() -> {
            LogicalSide side = ctx.get().getDirection().getReceptionSide();

            if (side.isServer()) {
                byte[] stream = DogTextureServer.INSTANCE.getCachedBytes(DogTextureServer.INSTANCE.getServerFolder(), data.hash);
                if (stream != null) {
                    DoggyTalents2.HANDLER.reply(new SendSkinData(0, stream), ctx.get());

                    DoggyTalents2.LOGGER.debug("Client requested skin for hash  {}", data.hash);
                } else {
                    DoggyTalents2.LOGGER.warn("Client requested skin but no cache was available {}", data.hash);
                }
            }

        });

        ctx.get().setPacketHandled(true);
    }
}
