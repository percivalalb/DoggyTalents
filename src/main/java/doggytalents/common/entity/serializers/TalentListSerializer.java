package doggytalents.common.entity.serializers;

import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Maps;

import doggytalents.api.DoggyTalentsAPI;
import doggytalents.api.registry.Talent;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.IDataSerializer;

public class TalentListSerializer implements IDataSerializer<Map<Talent, Integer>> {

    @Override
    public void write(PacketBuffer buf, Map<Talent, Integer> value) {
        buf.writeInt(value.size());
        for (Entry<Talent, Integer> entry : value.entrySet()) {
            buf.writeRegistryIdUnsafe(DoggyTalentsAPI.TALENTS, entry.getKey());
            buf.writeByte(entry.getValue());
        }
    }

    @Override
    public Map<Talent, Integer> read(PacketBuffer buf) {
        Map<Talent, Integer> map = Maps.newHashMap();
        int size = buf.readInt();
        for (int i = 0; i < size; i++) {
            Talent talent = buf.readRegistryIdUnsafe(DoggyTalentsAPI.TALENTS);
            byte level = buf.readByte();
            map.put(talent, (int)level);
        }
        return map;
    }

    @Override
    public DataParameter<Map<Talent, Integer>> createKey(int id) {
        return new DataParameter<>(id, this);
    }

    @Override
    public Map<Talent, Integer> copyValue(Map<Talent, Integer> value) {
        return Maps.newHashMap(value);
    }
}
