package doggytalents.common.network.packet;

import java.util.function.Supplier;

import doggytalents.common.entity.DogEntity;
import doggytalents.common.network.packet.data.DogNameData;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class DogNamePacket extends DogPacket<DogNameData> {

    @Override
    public void encode(DogNameData data, PacketBuffer buf) {
        super.encode(data, buf);
        buf.writeUtf(data.name, 64);
    }

    @Override
    public DogNameData decode(PacketBuffer buf) {
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
            dogIn.setCustomName(new StringTextComponent(data.name));
        }
    }
}
