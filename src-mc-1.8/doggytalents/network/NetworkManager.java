package doggytalents.network;

import java.util.EnumMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.FMLEmbeddedChannel;
import net.minecraftforge.fml.common.network.FMLOutboundHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.relauncher.Side;
import doggytalents.lib.Reference;

/**
 * @author ProPercivalalb
 */
public class NetworkManager {

    public final ChannelHandler channelHandler;
    public final FMLEmbeddedChannel clientOutboundChannel;
    public final FMLEmbeddedChannel serverOutboundChannel;
    
    public NetworkManager() {
        this.channelHandler = new ChannelHandler();
        
        EnumMap<Side, FMLEmbeddedChannel> channelPair = NetworkRegistry.INSTANCE.newChannel(Reference.CHANNEL_NAME, this.channelHandler);
        this.clientOutboundChannel = channelPair.get(Side.CLIENT);
        this.serverOutboundChannel = channelPair.get(Side.SERVER);
        
    }
    
    public void sendPacketToServer(IPacket packet) {
        if (FMLCommonHandler.instance().getEffectiveSide().isClient()) {
            this.clientOutboundChannel.attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.TOSERVER);
            this.clientOutboundChannel.writeOutbound(packet);
        }
    }
    
    public void sendPacketToPlayer(IPacket packet, EntityPlayer player) {
        if (FMLCommonHandler.instance().getEffectiveSide().isServer()) {
            this.serverOutboundChannel.attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.PLAYER);
            this.serverOutboundChannel.attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(player);
            this.serverOutboundChannel.writeOutbound(packet);
        }
    }
    
    public void sendPacketToAllInDimension(IPacket packet, int dimensionId) {
        if (FMLCommonHandler.instance().getEffectiveSide().isServer()) {
            this.serverOutboundChannel.attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.DIMENSION);
            this.serverOutboundChannel.attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(dimensionId);
            this.serverOutboundChannel.writeOutbound(packet);
        }
    }
    
    public void sendPacketToAllAround(IPacket packet, int dimensionId, double x, double y, double z, double range) {
        if (FMLCommonHandler.instance().getEffectiveSide().isServer()) {
            this.serverOutboundChannel.attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALLAROUNDPOINT);
            this.serverOutboundChannel.attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(new TargetPoint(dimensionId, x, y, z, range));
            this.serverOutboundChannel.writeOutbound(packet);
        }
    }
}
