package doggytalents.common.storage;

import static net.minecraftforge.common.util.Constants.NBT.*;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import com.google.common.collect.Maps;

import doggytalents.DoggyTalents2;
import doggytalents.common.entity.DogEntity;
import doggytalents.common.lib.Constants;
import doggytalents.common.util.NBTUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraft.world.storage.WorldSavedData;

public class DogLocationStorage extends WorldSavedData {

    private Map<UUID, DogLocationData> locationDataMap = Maps.newConcurrentMap();

    public DogLocationStorage() {
        super(Constants.STORAGE_DOG_LOCATION);
    }

    public static DogLocationStorage get(World world) {
        if (!(world instanceof ServerWorld)) {
            throw new RuntimeException("Tried to access dog location data from the client. This should not happen...");
        }

        ServerWorld overworld = world.getServer().getWorld(DimensionType.OVERWORLD);

        DimensionSavedDataManager storage = overworld.getSavedData();
        return storage.getOrCreate(DogLocationStorage::new, Constants.STORAGE_DOG_LOCATION);
    }

    public Stream<DogLocationData> getDogs(LivingEntity owner) {
        UUID ownerId = owner.getUniqueID();

        return this.locationDataMap.values().stream()
                .filter(data -> ownerId.equals(data.getOwnerId()));
    }

    public Stream<DogLocationData> getDogs(LivingEntity owner, DimensionType type) {
        UUID ownerId = owner.getUniqueID();

        return this.locationDataMap.values().stream()
                .filter(data -> ownerId.equals(data.getOwnerId()))
                .filter(data -> type.equals(data.getDimension()));
    }

    @Nullable
    public DogLocationData getData(DogEntity dogIn) {
        return getData(dogIn.getUniqueID());
    }

    @Nullable
    public DogLocationData getData(UUID uuid) {
        if (this.locationDataMap.containsKey(uuid)) {
            return this.locationDataMap.get(uuid);
        }

        return null;
    }

    @Nullable
    public DogLocationData remove(DogEntity dogIn) {
        return remove(dogIn.getUniqueID());
    }

    @Nullable
    public DogLocationData getOrCreateData(DogEntity dogIn) {
        UUID uuid = dogIn.getUniqueID();

        return this.locationDataMap.computeIfAbsent(uuid, ($) -> {
            this.markDirty();
            return DogLocationData.from(this, dogIn);
        });
    }

    @Nullable
    public DogLocationData remove(UUID uuid) {
        if (this.locationDataMap.containsKey(uuid)) {
            DogLocationData storage = this.locationDataMap.remove(uuid);

            // Mark dirty so changes are saved
            this.markDirty();
            return storage;
        }

        return null;
    }

    @Nullable
    public DogLocationData putData(DogEntity dogIn) {
        UUID uuid = dogIn.getUniqueID();

        DogLocationData storage = new DogLocationData(this, uuid);

        this.locationDataMap.put(uuid, storage);
        // Mark dirty so changes are saved
        this.markDirty();
        return storage;
    }

    public Set<UUID> getAllUUID() {
        return Collections.unmodifiableSet(this.locationDataMap.keySet());
    }

    public Collection<DogLocationData> getAll() {
        return Collections.unmodifiableCollection(this.locationDataMap.values());
    }

    @Override
    public void read(CompoundNBT nbt) {
        this.locationDataMap.clear();

        ListNBT list = nbt.getList("locationData", TAG_COMPOUND);

        // Old style
        if (list.isEmpty()) {
            list = nbt.getList("dog_locations", TAG_COMPOUND);
        }

        for (int i = 0; i < list.size(); ++i) {
            CompoundNBT locationCompound = list.getCompound(i);

            UUID uuid = NBTUtil.getUniqueId(locationCompound, "uuid");

            // Old style
            if (uuid == null) {
                uuid = NBTUtil.getUniqueId(locationCompound, "entityId");
            }

            DogLocationData locationData = new DogLocationData(this, uuid);
            locationData.read(locationCompound);

            if (uuid == null) {
                DoggyTalents2.LOGGER.info("Failed to load dog location data. Please report to mod author...");
                DoggyTalents2.LOGGER.info(locationData);
                continue;
            }

            this.locationDataMap.put(uuid, locationData);
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        ListNBT list = new ListNBT();

        for (Entry<UUID, DogLocationData> entry : this.locationDataMap.entrySet()) {
            CompoundNBT locationCompound = new CompoundNBT();

            DogLocationData locationData = entry.getValue();
            NBTUtil.putUniqueId(locationCompound, "uuid", entry.getKey());
            locationData.write(locationCompound);

            list.add(locationCompound);
        }

        compound.put("locationData", list);

        return compound;
    }

}
