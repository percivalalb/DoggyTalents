package doggytalents.common.network.packet;

import java.util.function.Supplier;

import doggytalents.DoggyTalents2;
import doggytalents.api.DoggyTalentsAPI;
import doggytalents.api.registry.Talent;
import doggytalents.common.config.ConfigValues;
import doggytalents.common.entity.DogEntity;
import doggytalents.common.network.packet.data.DogTalentData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fmllegacy.network.NetworkEvent.Context;

public class DogTalentPacket extends DogPacket<DogTalentData> {

    @Override
    public void encode(DogTalentData data, FriendlyByteBuf buf) {
        super.encode(data, buf);
        buf.writeRegistryIdUnsafe(DoggyTalentsAPI.TALENTS, data.talent);
    }

    @Override
    public DogTalentData decode(FriendlyByteBuf buf) {
        int entityId = buf.readInt();
        Talent talent = buf.readRegistryIdUnsafe(DoggyTalentsAPI.TALENTS);
        return new DogTalentData(entityId, talent);
    }

    @Override
    public void handleDog(DogEntity dogIn, DogTalentData data, Supplier<Context> ctx) {
        if (!dogIn.canInteract(ctx.get().getSender())) {
            return;
        }

        if (ConfigValues.DISABLED_TALENTS.contains(data.talent)) {
            DoggyTalents2.LOGGER.info("{} tried to level a disabled talent ({})",
                    ctx.get().getSender().getGameProfile().getName(),
                    data.talent.getRegistryName());
            return;
        }

        int level = dogIn.getLevel(data.talent);

        if (level < data.talent.getMaxLevel() && dogIn.canSpendPoints(data.talent.getLevelCost(level + 1))) {
            dogIn.setTalentLevel(data.talent, level + 1);
        }
    }
}
