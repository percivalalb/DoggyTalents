package doggytalents.block;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

import net.minecraftforge.common.property.IUnlistedProperty;


/**
 * @author ProPercivalalb
 */
public class PropertyString<V extends Comparable> implements IUnlistedProperty<V> {

	private final Class valueClass;
    private final String name;

    public PropertyString(String name) {
    	this.name = name;
        this.valueClass = String.class;
    }

    public String getName() {
        return name;
    }

    public boolean isValid(V value) {
        return true;
    }

    public Class<V> getType() {
        return this.valueClass;
    }

    public String valueToString(V value){
        return (String)value;
    }
    
    public static PropertyString create(String name) {
        return new PropertyString(name);
    }
}