package doggytalents.entity.data;

/**
 * @author ProPercivalalb
 **/
public enum EnumTalents {

	BLACKPELT(1),
	GUARDDOG(2),
	HUNTERDOG(3),
	HELLHOUND(4),
	WOLFMOUNT(5),
	PACKPUPPY(6),
	QUICKHEALER(7),
	PILLOWPAW(8),
	CREEPERSWEEPER(9),
	DOGGYDASH(10),
	FISHERDOG(11),
	HAPPYEATER(12),
	BEDFINDER(13),
	PESTFIGHTER(14),
	RESCUEDOG(15),
	POSIONFANG(16),
	SHEPHERDDOG(17),
	PUPPYEYES(18),
	UNUSED_19(19), // Will always return 0 because no skill is associated with it.
	UNUSED_20(20), // Will always return 0 because no skill is associated with it.
	UNUSED_21(21), // Will always return 0 because no skill is associated with it.
	UNUSED_22(22), // Will always return 0 because no skill is associated with it.
	UNUSED_23(23); // Will always return 0 because no skill is associated with it.
	
	/** The integer in the 
	 * split string of skills **/
	int ID;
	
	private EnumTalents(int par1) {
		ID = par1 - 1;
	}
	
	public int getID() {
		return ID;
	}
	
	private static int upperLimit = 23;
	public static int getUpperLimit() {
		return upperLimit;
	}
}
