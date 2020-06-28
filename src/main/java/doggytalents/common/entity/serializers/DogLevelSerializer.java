package doggytalents.common.entity.serializers;

import doggytalents.api.feature.DogLevel;
import doggytalents.api.feature.DogLevel.Type;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.IDataSerializer;

public class DogLevelSerializer implements IDataSerializer<DogLevel> {

    @Override
    public void write(PacketBuffer buf, DogLevel value) {
        buf.writeInt(value.getLevel(Type.NORMAL));
        buf.writeInt(value.getLevel(Type.DIRE));
    }

    @Override
    public DogLevel read(PacketBuffer buf) {
        return new DogLevel(buf.readInt(), buf.readInt());
    }

    @Override
    public DogLevel copyValue(DogLevel value) {
        return value.copy();
    }

}
