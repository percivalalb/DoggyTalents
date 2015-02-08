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
public class PacketDogTexture extends IPacket {

	public int entityId, doggyTexture;
	
	public PacketDogTexture() {}
	public PacketDogTexture(int entityId, int doggyTexture) {
		this();
		this.entityId = entityId;
		this.doggyTexture = doggyTexture;
	}

	@Override
	public void read(DataInputStream data) throws IOException {
		this.entityId = data.readInt();
		this.doggyTexture = data.readInt();
	}

	@Override
	public void write(DataOutputStream dos) throws IOException {
		dos.writeInt(this.entityId);
		dos.writeInt(this.doggyTexture);
	}

	@Override
	public void execute(EntityPlayer player) {
		Entity target = player.worldObj.getEntityByID(this.entityId);
        if(!(target instanceof EntityDog))
        	return;
        
        EntityDog dog = (EntityDog)target;
        
		dog.setTameSkin(this.doggyTexture);
	}

}
