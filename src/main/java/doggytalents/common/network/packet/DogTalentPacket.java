package doggytalents.common.network.packet;

import java.util.function.Supplier;

import doggytalents.api.DoggyTalentsAPI;
import doggytalents.api.registry.Talent;
import doggytalents.common.entity.DogEntity;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;

public class DogTalentPacket {

    public int entityId;
    public ResourceLocation talentId;

    public DogTalentPacket(int entityId, ResourceLocation talentId) {
        this.entityId = entityId;
        this.talentId = talentId;
    }

    public static void encode(DogTalentPacket msg, PacketBuffer buf) {
        buf.writeInt(msg.entityId);
        buf.writeResourceLocation(msg.talentId);
    }

    public static DogTalentPacket decode(PacketBuffer buf) {
        int entityId = buf.readInt();
        ResourceLocation talentId = buf.readResourceLocation();
        return new DogTalentPacket(entityId, talentId);
    }


    public static class Handler {
        public static void handle(final DogTalentPacket msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                Entity target = ctx.get().getSender().world.getEntityByID(msg.entityId);

                if(!(target instanceof DogEntity)) {
                    return;
                }

                DogEntity dog = (DogEntity)target;
//                if(!dog.canInteract(ctx.get().getSender())) {
//                    return;
//                }
                Talent talent = DoggyTalentsAPI.TALENTS.getValue(msg.talentId);
                int level = dog.getLevel(talent);

                if(level < talent.getMaxLevel() && dog.getSpendablePoints() >= talent.getLevelCost(level + 1)) {
                    dog.setTalentLevel(talent, level + 1);
                }
            });

            ctx.get().setPacketHandled(true);
        }
    }
}
