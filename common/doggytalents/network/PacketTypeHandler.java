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
import doggytalents.network.packet.*;

/**
 * @author ProPercivalalb
 */
public enum PacketTypeHandler {
	
	TALENT_TO_SERVER(PacketTalentToServer.class),
	DOGGY_TEXTURE(PacketDoggyTexture.class),
	OBEY_OTHERS(PacketObeyOthers.class),
	DOGGY_MODE(PacketDoggyMode.class),
	DOGGY_NAME(PacketDoggyName.class);
	
    private Class<? extends DTPacket> clazz;

    PacketTypeHandler(Class<? extends DTPacket> clazz) {
        this.clazz = clazz;
    }

    public static DTPacket buildPacket(byte[] data) {
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        int selector = bis.read();
        DataInputStream dis = new DataInputStream(bis);
        DTPacket packet = null;
        try {
            packet = values()[selector].clazz.newInstance();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        packet.readPopulate(dis);
        return packet;
    }

    public static DTPacket buildPacket(PacketTypeHandler type) {
        DTPacket packet = null;
        try {
            packet = values()[type.ordinal()].clazz.newInstance();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return packet;
    }

    public static Packet populatePacket(DTPacket dtPacket) {
        byte[] data = dtPacket.populate();
        Packet250CustomPayload packet250 = new Packet250CustomPayload();
        packet250.channel = Reference.CHANNEL_NAME;
        packet250.data = data;
        packet250.length = data.length;
        packet250.isChunkDataPacket = dtPacket.isChunkDataPacket;
        return packet250;
    }
    
    public static void populatePacketAndSendToServer(DTPacket dtPacket) {
    	Packet packet = populatePacket(dtPacket);
    	Side side = FMLCommonHandler.instance().getEffectiveSide();
	    if(side == Side.CLIENT) {
	    	net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getMinecraft();
	    	mc.getNetHandler().addToSendQueue(packet);
	    }
    }
    
    public static void populatePacketAndSendToClient(DTPacket dtPacket, EntityPlayerMP player) {
    	Packet packet = populatePacket(dtPacket);
    	Side side = FMLCommonHandler.instance().getEffectiveSide();
	    if(side == Side.SERVER) {
	    	MinecraftServer server = MinecraftServer.getServer();
	    	player.playerNetServerHandler.sendPacketToPlayer(packet);
	    }
    }
}