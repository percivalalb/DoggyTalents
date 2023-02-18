package doggytalents.common.network.packet;

import java.util.function.Supplier;

import doggytalents.common.entity.DogEntity;
import doggytalents.common.network.IPacket;
import doggytalents.common.network.packet.data.DogDismountData;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent.Context;

public class DogDismountPacket implements IPacket<DogDismountData> {

    @Override
    public void encode(DogDismountData data, FriendlyByteBuf buf) {
        buf.writeInt(data.dogId);
    }

    @Override
    public DogDismountData decode(FriendlyByteBuf buf) {
        int id = buf.readInt();
        return new DogDismountData(id);
    }

    @Override
    public void handle(DogDismountData data, Supplier<Context> ctx) {
        ctx.get().enqueueWork(() -> {

            if (ctx.get().getDirection().getReceptionSide().isClient()) { 
                Minecraft mc = Minecraft.getInstance();
                Entity e = mc.level.getEntity(data.dogId);
                if (e instanceof DogEntity d) {
                    if (d.isPassenger()) d.stopRiding();
                }
            }

        });

        ctx.get().setPacketHandled(true);
    }
    
}
