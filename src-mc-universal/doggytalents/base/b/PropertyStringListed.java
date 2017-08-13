package doggytalents.base.b;

import com.google.common.collect.ImmutableSet;

import net.minecraft.block.properties.PropertyHelper;

import java.util.Collection;

public class PropertyStringListed extends PropertyHelper<String> {
	
    private final ImmutableSet<String> allowedValues = ImmutableSet.<String>of("1.8");

    protected PropertyStringListed(String name) {
        super(name, String.class);
    }

    @Override
    public Collection<String> getAllowedValues() {
        return this.allowedValues;
    }

    public static PropertyStringListed create(String name) {
        return new PropertyStringListed(name);
    }

    @Override
    public String getName(String value) {
        return value;
    }
}