package doggytalents.block;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import com.google.common.collect.ImmutableSet;

import doggytalents.DoggyTalentsMod;
import doggytalents.ModBlocks;
import doggytalents.api.registry.DogBedRegistry;
import doggytalents.api.registry.BedMaterial;
import net.minecraft.state.AbstractProperty;
import net.minecraft.util.ResourceLocation;

public class StringProperty extends AbstractProperty<BedMaterial> {

	private ImmutableSet<BedMaterial> allowedValues;
	private final DogBedRegistry reg;

	protected StringProperty(String name, DogBedRegistry reg) {
		super(name, BedMaterial.class);
		this.reg = reg;
		this.allowedValues = ImmutableSet.copyOf(this.reg.getKeys());
	}

	@Override
	public Collection<BedMaterial> getAllowedValues() {
		if(this.allowedValues == null) {
			this.allowedValues = ImmutableSet.copyOf(this.reg.getKeys());
		}
		
		return this.allowedValues;
	}

	@Override
	public String getName(BedMaterial value) {
		return value.key;
	}

	@Override
	public Optional<BedMaterial> parseValue(String value) {
		return Optional.ofNullable(this.reg.getFromString(value));
	}

	@Override
	public int computeHashCode() {
		int i = super.computeHashCode();
		i = 31 * i + this.allowedValues.hashCode();
		// i = 31 * i + this.nameToValue.hashCode();
		return i;
	}
	
	public static StringProperty create(String name, DogBedRegistry reg) {
		return new StringProperty(name, reg);
	}
}
