package doggytalents.tileentity;

import org.apache.logging.log4j.util.Strings;

import doggytalents.ModBlocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileEntityDogBed extends TileEntity {
	
	private String casingId = Strings.EMPTY;
	private String beddingId = Strings.EMPTY;
	
	public TileEntityDogBed() {
		super(ModBlocks.TILE_DOG_BED);
	}
	
	@Override
	public void read(NBTTagCompound tag) {
		super.read(tag);
		this.casingId = tag.getString("casingId");
		this.beddingId = tag.getString("beddingId");
    }

    @Override
    public NBTTagCompound write(NBTTagCompound tag) {
		super.write(tag);
		tag.putString("casingId", this.casingId);
		tag.putString("beddingId", this.beddingId);
		return tag;
	}
	
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(this.pos, 0, this.getUpdateTag());
	}
	
	@Override
	public void handleUpdateTag(NBTTagCompound tag) {
		super.handleUpdateTag(tag);
	}


	@Override
	public NBTTagCompound getUpdateTag() {
		return write(new NBTTagCompound());
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		this.read(pkt.getNbtCompound());
	}
	
	public void setCasingId(String newId) {
		this.casingId = newId;
	}
	
	public void setBeddingId(String newId) {
		this.beddingId = newId;
	}
	
	public String getCasingId() {
		return this.casingId;
	}
	
	public String getBeddingId() {
		return this.beddingId;
	}
}
