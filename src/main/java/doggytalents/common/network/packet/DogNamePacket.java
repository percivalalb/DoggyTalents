package doggytalents.common.network.packet;

import doggytalents.common.entity.DogEntity;
import doggytalents.common.network.packet.data.DogNameData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraftforge.network.NetworkEvent.Context;

import java.util.function.Supplier;
public class DogNamePacket extends DogPacket<DogNameData> {

    @Override
    public void encode(DogNameData data, FriendlyByteBuf buf) {
        super.encode(data, buf);
        buf.writeUtf(data.name, 64);
    }

    @Override
    public DogNameData decode(FriendlyByteBuf buf) {
        int entityId = buf.readInt();
        String name = buf.readUtf(64);
        return new DogNameData(entityId, name);
    }

    @Override
    public void handleDog(DogEntity dogIn, DogNameData data, Supplier<Context> ctx) {
        if (!dogIn.canInteract(ctx.get().getSender())) {
            return;
        }

        if (data.name.isEmpty()) {
            dogIn.setCustomName(null);
        }
        else {
            dogIn.setCustomName(Component.literal(data.name));
        }
    }
}
