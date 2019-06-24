package doggytalents.tileentity;

import doggytalents.ModTileEntities;
import doggytalents.api.inferface.IBedMaterial;
import doggytalents.api.registry.DogBedRegistry;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.client.model.ModelDataManager;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.client.model.data.ModelProperty;

public class TileEntityDogBed extends TileEntity {
    
    private IBedMaterial casingId = IBedMaterial.NULL;
    private IBedMaterial beddingId = IBedMaterial.NULL;
    
    
    public static ModelProperty<IBedMaterial> CASING = new ModelProperty<IBedMaterial>();
    public static ModelProperty<IBedMaterial> BEDDING = new ModelProperty<IBedMaterial>();
    public static ModelProperty<Direction> FACING = new ModelProperty<Direction>();
    
    public TileEntityDogBed() {
        super(ModTileEntities.DOG_BED);
    }
    
    @Override
    public void read(CompoundNBT tag) {
        super.read(tag);
        this.casingId = DogBedRegistry.CASINGS.get(tag.getString("casingId"));
        this.beddingId = DogBedRegistry.BEDDINGS.get(tag.getString("beddingId"));
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        super.write(tag);
        tag.putString("casingId", this.casingId != null ? this.casingId.getSaveId() : "missing");
        tag.putString("beddingId", this.beddingId != null ? this.beddingId.getSaveId() : "missing");
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
    
    public void setCasingId(IBedMaterial newId) {
        this.casingId = newId;
        this.markDirty();
        if(this.world.isRemote) {
            ModelDataManager.requestModelDataRefresh(this);
            this.world.markForRerender(this.getPos());
        }
    }
    
    public void setBeddingId(IBedMaterial newId) {
        this.beddingId = newId;
        this.markDirty();
        if(this.world.isRemote) {
            ModelDataManager.requestModelDataRefresh(this);
            this.world.markForRerender(this.getPos());
        }
    }
    
    public IBedMaterial getCasingId() {
        return this.casingId;
    }
    
    public IBedMaterial getBeddingId() {
        return this.beddingId;
    }
    
    @Override
    public IModelData getModelData() {
        return new ModelDataMap.Builder().withProperty(CASING).withProperty(BEDDING).withInitial(FACING, Direction.NORTH).build();
    }
}
