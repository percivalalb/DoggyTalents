package doggytalents.entity.data;

import doggytalents.entity.EntityDTDoggy;

/**
 * @author ProPercivalalb
 **/
public class DogSavePosition {

	public EntityDTDoggy doggy;
	
	public DogSavePosition(EntityDTDoggy par1EntityDTDoggy) {
		this.doggy = par1EntityDTDoggy;
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
		this.doggy.getDataWatcher().updateObject(WatchableDataLib.ID_SAVE_POSITION, "-1:-1:-1:-1:-1:-1");
	}
	
	public String getDefaultStr() {
		return "-1:-1:-1:-1:-1:-1";
	}
	
	public void setBedX(int position) {
		String structure = "";
		structure = position + ":" + getBedY() + ":" + getBedZ() + ":" + getBowlX()+ ":" + getBowlY() + ":" + getBowlZ();
		this.doggy.getDataWatcher().updateObject(WatchableDataLib.ID_SAVE_POSITION, structure);
	}
	
	public void setBedY(int position) {
		String structure = "";
		structure = getBedX() + ":" +  position + ":" + getBedZ() + ":" + getBowlX()+ ":" + getBowlY() + ":" + getBowlZ();
		this.doggy.getDataWatcher().updateObject(WatchableDataLib.ID_SAVE_POSITION, structure);
	}
	
	public void setBedZ(int position) {
		String structure = "";
		structure = getBedX() + ":" + getBedY() + ":" + position + ":" + getBowlX()+ ":" + getBowlY() + ":" + getBowlZ();
		this.doggy.getDataWatcher().updateObject(WatchableDataLib.ID_SAVE_POSITION, structure);
	}
	
	public void setBowlX(int position) {
		String structure = "";
		structure = getBedX() + ":" + getBedY() + ":" + getBedZ() + ":" + position+ ":" + getBowlY() + ":" + getBowlZ();
		this.doggy.getDataWatcher().updateObject(WatchableDataLib.ID_SAVE_POSITION, structure);
	}
	
	public void setBowlY(int position) {
		String structure = "";
		structure = getBedX() + ":" +  getBedY() + ":" + getBedZ() + ":" + getBowlX()+ ":" + position + ":" + getBowlZ();
		this.doggy.getDataWatcher().updateObject(WatchableDataLib.ID_SAVE_POSITION, structure);
	}
	
	public void setBowlZ(int position) {
		String structure = "";
		structure = getBedX() + ":" + getBedY() + ":" + getBedZ() + ":" + getBowlX()+ ":" + getBowlY() + ":" + position;
		this.doggy.getDataWatcher().updateObject(WatchableDataLib.ID_SAVE_POSITION, structure);
	}
	
	protected String writePosition() {
		String structure = this.getBedX() + ":" + this.getBedY() + ":" + this.getBedZ() + ":" + getBowlX()+ ":" + getBowlY() + ":" + getBowlZ();
		this.doggy.getDataWatcher().updateObject(WatchableDataLib.ID_SAVE_POSITION, structure);
		return structure;
	}
	
	private String[] getSkillsLevels() {
		String data = this.getLevelString();
		String[] tokens = data.split(":");
		return tokens;
	}
	
	private String getLevelString() {
		return doggy.getDataWatcher().getWatchableObjectString(WatchableDataLib.ID_SAVE_POSITION);
	}
}
