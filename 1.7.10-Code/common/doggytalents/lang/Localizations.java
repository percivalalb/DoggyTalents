package doggytalents.lang;

/**
 * @author ProPercivalalb
 */
public enum Localizations {

	EN_GB("en_GB.txt"),
	EN_US("en_US.txt");
    
	String fileName;
	
	Localizations(String file) {
		this.fileName = file;
	}
	
	public String getFile() {
		return this.fileName;
	}
	
    public static final String LANG_RESOURCE_LOCATION = "/assets/doggytalents/lang/";
    //Example
    //<entry key=""></entry>
}
