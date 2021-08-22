package doggytalents.common.network.packet;

import java.util.function.Supplier;

import doggytalents.common.entity.DogEntity;
import doggytalents.common.network.IPacket;
import doggytalents.common.network.packet.data.DogData;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public abstract class DogPacket<T extends DogData> implements IPacket<T> {

    @Override
    public void encode(T data, FriendlyByteBuf buf) {
        buf.writeInt(data.entityId);
    }

    @Override
    public abstract T decode(FriendlyByteBuf buf);

    @Override
    public final void handle(T data, Supplier<Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Entity target = ctx.get().getSender().level.getEntity(data.entityId);

            if (!(target instanceof DogEntity)) {
                return;
            }

            this.handleDog((DogEntity) target, data, ctx);
        });

        ctx.get().setPacketHandled(true);
    }

    public abstract void handleDog(DogEntity dogIn, T data, Supplier<Context> ctx);

}
