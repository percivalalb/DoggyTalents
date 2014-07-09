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
public class PacketDoggyName extends IPacket {

	public int entityId;
	public String name;
	
	public PacketDoggyName() {}
	public PacketDoggyName(int entityId, String name) {
		this();
		this.entityId = entityId;
		this.name = name;
	}

	@Override
	public void read(DataInputStream data) throws IOException {
		this.entityId = data.readInt();
		this.name = data.readUTF();
	}

	@Override
	public void write(DataOutputStream dos) throws IOException {
		dos.writeInt(entityId);
		dos.writeUTF(name);
	}

	@Override
	public void execute(EntityPlayer player) {
		Entity target = player.worldObj.getEntityByID(entityId);
        if(!(target instanceof EntityDTDoggy))
        	return;
        
		EntityDTDoggy dog = (EntityDTDoggy)target;
        
		dog.setWolfName(name);
	}

}
