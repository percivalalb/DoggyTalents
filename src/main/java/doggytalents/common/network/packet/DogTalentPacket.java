package doggytalents.common.network.packet;

import java.util.function.Supplier;

import doggytalents.api.DoggyTalentsAPI;
import doggytalents.api.registry.Talent;
import doggytalents.common.entity.DogEntity;
import doggytalents.common.network.packet.data.DogTalentData;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class DogTalentPacket extends DogPacket<DogTalentData> {

    @Override
    public void encode(DogTalentData data, PacketBuffer buf) {
        super.encode(data, buf);
        buf.writeRegistryIdUnsafe(DoggyTalentsAPI.TALENTS, data.talent);
    }

    @Override
    public DogTalentData decode(PacketBuffer buf) {
        int entityId = buf.readInt();
        Talent talent = buf.readRegistryIdUnsafe(DoggyTalentsAPI.TALENTS);
        return new DogTalentData(entityId, talent);
    }

    @Override
    public void handleDog(DogEntity dogIn, DogTalentData data, Supplier<Context> ctx) {
        if(!dogIn.canInteract(ctx.get().getSender())) {
            return;
        }

        int level = dogIn.getLevel(data.talent);

        if(level < data.talent.getMaxLevel() && dogIn.getSpendablePoints() >= data.talent.getLevelCost(level + 1)) {
            dogIn.setTalentLevel(data.talent, level + 1);
        }
    }
}
