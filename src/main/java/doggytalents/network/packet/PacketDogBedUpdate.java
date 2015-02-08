package doggytalents.network.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import doggytalents.network.IPacket;
import doggytalents.tileentity.TileEntityDogBed;

/**
 * @author ProPercivalalb
 */
public class PacketDogBedUpdate extends IPacket {

	public int x, y, z;
	public String casingId, bedingId;
	
	public PacketDogBedUpdate() {}
	public PacketDogBedUpdate(int x, int y, int z, String casingId, String bedingId) {
		this();
		this.x = x;
		this.y = y;
		this.z = z;
		this.casingId = casingId;
		this.bedingId = bedingId;
	}

	@Override
	public void read(DataInputStream data) throws IOException {
		this.x = data.readInt();
		this.y = data.readInt();
		this.z = data.readInt();
		this.casingId = data.readUTF();
		this.bedingId = data.readUTF();
	}

	@Override
	public void write(DataOutputStream dos) throws IOException {
		dos.writeInt(this.x);
		dos.writeInt(this.y);
		dos.writeInt(this.z);
		dos.writeUTF(this.casingId);
		dos.writeUTF(this.bedingId);
	}

	@Override
	public void execute(EntityPlayer player) {
		TileEntity target = player.worldObj.getTileEntity(this.x, this.y, this.z);
		
		if(!(target instanceof TileEntityDogBed))
			return;
		
		TileEntityDogBed dogBed = (TileEntityDogBed)target;
		dogBed.setCasingId(this.casingId);
		dogBed.setBeddingId(this.bedingId);
	}

}
