package doggytalents.common.entity.serializers;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

public class BedLocationsSerializer<D, T extends EntityDataSerializer<D>> implements EntityDataSerializer<DimensionDependantArg<D>> {

    @Override
    public void write(FriendlyByteBuf buf, DimensionDependantArg<D> value) {
        EntityDataSerializer<D> ser = value.getSerializer();
        buf.writeInt(EntityDataSerializers.getSerializedId(ser));
        buf.writeInt(value.size());
        value.entrySet().forEach((entry) -> {
            buf.writeResourceLocation(entry.getKey().location());
            ser.write(buf, entry.getValue());
        });
    }

    @Override
    public DimensionDependantArg<D> read(FriendlyByteBuf buf) {
        EntityDataSerializer<D> ser = (EntityDataSerializer<D>) EntityDataSerializers.getSerializer(buf.readInt());
        DimensionDependantArg<D> value = new DimensionDependantArg<>(() -> ser);
        int size = buf.readInt();

        for (int i = 0; i < size; i++) {
            ResourceLocation loc = buf.readResourceLocation();
            ResourceKey<Level> type = ResourceKey.create(Registries.DIMENSION, loc);
            D subV = ser.read(buf);
            value.map.put(type, subV);
        }

        return value;
    }

    @Override
    public DimensionDependantArg<D> copy(DimensionDependantArg<D> value) {
        return value.copy();
    }
}
