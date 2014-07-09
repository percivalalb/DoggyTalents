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
public class PacketObeyOthers extends IPacket {

	public int entityId;
	public boolean obey;
	
	public PacketObeyOthers() {}
	public PacketObeyOthers(int entityId, boolean obey) {
		this();
		this.entityId = entityId;
		this.obey = obey;
	}

	@Override
	public void read(DataInputStream data) throws IOException {
		this.entityId = data.readInt();
		this.obey = data.readBoolean();
	}

	@Override
	public void write(DataOutputStream dos) throws IOException {
		dos.writeInt(entityId);
		dos.writeBoolean(obey);
	}

	@Override
	public void execute(EntityPlayer player) {
		Entity target = player.worldObj.getEntityByID(entityId);
        if(!(target instanceof EntityDTDoggy))
        	return;
        
		EntityDTDoggy dog = (EntityDTDoggy)target;
        
		dog.setWillObeyOthers(obey);
	}

}
