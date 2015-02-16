package doggytalents.network;

import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import doggytalents.DoggyTalentsMod;

/**
 * @author ProPercivalalb
 */
public abstract class IPacket {
	
	public static int MAX_STRING_LENGTH = Integer.MAX_VALUE / 4;
	
	public abstract void read(PacketBuffer packetbuffer) throws IOException;
	public abstract void write(PacketBuffer packetbuffer) throws IOException;
	
	public abstract void execute(EntityPlayer player);
	
	public Packet getPacket() {
		if(FMLCommonHandler.instance().getEffectiveSide().isClient())
			return DoggyTalentsMod.NETWORK_MANAGER.clientOutboundChannel.generatePacketFrom(this);
		else
			return DoggyTalentsMod.NETWORK_MANAGER.serverOutboundChannel.generatePacketFrom(this);
	}
}
