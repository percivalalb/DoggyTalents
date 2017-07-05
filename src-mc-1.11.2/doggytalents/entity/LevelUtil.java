package doggytalents.entity;

import net.minecraft.nbt.NBTTagCompound;

/**
 * @author ProPercivalalb
 */
public class LevelUtil {
		
	private EntityDog dog;
	
	public LevelUtil(EntityDog dog) {
		this.dog = dog;
	}
	
	public void writeTalentsToNBT(NBTTagCompound tagCompound) {
		tagCompound.setInteger("level_normal", this.getLevel());		
		tagCompound.setInteger("level_dire", this.getDireLevel());	
	}

	public void readTalentsFromNBT(NBTTagCompound tagCompound) {
		if(tagCompound.hasKey("level_normal"))
			this.dog.getDataManager().set(EntityDog.LEVEL, tagCompound.getInteger("level_normal"));
		
		if(tagCompound.hasKey("level_dire"))
			this.dog.getDataManager().set(EntityDog.LEVEL_DIRE, tagCompound.getInteger("level_dire"));
		
		//Backwards compatibility
		if(tagCompound.hasKey("levels", 8)) {
			String[] split = tagCompound.getString("levels").split(":");
			this.dog.getDataManager().set(EntityDog.LEVEL, new Integer(split[0]));
			this.dog.getDataManager().set(EntityDog.LEVEL_DIRE, new Integer(split[1]));
		}
	}
	
	public int getLevel() {
		return this.dog.getDataManager().get(EntityDog.LEVEL);
	}
	
	public int getDireLevel() {
		return this.dog.getDataManager().get(EntityDog.LEVEL_DIRE);
	}
	
	public void increaseLevel() {
		this.setLevel(this.getLevel() + 1);
	}
	
	public void increaseDireLevel() {
		this.setDireLevel(this.getDireLevel() + 1);
	}
	
	public void setLevel(int level) {
		this.dog.getDataManager().set(EntityDog.LEVEL, level);
		this.dog.updateEntityAttributes();
	}
	
	public void setDireLevel(int level) {
		this.dog.getDataManager().set(EntityDog.LEVEL_DIRE, level);
		this.dog.updateEntityAttributes();
	}

	public boolean isDireDog() {
		return this.getDireLevel() == 30;
	}
}
