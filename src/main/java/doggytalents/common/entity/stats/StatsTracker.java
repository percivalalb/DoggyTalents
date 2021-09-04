package doggytalents.common.entity.stats;

import com.google.common.collect.Maps;
import doggytalents.common.util.Cache;
import doggytalents.common.util.NBTUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Predicate;

public class StatsTracker {

    private Map<EntityType<?>, Integer> ENTITY_KILLS = Maps.newHashMap();
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
        for (Entry<EntityType<?>, Integer> entry : this.ENTITY_KILLS.entrySet()) {
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
        ListTag killList = compound.getList("entityKills", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < killList.size(); i++) {
            CompoundTag stats = killList.getCompound(i);
            EntityType<?> type = NBTUtil.getRegistryValue(stats, "type", ForgeRegistries.ENTITIES);
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
        for (Entry<EntityType<?>, Integer> entry : this.ENTITY_KILLS.entrySet()) {
            if (classification.test(entry.getKey().getCategory())) {
                total += entry.getValue();
            }
        }
        return total;
    }

    private int getTotalKillCountInternal() {
        int total = 0;
        for (Entry<EntityType<?>, Integer> entry : this.ENTITY_KILLS.entrySet()) {
            total += entry.getValue();
        }
        return total;
    }

    public int getTotalKillCount() {
        return this.killCount.get();
    }

    public void incrementKillCount(Entity entity) {
        this.incrementKillCount(entity.getType());
    }

    private void incrementKillCount(EntityType<?> type) {
        this.ENTITY_KILLS.compute(type, (k, v) -> (v == null ? 0 : v) + 1);
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
}
