package doggytalents.entity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;

/**
 * @author ProPercivalalb
 **/
public class CoordUtil {

	public EntityDog dog;
	
	public CoordUtil(EntityDog dog) {
		this.dog = dog;
	}
	
	public int getBedX() {
		try{return new Integer(this.getSkillsLevels()[0]);}
    	catch(Exception e){e.printStackTrace(); return -1;}
	}
	
	public int getBedY() {
		try{return new Integer(this.getSkillsLevels()[1]);}
    	catch(Exception e){e.printStackTrace(); return -1;}
	}
	
	public int getBedZ() {
		try{return new Integer(this.getSkillsLevels()[2]);}
    	catch(Exception e){e.printStackTrace(); return -1;}
	}
	
	public int getBowlX() {
		try{return new Integer(this.getSkillsLevels()[3]);}
    	catch(Exception e){e.printStackTrace(); return -1;}
	}
	
	public int getBowlY() {
		try{return new Integer(this.getSkillsLevels()[4]);}
    	catch(Exception e){e.printStackTrace(); return -1;}
	}
	
	public int getBowlZ() {
		try{return new Integer(this.getSkillsLevels()[5]);}
    	catch(Exception e){e.printStackTrace(); return -1;}
	}

	public void resetBedPosition() {
		this.dog.getDataWatcher().updateObject(28, this.getDefaultStr());
	}
	
	public String getDefaultStr() {
		return "-1:-1:-1:-1:-1:-1";
	}
	
	public void setBedX(int position) {
		String structure = "";
		structure = position + ":" + getBedY() + ":" + getBedZ() + ":" + getBowlX()+ ":" + getBowlY() + ":" + getBowlZ();
		this.dog.getDataWatcher().updateObject(28, structure);
	}
	
	public void setBedY(int position) {
		String structure = "";
		structure = getBedX() + ":" +  position + ":" + getBedZ() + ":" + getBowlX()+ ":" + getBowlY() + ":" + getBowlZ();
		this.dog.getDataWatcher().updateObject(28, structure);
	}
	
	public void setBedZ(int position) {
		String structure = "";
		structure = getBedX() + ":" + getBedY() + ":" + position + ":" + getBowlX()+ ":" + getBowlY() + ":" + getBowlZ();
		this.dog.getDataWatcher().updateObject(28, structure);
	}
	
	public void setBedPos(BlockPos pos) {
		String structure = "";
		structure = pos.getX() + ":" + pos.getY() + ":" + pos.getZ() + ":" + getBowlX()+ ":" + getBowlY() + ":" + getBowlZ();
		this.dog.getDataWatcher().updateObject(28, structure);
	}
	
	public boolean hasBedPos() {
		return this.getBedY() != -1;
	}
	
	public BlockPos getBedPos() {
		return new BlockPos(this.getBedX(), this.getBedY(), this.getBedZ());
	}
	
	public void setBowlX(int position) {
		String structure = "";
		structure = getBedX() + ":" + getBedY() + ":" + getBedZ() + ":" + position+ ":" + getBowlY() + ":" + getBowlZ();
		this.dog.getDataWatcher().updateObject(28, structure);
	}
	
	public void setBowlY(int position) {
		String structure = "";
		structure = getBedX() + ":" +  getBedY() + ":" + getBedZ() + ":" + getBowlX()+ ":" + position + ":" + getBowlZ();
		this.dog.getDataWatcher().updateObject(28, structure);
	}
	
	public void setBowlZ(int position) {
		String structure = "";
		structure = getBedX() + ":" + getBedY() + ":" + getBedZ() + ":" + getBowlX()+ ":" + getBowlY() + ":" + position;
		this.dog.getDataWatcher().updateObject(28, structure);
	}
	
	public void setBowlPos(BlockPos pos) {
		String structure = "";
		structure = getBedX() + ":" + getBedY() + ":" + getBedZ() + ":" + pos.getX() + ":" + pos.getY() + ":" + pos.getZ();
		this.dog.getDataWatcher().updateObject(28, structure);
	}
	
	public boolean hasBowlPos() {
		return this.getBowlY() != -1;
	}
	
	public BlockPos getBowlPos() {
		return new BlockPos(this.getBowlX(), this.getBowlY(), this.getBowlZ());
	}
	
	protected String writePosition() {
		String structure = this.getBedX() + ":" + this.getBedY() + ":" + this.getBedZ() + ":" + getBowlX()+ ":" + getBowlY() + ":" + getBowlZ();
		this.dog.getDataWatcher().updateObject(28, structure);
		return structure;
	}
	
	private String[] getSkillsLevels() {
		String data = this.getLevelString();
		String[] tokens = data.split(":");
		return tokens;
	}
	
	private String getLevelString() {
		return dog.getDataWatcher().getWatchableObjectString(28);
	}
	
	public void writeToNBT(NBTTagCompound tagCompound) {
		tagCompound.setString("coords", this.getLevelString());
	}
	
	public void readFromNBT(NBTTagCompound tagCompound) {
		if(tagCompound.hasKey("coords"))
			this.dog.getDataWatcher().updateObject(28, tagCompound.getString("coords"));
		else
			this.resetBedPosition();
	}


}

