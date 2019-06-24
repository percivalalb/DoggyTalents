package doggytalents;

import doggytalents.api.BeddingRegistryEvent;
import doggytalents.api.inferface.IBedMaterial;
import doggytalents.api.registry.DogBedRegistry;
import doggytalents.lib.Reference;
import net.minecraft.block.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class ModBeddings {

    public static IBedMaterial OAK;
    public static IBedMaterial WHITE;
    
    @Mod.EventBusSubscriber(modid = Reference.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class Registration {
        
        @SubscribeEvent
        public static void registerBeddingMaterial(BeddingRegistryEvent event) {
            OAK = DogBedRegistry.CASINGS.registerMaterial(Blocks.OAK_PLANKS, new ResourceLocation("block/oak_planks"));
            DogBedRegistry.CASINGS.registerMaterial(Blocks.SPRUCE_PLANKS, new ResourceLocation("block/spruce_planks"));
            DogBedRegistry.CASINGS.registerMaterial(Blocks.BIRCH_PLANKS, new ResourceLocation("block/birch_planks"));
            DogBedRegistry.CASINGS.registerMaterial(Blocks.JUNGLE_PLANKS, new ResourceLocation("block/jungle_planks"));
            DogBedRegistry.CASINGS.registerMaterial(Blocks.ACACIA_PLANKS, new ResourceLocation("block/acacia_planks"));
            DogBedRegistry.CASINGS.registerMaterial(Blocks.DARK_OAK_PLANKS, new ResourceLocation("block/dark_oak_planks"));
            
            WHITE = DogBedRegistry.BEDDINGS.registerMaterial(Blocks.WHITE_WOOL, new ResourceLocation("block/white_wool"));
            DogBedRegistry.BEDDINGS.registerMaterial(Blocks.ORANGE_WOOL, new ResourceLocation("block/orange_wool"));
            DogBedRegistry.BEDDINGS.registerMaterial(Blocks.MAGENTA_WOOL, new ResourceLocation("block/magenta_wool"));
            DogBedRegistry.BEDDINGS.registerMaterial(Blocks.LIGHT_BLUE_WOOL, new ResourceLocation("block/light_blue_wool"));
            DogBedRegistry.BEDDINGS.registerMaterial(Blocks.YELLOW_WOOL, new ResourceLocation("block/yellow_wool"));
            DogBedRegistry.BEDDINGS.registerMaterial(Blocks.LIME_WOOL, new ResourceLocation("block/lime_wool"));
            DogBedRegistry.BEDDINGS.registerMaterial(Blocks.PINK_WOOL, new ResourceLocation("block/pink_wool"));
            DogBedRegistry.BEDDINGS.registerMaterial(Blocks.GRAY_WOOL, new ResourceLocation("block/gray_wool"));
            DogBedRegistry.BEDDINGS.registerMaterial(Blocks.LIGHT_GRAY_WOOL, new ResourceLocation("block/light_gray_wool"));
            DogBedRegistry.BEDDINGS.registerMaterial(Blocks.CYAN_WOOL, new ResourceLocation("block/cyan_wool"));
            DogBedRegistry.BEDDINGS.registerMaterial(Blocks.PURPLE_WOOL, new ResourceLocation("block/purple_wool"));
            DogBedRegistry.BEDDINGS.registerMaterial(Blocks.BLUE_WOOL, new ResourceLocation("block/blue_wool"));
            DogBedRegistry.BEDDINGS.registerMaterial(Blocks.BROWN_WOOL, new ResourceLocation("block/brown_wool"));
            DogBedRegistry.BEDDINGS.registerMaterial(Blocks.GREEN_WOOL, new ResourceLocation("block/green_wool"));
            DogBedRegistry.BEDDINGS.registerMaterial(Blocks.RED_WOOL, new ResourceLocation("block/red_wool"));
            DogBedRegistry.BEDDINGS.registerMaterial(Blocks.BLACK_WOOL, new ResourceLocation("block/black_wool"));
        }
    }
}
