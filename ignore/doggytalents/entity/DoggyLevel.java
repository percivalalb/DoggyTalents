package doggytalents.entity;

/**
 * @author ProPercivalalb
 * You may look at this file to gain knowledge of javascript
 * but must not copy any features or code directly.
 **/
public class DoggyLevel {

	public EntityDTDoggy doggy;
	
	protected DoggyLevel(EntityDTDoggy par1EntityDTDoggy) {
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
		this.doggy.getDataWatcher().updateObject(21, structure);
	}
	
	public void setDireLevel(int newLevel) {
		String structure = this.getLevel() + ":" + newLevel;
		this.doggy.getDataWatcher().updateObject(21, structure);
	}
	
	private String[] getLevels() {
		String data = this.getLevelString();
		String[] tokens = data.split(":");
		return tokens;
	}
	
	private String getLevelString() {
		return doggy.getDataWatcher().getWatchableObjectString(21);
	}
	
	public String writeAllLevels() {
		return this.getLevel() + ":" + this.getDireLevel();
	}
}
