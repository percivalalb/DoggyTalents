package doggytalents.entity.data;

import net.minecraft.nbt.NBTTagCompound;
import doggytalents.entity.EntityDTDoggy;

/**
 * @author ProPercivalalb
 **/
public class DogLevel {

	public EntityDTDoggy doggy;
	
	public DogLevel(EntityDTDoggy par1EntityDTDoggy) {
		this.doggy = par1EntityDTDoggy;
	}
	
	public boolean isDireDog() {
		return this.getDireLevel() == 30;
	}
	
	public int getLevel() {
		try{return new Integer(this.getLevels()[0]);}
    	catch(Exception e){e.printStackTrace(); return 0;}
	}
	
	public int getDireLevel() {
		try{return new Integer(this.getLevels()[1]);}
    	catch(Exception e){e.printStackTrace(); return 0;}
	}
	
	public void increaseLevel(int factor) {
		this.setLevel(this.getLevel() + factor);
	}
	
	public void increaseDireLevel(int factor) {
		this.setDireLevel(this.getDireLevel() + factor);
	}
	
	public void setLevel(int newLevel) {
		String structure = newLevel + ":" + this.getDireLevel();
		this.doggy.getDataWatcher().updateObject(WatchableDataLib.ID_LEVEL, structure);
	}
	
	public void setDireLevel(int newLevel) {
		String structure = this.getLevel() + ":" + newLevel;
		this.doggy.getDataWatcher().updateObject(WatchableDataLib.ID_LEVEL, structure);
	}
	
	private String[] getLevels() {
		String data = this.getLevelString();
		String[] tokens = data.split(":");
		return tokens;
	}
	
	private String getLevelString() {
		return doggy.getDataWatcher().getWatchableObjectString(WatchableDataLib.ID_LEVEL);
	}
	
	public String writeAllLevels() {
		return this.getLevel() + ":" + this.getDireLevel();
	}
	
	public String getDefaultStr() {
		return "0:0";
	}
	
	public void writeToNBT(NBTTagCompound tag) {
		tag.setString("pointLevels", this.getLevelString());
	}
	
	public void readFromNBT(NBTTagCompound tag) {
		 String level = tag.getString("pointLevels"); 
		 if(level.equals(""))
			 level = this.getDefaultStr();
		 
		 this.doggy.getDataWatcher().updateObject(WatchableDataLib.ID_LEVEL, level);
	}
}
