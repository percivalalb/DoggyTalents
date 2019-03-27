package doggytalents.block;

import java.util.Collection;
import java.util.Optional;

import doggytalents.api.registry.DogBedRegistry;
import net.minecraft.state.IProperty;

public class PropertyBedding implements IProperty<String> {

	private final String name;	

    public PropertyBedding(String name) {
    	this.name = name;
    }

	@Override
	public Collection<String> getAllowedValues() {
		return DogBedRegistry.BEDDINGS.getKeys();
	}

	@Override
	public String getName(String arg0) {
		return name;
	}

	@Override
	public Class<String> getValueClass() {
		return String.class;
	}

	@Override
	public Optional<String> parseValue(String arg0) {
		return Optional.of(arg0);
	}

	@Override
	public String getName() {
		return this.name;
	}
	
	public static PropertyBedding create(String string) {
		return new PropertyBedding(string);
	}
	
	@Override
	public boolean equals(Object p_equals_1_) {
		if (this == p_equals_1_) {
			return true;
		}
		else if (!(p_equals_1_ instanceof PropertyBedding))
		{
			return false;
		}
		else
		{
			PropertyBedding propertyhelper = (PropertyBedding)p_equals_1_;
			return this.name.equals(propertyhelper.name);
		}
	}

	@Override
	public int hashCode() {
		return 31 * String.class.hashCode() + this.name.hashCode();
	}
}