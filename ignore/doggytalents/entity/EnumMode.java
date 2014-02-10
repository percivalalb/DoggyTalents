package doggytalents.entity;

/**
 * @author ProPercivalalb
 * You may look at this file to gain knowledge of javascript
 * but must not copy any features or code directly.
 **/
public enum EnumMode {

	UNKNOWN(0),
	WANDERING(1),
	DOCILE(2),
	AGGRESIVE(3),
	BERSERKER(4),
	UNUSED_5(5),
	UNUSED_6(6);
	
	/** The integer in the 
	 * split string of modes **/
	int ID;
	
	private EnumMode(int par1) {
		ID = par1 - 1;
	}
	
	public int getID() {
		return ID;
	}
	
	private static int upperLimit = 5;
	public static int getUpperLimit() {
		return upperLimit;
	}
}
