package doggytalents.entity.data;

/**
 * @author ProPercivalalb
 **/
public enum EnumMode {

	UNKNOWN("Unknown", "(UNKNOWN)"),
	WANDERING("Wandering", "(W)"),
	DOCILE("Docile", "(D)"),
	AGGRESIVE("Aggresive", "(A)"),
	BERSERKER("Berserker", "(B)");
	
	String tip;
	String name;
	
	private EnumMode(String name, String tip) {
		this.name = name;
		this.tip = tip;
	}
	
	public String getTip() {
		return tip;
	}
	
	private static int upperLimit = 5;
	public static int getUpperLimit() {
		return upperLimit;
	}
	
	public String modeName() {
		return name;
	}
}
