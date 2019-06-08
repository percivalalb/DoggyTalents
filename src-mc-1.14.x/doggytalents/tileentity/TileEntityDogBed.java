package doggytalents.tileentity;

import doggytalents.ModTileEntities;
import doggytalents.api.registry.BedMaterial;
import doggytalents.api.registry.DogBedRegistry;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;

public class TileEntityDogBed extends TileEntity {
	
	private BedMaterial casingId = BedMaterial.NULL;
	private BedMaterial beddingId = BedMaterial.NULL;
	
	public TileEntityDogBed() {
		super(ModTileEntities.DOG_BED);
	}
	
	@Override
	public void read(CompoundNBT tag) {
		super.read(tag);
		this.casingId = DogBedRegistry.CASINGS.getFromString(tag.getString("casingId"));
		this.beddingId = DogBedRegistry.BEDDINGS.getFromString(tag.getString("beddingId"));
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
		super.write(tag);
		tag.putString("casingId", this.casingId != null ? this.casingId.key : "missing");
		tag.putString("beddingId", this.beddingId != null ? this.beddingId.key : "missing");
		return tag;
	}
	
	@Override
	public SUpdateTileEntityPacket getUpdatePacket() {
		return new SUpdateTileEntityPacket(this.pos, 0, this.getUpdateTag());
	}
	
	@Override
	public void handleUpdateTag(CompoundNBT tag) {
		super.handleUpdateTag(tag);
	}

	@Override
	public CompoundNBT getUpdateTag() {
		return write(new CompoundNBT());
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
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
