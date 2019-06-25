package doggytalents;

import doggytalents.api.BeddingRegistryEvent;
import doggytalents.api.registry.DogBedRegistry;
import doggytalents.lib.Reference;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ModBeddings {
    
    @Mod.EventBusSubscriber(modid = Reference.MOD_ID)
    public static class Registration {
        
        @SubscribeEvent
        public static void registerBeddingMaterial(BeddingRegistryEvent event) {
            DogBedRegistry.CASINGS.registerMaterial(Blocks.PLANKS, 0, new ResourceLocation("blocks/planks_oak"));
            DogBedRegistry.CASINGS.registerMaterial(Blocks.PLANKS, 1, new ResourceLocation("blocks/planks_spruce"));
            DogBedRegistry.CASINGS.registerMaterial(Blocks.PLANKS, 2, new ResourceLocation("blocks/planks_birch"));
            DogBedRegistry.CASINGS.registerMaterial(Blocks.PLANKS, 3, new ResourceLocation("blocks/planks_jungle"));
            DogBedRegistry.CASINGS.registerMaterial(Blocks.PLANKS, 4, new ResourceLocation("blocks/planks_acacia"));
            DogBedRegistry.CASINGS.registerMaterial(Blocks.PLANKS, 5, new ResourceLocation("blocks/planks_big_oak"));
            
            DogBedRegistry.BEDDINGS.registerMaterial(Blocks.WOOL, 0, new ResourceLocation("blocks/wool_colored_white"));
            DogBedRegistry.BEDDINGS.registerMaterial(Blocks.WOOL, 1, new ResourceLocation("blocks/wool_colored_orange"));
            DogBedRegistry.BEDDINGS.registerMaterial(Blocks.WOOL, 2, new ResourceLocation("blocks/wool_colored_magenta"));
            DogBedRegistry.BEDDINGS.registerMaterial(Blocks.WOOL, 3, new ResourceLocation("blocks/wool_colored_light_blue"));
            DogBedRegistry.BEDDINGS.registerMaterial(Blocks.WOOL, 4, new ResourceLocation("blocks/wool_colored_yellow"));
            DogBedRegistry.BEDDINGS.registerMaterial(Blocks.WOOL, 5, new ResourceLocation("blocks/wool_colored_lime"));
            DogBedRegistry.BEDDINGS.registerMaterial(Blocks.WOOL, 6, new ResourceLocation("blocks/wool_colored_pink"));
            DogBedRegistry.BEDDINGS.registerMaterial(Blocks.WOOL, 7, new ResourceLocation("blocks/wool_colored_gray"));
            DogBedRegistry.BEDDINGS.registerMaterial(Blocks.WOOL, 8, new ResourceLocation("blocks/wool_colored_silver"));
            DogBedRegistry.BEDDINGS.registerMaterial(Blocks.WOOL, 9, new ResourceLocation("blocks/wool_colored_cyan"));
            DogBedRegistry.BEDDINGS.registerMaterial(Blocks.WOOL, 10, new ResourceLocation("blocks/wool_colored_purple"));
            DogBedRegistry.BEDDINGS.registerMaterial(Blocks.WOOL, 11, new ResourceLocation("blocks/wool_colored_blue"));
            DogBedRegistry.BEDDINGS.registerMaterial(Blocks.WOOL, 12, new ResourceLocation("blocks/wool_colored_brown"));
            DogBedRegistry.BEDDINGS.registerMaterial(Blocks.WOOL, 13, new ResourceLocation("blocks/wool_colored_green"));
            DogBedRegistry.BEDDINGS.registerMaterial(Blocks.WOOL, 14, new ResourceLocation("blocks/wool_colored_red"));
            DogBedRegistry.BEDDINGS.registerMaterial(Blocks.WOOL, 15, new ResourceLocation("blocks/wool_colored_black"));
        }
    }
}
