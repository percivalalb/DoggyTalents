package doggytalents.network;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.network.play.server.S3FPacketCustomPayload;
import cpw.mods.fml.common.FMLCommonHandler;
import doggytalents.DoggyTalentsMod;
import doggytalents.lib.Reference;

/**
 * @author ProPercivalalb
 */
public abstract class IPacket {
	
	public abstract void read(DataInputStream data) throws IOException;
	public abstract void write(DataOutputStream data) throws IOException;
	
	public abstract void execute(EntityPlayer player);
	
	public Packet getPacket() {
		if(FMLCommonHandler.instance().getEffectiveSide().isClient())
			return new C17PacketCustomPayload(Reference.CHANNEL_NAME, this.getPacketBytes(true));
		else
			return new S3FPacketCustomPayload(Reference.CHANNEL_NAME, this.getPacketBytes(false));
	}
	
	private byte[] getPacketBytes(boolean isClient) {
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
	    	DataOutputStream dos = new DataOutputStream(bos);
	    	dos.writeByte(PacketType.getIdFromClass(this.getClass()));
	    	if(isClient)
	    		dos.writeUTF(DoggyTalentsMod.proxy.getClientPlayer().getCommandSenderName());
			this.write(dos);
	    	return bos.toByteArray();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
