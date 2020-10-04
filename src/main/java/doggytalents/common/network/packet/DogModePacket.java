package doggytalents.common.network.packet;

import java.util.function.Supplier;

import doggytalents.api.feature.EnumMode;
import doggytalents.common.entity.DogEntity;
import doggytalents.common.network.packet.data.DogModeData;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class DogModePacket extends DogPacket<DogModeData> {

    @Override
    public void encode(DogModeData data, PacketBuffer buf) {
        super.encode(data, buf);
        buf.writeInt(data.mode.getIndex());
    }

    @Override
    public DogModeData decode(PacketBuffer buf) {
        int entityId = buf.readInt();
        int modeIndex = buf.readInt();
        return new DogModeData(entityId, EnumMode.byIndex(modeIndex));
    }

    @Override
    public void handleDog(DogEntity dogIn, DogModeData data, Supplier<Context> ctx) {
        if (!dogIn.canInteract(ctx.get().getSender())) {
            return;
        }

        dogIn.setMode(data.mode);
    }
}
