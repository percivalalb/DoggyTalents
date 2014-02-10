package doggytalents.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.FMLIndexedMessageToMessageCodec;

/**
 * @author ProPercivalalb
 */
public class ChannelHandler extends FMLIndexedMessageToMessageCodec<IPacket>{
	
    public ChannelHandler() {
        for (int i = 0; i < PacketType.values().length; i++)
            addDiscriminator(i, PacketType.values()[i].packetClass);
    }

    @Override
    public void encodeInto(ChannelHandlerContext ctx, IPacket msg, ByteBuf bytes) throws Exception {
    	ByteArrayOutputStream bos = new ByteArrayOutputStream();
    	DataOutputStream dos = new DataOutputStream(bos);
    	
    	if (FMLCommonHandler.instance().getEffectiveSide().isClient())
        	dos.writeUTF(FMLClientHandler.instance().getClientPlayerEntity().getCommandSenderName());

    	msg.write(dos);
    	bytes.writeBytes(bos.toByteArray());
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf bytes, IPacket msg)  {
		try {
			byte[] data = new byte[bytes.capacity()];
			bytes.readBytes(data);

			ByteArrayInputStream bis = new ByteArrayInputStream(data);
	        DataInputStream dis = new DataInputStream(bis);
	
			EntityPlayer player;
			
			if(FMLCommonHandler.instance().getEffectiveSide().isClient())
				player = FMLClientHandler.instance().getClientPlayerEntity();
			else
				player = FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().getPlayerForUsername(dis.readUTF());

			msg.read(dis);
			msg.execute(player);
		} 
    	catch(Exception e) {
			e.printStackTrace();
		}
    }
}
