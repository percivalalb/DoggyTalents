package doggytalents.entity.doggyhelper;

import doggytalents.entity.EntityDTDoggy;

/**
 * @author ProPercivalalb
 */
public class DataWatcherHelper {
	
	/** The index the custom data is in the data watcher **/
	public static final int INDEX_BEGGING = 19;
	
	//Called everytime a new EntityDTDoggy is created
	public static void entityInit(EntityDTDoggy dog) {
		dog.getDataWatcher().addObject(INDEX_BEGGING, new Byte((byte)0));
	}
	
	//Setter Methods
	public static void setBegging(EntityDTDoggy dog, boolean beg) {
        byte b0 = dog.getDataWatcher().getWatchableObjectByte(INDEX_BEGGING);
        if (beg) {
        	dog.getDataWatcher().updateObject(INDEX_BEGGING, Byte.valueOf((byte)1));
        }
        else {
        	dog.getDataWatcher().updateObject(INDEX_BEGGING, Byte.valueOf((byte)0));
        }
    }
	
	
	//Getter Methods
	public static boolean isBegging(EntityDTDoggy dog) {
	    return dog.getDataWatcher().getWatchableObjectByte(INDEX_BEGGING) == 1;
	}
	
}
