package doggytalents.network.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import doggytalents.entity.EntityDog;
import doggytalents.network.IPacket;

/**
 * @author ProPercivalalb
 */
public class PacketDogMode extends IPacket {

	public int entityId, doggyMode;
	
	public PacketDogMode() {}
	public PacketDogMode(int entityId, int doggyMode) {
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
		dos.writeInt(this.entityId);
		dos.writeInt(this.doggyMode);
	}

	@Override
	public void execute(EntityPlayer player) {
		Entity target = player.worldObj.getEntityByID(this.entityId);
        if(!(target instanceof EntityDog))
        	return;
        
        EntityDog dog = (EntityDog)target;
        
		dog.mode.setMode(this.doggyMode);
	}

}
