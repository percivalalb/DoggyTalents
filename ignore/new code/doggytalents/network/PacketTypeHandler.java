package doggytalents.network;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

import cpw.mods.fml.relauncher.Side;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.server.MinecraftServer;
import cpw.mods.fml.common.FMLCommonHandler;
import doggytalents.lib.Reference;
import doggytalents.network.packet.PacketDT;
import doggytalents.network.packet.PacketTalentUpdate;

/**
 * @author ProPercivalalb
 */
public enum PacketTypeHandler {
	
	TALENT_UPDATE(PacketTalentUpdate.class);
	
    private Class<? extends PacketDT> clazz;

    PacketTypeHandler(Class<? extends PacketDT> clazz) {
        this.clazz = clazz;
    }

    public static PacketDT buildPacket(byte[] data) {
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        int selector = bis.read();
        DataInputStream dis = new DataInputStream(bis);
        PacketDT packet = null;
        try {
            packet = values()[selector].clazz.newInstance();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        packet.readPopulate(dis);
        return packet;
    }

    public static PacketDT buildPacket(PacketTypeHandler type) {
        PacketDT packet = null;
        try {
            packet = values()[type.ordinal()].clazz.newInstance();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return packet;
    }

    public static Packet populatePacket(PacketDT packetMMT) {
        byte[] data = packetMMT.populate();
        Packet250CustomPayload packet250 = new Packet250CustomPayload();
        packet250.channel = Reference.CHANNEL_NAME;
        packet250.data = data;
        packet250.length = data.length;
        packet250.isChunkDataPacket = packetMMT.isChunkDataPacket;
        return packet250;
    }
    
    public static void populatePacketAndSendToServer(PacketDT packetMMT) {
    	Packet packet = populatePacket(packetMMT);
    	Side side = FMLCommonHandler.instance().getEffectiveSide();
	    if(side == Side.CLIENT) {
	    	net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getMinecraft();
	    	mc.getNetHandler().addToSendQueue(packet);
	    }
    }
    
    public static void populatePacketAndSendToClient(PacketDT packetMMT, EntityPlayerMP player) {
    	Packet packet = populatePacket(packetMMT);
    	Side side = FMLCommonHandler.instance().getEffectiveSide();
	    if(side == Side.SERVER) {
	    	MinecraftServer server = MinecraftServer.getServer();
	    	player.playerNetServerHandler.sendPacketToPlayer(packet);
	    }
    }
}