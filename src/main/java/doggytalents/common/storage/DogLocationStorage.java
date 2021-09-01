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
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.minecraft.world.level.saveddata.SavedData;

public class DogLocationStorage extends SavedData {

    private Map<UUID, DogLocationData> locationDataMap = Maps.newConcurrentMap();

    public DogLocationStorage() {}

    public static DogLocationStorage get(Level world) {
        if (!(world instanceof ServerLevel)) {
            throw new RuntimeException("Tried to access dog location data from the client. This should not happen...");
        }

        ServerLevel overworld = world.getServer().getLevel(Level.OVERWORLD);

        DimensionDataStorage storage = overworld.getDataStorage();
        return storage.computeIfAbsent(DogLocationStorage::load, DogLocationStorage::new, Constants.STORAGE_DOG_LOCATION);
    }

    public Stream<DogLocationData> getDogs(LivingEntity owner) {
        UUID ownerId = owner.getUUID();

        return this.locationDataMap.values().stream()
                .filter(data -> ownerId.equals(data.getOwnerId()));
    }

    public Stream<DogLocationData> getDogs(LivingEntity owner, ResourceKey<Level> key) {
        UUID ownerId = owner.getUUID();

        return this.locationDataMap.values().stream()
                .filter(data -> ownerId.equals(data.getOwnerId()))
                .filter(data -> key.equals(data.getDimension()));
    }

    @Nullable
    public DogLocationData getData(DogEntity dogIn) {
        return getData(dogIn.getUUID());
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
        return remove(dogIn.getUUID());
    }

    @Nullable
    public DogLocationData getOrCreateData(DogEntity dogIn) {
        UUID uuid = dogIn.getUUID();

        return this.locationDataMap.computeIfAbsent(uuid, ($) -> {
            this.setDirty();
            return DogLocationData.from(this, dogIn);
        });
    }

    @Nullable
    public DogLocationData remove(UUID uuid) {
        if (this.locationDataMap.containsKey(uuid)) {
            DogLocationData storage = this.locationDataMap.remove(uuid);

            // Mark dirty so changes are saved
            this.setDirty();
            return storage;
        }

        return null;
    }

    @Nullable
    public DogLocationData putData(DogEntity dogIn) {
        UUID uuid = dogIn.getUUID();

        DogLocationData storage = new DogLocationData(this, uuid);

        this.locationDataMap.put(uuid, storage);
        // Mark dirty so changes are saved
        this.setDirty();
        return storage;
    }

    public Set<UUID> getAllUUID() {
        return Collections.unmodifiableSet(this.locationDataMap.keySet());
    }

    public Collection<DogLocationData> getAll() {
        return Collections.unmodifiableCollection(this.locationDataMap.values());
    }

    public static DogLocationStorage load(CompoundTag nbt) {
        DogLocationStorage store = new DogLocationStorage();
        store.locationDataMap.clear();

        ListTag list = nbt.getList("locationData", TAG_COMPOUND);

        // Old style
        if (list.isEmpty()) {
            list = nbt.getList("dog_locations", TAG_COMPOUND);
        }

        for (int i = 0; i < list.size(); ++i) {
            CompoundTag locationCompound = list.getCompound(i);

            UUID uuid = NBTUtil.getUniqueId(locationCompound, "uuid");

            // Old style
            if (uuid == null) {
                uuid = NBTUtil.getUniqueId(locationCompound, "entityId");
            }

            DogLocationData locationData = new DogLocationData(store, uuid);
            locationData.read(locationCompound);

            if (uuid == null) {
                DoggyTalents2.LOGGER.info("Failed to load dog location data. Please report to mod author...");
                DoggyTalents2.LOGGER.info(locationData);
                continue;
            }

            store.locationDataMap.put(uuid, locationData);
        }

        return store;
    }

    @Override
    public CompoundTag save(CompoundTag compound) {
        ListTag list = new ListTag();

        for (Entry<UUID, DogLocationData> entry : this.locationDataMap.entrySet()) {
            CompoundTag locationCompound = new CompoundTag();

            DogLocationData locationData = entry.getValue();
            NBTUtil.putUniqueId(locationCompound, "uuid", entry.getKey());
            locationData.write(locationCompound);

            list.add(locationCompound);
        }

        compound.put("locationData", list);

        return compound;
    }

}
