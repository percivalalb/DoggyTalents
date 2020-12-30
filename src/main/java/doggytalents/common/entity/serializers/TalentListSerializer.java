package doggytalents.common.entity.serializers;

import java.util.ArrayList;
import java.util.List;

import doggytalents.api.DoggyTalentsAPI;
import doggytalents.api.registry.TalentInstance;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.IDataSerializer;

public class TalentListSerializer implements IDataSerializer<List<TalentInstance>> {

    @Override
    public void write(PacketBuffer buf, List<TalentInstance> value) {
        buf.writeInt(value.size());

        for (TalentInstance inst : value) {
            buf.writeRegistryIdUnsafe(DoggyTalentsAPI.TALENTS, inst.getTalent());
            inst.writeToBuf(buf);
        }
    }

    @Override
    public List<TalentInstance> read(PacketBuffer buf) {
        int size = buf.readInt();
        List<TalentInstance> newInst = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            TalentInstance inst = buf.readRegistryIdUnsafe(DoggyTalentsAPI.TALENTS).getDefault();
            inst.readFromBuf(buf);
            newInst.add(inst);
        }
        return newInst;
    }

    @Override
    public DataParameter<List<TalentInstance>> createKey(int id) {
        return new DataParameter<>(id, this);
    }

    @Override
    public List<TalentInstance> copyValue(List<TalentInstance> value) {
        List<TalentInstance> newInst = new ArrayList<>(value.size());

        for (TalentInstance inst : value) {
            newInst.add(inst.copy());
        }

        return newInst;
    }
}
