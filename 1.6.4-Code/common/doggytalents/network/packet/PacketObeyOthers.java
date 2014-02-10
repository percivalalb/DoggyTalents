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
public class PacketObeyOthers extends DTPacket {

	public int entityId;
	public boolean obey;
	
	public PacketObeyOthers() {
		super(PacketTypeHandler.OBEY_OTHERS, false);
	}
	
	public PacketObeyOthers(int entityId, boolean obey) {
		this();
		this.entityId = entityId;
		this.obey = obey;
	}

	@Override
	public void readData(DataInputStream data) throws IOException {
		this.entityId = data.readInt();
		this.obey = data.readBoolean();
	}

	@Override
	public void writeData(DataOutputStream dos) throws IOException {
		dos.writeInt(entityId);
		dos.writeBoolean(obey);
	}

	@Override
	public void execute(INetworkManager network, EntityPlayer player) {
		Entity target = player.worldObj.getEntityByID(entityId);
        if(!(target instanceof EntityDTDoggy))
        	return;
        
		EntityDTDoggy dog = (EntityDTDoggy)target;
        
		dog.setWillObeyOthers(obey);
	}

}
