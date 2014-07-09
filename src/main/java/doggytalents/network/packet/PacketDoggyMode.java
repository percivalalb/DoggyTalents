package doggytalents.network.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import doggytalents.entity.EntityDTDoggy;
import doggytalents.network.IPacket;

/**
 * @author ProPercivalalb
 */
public class PacketDoggyMode extends IPacket {

	public int entityId, doggyMode;
	
	public PacketDoggyMode() {}
	public PacketDoggyMode(int entityId, int doggyMode) {
		this();
		this.entityId = entityId;
		this.doggyMode = doggyMode;
	}

	@Override
	public void read(DataInputStream data) throws IOException {
		this.entityId = data.readInt();
		this.doggyMode = data.readInt();
	}

	@Override
	public void write(DataOutputStream dos) throws IOException {
		dos.writeInt(entityId);
		dos.writeInt(doggyMode);
	}

	@Override
	public void execute(EntityPlayer player) {
		Entity target = player.worldObj.getEntityByID(entityId);
        if(!(target instanceof EntityDTDoggy))
        	return;
        
		EntityDTDoggy dog = (EntityDTDoggy)target;
        
		dog.mode.setMode(doggyMode);
	}

}
