package doggytalents.common.network.packet;

import java.util.function.Supplier;

import doggytalents.common.entity.DogEntity;
import doggytalents.common.entity.stats.StatsTracker;
import doggytalents.common.network.IPacket;
import doggytalents.common.network.PacketHandler;
import doggytalents.common.network.packet.data.StatsSyncData;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.NetworkEvent.Context;

public class StatsSyncPackets {
    public static class Request extends DogPacket<StatsSyncData.Request> {

        @Override
        public StatsSyncData.Request decode(FriendlyByteBuf buf) {
            int id = buf.readInt();
            return new StatsSyncData.Request(id);
        }

        @Override
        public void handleDog(DogEntity dogIn, StatsSyncData.Request data,
                Supplier<Context> ctx) {
            var tracker = dogIn.getStatTracker();
            var sender = ctx.get().getSender();
            PacketHandler.send(
                PacketDistributor.PLAYER.with(() -> sender), 
                new StatsSyncData.Response(dogIn.getId(), tracker)
            );
        }


    }

    public static class Response implements IPacket<StatsSyncData.Response> {

        @Override
        public void encode(StatsSyncData.Response data, FriendlyByteBuf buf) {
            buf.writeInt(data.entityId);
            data.tracker.serializeToBuf(buf);
        }

        @Override
        public StatsSyncData.Response decode(FriendlyByteBuf buf) {
            int id = buf.readInt();
            var newTracker = new StatsTracker();
            newTracker.deserializeFromBuf(buf);
            return new StatsSyncData.Response(id, newTracker);
        }

        @Override
        public void handle(StatsSyncData.Response data, Supplier<Context> ctx) {
            ctx.get().enqueueWork(() -> {

                if (ctx.get().getDirection().getReceptionSide().isClient()) { 
                    Minecraft mc = Minecraft.getInstance();
                    var e = mc.level.getEntity(data.entityId);
                    if (e instanceof DogEntity d) {
                        d.getStatTracker().shallowCopyFrom(data.tracker);
                    }
                }

            });

            ctx.get().setPacketHandled(true);
        }

    }
}
