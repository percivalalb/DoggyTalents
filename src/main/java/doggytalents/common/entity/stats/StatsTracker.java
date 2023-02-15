package doggytalents.common.entity.stats;

import com.google.common.collect.Maps;
import doggytalents.common.util.Cache;
import doggytalents.common.util.NBTUtil;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Predicate;

public class StatsTracker {

    private Map<Holder.Reference<EntityType<?>>, Integer> ENTITY_KILLS = Maps.newHashMap();
    private float damageDealt = 0;
    private int distanceOnWater = 0;
    private int distanceInWater = 0;
    private int distanceSprinting = 0;
    private int distanceSneaking = 0;
    private int distanceWalking = 0;
    private int distanceRidden = 0;

    // Cache
    private final Cache<Integer> killCount = Cache.make(this::getTotalKillCountInternal);

    public void writeAdditional(CompoundTag compound) {
        ListTag killList = new ListTag();
        for (Entry<Holder.Reference<EntityType<?>>, Integer> entry : this.ENTITY_KILLS.entrySet()) {
            CompoundTag stats = new CompoundTag();
            NBTUtil.putRegistryValue(stats, "type", entry.getKey());
            stats.putInt("count", entry.getValue());
            killList.add(stats);
        }
        compound.put("entityKills", killList);
        compound.putDouble("damageDealt", this.damageDealt);
        compound.putInt("distanceOnWater", this.distanceOnWater);
        compound.putInt("distanceInWater", this.distanceInWater);
        compound.putInt("distanceSprinting", this.distanceSprinting);
        compound.putInt("distanceSneaking", this.distanceSneaking);
        compound.putInt("distanceWalking", this.distanceWalking);
        compound.putInt("distanceRidden", this.distanceRidden);
    }

    public void readAdditional(CompoundTag compound) {
        ListTag killList = compound.getList("entityKills", Tag.TAG_COMPOUND);
        for (int i = 0; i < killList.size(); i++) {
            CompoundTag stats = killList.getCompound(i);
            Holder.Reference<EntityType<?>> type = NBTUtil.getRegistryDelegate(stats, "type", ForgeRegistries.ENTITY_TYPES);
            this.ENTITY_KILLS.put(type, stats.getInt("count"));
        }
        this.damageDealt = compound.getFloat("damageDealt");
        this.distanceOnWater = compound.getInt("distanceOnWater");
        this.distanceInWater = compound.getInt("distanceInWater");
        this.distanceSprinting = compound.getInt("distanceSprinting");
        this.distanceSneaking = compound.getInt("distanceSneaking");
        this.distanceWalking = compound.getInt("distanceWalking");
        this.distanceRidden = compound.getInt("distanceRidden");
    }

    public int getKillCountFor(EntityType<?> type) {
        return this.ENTITY_KILLS.getOrDefault(type, 0);
    }

    public int getKillCountFor(Predicate<MobCategory> classification) {
        int total = 0;
        for (Entry<Holder.Reference<EntityType<?>>, Integer> entry : this.ENTITY_KILLS.entrySet()) {
            if (classification.test(entry.getKey().get().getCategory())) {
                total += entry.getValue();
            }
        }
        return total;
    }

    private int getTotalKillCountInternal() {
        int total = 0;
        for (Entry<Holder.Reference<EntityType<?>>, Integer> entry : this.ENTITY_KILLS.entrySet()) {
            total += entry.getValue();
        }
        return total;
    }

    public Map<Holder.Reference<EntityType<?>>, Integer> getAllKillCount() {
        return Collections.unmodifiableMap(ENTITY_KILLS);
    }

    public int getTotalKillCount() {
        return this.killCount.get();
    }

    public void incrementKillCount(Entity entity) {
        this.incrementKillCount(entity.getType());
    }

    private void incrementKillCount(EntityType<?> type) {
        this.ENTITY_KILLS.compute(ForgeRegistries.ENTITY_TYPES.getDelegateOrThrow(type), (k, v) -> (v == null ? 0 : v) + 1);
    }

    public void increaseDamageDealt(float damage) {
        this.damageDealt += damage;
    }

    public void increaseDistanceOnWater(int distance) {
        this.distanceOnWater += distance;
    }

    public void increaseDistanceInWater(int distance) {
        this.distanceInWater += distance;
    }

    public void increaseDistanceSprint(int distance) {
        this.distanceSprinting += distance;
    }

    public void increaseDistanceSneaking(int distance) {
        this.distanceSneaking += distance;
    }

    public void increaseDistanceWalk(int distance) {
        this.distanceWalking += distance;
    }

    public void increaseDistanceRidden(int distance) {
        this.distanceRidden += distance;
    }

    

    public float getDamageDealt() {
        return damageDealt;
    }

    public int getDistanceOnWater() {
        return distanceOnWater;
    }

    public int getDistanceInWater() {
        return distanceInWater;
    }

    public int getDistanceSprint() {
        return distanceSprinting;
    }

    public int getDistanceSneaking() {
        return distanceSneaking;
    }

    public int getDistanceWalk() {
        return distanceWalking;
    }

    public int getDistanceRidden() {
        return distanceRidden;
    }

    public void serializeToBuf(FriendlyByteBuf buf) {

        buf.writeFloat(damageDealt);

        buf.writeInt(distanceOnWater);
        buf.writeInt(distanceInWater);
        buf.writeInt(distanceSprinting);
        buf.writeInt(distanceSneaking);
        buf.writeInt(distanceWalking);
        buf.writeInt(distanceRidden);


        int mapSize = this.ENTITY_KILLS.size();
        buf.writeInt(mapSize);
        for (var entry : this.ENTITY_KILLS.entrySet()) {
            var typeId = entry.getKey().key().location();
            var killCount = entry.getValue();
            buf.writeResourceLocation(typeId);
            buf.writeInt(killCount);
        }
    }

    public void deserializeFromBuf(FriendlyByteBuf buf) {

        this.damageDealt = buf.readFloat();
        this.distanceOnWater = buf.readInt();
        this.distanceInWater = buf.readInt();
        this.distanceSprinting = buf.readInt();
        this.distanceSneaking = buf.readInt();
        this.distanceWalking = buf.readInt();
        this.distanceRidden = buf.readInt();

        this.ENTITY_KILLS.clear();
        int mapSize = buf.readInt();
        for (int i = 0; i < mapSize; ++i) {
            var typeId = buf.readResourceLocation();
            var killCount = buf.readInt();
            var type = ForgeRegistries.ENTITY_TYPES.getDelegate(typeId).orElse(null);
            if (type == null) continue;
            this.ENTITY_KILLS.put(type, killCount);
        }
    }

    public void shallowCopyFrom(StatsTracker stats) {
        this.ENTITY_KILLS = stats.ENTITY_KILLS;
        this.damageDealt = stats.damageDealt;
        this.distanceOnWater = stats.distanceOnWater;
        this.distanceInWater = stats.distanceInWater;
        this.distanceSprinting = stats.distanceSprinting;
        this.distanceSneaking = stats.distanceSneaking;
        this.distanceWalking = stats.distanceWalking;
        this.distanceRidden = stats.distanceRidden;
    }
}
