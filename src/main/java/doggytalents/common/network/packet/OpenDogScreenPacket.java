package doggytalents.common.network.packet;

import java.util.List;
import java.util.function.Supplier;

import doggytalents.common.Screens;
import doggytalents.common.entity.DogEntity;
import doggytalents.common.network.IPacket;
import doggytalents.common.network.packet.data.OpenDogScreenData;
import doggytalents.common.talent.PackPuppyTalent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class OpenDogScreenPacket implements IPacket<OpenDogScreenData>  {

    @Override
    public OpenDogScreenData decode(FriendlyByteBuf buf) {
        return new OpenDogScreenData();
    }


    @Override
    public void encode(OpenDogScreenData data, FriendlyByteBuf buf) {

    }

    @Override
    public void handle(OpenDogScreenData data, Supplier<Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (ctx.get().getDirection().getReceptionSide() == LogicalSide.SERVER) {
                ServerPlayer player = ctx.get().getSender();
                List<DogEntity> dogs = player.level.getEntitiesOfClass(DogEntity.class, player.getBoundingBox().inflate(12D, 12D, 12D), PackPuppyTalent::hasInventory);
                Screens.openDogInventoriesScreen(player, dogs);
            }
        });

        ctx.get().setPacketHandled(true);
    }
}
