package doggytalents.block;

import net.minecraftforge.common.property.IUnlistedProperty;


/**
 * @author ProPercivalalb
 */
public class PropertyString implements IUnlistedProperty<String> {

	private final String name;	

    public PropertyString(String name) {
    	this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public boolean isValid(String value) {
        return true;
    }

    public Class<String> getType() {
        return String.class;
    }

    public String valueToString(String value){
        return value;
    }
    
    public static PropertyString create(String name) {
        return new PropertyString(name);
    }
}