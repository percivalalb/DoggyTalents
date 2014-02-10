package doggytalents.network.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.tileentity.TileEntity;
import doggytalents.core.helper.LogHelper;
import doggytalents.entity.EntityDTDoggy;
import doggytalents.entity.data.EnumTalents;
import doggytalents.network.PacketTypeHandler;
import doggytalents.tileentity.TileEntityDogBed;

/**
 * @author ProPercivalalb
 */
public class PacketDogBedUpdate extends DTPacket {

	public int x, y, z;
	public String woolId, woodId;
	
	public PacketDogBedUpdate() {
		super(PacketTypeHandler.DOG_BED_UPDATE, false);
	}
	
	public PacketDogBedUpdate(int x, int y, int z, String woolId, String woodId) {
		this();
		this.x = x;
		this.y = y;
		this.z = z;
		this.woolId = woolId;
		this.woodId = woodId;
	}

	@Override
	public void readData(DataInputStream data) throws IOException {
		this.x = data.readInt();
		this.y = data.readInt();
		this.z = data.readInt();
		this.woolId = data.readUTF();
		this.woodId = data.readUTF();
	}

	@Override
	public void writeData(DataOutputStream dos) throws IOException {
		dos.writeInt(x);
		dos.writeInt(y);
		dos.writeInt(z);
		dos.writeUTF(woolId);
		dos.writeUTF(woodId);
	}

	@Override
	public void execute(INetworkManager network, EntityPlayer player) {
		TileEntity target = player.worldObj.getBlockTileEntity(x, y, z);
		
		if(!(target instanceof TileEntityDogBed))
			return;
		
		TileEntityDogBed dogBed = (TileEntityDogBed)target;
		dogBed.setWoolId(woolId);
		dogBed.setWoodId(woodId);
	}

}
