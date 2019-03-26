package doggytalents;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import doggytalents.api.registry.DogBedRegistry;
import doggytalents.configuration.ConfigHandler;
import doggytalents.handler.GuiHandler;
import doggytalents.lib.Reference;
import doggytalents.proxy.ClientProxy;
import doggytalents.proxy.ServerProxy;
import doggytalents.proxy.SideProxy;
import net.minecraft.init.Blocks;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;

/**
 * @author ProPercivalalb
 */
@Mod(value = Reference.MOD_ID)
public class DoggyTalentsMod {
	
	public static final Logger LOGGER = LogManager.getLogger(Reference.MOD_NAME);
	
	//public static ItemGroup CREATIVE_TAB = new CreativeTabDoggyTalents();
	//public static ItemGroup CREATIVE_TAB_BED = new CreativeTabDogBed();
	public static DoggyTalentsMod INSTANCE;
	public static SideProxy PROXY;

	public DoggyTalentsMod() {
		INSTANCE = this;
        PROXY = DistExecutor.runForDist(() -> ClientProxy::new, () -> ServerProxy::new);
        ConfigHandler.init();
        ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.GUIFACTORY, () -> GuiHandler::openGui);
        
        DogBedRegistry.CASINGS.registerMaterial(Blocks.OAK_PLANKS, "minecraft:block/oak_planks");
		DogBedRegistry.CASINGS.registerMaterial(Blocks.SPRUCE_PLANKS,"minecraft:block/spruce_planks");
		DogBedRegistry.CASINGS.registerMaterial(Blocks.BIRCH_PLANKS, "minecraft:block/birch_planks");
		DogBedRegistry.CASINGS.registerMaterial(Blocks.JUNGLE_PLANKS, "minecraft:block/jungle_planks");
		DogBedRegistry.CASINGS.registerMaterial(Blocks.ACACIA_PLANKS, "minecraft:block/acacia_planks");
		DogBedRegistry.CASINGS.registerMaterial(Blocks.DARK_OAK_PLANKS, "minecraft:block/dark_oak_planks");
		
		DogBedRegistry.BEDDINGS.registerMaterial(Blocks.WHITE_WOOL, "minecraft:block/white_wool");
		DogBedRegistry.BEDDINGS.registerMaterial(Blocks.ORANGE_WOOL, "minecraft:block/orange_wool");
		DogBedRegistry.BEDDINGS.registerMaterial(Blocks.MAGENTA_WOOL, "minecraft:blocks/magenta_wool");
		DogBedRegistry.BEDDINGS.registerMaterial(Blocks.LIGHT_BLUE_WOOL, "minecraft:blocks/light_blue_wool");
		DogBedRegistry.BEDDINGS.registerMaterial(Blocks.YELLOW_WOOL, "minecraft:blocks/yellow_wool");
		DogBedRegistry.BEDDINGS.registerMaterial(Blocks.LIME_WOOL, "minecraft:blocks/lime_wool");
		DogBedRegistry.BEDDINGS.registerMaterial(Blocks.PINK_WOOL, "minecraft:blocks/pink_wool");
		DogBedRegistry.BEDDINGS.registerMaterial(Blocks.GRAY_WOOL, "minecraft:blocks/gray_wool");
		DogBedRegistry.BEDDINGS.registerMaterial(Blocks.LIGHT_GRAY_WOOL, "minecraft:blocks/light_gray_wool");
		DogBedRegistry.BEDDINGS.registerMaterial(Blocks.CYAN_WOOL, "minecraft:blocks/cyan_wool");
		DogBedRegistry.BEDDINGS.registerMaterial(Blocks.PURPLE_WOOL, "minecraft:blocks/purple_wool");
		DogBedRegistry.BEDDINGS.registerMaterial(Blocks.BLUE_WOOL, "minecraft:blocks/blue_wool");
		DogBedRegistry.BEDDINGS.registerMaterial(Blocks.BROWN_WOOL, "minecraft:blocks/brown_wool");
		DogBedRegistry.BEDDINGS.registerMaterial(Blocks.GREEN_WOOL, "minecraft:blocks/green_wool");
		DogBedRegistry.BEDDINGS.registerMaterial(Blocks.RED_WOOL, "minecraft:blocks/red_wool");
		DogBedRegistry.BEDDINGS.registerMaterial(Blocks.BLACK_WOOL, "minecraft:blocks/black_wool");
	}
}
