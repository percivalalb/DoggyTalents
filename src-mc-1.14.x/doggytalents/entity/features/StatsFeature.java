package doggytalents.entity.features;

import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Predicate;

import com.google.common.base.Predicates;
import com.google.common.collect.Maps;

import doggytalents.DoggyTalentsMod;
import doggytalents.entity.EntityDog;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.registries.ForgeRegistries;

public class StatsFeature extends DogFeature {

    private Map<EntityType<?>, Integer> ENTITY_KILLS = Maps.newHashMap();
    private double damageDealt = 0;
    
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
    }
    
    @Override
    public void tick() {
        
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
}
