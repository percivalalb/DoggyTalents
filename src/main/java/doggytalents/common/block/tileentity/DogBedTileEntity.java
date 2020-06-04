package doggytalents.common.block.tileentity;

import java.util.UUID;

import javax.annotation.Nullable;

import doggytalents.DoggyTalents2;
import doggytalents.DoggyTileEntityTypes;
import doggytalents.api.inferface.IBedMaterial;
import doggytalents.common.block.DogBedRegistry;
import doggytalents.common.entity.DogEntity;
import doggytalents.common.util.NBTUtil;
import doggytalents.common.util.WorldUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.client.model.data.ModelProperty;

public class DogBedTileEntity extends PlacedTileEntity {

    private IBedMaterial casingType = IBedMaterial.NULL;
    private IBedMaterial beddingType = IBedMaterial.NULL;


    public static ModelProperty<IBedMaterial> CASING = new ModelProperty<>();
    public static ModelProperty<IBedMaterial> BEDDING = new ModelProperty<>();
    public static ModelProperty<Direction> FACING = new ModelProperty<>();

    private @Deprecated @Nullable DogEntity dog;
    private @Nullable UUID dogUUID;

    private @Nullable ITextComponent name;
    private @Nullable ITextComponent ownerName;

    public DogBedTileEntity() {
        super(DoggyTileEntityTypes.DOG_BED.get());
    }

    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);

        this.casingType = DogBedRegistry.CASINGS.get(compound.getString("casingId"));
        this.beddingType = DogBedRegistry.BEDDINGS.get(compound.getString("beddingId"));

        this.dogUUID = NBTUtil.getUniqueId(compound, "ownerId");
        this.name = NBTUtil.getTextComponent(compound, "name");
        this.ownerName = NBTUtil.getTextComponent(compound, "ownerName");
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);

        compound.putString("casingId", this.casingType != null ? this.casingType.getSaveId() : "missing");
        compound.putString("beddingId", this.beddingType != null ? this.beddingType.getSaveId() : "missing");

        NBTUtil.putUniqueId(compound, "ownerId", this.dogUUID);
        NBTUtil.putTextComponent(compound, "name", this.name);
        NBTUtil.putTextComponent(compound, "ownerName", this.ownerName);

        return compound;
    }

    public void setCasing(IBedMaterial casingType) {
        this.casingType = casingType;
        this.markDirty();
        this.requestModelDataUpdate();
    }

    public void setBedding(IBedMaterial beddingType) {
        this.beddingType = beddingType;
        this.markDirty();
        this.requestModelDataUpdate();
    }

    public IBedMaterial getCasing() {
        return this.casingType;
    }

    public IBedMaterial getBedding() {
        return this.beddingType;
    }

    @Override
    public IModelData getModelData() {
        return new ModelDataMap.Builder()
                .withProperty(CASING)
                .withProperty(BEDDING)
                .withInitial(FACING, Direction.NORTH)
                .build();
    }

    public void setOwner(@Nullable DogEntity owner) {
        this.setOwner(owner == null ? null : owner.getUniqueID());
        this.ownerName = owner == null ? null : owner.getName();

        this.dog = owner;

    }

    public void setOwner(@Nullable UUID owner) {
        this.dog = null;
        this.dogUUID = owner;

        this.markDirty();

        DoggyTalents2.LOGGER.debug("Set bed owner to {}", owner);
    }

    @Nullable
    public UUID getOwnerUUID() {
        return this.dogUUID;
    }

    @Nullable
    public DogEntity getOwner() {
       return WorldUtil.getCachedEntity(this.world, DogEntity.class, this.dog, this.dogUUID);
    }

    @Nullable
    public ITextComponent getBedName() {
        return this.name;
    }

    @Nullable
    public ITextComponent getOwnerName() {
        return this.ownerName;
    }

    public boolean shouldDisplayName(LivingEntity camera) {
        return true;
    }

    public void setBedName(@Nullable ITextComponent nameIn) {
        this.name = nameIn;
        this.markDirty();
    }

    public void setOwnerName(@Nullable ITextComponent nameIn) {
        this.ownerName = nameIn;
        this.markDirty();
    }
}
