package doggytalents.common.entity.serializers;

import java.util.ArrayList;
import java.util.List;

import doggytalents.api.DoggyTalentsAPI;
import doggytalents.api.inferface.Accessory;
import doggytalents.api.inferface.AccessoryInstance;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.IDataSerializer;

public class AccessorySerializer implements IDataSerializer<List<AccessoryInstance>> {

    @Override
    public void write(PacketBuffer buf, List<AccessoryInstance> value) {
        buf.writeInt(value.size());

        for (AccessoryInstance inst : value) {
            buf.writeRegistryIdUnsafe(DoggyTalentsAPI.ACCESSORIES, inst.getAccessory());
            inst.getAccessory().write(inst, buf);
        }
    }

    @Override
    public List<AccessoryInstance> read(PacketBuffer buf) {
        int size = buf.readInt();
        List<AccessoryInstance> newInst = new ArrayList<>(size);

        for (int i = 0; i < size; i++) {
            Accessory type = buf.readRegistryIdUnsafe(DoggyTalentsAPI.ACCESSORIES);
            newInst.add(type.createInstance(buf));
        }

        return newInst;
    }

    @Override
    public List<AccessoryInstance> copyValue(List<AccessoryInstance> value) {
        List<AccessoryInstance> newInst = new ArrayList<>(value.size());

        for (AccessoryInstance inst : value) {
            newInst.add(inst.copy());
        }

        return newInst;
    }

}
