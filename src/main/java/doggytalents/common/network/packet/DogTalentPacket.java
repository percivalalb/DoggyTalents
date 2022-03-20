package doggytalents.common.network.packet;

import doggytalents.DoggyTalents2;
import doggytalents.api.DoggyTalentsAPI;
import doggytalents.api.registry.Talent;
import doggytalents.common.config.ConfigHandler;
import doggytalents.common.entity.DogEntity;
import doggytalents.common.network.packet.data.DogTalentData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent.Context;

import java.util.function.Supplier;

public class DogTalentPacket extends DogPacket<DogTalentData> {

    @Override
    public void encode(DogTalentData data, FriendlyByteBuf buf) {
        super.encode(data, buf);
        buf.writeRegistryIdUnsafe(DoggyTalentsAPI.TALENTS.get(), data.talent);
    }

    @Override
    public DogTalentData decode(FriendlyByteBuf buf) {
        int entityId = buf.readInt();
        Talent talent = buf.readRegistryIdUnsafe(DoggyTalentsAPI.TALENTS.get());
        return new DogTalentData(entityId, talent);
    }

    @Override
    public void handleDog(DogEntity dogIn, DogTalentData data, Supplier<Context> ctx) {
        if (!dogIn.canInteract(ctx.get().getSender())) {
            return;
        }

        if (!ConfigHandler.TALENT.getFlag(data.talent)) {
            DoggyTalents2.LOGGER.info("{} tried to level a disabled talent ({})",
                    ctx.get().getSender().getGameProfile().getName(),
                    data.talent.getRegistryName());
            return;
        }

        int level = dogIn.getDogLevel(data.talent);

        if (level < data.talent.getMaxLevel() && dogIn.canSpendPoints(data.talent.getLevelCost(level + 1))) {
            dogIn.setTalentLevel(data.talent, level + 1);
        }
    }
}
