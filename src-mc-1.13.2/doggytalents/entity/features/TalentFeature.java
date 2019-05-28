package doggytalents.entity.features;

import java.util.HashMap;
import java.util.Map;

import doggytalents.api.inferface.Talent;
import doggytalents.entity.EntityDog;
import doggytalents.helper.Compatibility;
import net.minecraft.nbt.NBTTagCompound;

/**
 * @author ProPercivalalb
 */
public class TalentFeature extends DogFeature {
	
	private Map<String, Integer> dataMap;
	private String lastSaveString;
	
	public TalentFeature(EntityDog dogIn) {
		super(dogIn);
		this.dataMap = new HashMap<String, Integer>();
		this.lastSaveString = "";
	}
	
	@Override
    public void writeAdditional(NBTTagCompound compound) {
        compound.putString("talents", this.getSaveString());
        compound.putBoolean("talent_reg_update", true);
    }

    @Override
    public void readAdditional(NBTTagCompound compound) {
    	String talentString = compound.getString("talents");
    	
    	// Correct order version of save data from 1.14.3.451 & before
    	if(!compound.getBoolean("talent_reg_update")) {
	    	String newTalentString = "";
	    	String[] split = talentString.split(":");
			if(split.length > 0 && split.length % 2 == 0) {
				for(int i = 0; i < split.length; i += 2) {
					String id = Compatibility.getTalentOldNamingScheme(split[i]);
					this.dataMap.put(id, Integer.valueOf(split[i + 1]));
					newTalentString += id + ":" + split[i + 1];
				}
			}
			talentString = newTalentString;
			this.lastSaveString = talentString;
    	}
    	
    	this.dog.dataTracker.setTalentString(talentString);
    }
	
	public String getSaveString() {
		String saveString = this.dog.dataTracker.getTalentString();
		return saveString == null ? "" : saveString;
	}
	
	public int getLevel(Talent talent) {
		return this.getLevel(talent.getRegistryName().getPath());
	}
	
	public int getLevel(String location) {
		String saveString = this.getSaveString();
		if(!this.lastSaveString.equals(saveString)) {
			this.dataMap.clear();
			String[] split = saveString.split(":");
			if(split.length % 2 == 0 && split.length > 0) {
				for(int i = 0; i < split.length; i += 2)
					this.dataMap.put(split[i], Integer.valueOf(split[i + 1]));
			}
			this.lastSaveString = saveString;
		}
		
		if(!this.dataMap.containsKey(location))
			return 0;
		else {
			int level = this.dataMap.get(location);
			return level;
		}
	}
	
	public void setLevel(Talent talent, int level) {
		String location = talent.getRegistryName().getPath();
		if(level == this.getLevel(location) && level > 5)
			return;

		this.dataMap.put(location, level);
		talent.onLevelUpdate(this.dog, level);
		
		String saveString = "";
		boolean first = true;
		
		for(String key : this.dataMap.keySet()) {
			if(!first)
				saveString += ":";
			
			saveString += key.toString() + ":" + this.dataMap.get(key);
			first = false;
		}
		
		this.dog.dataTracker.setTalentString(saveString);
	}
	
	public void resetTalents() {
		this.dataMap.clear();
		this.dog.dataTracker.setTalentString("");
	}
}
