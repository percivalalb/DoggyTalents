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
public class PacketDogName extends IPacket {

	public int entityId;
	public String name;
	
	public PacketDogName() {}
	public PacketDogName(int entityId, String name) {
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
		dos.writeInt(this.entityId);
		dos.writeUTF(this.name);
	}

	@Override
	public void execute(EntityPlayer player) {
		Entity target = player.worldObj.getEntityByID(this.entityId);
		
        if(!(target instanceof EntityDog))
        	return;
        
		EntityDog dog = (EntityDog)target;
        
		dog.setDogName(this.name);
	}

}
