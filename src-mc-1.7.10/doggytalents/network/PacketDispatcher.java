package doggytalents.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import doggytalents.lib.Reference;
import doggytalents.network.packet.client.CommandMessage;
import doggytalents.network.packet.client.DogBedUpdateMessage;
import doggytalents.network.packet.client.DogJumpMessage;
import doggytalents.network.packet.client.DogModeMessage;
import doggytalents.network.packet.client.DogNameMessage;
import doggytalents.network.packet.client.DogObeyMessage;
import doggytalents.network.packet.client.DogTalentMessage;
import doggytalents.network.packet.client.DogTextureMessage;

/**
 * @author ProPercivalalb
 * Thanks to coolAlias for providing the tutorial 
 * that contains most of this network handler code
 * https://github.com/coolAlias/Tutorial-Demo
 */
public class PacketDispatcher {
	
	private static int packetId = 0;

	private static final SimpleNetworkWrapper dispatcher = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.CHANNEL_NAME);

	public static final void registerPackets() {
		registerMessage(CommandMessage.class);
		registerMessage(DogJumpMessage.class);
		registerMessage(DogModeMessage.class);
		registerMessage(DogNameMessage.class);
		registerMessage(DogObeyMessage.class);
		registerMessage(DogTalentMessage.class);
		registerMessage(DogTextureMessage.class);
		registerMessage(DogBedUpdateMessage.class);
	}
	
	private static final <T extends AbstractMessage<T> & IMessageHandler<T, IMessage>> void registerMessage(Class<T> clazz) {
		if (AbstractMessage.AbstractClientMessage.class.isAssignableFrom(clazz)) {
			PacketDispatcher.dispatcher.registerMessage(clazz, clazz, packetId++, Side.CLIENT);
		} else if (AbstractMessage.AbstractServerMessage.class.isAssignableFrom(clazz)) {
			PacketDispatcher.dispatcher.registerMessage(clazz, clazz, packetId++, Side.SERVER);
		} else {
			PacketDispatcher.dispatcher.registerMessage(clazz, clazz, packetId, Side.CLIENT);
			PacketDispatcher.dispatcher.registerMessage(clazz, clazz, packetId++, Side.SERVER);
		}
	}

	public static final void sendTo(IMessage message, EntityPlayerMP player) {
		PacketDispatcher.dispatcher.sendTo(message, player);
	}
	
	public static void sendToAll(IMessage message) {
		PacketDispatcher.dispatcher.sendToAll(message);
	}

	public static final void sendToAllAround(IMessage message, NetworkRegistry.TargetPoint point) {
		PacketDispatcher.dispatcher.sendToAllAround(message, point);
	}

	public static final void sendToAllAround(IMessage message, int dimension, double x, double y, double z, double range) {
		PacketDispatcher.sendToAllAround(message, new NetworkRegistry.TargetPoint(dimension, x, y, z, range));
	}

	public static final void sendToAllAround(IMessage message, EntityPlayer player, double range) {
		PacketDispatcher.sendToAllAround(message, player.worldObj.provider.dimensionId, player.posX, player.posY, player.posZ, range);
	}
	
	public static final void sendToAllAround(IMessage message, TileEntity tileEntity, double range) {
		PacketDispatcher.sendToAllAround(message, tileEntity.getWorldObj().provider.dimensionId, tileEntity.xCoord + 0.5D, tileEntity.yCoord + 0.5D, tileEntity.zCoord + 0.5D, range);
	}

	public static final void sendToDimension(IMessage message, int dimensionId) {
		PacketDispatcher.dispatcher.sendToDimension(message, dimensionId);
	}

	public static final void sendToServer(IMessage message) {
		PacketDispatcher.dispatcher.sendToServer(message);
	}
	
	public static final Packet getPacket(IMessage message) {
		return PacketDispatcher.dispatcher.getPacketFrom(message);
	}
}
