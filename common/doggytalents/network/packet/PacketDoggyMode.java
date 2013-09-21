package doggytalents.network.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import doggytalents.core.helper.LogHelper;
import doggytalents.entity.EntityDTDoggy;
import doggytalents.entity.data.EnumMode;
import doggytalents.entity.data.EnumTalents;
import doggytalents.network.PacketTypeHandler;

/**
 * @author ProPercivalalb
 */
public class PacketDoggyMode extends DTPacket {

	public int entityId, doggyMode;
	
	public PacketDoggyMode() {
		super(PacketTypeHandler.DOGGY_MODE, false);
	}
	
	public PacketDoggyMode(int entityId, int doggyMode) {
		this();
		this.entityId = entityId;
		this.doggyMode = doggyMode;
	}

	@Override
	public void readData(DataInputStream data) throws IOException {
		this.entityId = data.readInt();
		this.doggyMode = data.readInt();
	}

	@Override
	public void writeData(DataOutputStream dos) throws IOException {
		dos.writeInt(entityId);
		dos.writeInt(doggyMode);
	}

	@Override
	public void execute(INetworkManager network, EntityPlayer player) {
		Entity target = player.worldObj.getEntityByID(entityId);
        if(!(target instanceof EntityDTDoggy))
        	return;
        
		EntityDTDoggy dog = (EntityDTDoggy)target;
        
		dog.mode.setMode(doggyMode);
	}

}
