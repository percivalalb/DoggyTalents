package doggytalents.common.entity.serializers;

import doggytalents.api.feature.EnumGender;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.IDataSerializer;

public class GenderSerializer implements IDataSerializer<EnumGender> {

    @Override
    public void write(PacketBuffer buf, EnumGender value) {
        buf.writeByte(value.getIndex());
    }

    @Override
    public EnumGender read(PacketBuffer buf) {
        return EnumGender.byIndex(buf.readByte());
    }

    @Override
    public EnumGender copyValue(EnumGender value) {
        return value;
    }

}
