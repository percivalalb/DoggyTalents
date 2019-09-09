package doggytalents.entity.features;

import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Predicate;

import com.google.common.collect.Maps;

import doggytalents.api.feature.IStatsFeature;
import doggytalents.entity.EntityDog;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.registries.ForgeRegistries;

public class StatsFeature extends DogFeature implements IStatsFeature {

    private Map<EntityType<?>, Integer> ENTITY_KILLS = Maps.newHashMap();
    private double damageDealt = 0;
    private int distanceOnWater = 0;
    private int distanceInWater = 0;
    private int distanceSprinting = 0;
    private int distanceSneaking = 0;
    private int distanceWalking = 0;
    private int distanceRidden = 0;
    
    public StatsFeature(EntityDog dogIn) {
        super(dogIn);
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        ListNBT killList = new ListNBT();
        for(Entry<EntityType<?>, Integer> entry : this.ENTITY_KILLS.entrySet()) {
            CompoundNBT stats = new CompoundNBT();
            stats.putString("type", ForgeRegistries.ENTITIES.getKey(entry.getKey()).toString());
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
    
    @Override
    public void readAdditional(CompoundNBT compound) {
        ListNBT killList = compound.getList("entityKills", Constants.NBT.TAG_COMPOUND);
        for(int i = 0; i < killList.size(); i++) {
            CompoundNBT stats = killList.getCompound(i);
            ResourceLocation typeLoc = new ResourceLocation(stats.getString("type"));
            if(ForgeRegistries.ENTITIES.containsKey(typeLoc)) {
                this.ENTITY_KILLS.put(ForgeRegistries.ENTITIES.getValue(typeLoc), stats.getInt("count"));
            }
        }
        this.damageDealt = compound.getDouble("damageDealt");
        this.distanceOnWater = compound.getInt("distanceOnWater");
        this.distanceInWater = compound.getInt("distanceInWater");
        this.distanceSprinting = compound.getInt("distanceSprinting");
        this.distanceSneaking = compound.getInt("distanceSneaking");
        this.distanceWalking = compound.getInt("distanceWalking");
        this.distanceRidden = compound.getInt("distanceRidden");
    }
    
    @Override
    public void tick() {
        //if(this.dog.ticksExisted % 20 == 0 && this.dog.isServerWorld()) {
        //    DoggyTalentsMod.LOGGER.info("OnWater:{} InWater{} {} Walking:{} Ridden:{}", this.distanceOnWater, this.distanceInWater, this.distanceSneaking, this.distanceWalking, this.distanceRidden);
        //}
    }
    
    public int getKillCountFor(EntityType<?> type) {
        return this.ENTITY_KILLS.getOrDefault(type, 0);
    }
    
    public int getKillCountFor(Predicate<EntityClassification> classification) {
        int total = 0;
        for(Entry<EntityType<?>, Integer> entry : this.ENTITY_KILLS.entrySet()) {
            if(classification.test(entry.getKey().getClassification())) {
                total += entry.getValue();
            }
        }
        return total;
    }
    
    public int getTotalKillCount() {
        int total = 0;
        for(Entry<EntityType<?>, Integer> entry : this.ENTITY_KILLS.entrySet()) {
            total += entry.getValue();
        }
        return total;
    }
    
    public void incrementKillCount(Entity entity) {
        this.incrementKillCount(entity.getType());
    }
    
    private void incrementKillCount(EntityType<?> type) {
        this.ENTITY_KILLS.put(type, this.getKillCountFor(type) + 1);
    }
    
    public void increaseDamageDealt(double damage) {
        this.damageDealt += damage;
    }

    public void increaseDistanceOnWater(int distance) {
        this.distanceOnWater  += distance;
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
