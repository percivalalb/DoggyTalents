package doggytalents.common.network.packet;

import doggytalents.common.entity.Dog;
import doggytalents.common.network.packet.data.DogTextureData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent.Context;

import java.util.function.Supplier;

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
    public void handleDog(Dog dogIn, DogTextureData data, Supplier<Context> ctx) {
        if (!dogIn.canInteract(ctx.get().getSender())) {
            return;
        }

        dogIn.setSkinHash(data.hash);
    }
}
