package doggytalents.common.network;

import java.util.function.Supplier;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public interface IPacket<D> {

    public void encode(D data, PacketBuffer buf);

    public D decode(PacketBuffer buf);

    public void handle(D data, Supplier<NetworkEvent.Context> ctx);
}
