package doggytalents.common.entity.serializers;

import java.util.Optional;

import doggytalents.DoggyTalents2;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.IDataSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.dimension.DimensionType;

public class BedLocationsSerializer<D, T extends IDataSerializer<D>> implements IDataSerializer<DimensionDependantArg<D>> {

    @Override
    public void write(PacketBuffer buf, DimensionDependantArg<D> value) {
        IDataSerializer<D> ser = value.getSerializer();
        buf.writeInt(DataSerializers.getSerializerId(ser));
        buf.writeInt(value.size());
        value.entrySet().forEach((entry) -> {
            buf.writeResourceLocation(entry.getKey().getRegistryName());
            ser.write(buf, entry.getValue());
        });
    }

    @Override
    public DimensionDependantArg<D> read(PacketBuffer buf) {
        IDataSerializer ser = DataSerializers.getSerializer(buf.readInt());
        DimensionDependantArg<D> value = new DimensionDependantArg<>(() -> ser);
        int size = buf.readInt();

        for (int i = 0; i < size; i++) {
            ResourceLocation loc = buf.readResourceLocation();
            Optional<DimensionType> type = Registry.DIMENSION_TYPE.getValue(loc);
            D subV = (D) ser.read(buf);
            if (type.isPresent()) {
                value.map.put(type.get(), subV);
            } else {
                DoggyTalents2.LOGGER.warn("Failed loading from PacketBuffer. Could not find dimension {}", loc);
            }
        }
        DoggyTalents2.LOGGER.debug("Loaded {}", value);

        return value;
    }

    @Override
    public DimensionDependantArg<D> copyValue(DimensionDependantArg<D> value) {
        return value.copy();
    }
}
