package doggytalents.entity.features;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import doggytalents.api.DoggyTalentsAPI;
import doggytalents.api.inferface.Talent;
import doggytalents.entity.EntityDog;
import doggytalents.helper.Compatibility;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;

/**
 * @author ProPercivalalb
 */
public class TalentFeature extends DogFeature {
	
	public TalentFeature(EntityDog dogIn) {
		super(dogIn);
	}
	
	@Override
    public void writeAdditional(NBTTagCompound compound) {
		
		NBTTagList list = new NBTTagList();
		Map<Talent, Integer> map = this.dog.getTalentMap();
		for(Entry<Talent, Integer> entry : map.entrySet()) {
			NBTTagCompound subCompound = new NBTTagCompound();
			subCompound.putString("talent", DoggyTalentsAPI.TALENTS.getKey(entry.getKey()).toString());
			subCompound.putInt("level", entry.getValue());
			list.add(subCompound);
		}
		
		if(list.size() > 0)
			compound.put("talent_level_list", list);
    }

    @Override
    public void readAdditional(NBTTagCompound compound) {
    	
		Map<Talent, Integer> talentMap = new HashMap<>();
    	
    	if(compound.contains("talent_level_list", 9)) {
    		
    		NBTTagList list = compound.getList("talent_level_list", 10);
    		for(int i = 0; i < list.size(); i++) {
    			NBTTagCompound subCompound = list.getCompound(i);
    			ResourceLocation talentId = new ResourceLocation(subCompound.getString("talent"));
    			int level = subCompound.getInt("level");
    			
    			if(DoggyTalentsAPI.TALENTS.containsKey(talentId)) // Only load talent if enabled in config
    				talentMap.put(DoggyTalentsAPI.TALENTS.getValue(talentId), level);
    		}
    		
    	} else if(compound.contains("talents", 8)) {
    		
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
					
					if(talent != null) // Only load talent if enabled in config
						talentMap.put(talent, level);
				}
			}
    	}
    	
    	if(talentMap.size() > 0) {
			this.dog.setTalentMap(talentMap);
			for(Entry<Talent, Integer> entry : talentMap.entrySet()) {
				entry.getKey().onLevelSet(this.dog, entry.getValue());
			}
    	} else {
			this.dog.setTalentMap(Collections.emptyMap());
    	}
    }
	
	public int getLevel(@Nullable Talent talent) {
		Map<Talent, Integer> map = this.dog.getTalentMap();
		return map.containsKey(talent) ? map.get(talent) : 0;
	}
	
	public void setLevel(@Nonnull Talent talent, int level) {
		Map<Talent, Integer> map = new HashMap<>(this.dog.getTalentMap());
		map.put(talent, level);
		talent.onLevelSet(this.dog, level);
		this.dog.setTalentMap(map);
	}
	
	public void resetTalents() {
		this.dog.getTalentMap().forEach((talent, fromLevel) -> talent.onLevelReset(this.dog, fromLevel));
		this.dog.setTalentMap(Collections.emptyMap());
	}
}
