package doggytalents.serializer;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import doggytalents.api.DoggyTalentsAPI;
import doggytalents.api.inferface.Talent;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.IDataSerializer;

public class TalentListSerializer implements IDataSerializer<Map<Talent, Integer>> {

	@Override
	public void write(PacketBuffer buf, Map<Talent, Integer> value) {
		buf.writeInt(value.size());
		for(Entry<Talent, Integer> entry : value.entrySet()) {
			buf.writeResourceLocation(entry.getKey().getRegistryName());
			buf.writeByte(entry.getValue());
		}
	}

	@Override
	public Map<Talent, Integer> read(PacketBuffer buf) {
		Map<Talent, Integer> map = new HashMap<>();
		int size = buf.readInt();
		for(int i = 0; i < size; i++) {
			map.put(DoggyTalentsAPI.TALENTS.getValue(buf.readResourceLocation()), (int)buf.readByte());
		}
		return map;
	}

	@Override
	public DataParameter<Map<Talent, Integer>> createKey(int id) {
		return new DataParameter<>(id, this);
	}

	@Override
	public Map<Talent, Integer> copyValue(Map<Talent, Integer> value) {
		Map<Talent, Integer> copy = new HashMap<>();
		for(Entry<Talent, Integer> entry : value.entrySet()) {
			copy.put(entry.getKey(), entry.getValue());
		}
		return copy;
	}
}
