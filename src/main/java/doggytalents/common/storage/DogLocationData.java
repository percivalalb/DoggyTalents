package doggytalents.common.storage;

import java.util.Optional;
import java.util.UUID;

import javax.annotation.Nullable;

import doggytalents.DoggyAccessories;
import doggytalents.DoggyItems;
import doggytalents.api.feature.EnumGender;
import doggytalents.common.entity.DogEntity;
import doggytalents.common.util.NBTUtil;
import doggytalents.common.util.Util;
import doggytalents.common.util.WorldUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Hand;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Constants;

public class DogLocationData implements IDogData {

    private final DogLocationStorage storage;
    private final UUID uuid;
    private @Nullable UUID ownerId;

    // Location data
    private @Nullable Vector3d position;
    private @Nullable RegistryKey<World> dimension;

    // Other saved data
    private @Nullable ITextComponent name;
    private @Nullable ITextComponent ownerName;
    private @Nullable EnumGender gender;
    private boolean hasRadarCollar;

    // Cached objects
    private DogEntity dog;
    private LivingEntity owner;

    protected DogLocationData(DogLocationStorage storageIn, UUID uuid) {
        this.storage = storageIn;
        this.uuid = uuid;
    }

    @Override
    public UUID getDogId() {
        return this.uuid;
    }

    @Override
    @Nullable
    public UUID getOwnerId() {
        return this.ownerId;
    }

    @Override
    public String getDogName() {
        return this.name == null ? "" : this.name.getString();
    }

    @Override
    public String getOwnerName() {
        return this.ownerName == null ? "" : this.ownerName.getString();
    }

    public void populate(DogEntity dogIn) {
        this.update(dogIn);
    }

    public void update(DogEntity dogIn) {
        this.ownerId = dogIn.getOwnerId();
        this.position = dogIn.getPositionVec();
        this.dimension = dogIn.world.getDimensionKey();

        this.name = dogIn.getName();
        this.ownerName = dogIn.getOwnersName().orElse(null);
        this.gender = dogIn.getGender();
        this.hasRadarCollar = dogIn.getAccessory(DoggyAccessories.RADIO_BAND.get()).isPresent();

        this.dog = dogIn;
        this.storage.markDirty();
    }


    public void read(CompoundNBT compound) {
        this.ownerId = NBTUtil.getUniqueId(compound, "ownerId");
        this.position = NBTUtil.getVector3d(compound);
        this.dimension = RegistryKey.getOrCreateKey(Registry.WORLD_KEY, NBTUtil.getResourceLocation(compound, "dimension"));
        this.name = NBTUtil.getTextComponent(compound, "name_text_component");
        if (compound.contains("gender", Constants.NBT.TAG_STRING)) {
            this.gender = EnumGender.bySaveName(compound.getString("gender"));
        }
        this.hasRadarCollar = compound.getBoolean("collar");
    }

    public CompoundNBT write(CompoundNBT compound) {
        NBTUtil.putUniqueId(compound, "ownerId", this.ownerId);
        NBTUtil.putVector3d(compound, this.position);
        NBTUtil.putResourceLocation(compound, "dimension", this.dimension.getLocation());
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
    public Optional<LivingEntity> getOwner(@Nullable World worldIn) {
        if (worldIn == null) {
            return Optional.ofNullable(this.owner);
        }

        MinecraftServer server = worldIn.getServer();
        if (server == null) {
            throw new IllegalArgumentException("worldIn must be of ServerWorld");
        }

        for (ServerWorld world : server.getWorlds()) {
            LivingEntity possibleOwner = WorldUtil.getCachedEntity(world, LivingEntity.class, this.owner, this.uuid);
            if (possibleOwner != null) {
                this.owner = possibleOwner;
                return Optional.of(this.owner);
            }
        }

        this.owner = null;
        return Optional.empty();
    }

    @Nullable
    public Optional<DogEntity> getDog(@Nullable World worldIn) {
        if (worldIn == null) {
            return Optional.ofNullable(this.dog);
        }

        MinecraftServer server = worldIn.getServer();
        if (server == null) {
            throw new IllegalArgumentException("worldIn must be of ServerWorld");
        }

        for (ServerWorld world : server.getWorlds()) {
            DogEntity possibleDog = WorldUtil.getCachedEntity(world, DogEntity.class, this.dog, this.uuid);
            if (possibleDog != null) {
                this.dog = possibleDog;
                return Optional.of(this.dog);
            }
        }

        this.dog = null;
        return Optional.empty();
    }

    public boolean shouldDisplay(World worldIn, PlayerEntity playerIn, Hand handIn) {
        return this.hasRadarCollar || playerIn.isCreative() || playerIn.getHeldItem(handIn).getItem() == DoggyItems.CREATIVE_RADAR.get();
    }

    @Nullable
    public ITextComponent getName(@Nullable World worldIn) {
        return this.getDog(worldIn).map(DogEntity::getDisplayName).orElse(this.name);
    }

    @Nullable
    public Vector3d getPos(@Nullable ServerWorld worldIn) {
        return this.getDog(worldIn).map(DogEntity::getPositionVec).orElse(this.position);
    }

    @Nullable
    public Vector3d getPos() {
        return this.position;
    }

    @Nullable
    public RegistryKey<World> getDimension() {
        return this.dimension;
    }

    @Override
    public String toString() {
        return "DogLocationData [uuid=" + uuid + ", owner=" + ownerId + ", position=" + position + ", dimension=" + dimension + ", name=" + name + ", gender=" + gender + ", hasRadarCollar=" + hasRadarCollar + "]";
    }


}
