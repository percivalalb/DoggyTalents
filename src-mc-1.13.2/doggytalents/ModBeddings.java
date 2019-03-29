package doggytalents;

import doggytalents.api.registry.BedMaterial;
import doggytalents.api.registry.DogBedRegistry;
import doggytalents.event.BeddingRegistryEvent;
import doggytalents.lib.Reference;
import net.minecraft.init.Blocks;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class ModBeddings {

	public static BedMaterial OAK;
	public static BedMaterial WHITE;
	
	@Mod.EventBusSubscriber(modid = Reference.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class Registration {
		
		@SubscribeEvent
	    public static void registerBeddingMaterial(final BeddingRegistryEvent event) {
			OAK = DogBedRegistry.CASINGS.registerMaterial(Blocks.OAK_PLANKS, "minecraft:block/oak_planks");
	        DogBedRegistry.CASINGS.registerMaterial(Blocks.SPRUCE_PLANKS, "minecraft:block/spruce_planks");
			DogBedRegistry.CASINGS.registerMaterial(Blocks.BIRCH_PLANKS, "minecraft:block/birch_planks");
			DogBedRegistry.CASINGS.registerMaterial(Blocks.JUNGLE_PLANKS, "minecraft:block/jungle_planks");
			DogBedRegistry.CASINGS.registerMaterial(Blocks.ACACIA_PLANKS, "minecraft:block/acacia_planks");
			DogBedRegistry.CASINGS.registerMaterial(Blocks.DARK_OAK_PLANKS, "minecraft:block/dark_oak_planks");
			
			WHITE = DogBedRegistry.BEDDINGS.registerMaterial(Blocks.WHITE_WOOL, "minecraft:block/white_wool");
			DogBedRegistry.BEDDINGS.registerMaterial(Blocks.ORANGE_WOOL, "minecraft:block/orange_wool");
			DogBedRegistry.BEDDINGS.registerMaterial(Blocks.MAGENTA_WOOL, "minecraft:block/magenta_wool");
			DogBedRegistry.BEDDINGS.registerMaterial(Blocks.LIGHT_BLUE_WOOL, "minecraft:block/light_blue_wool");
			DogBedRegistry.BEDDINGS.registerMaterial(Blocks.YELLOW_WOOL, "minecraft:block/yellow_wool");
			DogBedRegistry.BEDDINGS.registerMaterial(Blocks.LIME_WOOL, "minecraft:block/lime_wool");
			DogBedRegistry.BEDDINGS.registerMaterial(Blocks.PINK_WOOL, "minecraft:block/pink_wool");
			DogBedRegistry.BEDDINGS.registerMaterial(Blocks.GRAY_WOOL, "minecraft:block/gray_wool");
			DogBedRegistry.BEDDINGS.registerMaterial(Blocks.LIGHT_GRAY_WOOL, "minecraft:block/light_gray_wool");
			DogBedRegistry.BEDDINGS.registerMaterial(Blocks.CYAN_WOOL, "minecraft:block/cyan_wool");
			DogBedRegistry.BEDDINGS.registerMaterial(Blocks.PURPLE_WOOL, "minecraft:block/purple_wool");
			DogBedRegistry.BEDDINGS.registerMaterial(Blocks.BLUE_WOOL, "minecraft:block/blue_wool");
			DogBedRegistry.BEDDINGS.registerMaterial(Blocks.BROWN_WOOL, "minecraft:block/brown_wool");
			DogBedRegistry.BEDDINGS.registerMaterial(Blocks.GREEN_WOOL, "minecraft:block/green_wool");
			DogBedRegistry.BEDDINGS.registerMaterial(Blocks.RED_WOOL, "minecraft:block/red_wool");
			DogBedRegistry.BEDDINGS.registerMaterial(Blocks.BLACK_WOOL, "minecraft:block/black_wool");
		}
    }
}
