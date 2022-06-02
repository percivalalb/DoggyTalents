package doggytalents.common.network.packet;

import doggytalents.api.feature.EnumMode;
import doggytalents.common.entity.Dog;
import doggytalents.common.network.packet.data.DogModeData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent.Context;

import java.util.function.Supplier;

public class DogModePacket extends DogPacket<DogModeData> {

    @Override
    public void encode(DogModeData data, FriendlyByteBuf buf) {
        super.encode(data, buf);
        buf.writeInt(data.mode.getIndex());
    }

    @Override
    public DogModeData decode(FriendlyByteBuf buf) {
        int entityId = buf.readInt();
        int modeIndex = buf.readInt();
        return new DogModeData(entityId, EnumMode.byIndex(modeIndex));
    }

    @Override
    public void handleDog(Dog dogIn, DogModeData data, Supplier<Context> ctx) {
        if (!dogIn.canInteract(ctx.get().getSender())) {
            return;
        }

        dogIn.setMode(data.mode);
    }
}
