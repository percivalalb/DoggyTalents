package doggytalents.entity;

import com.google.common.base.Optional;

import net.minecraft.nbt.NBTTagCompound;

/**
 * @author ProPercivalalb
 **/
public class CoordUtil {

	public EntityDog dog;
	
	public CoordUtil(EntityDog dog) {
		this.dog = dog;
	}
	
	public boolean hasBedPos() {
		return this.dog.dataTracker.hasBedPos();
	}
	
	public boolean hasBowlPos() {
		return this.dog.dataTracker.hasBowlPos();
	}
	
	public void setBedPos(int x, int y, int z) {
		this.dog.dataTracker.setBedPos(x, y, z);
	}
	
	public void setBowlPos(int x, int y, int z) {
		this.dog.dataTracker.setBowlPos(x, y, z);
	}
	
	public int getBedX() {
		return this.dog.dataTracker.getBedX();
	}
	
	public int getBedY() {
		return this.dog.dataTracker.getBedY();
	}
	
	public int getBedZ() {
		return this.dog.dataTracker.getBedZ();
	}
	
	public int getBowlX() {
		return this.dog.dataTracker.getBowlX();
	}
	
	public int getBowlY() {
		return this.dog.dataTracker.getBowlY();
	}
	
	public int getBowlZ() {
		return this.dog.dataTracker.getBowlZ();
	}
	
	public void resetBedPosition() {
		this.dog.dataTracker.resetBedPosition();
	}
	
	public void resetBowlPosition() {
		this.dog.dataTracker.resetBedPosition();
	}
	
	public void writeToNBT(NBTTagCompound tagCompound) {
		if(this.hasBedPos()) {
			tagCompound.setInteger("bedPosX", this.getBedX());
			tagCompound.setInteger("bedPosY", this.getBedY());
			tagCompound.setInteger("bedPosZ", this.getBedZ());
		}
		
		if(this.hasBowlPos()) {
			tagCompound.setInteger("bowlPosX", this.getBowlX());
			tagCompound.setInteger("bowlPosY", this.getBowlY());
			tagCompound.setInteger("bowlPosZ", this.getBowlZ());
		}
	}
	
	public void readFromNBT(NBTTagCompound tagCompound) {
		if(tagCompound.hasKey("bedPosX")) {
			this.setBedPos(tagCompound.getInteger("bedPosX"), tagCompound.getInteger("bedPosY"), tagCompound.getInteger("bedPosZ"));
		}
		if(tagCompound.hasKey("bowlPosX")) {
			this.setBowlPos(tagCompound.getInteger("bowlPosX"), tagCompound.getInteger("bowlPosY"), tagCompound.getInteger("bowlPosZ"));
		}
		
		//Backwards compatibility
		if(tagCompound.hasKey("coords")) {
			String[] split = tagCompound.getString("coords").split(":");
			this.setBedPos(new Integer(split[0]), new Integer(split[1]), new Integer(split[2]));
			this.setBowlPos(new Integer(split[3]), new Integer(split[4]), new Integer(split[5]));
		}
	}
}

