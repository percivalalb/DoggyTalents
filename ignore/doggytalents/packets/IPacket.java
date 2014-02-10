package doggytalents.packets;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;

/**
 * @author ProPercivalalb
 * You may look at this file to gain knowledge of javascript
 * but must not copy any features or code directly.
 **/
public interface IPacket {

	/** Called in the main packet handler class **/
	public void handle(INetworkManager manager, Packet250CustomPayload packet, EntityPlayer player);
}
