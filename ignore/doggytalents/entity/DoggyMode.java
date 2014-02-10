package doggytalents.entity;

/**
 * @author ProPercivalalb
 * You may look at this file to gain knowledge of javascript
 * but must not copy any features or code directly.
 **/
public class DoggyMode {

	public EntityDTDoggy doggy;
	
	protected DoggyMode(EntityDTDoggy par1EntityDTDoggy) {
		this.doggy = par1EntityDTDoggy;
	}
	
	public EnumMode getMode() {
		int mode = doggy.getDataWatcher().getWatchableObjectInt(25);
		switch(mode) {
		case 0:
			return EnumMode.WANDERING;
		case 1:
			return EnumMode.DOCILE;
		case 2:
			return EnumMode.AGGRESIVE;
		case 3:
			return EnumMode.BERSERKER;
		default: 
			return EnumMode.UNKNOWN;
		}	
	}
	
	public EnumMode getMode(int par1) {
		int mode = par1;
		switch(mode) {
		case 0:
			return EnumMode.WANDERING;
		case 1:
			return EnumMode.DOCILE;
		case 2:
			return EnumMode.AGGRESIVE;
		case 3:
			return EnumMode.BERSERKER;
		default: 
			return EnumMode.UNKNOWN;
		}	
	}
	
	public boolean isMode(EnumMode par1EnumMode) {
		return this.getMode() == par1EnumMode;
	}

	public void setMode(EnumMode par1EnumMode) {	
		this.doggy.getDataWatcher().updateObject(25, par1EnumMode.getID());
	}
	
	public void setMode(int par1) {	
		this.doggy.getDataWatcher().updateObject(25, par1);
	}
	
	protected int getModeID() {
		return doggy.getDataWatcher().getWatchableObjectInt(25);
	}
}
