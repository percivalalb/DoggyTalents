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
	public String woolId, woodId;
	
	public PacketDogBedUpdate() {}
	public PacketDogBedUpdate(int x, int y, int z, String woolId, String woodId) {
		this();
		this.x = x;
		this.y = y;
		this.z = z;
		this.woolId = woolId;
		this.woodId = woodId;
	}

	@Override
	public void read(DataInputStream data) throws IOException {
		this.x = data.readInt();
		this.y = data.readInt();
		this.z = data.readInt();
		this.woolId = data.readUTF();
		this.woodId = data.readUTF();
	}

	@Override
	public void write(DataOutputStream dos) throws IOException {
		dos.writeInt(this.x);
		dos.writeInt(this.y);
		dos.writeInt(this.z);
		dos.writeUTF(this.woolId);
		dos.writeUTF(this.woodId);
	}

	@Override
	public void execute(EntityPlayer player) {
		TileEntity target = player.worldObj.getTileEntity(x, y, z);
		
		if(!(target instanceof TileEntityDogBed))
			return;
		
		TileEntityDogBed dogBed = (TileEntityDogBed)target;
		dogBed.setWoolId(this.woolId);
		dogBed.setWoodId(this.woodId);
	}

}
