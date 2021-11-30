package doggytalents.common.network.packet;

import doggytalents.common.inventory.container.DogInventoriesContainer;
import doggytalents.common.network.IPacket;
import doggytalents.common.network.packet.data.DogInventoryPageData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent.Context;

import java.util.function.Supplier;

public class DogInventoryPagePacket implements IPacket<DogInventoryPageData>  {

    @Override
    public DogInventoryPageData decode(FriendlyByteBuf buf) {
        return new DogInventoryPageData(buf.readInt());
    }


    @Override
    public void encode(DogInventoryPageData data, FriendlyByteBuf buf) {
        buf.writeInt(data.page);
    }

    @Override
    public void handle(DogInventoryPageData data, Supplier<Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (ctx.get().getDirection().getReceptionSide() == LogicalSide.SERVER) {
                ServerPlayer player = ctx.get().getSender();
                AbstractContainerMenu container = player.containerMenu;
                if (container instanceof DogInventoriesContainer) {
                    DogInventoriesContainer inventories = (DogInventoriesContainer) container;
                    int page = Mth.clamp(data.page, 0, Math.max(0, inventories.getTotalNumColumns() - 9));

                    inventories.setPage(page);
                    inventories.setData(0, page);
                }
            }
        });

        ctx.get().setPacketHandled(true);
    }

}
