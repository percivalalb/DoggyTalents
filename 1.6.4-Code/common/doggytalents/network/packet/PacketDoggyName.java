package doggytalents.network.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import doggytalents.core.helper.LogHelper;
import doggytalents.entity.EntityDTDoggy;
import doggytalents.entity.data.EnumTalents;
import doggytalents.network.PacketTypeHandler;

/**
 * @author ProPercivalalb
 */
public class PacketDoggyName extends DTPacket {

	public int entityId;
	public String name;
	
	public PacketDoggyName() {
		super(PacketTypeHandler.DOGGY_NAME, false);
	}
	
	public PacketDoggyName(int entityId, String name) {
		this();
		this.entityId = entityId;
		this.name = name;
	}

	@Override
	public void readData(DataInputStream data) throws IOException {
		this.entityId = data.readInt();
		this.name = data.readUTF();
	}

	@Override
	public void writeData(DataOutputStream dos) throws IOException {
		dos.writeInt(entityId);
		dos.writeUTF(name);
	}

	@Override
	public void execute(INetworkManager network, EntityPlayer player) {
		Entity target = player.worldObj.getEntityByID(entityId);
        if(!(target instanceof EntityDTDoggy))
        	return;
        
		EntityDTDoggy dog = (EntityDTDoggy)target;
        
		dog.setWolfName(name);
	}

}
