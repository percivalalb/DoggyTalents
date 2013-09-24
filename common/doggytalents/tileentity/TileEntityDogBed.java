package doggytalents.tileentity;

import doggytalents.network.PacketTypeHandler;
import doggytalents.network.packet.PacketDogBedUpdate;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet;
import net.minecraft.tileentity.TileEntity;

/**
 * @author ProPercivalalb
 */
public class TileEntityDogBed extends TileEntity {

	private String woolId;
	private String woodId;
	
	public TileEntityDogBed() {
		this.woolId = "";
		this.woodId = "";
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		this.woolId = tag.getString("bedWoolId");
		this.woodId = tag.getString("bedWoodId");
    }

	@Override
    public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		tag.setString("bedWoolId", this.woolId);
		tag.setString("bedWoodId", this.woodId);
	}
	
	@Override
    public Packet getDescriptionPacket() {
        return PacketTypeHandler.populatePacket(new PacketDogBedUpdate(this.xCoord, this.yCoord, this.zCoord, this.woolId, this.woodId));
    }
	
	public void setWoolId(String newId) {
		this.woolId = newId;
	}
	
	public void setWoodId(String newId) {
		this.woodId = newId;
	}
	
	public String getWoolId() {
		return this.woolId;
	}
	
	public String getWoodId() {
		return this.woodId;
	}
}
