package doggytalents.common.storage;

import static net.minecraftforge.common.util.Constants.NBT.*;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.Maps;

import doggytalents.DoggyTalents2;
import doggytalents.common.entity.DogEntity;
import doggytalents.common.lib.Constants;
import doggytalents.common.util.NBTUtil;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraft.world.storage.WorldSavedData;

public class DogRespawnStorage extends WorldSavedData {

    private Map<UUID, DogRespawnData> respawnDataMap = Maps.newConcurrentMap();

    public DogRespawnStorage() {
        super(Constants.STORAGE_DOG_RESPAWN);
    }

    public static DogRespawnStorage get(World world) {
        if (!(world instanceof ServerWorld)) {
            throw new RuntimeException("Tried to access dog respawn data from the client. This should not happen...");
        }

        ServerWorld overworld = world.getServer().getLevel(World.OVERWORLD);

        DimensionSavedDataManager storage = overworld.getDataStorage();
        return storage.computeIfAbsent(DogRespawnStorage::new, Constants.STORAGE_DOG_RESPAWN);
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

    @Override
    public void load(CompoundNBT nbt) {
        this.respawnDataMap.clear();

        ListNBT list = nbt.getList("respawnData", TAG_COMPOUND);

        for (int i = 0; i < list.size(); ++i) {
            CompoundNBT respawnCompound = list.getCompound(i);

            UUID uuid = NBTUtil.getUniqueId(respawnCompound, "uuid");
            DogRespawnData respawnData = new DogRespawnData(this, uuid);
            respawnData.read(respawnCompound);

            if (uuid == null) {
                DoggyTalents2.LOGGER.info("Failed to load dog respawn data. Please report to mod author...");
                DoggyTalents2.LOGGER.info(respawnData);
                continue;
            }

            this.respawnDataMap.put(uuid, respawnData);
        }
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        ListNBT list = new ListNBT();

        for (Map.Entry<UUID, DogRespawnData> entry : this.respawnDataMap.entrySet()) {
            CompoundNBT respawnCompound = new CompoundNBT();

            DogRespawnData respawnData = entry.getValue();
            NBTUtil.putUniqueId(respawnCompound, "uuid", entry.getKey());
            respawnData.write(respawnCompound);

            list.add(respawnCompound);
        }

        compound.put("respawnData", list);

        return compound;
    }

}
