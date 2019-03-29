package doggytalents.entity.features;

import doggytalents.entity.EntityDog;
import net.minecraft.nbt.NBTTagCompound;

/**
 * @author ProPercivalalb
 **/
public class CoordFeature extends DogFeature {
	
	public CoordFeature(EntityDog dogIn) {
		super(dogIn);
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
		this.dog.dataTracker.resetBowlPosition();
	}
	
	@Override
	public void writeAdditional(NBTTagCompound tagCompound) {
		if(this.hasBedPos()) {
			tagCompound.putInt("bedPosX", this.getBedX());
			tagCompound.putInt("bedPosY", this.getBedY());
			tagCompound.putInt("bedPosZ", this.getBedZ());
		}
		
		if(this.hasBowlPos()) {
			tagCompound.putInt("bowlPosX", this.getBowlX());
			tagCompound.putInt("bowlPosY", this.getBowlY());
			tagCompound.putInt("bowlPosZ", this.getBowlZ());
		}
	}
	
	@Override
	public void readAdditional(NBTTagCompound tagCompound) {
		if(tagCompound.contains("bedPosX")) {
			this.setBedPos(tagCompound.getInt("bedPosX"), tagCompound.getInt("bedPosY"), tagCompound.getInt("bedPosZ"));
		}
		if(tagCompound.contains("bowlPosX")) {
			this.setBowlPos(tagCompound.getInt("bowlPosX"), tagCompound.getInt("bowlPosY"), tagCompound.getInt("bowlPosZ"));
		}
		
		//Backwards compatibility
		if(tagCompound.contains("coords")) {
			String[] split = tagCompound.getString("coords").split(":");
			this.setBedPos(new Integer(split[0]), new Integer(split[1]), new Integer(split[2]));
			this.setBowlPos(new Integer(split[3]), new Integer(split[4]), new Integer(split[5]));
		}
	}
}

