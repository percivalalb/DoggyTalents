package doggytalents.serializers;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import doggytalents.api.DoggyTalentsAPI;
import doggytalents.api.inferface.Talent;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializer;
import net.minecraft.util.ResourceLocation;

public class TalentListSerializer implements DataSerializer<Map<Talent, Integer>> {

	@Override
	public void write(PacketBuffer buf, Map<Talent, Integer> value) {
		buf.writeInt(value.size());
		for(Entry<Talent, Integer> entry : value.entrySet()) {
			buf.writeString(DoggyTalentsAPI.TALENTS.getKey(entry.getKey()).toString());
			buf.writeByte(entry.getValue());
		}
	}

	@Override
	public Map<Talent, Integer> read(PacketBuffer buf) {
		Map<Talent, Integer> map = new HashMap<>();
		int size = buf.readInt();
		for(int i = 0; i < size; i++) {
			map.put(DoggyTalentsAPI.TALENTS.getValue(new ResourceLocation(buf.readString(Short.MAX_VALUE))), (int)buf.readByte());
		}
		return map;
	}

	@Override
	public DataParameter<Map<Talent, Integer>> createKey(int id) {
		return new DataParameter<>(id, this);
	}
}
