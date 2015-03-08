package doggytalents.entity;

import net.minecraft.nbt.NBTTagCompound;

import com.google.common.base.Strings;

/**
 * @author ProPercivalalb
 */
public class LevelUtil {
		
	private EntityDog dog;
	
	public LevelUtil(EntityDog dog) {
		this.dog = dog;
	}
	
	public void writeTalentsToNBT(NBTTagCompound tagCompound) {
		tagCompound.setString("levels", this.getSaveString());			
	}

	public void readTalentsFromNBT(NBTTagCompound tagCompound) {
		if(tagCompound.hasKey("levels", 8))
			this.dog.getDataWatcher().updateObject(24, tagCompound.getString("levels"));
		else
			this.dog.getDataWatcher().updateObject(24, "0:0");
	}
	
	public String getSaveString() {
		String saveString = this.dog.getDataWatcher().getWatchableObjectString(24);
		return Strings.isNullOrEmpty(saveString) ? "0:0" : saveString;
	}
	
	public int getLevel() {
		return Integer.valueOf(this.getSaveString().split(":")[0]);
	}
	
	public int getDireLevel() {
		return Integer.valueOf(this.getSaveString().split(":")[1]);
	}
	
	public void increaseLevel() {
		this.setLevel(this.getLevel() + 1);
	}
	
	public void increaseDireLevel() {
		this.setDireLevel(this.getDireLevel() + 1);
	}
	
	public void setLevel(int level) {
		this.dog.getDataWatcher().updateObject(24, level + ":" + this.getDireLevel());
		this.dog.updateEntityAttributes();
	}
	
	public void setDireLevel(int level) {
		this.dog.getDataWatcher().updateObject(24, this.getLevel() + ":" + level);
		this.dog.updateEntityAttributes();
	}

	public boolean isDireDog() {
		return this.getDireLevel() == 30;
	}
}
