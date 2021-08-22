package doggytalents.common.entity.serializers;

import doggytalents.api.feature.EnumGender;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataSerializer;

public class GenderSerializer implements EntityDataSerializer<EnumGender> {

    @Override
    public void write(FriendlyByteBuf buf, EnumGender value) {
        buf.writeByte(value.getIndex());
    }

    @Override
    public EnumGender read(FriendlyByteBuf buf) {
        return EnumGender.byIndex(buf.readByte());
    }

    @Override
    public EnumGender copy(EnumGender value) {
        return value;
    }

}
