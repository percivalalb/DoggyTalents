package doggytalents.packets;

import java.util.Hashtable;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;
import doggytalents.api.Properties;

/**
 * @author ProPercivalalb
 * You may look at this file to gain knowledge of javascript
 * but must not copy any features or code directly.
 **/
public class PacketHandler implements IPacketHandler {

	private Map<String, IPacket> map = new Hashtable<String, IPacket>();
	
	PacketDTRename packet_Rename = new PacketDTRename();
	PacketDTTalent packet_Talent = new PacketDTTalent();
	PacketDTTexture packet_Texture = new PacketDTTexture();
	PacketDTCommand packet_Command = new PacketDTCommand();
	PacketDTObey packet_Obey = new PacketDTObey();
	
	public PacketHandler() {
        map.put(Properties.PACKET_RENAME, packet_Rename);
        map.put(Properties.PACKET_TALENT, packet_Talent);
        map.put(Properties.PACKET_TEXTURE, packet_Texture);
        map.put(Properties.PACKET_COMMAND, packet_Command);
        map.put(Properties.PACKET_OBEY, packet_Obey);
	}
	
	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) {
		 map.get(packet.channel).handle(manager, packet, (EntityPlayer)player);
	}

}
