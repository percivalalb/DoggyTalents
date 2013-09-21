package doggytalents.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;

import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;
import doggytalents.network.packet.DTPacket;

/**
 * @author ProPercivalalb
 */
public class PacketHandler implements IPacketHandler {

    /***
     * Handles Packet250CustomPayload packets for Map Making Tools Mod
     * @param manager The NetworkManager associated with the current platform (client/server)
     * @param packet The Packet250CustomPayload that was received
     * @param player The Player associated with the packet
     */
    @Override
    public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) {
    	DTPacket dtPacket = PacketTypeHandler.buildPacket(packet.data);
    	dtPacket.execute(manager, (EntityPlayer)player);
    }

}