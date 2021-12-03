package doggytalents.common.storage;

import com.google.common.collect.Maps;
import doggytalents.DoggyTalents2;
import doggytalents.common.entity.DogEntity;
import doggytalents.common.lib.Constants;
import doggytalents.common.util.NBTUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Stream;

public class DogRespawnStorage extends SavedData {

    private Map<UUID, DogRespawnData> respawnDataMap = Maps.newConcurrentMap();

    public DogRespawnStorage() {}

    public static DogRespawnStorage get(Level world) {
        if (!(world instanceof ServerLevel)) {
            throw new RuntimeException("Tried to access dog respawn data from the client. This should not happen...");
        }

        ServerLevel overworld = world.getServer().getLevel(Level.OVERWORLD);

        DimensionDataStorage storage = overworld.getDataStorage();
        return storage.computeIfAbsent(DogRespawnStorage::load, DogRespawnStorage::new, Constants.STORAGE_DOG_RESPAWN);
    }

    public Stream<DogRespawnData> getDogs(@Nonnull UUID ownerId) {
        return this.respawnDataMap.values().stream()
                .filter(data -> ownerId.equals(data.getOwnerId()));
    }

    public Stream<DogRespawnData> getDogs(@Nonnull String ownerName) {
        return this.respawnDataMap.values().stream()
                .filter(data -> ownerName.equals(data.getOwnerName()));
    }

    @Nullable
    public DogRespawnData getData(UUID uuid) {
        if (this.respawnDataMap.containsKey(uuid)) {
            return this.respawnDataMap.get(uuid);
        }

        return null;
    }

    @Nullable
    public DogRespawnData remove(UUID uuid) {
        if (this.respawnDataMap.containsKey(uuid)) {
            DogRespawnData storage = this.respawnDataMap.remove(uuid);

            // Mark dirty so changes are saved
            this.setDirty();
            return storage;
        }

        return null;
    }

    @Nullable
    public DogRespawnData putData(DogEntity dogIn) {
        UUID uuid = dogIn.getUUID();

        DogRespawnData storage = new DogRespawnData(this, uuid);
        storage.populate(dogIn);

        this.respawnDataMap.put(uuid, storage);
        // Mark dirty so changes are saved
        this.setDirty();
        return storage;
    }

    public Set<UUID> getAllUUID() {
        return Collections.unmodifiableSet(this.respawnDataMap.keySet());
    }

    public Collection<DogRespawnData> getAll() {
        return Collections.unmodifiableCollection(this.respawnDataMap.values());
    }

    public static DogRespawnStorage load(CompoundTag nbt) {
        DogRespawnStorage store = new DogRespawnStorage();
        store.respawnDataMap.clear();

        ListTag list = nbt.getList("respawnData", Tag.TAG_COMPOUND);

        for (int i = 0; i < list.size(); ++i) {
            CompoundTag respawnCompound = list.getCompound(i);

            UUID uuid = NBTUtil.getUniqueId(respawnCompound, "uuid");
            DogRespawnData respawnData = new DogRespawnData(store, uuid);
            respawnData.read(respawnCompound);

            if (uuid == null) {
                DoggyTalents2.LOGGER.info("Failed to load dog respawn data. Please report to mod author...");
                DoggyTalents2.LOGGER.info(respawnData);
                continue;
            }

            store.respawnDataMap.put(uuid, respawnData);
        }

        return store;
    }

    @Override
    public CompoundTag save(CompoundTag compound) {
        ListTag list = new ListTag();

        for (Map.Entry<UUID, DogRespawnData> entry : this.respawnDataMap.entrySet()) {
            CompoundTag respawnCompound = new CompoundTag();

            DogRespawnData respawnData = entry.getValue();
            NBTUtil.putUniqueId(respawnCompound, "uuid", entry.getKey());
            respawnData.write(respawnCompound);

            list.add(respawnCompound);
        }

        compound.put("respawnData", list);

        return compound;
    }

}
