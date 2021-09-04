package doggytalents.common.block.tileentity;

import doggytalents.DoggyTileEntityTypes;
import doggytalents.api.DoggyTalentsAPI;
import doggytalents.api.registry.IBeddingMaterial;
import doggytalents.api.registry.ICasingMaterial;
import doggytalents.common.entity.DogEntity;
import doggytalents.common.storage.DogLocationStorage;
import doggytalents.common.util.NBTUtil;
import doggytalents.common.util.WorldUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.client.model.data.ModelProperty;

import javax.annotation.Nullable;
import java.util.UUID;

public class DogBedTileEntity extends PlacedTileEntity {

    private ICasingMaterial casingType = null;
    private IBeddingMaterial beddingType = null;


    public static ModelProperty<ICasingMaterial> CASING = new ModelProperty<>();
    public static ModelProperty<IBeddingMaterial> BEDDING = new ModelProperty<>();
    public static ModelProperty<Direction> FACING = new ModelProperty<>();

    private @Deprecated @Nullable DogEntity dog;
    private @Nullable UUID dogUUID;

    private @Nullable Component name;
    private @Nullable Component ownerName;

    public DogBedTileEntity(BlockPos pos, BlockState blockState) {
        super(DoggyTileEntityTypes.DOG_BED.get(), pos, blockState);
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);

        this.casingType = NBTUtil.getRegistryValue(compound, "casingId", DoggyTalentsAPI.CASING_MATERIAL);
        this.beddingType = NBTUtil.getRegistryValue(compound, "beddingId", DoggyTalentsAPI.BEDDING_MATERIAL);

        this.dogUUID = NBTUtil.getUniqueId(compound, "ownerId");
        this.name = NBTUtil.getTextComponent(compound, "name");
        this.ownerName = NBTUtil.getTextComponent(compound, "ownerName");
        this.requestModelDataUpdate();
    }

    @Override
    public CompoundTag save(CompoundTag compound) {
        super.save(compound);

        NBTUtil.putRegistryValue(compound, "casingId", this.casingType);
        NBTUtil.putRegistryValue(compound, "beddingId", this.beddingType);

        NBTUtil.putUniqueId(compound, "ownerId", this.dogUUID);
        NBTUtil.putTextComponent(compound, "name", this.name);
        NBTUtil.putTextComponent(compound, "ownerName", this.ownerName);

        return compound;
    }

    public void setCasing(ICasingMaterial casingType) {
        this.casingType = casingType;
        this.setChanged();
        this.requestModelDataUpdate();
    }

    public void setBedding(IBeddingMaterial beddingType) {
        this.beddingType = beddingType;
        this.setChanged();
        this.requestModelDataUpdate();
    }

    public ICasingMaterial getCasing() {
        return this.casingType;
    }

    public IBeddingMaterial getBedding() {
        return this.beddingType;
    }

    @Override
    public IModelData getModelData() {
        return new ModelDataMap.Builder()
                .withInitial(CASING, this.casingType)
                .withInitial(BEDDING, this.beddingType)
                .withInitial(FACING, Direction.NORTH)
                .build();
    }

    public void setOwner(@Nullable DogEntity owner) {
        this.setOwner(owner == null ? null : owner.getUUID());

        this.dog = owner;

    }

    public void setOwner(@Nullable UUID owner) {
        this.dog = null;
        this.dogUUID = owner;

        this.setChanged();
    }

    @Nullable
    public UUID getOwnerUUID() {
        return this.dogUUID;
    }

    @Nullable
    public DogEntity getOwner() {
       return WorldUtil.getCachedEntity(this.level, DogEntity.class, this.dog, this.dogUUID);
    }

    @Nullable
    public Component getBedName() {
        return this.name;
    }

    @Nullable
    public Component getOwnerName() {
        if (this.dogUUID == null || this.level == null) { return null; }

        Component name = DogLocationStorage
                .get(this.level)
                .getData(this.dogUUID)
                .getName(this.level);

        if (name != null) {
            this.ownerName = name;
        }

        return this.ownerName;
    }

    public boolean shouldDisplayName(LivingEntity camera) {
        return true;
    }

    public void setBedName(@Nullable Component nameIn) {
        this.name = nameIn;
        this.setChanged();
    }
}
