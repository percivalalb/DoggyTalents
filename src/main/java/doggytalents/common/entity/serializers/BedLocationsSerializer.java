package doggytalents.common.entity.serializers;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

public class BedLocationsSerializer<T> implements EntityDataSerializer<DimensionDependantArg<T>> {

    @Override
    public void write(FriendlyByteBuf buf, DimensionDependantArg<T> value) {
        EntityDataSerializer<T> ser = value.getSerializer();
        buf.writeInt(EntityDataSerializers.getSerializedId(ser));
        buf.writeInt(value.size());
        value.entrySet().forEach((entry) -> {
            buf.writeResourceLocation(entry.getKey().location());
            ser.write(buf, entry.getValue());
        });
    }

    @Override
    public DimensionDependantArg<T> read(FriendlyByteBuf buf) {
        EntityDataSerializer<T> ser = (EntityDataSerializer<T>) EntityDataSerializers.getSerializer(buf.readInt());
        DimensionDependantArg<T> value = new DimensionDependantArg<>(() -> ser);
        int size = buf.readInt();

        for (int i = 0; i < size; i++) {
            ResourceLocation loc = buf.readResourceLocation();
            ResourceKey<Level> type = ResourceKey.create(Registries.DIMENSION, loc);
            T subV = ser.read(buf);
            value.map.put(type, subV);
        }

        return value;
    }

    @Override
    public DimensionDependantArg<T> copy(DimensionDependantArg<T> value) {
        return value.copy();
    }
}
