package doggytalents.common.storage;

import java.util.UUID;

import javax.annotation.Nullable;

import doggytalents.DoggyAccessories;
import doggytalents.DoggyItems;
import doggytalents.api.feature.EnumGender;
import doggytalents.common.entity.DogEntity;
import doggytalents.common.util.NBTUtil;
import doggytalents.common.util.WorldUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Constants;

public class DogLocationData {

    private final DogLocationStorage storage;
    private final UUID uuid;
    private @Nullable UUID ownerId;

    // Location data
    private @Nullable Vec3d position;
    private @Nullable ResourceLocation dimension;

    // Other saved data
    private @Nullable ITextComponent name;
    private @Nullable EnumGender gender;
    private boolean hasRadarCollar;

    // Cached objects
    private DogEntity dog;
    private LivingEntity owner;
    private DimensionType dimensionType;

    protected DogLocationData(DogLocationStorage storageIn, UUID uuid) {
        this.storage = storageIn;
        this.uuid = uuid;
    }

    public UUID getId() {
        return this.uuid;
    }

    @Nullable
    public UUID getOwnerId() {
        return this.ownerId;
    }

    public void populate(DogEntity dogIn) {
        this.update(dogIn);
    }

    public void update(DogEntity dogIn) {
        this.ownerId = dogIn.getOwnerId();
        this.position = dogIn.getPositionVector();
        this.dimension = dogIn.world.getDimension().getType().getRegistryName();

        this.name = dogIn.getName();
        this.gender = dogIn.getGender();
        this.hasRadarCollar = dogIn.getAccessory(DoggyAccessories.RADIO_BAND).isPresent();

        this.dog = dogIn;
        this.storage.markDirty();
    }


    public void read(CompoundNBT compound) {
        this.ownerId = NBTUtil.getUniqueId(compound, "ownerId");
        this.position = NBTUtil.getVec3d(compound);
        this.dimension = NBTUtil.getResourceLocation(compound, "dimension");
        this.name = NBTUtil.getTextComponent(compound, "name_text_component");
        if (compound.contains("gender", Constants.NBT.TAG_STRING)) {
            this.gender = EnumGender.bySaveName(compound.getString("gender"));
        }
        this.hasRadarCollar = compound.getBoolean("collar");
    }

    public CompoundNBT write(CompoundNBT compound) {
        NBTUtil.putUniqueId(compound, "ownerId", this.ownerId);
        NBTUtil.putVec3d(compound, this.position);
        NBTUtil.putResourceLocation(compound, "dimension", this.dimension);
        NBTUtil.putTextComponent(compound, "name_text_component", this.name);
        if (this.gender != null) {
            compound.putString("gender", this.gender.getSaveName());
        }
        compound.putBoolean("collar", this.hasRadarCollar);
        return compound;
    }

    public static DogLocationData from(DogLocationStorage storageIn, DogEntity dogIn) {
        DogLocationData locationData = new DogLocationData(storageIn, dogIn.getUniqueID());
        locationData.populate(dogIn);
        return locationData;
    }

    @Nullable
    public LivingEntity getOwner(ServerWorld worldIn) {
        boolean flag = false;

        for (ServerWorld world : worldIn.getServer().getWorlds()) {
            LivingEntity possibleOwner = WorldUtil.getCachedEntity(world, LivingEntity.class, this.owner, this.ownerId);
            if (possibleOwner != null) {
                this.owner = possibleOwner;
                flag = true;
                break;
            }
        }

        if (!flag) {
            this.owner = null;
        }

        return this.owner;
    }

    @Nullable
    public DogEntity getDog(ServerWorld worldIn) {
        boolean flag = false;

        for (ServerWorld world : worldIn.getServer().getWorlds()) {
            DogEntity possibleDog = WorldUtil.getCachedEntity(world, DogEntity.class, this.dog, this.uuid);
            if (possibleDog != null) {
                this.dog = possibleDog;
                flag = true;
                break;
            }
        }

        if (!flag) {
            this.dog = null;
        }

        return this.dog;
    }

    public boolean shouldDisplay(World worldIn, PlayerEntity playerIn, Hand handIn) {
        return this.hasRadarCollar || playerIn.isCreative() || playerIn.getHeldItem(handIn).getItem() == DoggyItems.CREATIVE_RADAR.get();
    }

    @Nullable
    public ITextComponent getName(World worldIn) {
        return this.name;
    }

    @Nullable
    public Vec3d getPos() {
        return this.position;
    }

    @Nullable
    public DimensionType getDimension() {
        if (this.dimensionType == null || !this.dimensionType.getRegistryName().equals(this.dimension)) {
            this.dimensionType = Registry.DIMENSION_TYPE.getValue(this.dimension).orElseGet(null);
        }

        return this.dimensionType;
    }

    @Override
    public String toString() {
        return "DogLocationData [uuid=" + uuid + ", owner=" + ownerId + ", position=" + position + ", dimension=" + dimension + ", name=" + name + ", gender=" + gender + ", hasRadarCollar=" + hasRadarCollar + "]";
    }


}
