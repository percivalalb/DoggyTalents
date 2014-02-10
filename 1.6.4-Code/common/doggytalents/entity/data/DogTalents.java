package doggytalents.entity.data;

import doggytalents.entity.EntityDTDoggy;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;

/**
 * @author ProPercivalalb
 **/
public class DogTalents {

	public EntityDTDoggy doggy;
	
	public DogTalents(EntityDTDoggy par1EntityDTDoggy) {
		this.doggy = par1EntityDTDoggy;
	}
	
	public int getTalentLevel(EnumTalents par1EnumTalents) {
		int ID = par1EnumTalents.getID();
		try{return new Integer(this.getSkillsLevels()[ID]);}
    	catch(Exception e){e.printStackTrace(); return 0;}
	}
	
	protected int getTalentLevel(int par1) {
		try{return new Integer(this.getSkillsLevels()[par1]);}
    	catch(Exception e){e.printStackTrace(); return 0;}
	}
	
	public void increaseTalentLevel(EnumTalents par1EnumTalents, int factor) {
		this.setTalentLevel(par1EnumTalents, this.getTalentLevel(par1EnumTalents) + factor);
	}
	
	public void resetTalents() {
		this.doggy.getDataWatcher().updateObject(WatchableDataLib.ID_TALENTS, getDefaultStr());
	}
	
	public String getDefaultStr() {
		return "0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0";
	}
	
	public void setTalentLevel(EnumTalents par1EnumTalents, int newLevel) {
		if(newLevel < 0) newLevel = 0;
		if(newLevel > 5) newLevel = 5;
		int var1;
		String structure = "";
		for(var1 = 0; var1 < EnumTalents.getUpperLimit(); ++var1) {
			int level = this.getTalentLevel(var1);
			
			if(par1EnumTalents.getID() == var1) {
				structure = structure + newLevel + (var1 == EnumTalents.getUpperLimit() - 1 ? "" : ":");
			}
			else {
				structure = structure + level + (var1 == EnumTalents.getUpperLimit() - 1 ? "" : ":");	
			}
		}
		
		this.doggy.getDataWatcher().updateObject(WatchableDataLib.ID_TALENTS, structure);
	}
	
	protected String writeAllTalents() {
		int var1;
		String structure = "";
		for(var1 = 0; var1 < EnumTalents.getUpperLimit(); ++var1) {
			int level = this.getTalentLevel(var1);
			
			structure = structure + level + (var1 == EnumTalents.getUpperLimit() - 1 ? "" : ":");
		}
		
		this.doggy.getDataWatcher().updateObject(WatchableDataLib.ID_TALENTS, structure);
		return structure;
	}
	
	private String[] getSkillsLevels() {
		String data = this.getLevelString();
		String[] tokens = data.split(":");
		return tokens;
	}
	
	private String getLevelString() {
		return doggy.getDataWatcher().getWatchableObjectString(WatchableDataLib.ID_TALENTS);
	}
	
	public void writeToNBT(NBTTagCompound tag) {
		tag.setString("statLevels", this.getLevelString());
	}
	
	public void readFromNBT(NBTTagCompound tag) {
		 String talents = tag.getString("statLevels"); 
		 if(talents.equals(""))
			 talents = this.getDefaultStr();
		 
		 this.doggy.getDataWatcher().updateObject(WatchableDataLib.ID_TALENTS, talents);
	}
}
