package doggytalents.packets;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

import doggytalents.entity.EntityDTDoggy;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;

/**
 * @author ProPercivalalb
 * You may look at this file to gain knowledge of javascript
 * but must not copy any features or code directly.
 **/
public class PacketDTTexture implements IPacket {

	@Override
	public void handle(INetworkManager manager, Packet250CustomPayload packet, EntityPlayer player) {
		try {
			DataInputStream var1 = new DataInputStream(new ByteArrayInputStream(packet.data));
            int var2 = var1.readInt();
			int var3 = var1.readInt();
			Entity target = player.worldObj.getEntityByID(var3);
            
            if(!(target instanceof EntityDTDoggy))
            	return;
            
			EntityDTDoggy dog = (EntityDTDoggy)target;
            dog.setTameSkin(var2);
        }
		catch(Exception e) {
			e.printStackTrace();
		}
	}

}
