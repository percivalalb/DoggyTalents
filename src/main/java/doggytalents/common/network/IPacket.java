package doggytalents.common.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public interface IPacket<D> {

    public void encode(D data, FriendlyByteBuf buf);

    public D decode(FriendlyByteBuf buf);

    public void handle(D data, Supplier<NetworkEvent.Context> ctx);
}
