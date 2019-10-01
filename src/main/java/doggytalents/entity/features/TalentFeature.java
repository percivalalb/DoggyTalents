package doggytalents.entity.features;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import doggytalents.api.DoggyTalentsAPI;
import doggytalents.api.feature.ITalentFeature;
import doggytalents.api.inferface.Talent;
import doggytalents.entity.EntityDog;
import doggytalents.helper.Compatibility;
import doggytalents.lib.ConfigValues;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;

/**
 * @author ProPercivalalb
 */
public class TalentFeature extends DogFeature implements ITalentFeature {

    public TalentFeature(EntityDog dogIn) {
        super(dogIn);
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {

        ListNBT list = new ListNBT();
        Map<Talent, Integer> map = this.dog.getTalentMap();
        for(Entry<Talent, Integer> entry : map.entrySet()) {
            CompoundNBT subCompound = new CompoundNBT();
            subCompound.putString("talent", DoggyTalentsAPI.TALENTS.getKey(entry.getKey()).toString());
            subCompound.putInt("level", entry.getValue());
            list.add(subCompound);
        }

        if(list.size() > 0) {
            compound.put("talent_level_list", list);
        }
    }

    @Override
    public void readAdditional(CompoundNBT compound) {

        Map<Talent, Integer> talentMap = new HashMap<>();

        if(compound.contains("talent_level_list", Constants.NBT.TAG_LIST)) {

            ListNBT list = compound.getList("talent_level_list", Constants.NBT.TAG_COMPOUND);
            for(int i = 0; i < list.size(); i++) {
                CompoundNBT subCompound = list.getCompound(i);
                ResourceLocation talentId = new ResourceLocation(subCompound.getString("talent"));

                if(DoggyTalentsAPI.TALENTS.containsKey(talentId)) { // Only load if talent exists
                    int level = subCompound.getInt("level");
                    talentMap.put(DoggyTalentsAPI.TALENTS.getValue(talentId), level);
                }
            }

        } else if(compound.contains("talents", Constants.NBT.TAG_STRING)) {

            String[] split = compound.getString("talents").split(":");
            if(split.length > 0 && split.length % 2 == 0) {
                for(int i = 0; i < split.length; i += 2) {
                    Talent talent = Compatibility.getTalentOldNamingScheme(split[i]);
                    int level = 0;
                    try {
                        level = Integer.valueOf(split[i + 1]);
                    } catch(Exception e) {
                        continue;
                    }

                    if(talent != null) { // Only load if talent exists
                        talentMap.put(talent, level);
                    }
                }
            }
        }

        if(!talentMap.isEmpty()) {
            this.dog.setTalentMap(talentMap);
            for(Entry<Talent, Integer> entry : talentMap.entrySet()) {
                entry.getKey().onLevelSet(this.dog, entry.getValue());
            }
        } else {
            this.dog.setTalentMap(Collections.emptyMap());
        }
    }

    @Override
    public int getLevel(@Nullable Talent talent) {
        Map<Talent, Integer> map = this.dog.getTalentMap();
        return map.containsKey(talent) && ConfigValues.ENABLED_TALENTS.getOrDefault(DoggyTalentsAPI.TALENTS.getKey(talent), false) ? map.get(talent) : 0;
    }

    @Override
    public void setLevel(@Nonnull Talent talent, int level) {
        Map<Talent, Integer> map = new HashMap<>(this.dog.getTalentMap());
        map.put(talent, level);

        // Clears the old map to help the GC
        this.dog.getTalentMap().clear();
        this.dog.setTalentMap(map);

        talent.onLevelSet(this.dog, level);
    }

    @Override
    public void resetTalents() {
        this.dog.getTalentMap().forEach((talent, fromLevel) -> talent.onLevelReset(this.dog, fromLevel));
        this.dog.setTalentMap(Collections.emptyMap());
    }
}
