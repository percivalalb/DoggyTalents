package doggytalents.block;

import java.util.Collection;
import java.util.Optional;

import net.minecraft.state.AbstractProperty;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.IForgeRegistry;

public class RegisteredProperty<T extends ForgeRegistryEntry<T> & Comparable<T>> extends AbstractProperty<T> {

	private final IForgeRegistry<T> registry;

	protected RegisteredProperty(String name, IForgeRegistry<T> registry) {
		super(name, registry.getRegistrySuperType());
		this.registry = registry;
	}

	@Override
	public Collection<T> getAllowedValues() {
		return this.registry.getValues();
	}

	@Override
	public String getName(T value) {
		return value.getRegistryName().getNamespace() + "_" + value.getRegistryName().getPath();
	}

	@Override
	public Optional<T> parseValue(String value) {
		int index = value.indexOf("_");
		return Optional.ofNullable(this.registry.getValue(new ResourceLocation(value.substring(0, index), value.substring(index + 1, value.length()))));
	}

	@Override
	public int computeHashCode() {
		int i = super.computeHashCode();
		//i = 31 * i + this.allowedValues.hashCode();
		// i = 31 * i + this.nameToValue.hashCode();
		return i;
	}
	
	public static <T extends ForgeRegistryEntry<T> & Comparable<T>> RegisteredProperty<T> create(String name, IForgeRegistry<T> registry) {
		return new RegisteredProperty<T>(name, registry);
	}
}
