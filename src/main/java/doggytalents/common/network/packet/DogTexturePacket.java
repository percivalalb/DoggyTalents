package doggytalents.common.network.packet;

import java.util.function.Supplier;

import doggytalents.common.entity.DogEntity;
import doggytalents.common.network.packet.data.DogTextureData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fmllegacy.network.NetworkEvent.Context;

public class DogTexturePacket extends DogPacket<DogTextureData> {

    @Override
    public void encode(DogTextureData data, FriendlyByteBuf buf) {
        super.encode(data, buf);
        buf.writeUtf(data.hash);
    }

    @Override
    public DogTextureData decode(FriendlyByteBuf buf) {
        int entityId = buf.readInt();
        String texture = buf.readUtf(128);
        return new DogTextureData(entityId, texture);
    }

    @Override
    public void handleDog(DogEntity dogIn, DogTextureData data, Supplier<Context> ctx) {
        if (!dogIn.canInteract(ctx.get().getSender())) {
            return;
        }

        dogIn.setSkinHash(data.hash);
    }
}
