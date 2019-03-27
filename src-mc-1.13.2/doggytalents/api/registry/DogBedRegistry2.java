package doggytalents.api.registry;

import net.minecraft.util.IStringSerializable;

public class DogBedRegistry2 {

	public enum Casing implements IStringSerializable {
		OAK_PLANK("oak_planks", "minecraft:block/oak_planks"),
		SPRUCE_PLANKS("spruce_planks", "minecraft:block/spruce_planks");

		String name, texture;
		
		Casing(String name, String texture) {
			this.name = name;
			this.texture = texture;
		}
		
		@Override
		public String getName() {
			return this.name;
		}
		
		public String getTexture() {
			return this.texture;
		}
	}
	
	public enum Bedding implements IStringSerializable {
		WHITE_WOOL("white_wool", "minecraft:block/white_wool"),
		BROWN_WOOL("brown_wool", "minecraft:block/brown_wool");
		
		String name, texture;
		
		Bedding(String name, String texture) {
			this.name = name;
			this.texture = texture;
		}
		
		@Override
		public String getName() {
			return this.name;
		}
		
		public String getTexture() {
			return this.texture;
		}
	}

}
