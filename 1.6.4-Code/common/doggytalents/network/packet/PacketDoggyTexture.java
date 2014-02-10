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
public class PacketDoggyTexture extends DTPacket {

	public int entityId, doggyTexture;
	
	public PacketDoggyTexture() {
		super(PacketTypeHandler.DOGGY_TEXTURE, false);
	}
	
	public PacketDoggyTexture(int entityId, int doggyTexture) {
		this();
		this.entityId = entityId;
		this.doggyTexture = doggyTexture;
	}

	@Override
	public void readData(DataInputStream data) throws IOException {
		this.entityId = data.readInt();
		this.doggyTexture = data.readInt();
	}

	@Override
	public void writeData(DataOutputStream dos) throws IOException {
		dos.writeInt(entityId);
		dos.writeInt(doggyTexture);
	}

	@Override
	public void execute(INetworkManager network, EntityPlayer player) {
		Entity target = player.worldObj.getEntityByID(entityId);
        if(!(target instanceof EntityDTDoggy))
        	return;
        
		EntityDTDoggy dog = (EntityDTDoggy)target;
        
		dog.setTameSkin(doggyTexture);
	}

}
