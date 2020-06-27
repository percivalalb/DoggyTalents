package doggytalents.common.network.packet;

import java.util.function.Supplier;

import doggytalents.DoggyTalents2;
import doggytalents.common.inventory.container.DogInventoriesContainer;
import doggytalents.common.network.IPacket;
import doggytalents.common.network.packet.data.DogInventoryPageData;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class DogInventoryPagePacket implements IPacket<DogInventoryPageData>  {

    @Override
    public DogInventoryPageData decode(PacketBuffer buf) {
        return new DogInventoryPageData(buf.readInt());
    }


    @Override
    public void encode(DogInventoryPageData data, PacketBuffer buf) {
        buf.writeInt(data.page);
    }

    @Override
    public void handle(DogInventoryPageData data, Supplier<Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (ctx.get().getDirection().getReceptionSide() == LogicalSide.SERVER) {
                ServerPlayerEntity player = ctx.get().getSender();
                Container container = player.openContainer;
                if (container instanceof DogInventoriesContainer) {
                    DogInventoriesContainer inventories = (DogInventoriesContainer) container;
                    int page = MathHelper.clamp(data.page, 0, Math.max(0, inventories.possibleSlots - 9));

                    inventories.position.set(page);

                    inventories.updateProgressBar(0, page);


                    DoggyTalents2.LOGGER.debug("set {}", page);
                }
            }
        });

        ctx.get().setPacketHandled(true);
    }

}
