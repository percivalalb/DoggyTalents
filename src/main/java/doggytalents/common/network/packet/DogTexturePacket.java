package doggytalents.common.network.packet;

import java.util.function.Supplier;

import doggytalents.common.entity.DogEntity;
import doggytalents.common.network.packet.data.DogTextureData;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class DogTexturePacket extends DogPacket<DogTextureData> {

    @Override
    public void encode(DogTextureData data, PacketBuffer buf) {
        super.encode(data, buf);
        buf.writeString(data.hash);
    }

    @Override
    public DogTextureData decode(PacketBuffer buf) {
        int entityId = buf.readInt();
        String texture = buf.readString(128);
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
