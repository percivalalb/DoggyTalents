package doggytalents.common.network.packet;

import java.util.List;
import java.util.function.Supplier;

import doggytalents.common.Screens;
import doggytalents.common.entity.DogEntity;
import doggytalents.common.network.IPacket;
import doggytalents.common.network.packet.data.OpenDogScreenData;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EntityPredicates;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class OpenDogScreenPacket implements IPacket<OpenDogScreenData>  {

    @Override
    public OpenDogScreenData decode(PacketBuffer buf) {
        return new OpenDogScreenData();
    }


    @Override
    public void encode(OpenDogScreenData data, PacketBuffer buf) {

    }

    @Override
    public void handle(OpenDogScreenData data, Supplier<Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (ctx.get().getDirection().getReceptionSide() == LogicalSide.SERVER) {
                ServerPlayerEntity player = ctx.get().getSender();
                List<DogEntity> dogs = player.world.getEntitiesWithinAABB(DogEntity.class, player.getBoundingBox().grow(12D, 12D, 12D), EntityPredicates.IS_ALIVE);
                Screens.openDogInventoriesScreen(player, dogs);
            }
        });

        ctx.get().setPacketHandled(true);
    }

}
