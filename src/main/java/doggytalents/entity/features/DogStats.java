package doggytalents.entity.features;

import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Maps;

import doggytalents.entity.EntityDog;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.GameData;

public class DogStats extends DogFeature {

    private Map<EntityEntry, Integer> ENTITY_KILLS = Maps.newHashMap();
    private double damageDealt = 0;
    
    public DogStats(EntityDog dogIn) {
        super(dogIn);
    }

    @Override
    public void writeAdditional(NBTTagCompound compound) {
        NBTTagList killList = new NBTTagList();
        for(Entry<EntityEntry, Integer> entry : this.ENTITY_KILLS.entrySet()) {
            NBTTagCompound stats = new NBTTagCompound();
            stats.setString("type", ForgeRegistries.ENTITIES.getKey(entry.getKey()).toString());
            stats.setInteger("count", entry.getValue());
            killList.appendTag(stats);
        }
        compound.setTag("entityKills", killList);
        compound.setDouble("damageDealt", this.damageDealt);
    }
    
    @Override
    public void readAdditional(NBTTagCompound compound) {
        NBTTagList killList = compound.getTagList("entityKills", Constants.NBT.TAG_COMPOUND);
        for(int i = 0; i < killList.tagCount(); i++) {
            NBTTagCompound stats = killList.getCompoundTagAt(i);
            ResourceLocation typeLoc = new ResourceLocation(stats.getString("type"));
            if(ForgeRegistries.ENTITIES.containsKey(typeLoc)) {
                this.ENTITY_KILLS.put(ForgeRegistries.ENTITIES.getValue(typeLoc), stats.getInteger("count"));
            }
        }
        this.damageDealt = compound.getDouble("damageDealt");
    }
    
    @Override
    public void tick() {
        
    }
    
    public int getKillCountFor(EntityEntry type) {
        return this.ENTITY_KILLS.getOrDefault(type, 0);
    }
    
    public int getTotalKillCount() {
        int total = 0;
        for(Entry<EntityEntry, Integer> entry : this.ENTITY_KILLS.entrySet()) {
            total += entry.getValue();
        }
        return total;
    }
    
    public void incrementKillCount(Entity entity) {
        EntityEntry entityEntry = GameData.getEntityClassMap().get(entity.getClass());
        if(entityEntry != null)
            this.incrementKillCount(entityEntry);
    }
    
    private void incrementKillCount(EntityEntry type) {
        this.ENTITY_KILLS.put(type, this.getKillCountFor(type) + 1);
    }
    
    public void increaseDamageDealt(double damage) {
        this.damageDealt += damage;
    }
}
