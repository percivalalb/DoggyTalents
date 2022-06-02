package doggytalents.common.network.packet;

import doggytalents.common.entity.Dog;
import doggytalents.common.network.packet.data.DogObeyData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent.Context;

import java.util.function.Supplier;

public class DogObeyPacket extends DogPacket<DogObeyData> {

    @Override
    public void encode(DogObeyData data, FriendlyByteBuf buf) {
        super.encode(data, buf);
        buf.writeBoolean(data.obeyOthers);
    }

    @Override
    public DogObeyData decode(FriendlyByteBuf buf) {
        int entityId = buf.readInt();
        boolean obeyOthers = buf.readBoolean();
        return new DogObeyData(entityId, obeyOthers);
    }

    @Override
    public void handleDog(Dog dogIn, DogObeyData data, Supplier<Context> ctx) {
        if (!dogIn.canInteract(ctx.get().getSender())) {
            return;
        }

        dogIn.setWillObeyOthers(data.obeyOthers);
    }
}
