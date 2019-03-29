package doggytalents.tileentity;

import doggytalents.ModBlocks;
import doggytalents.api.registry.BedMaterial;
import doggytalents.api.registry.DogBedRegistry;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileEntityDogBed extends TileEntity {
	
	private BedMaterial casingId = BedMaterial.NULL;
	private BedMaterial beddingId = BedMaterial.NULL;
	
	public TileEntityDogBed() {
		super(ModBlocks.TILE_DOG_BED);
	}
	
	@Override
	public void read(NBTTagCompound tag) {
		super.read(tag);
		this.casingId = DogBedRegistry.CASINGS.getFromString(tag.getString("casingId"));
		this.beddingId = DogBedRegistry.BEDDINGS.getFromString(tag.getString("beddingId"));
    }

    @Override
    public NBTTagCompound write(NBTTagCompound tag) {
		super.write(tag);
		tag.putString("casingId", this.casingId != null ? this.casingId.key : "missing");
		tag.putString("beddingId", this.beddingId != null ? this.beddingId.key : "missing");
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
	
	public void setCasingId(BedMaterial newId) {
		this.casingId = newId;
	}
	
	public void setBeddingId(BedMaterial newId) {
		this.beddingId = newId;
	}
	
	public BedMaterial getCasingId() {
		return this.casingId;
	}
	
	public BedMaterial getBeddingId() {
		return this.beddingId;
	}
}
