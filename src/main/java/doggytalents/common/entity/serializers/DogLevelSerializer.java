package doggytalents.common.entity.serializers;

import doggytalents.api.feature.DogLevel;
import doggytalents.api.feature.DogLevel.Type;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataSerializer;

public class DogLevelSerializer implements EntityDataSerializer<DogLevel> {

    @Override
    public void write(FriendlyByteBuf buf, DogLevel value) {
        buf.writeInt(value.getLevel(Type.NORMAL));
        buf.writeInt(value.getLevel(Type.DIRE));
    }

    @Override
    public DogLevel read(FriendlyByteBuf buf) {
        return new DogLevel(buf.readInt(), buf.readInt());
    }

    @Override
    public DogLevel copy(DogLevel value) {
        return value.copy();
    }

}
