package doggytalents.api;

import net.minecraftforge.common.Configuration;

/**
 * @author ProPercivalalb
 * You may look at this file to gain knowledge of javascript
 * but must not copy any features or code directly.
 **/
public class Properties {

	/** Reference to the mod id which is used by forge **/
	public static final String MOD_ID = "Doggy Talents";
	/** The readable name used by forge **/
	public static final String MOD_NAME = "Doggy Talents";
	/** Version string for the mod **/
	public static final String MOD_VERSION = "v1.3.0";
	
	public static final String SP_CLIENT = "doggytalents.client.ClientProxy";
	public static final String SP_SERVER = "doggytalents.common.CommonProxy";

	/** The configuration file where all variables can be accessed**/
	public static Configuration config;
	
	//Packets
	public static final String PACKET_RENAME = "DT|RENAME";
	public static final String PACKET_TALENT = "DT|TALENT";
	public static final String PACKET_TEXTURE = "DT|TEXTURE";
	public static final String PACKET_COMMAND = "DT|COMMAND";
	public static final String PACKET_OBEY = "DT|OBEY";

	//Wolf Stats
    public static boolean allowPermaDeath = false;
    public static boolean tenDayPuppies = true;
    public static boolean isHungerOn = true;
    public static int barkRate = 10;
	public static boolean isStartingItemEnabled = true;
	public static boolean direParticalsOff = false;
	
	//ID'S
    public static int trainingTreatID = 6020;
    public static int superTreatID = 6021;
    public static int masterTreatID = 6022;
    public static int direTreatID = 6023;
    public static int breedingBoneID = 6024;
    public static int throwBoneID = 6025;
    public static int collarShearsID = 6027;
    public static int commandEmblemID = 6028;
    public static int doggyCharmID = 6029;
    public static int foodBowlID = 3210;
}