package doggytalents.api.registry;

import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.IRegistry;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class BedMaterial extends ForgeRegistryEntry<BedMaterial> implements Comparable<BedMaterial> {

	public static BedMaterial NULL = new BedMaterial("missing");
	public String key;
	
	public BedMaterial(String key) {
		this.key = key;
	}
	
	public BedMaterial(Block block) {
		ResourceLocation location = IRegistry.BLOCK.getKey(block);
		this.key = location.toString();
		this.setRegistryName(location);
	}


	@Override
	public int compareTo(BedMaterial o) {
		return o.key.compareTo(this.key);
	}
	
	@Override
	public boolean equals(Object o) {
		if(!(o instanceof BedMaterial))
			return false;
		BedMaterial other = (BedMaterial)o;
		return other.key.equals(this.key);
	}
	
	@Override
	public int hashCode() {
		return this.key.hashCode();
	}
}
