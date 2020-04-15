package doggytalents;

import doggytalents.api.BeddingRegistryEvent;
import doggytalents.api.inferface.IDogBedRegistry;
import doggytalents.api.lib.Reference;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ModBeddings {
    
    @Mod.EventBusSubscriber(modid = Reference.MOD_ID)
    public static class Registration {
        
        @SubscribeEvent
        public static void registerBeddingMaterial(BeddingRegistryEvent event) {
            IDogBedRegistry beddingReg = event.getBeddingRegistry();
            IDogBedRegistry casingReg = event.getCasingRegistry();
            
            casingReg.registerMaterial(Blocks.PLANKS, 0, new ResourceLocation("blocks/planks_oak"));
            casingReg.registerMaterial(Blocks.PLANKS, 1, new ResourceLocation("blocks/planks_spruce"));
            casingReg.registerMaterial(Blocks.PLANKS, 2, new ResourceLocation("blocks/planks_birch"));
            casingReg.registerMaterial(Blocks.PLANKS, 3, new ResourceLocation("blocks/planks_jungle"));
            casingReg.registerMaterial(Blocks.PLANKS, 4, new ResourceLocation("blocks/planks_acacia"));
            casingReg.registerMaterial(Blocks.PLANKS, 5, new ResourceLocation("blocks/planks_big_oak"));
            
            beddingReg.registerMaterial(Blocks.WOOL, 0, new ResourceLocation("blocks/wool_colored_white"));
            beddingReg.registerMaterial(Blocks.WOOL, 1, new ResourceLocation("blocks/wool_colored_orange"));
            beddingReg.registerMaterial(Blocks.WOOL, 2, new ResourceLocation("blocks/wool_colored_magenta"));
            beddingReg.registerMaterial(Blocks.WOOL, 3, new ResourceLocation("blocks/wool_colored_light_blue"));
            beddingReg.registerMaterial(Blocks.WOOL, 4, new ResourceLocation("blocks/wool_colored_yellow"));
            beddingReg.registerMaterial(Blocks.WOOL, 5, new ResourceLocation("blocks/wool_colored_lime"));
            beddingReg.registerMaterial(Blocks.WOOL, 6, new ResourceLocation("blocks/wool_colored_pink"));
            beddingReg.registerMaterial(Blocks.WOOL, 7, new ResourceLocation("blocks/wool_colored_gray"));
            beddingReg.registerMaterial(Blocks.WOOL, 8, new ResourceLocation("blocks/wool_colored_silver"));
            beddingReg.registerMaterial(Blocks.WOOL, 9, new ResourceLocation("blocks/wool_colored_cyan"));
            beddingReg.registerMaterial(Blocks.WOOL, 10, new ResourceLocation("blocks/wool_colored_purple"));
            beddingReg.registerMaterial(Blocks.WOOL, 11, new ResourceLocation("blocks/wool_colored_blue"));
            beddingReg.registerMaterial(Blocks.WOOL, 12, new ResourceLocation("blocks/wool_colored_brown"));
            beddingReg.registerMaterial(Blocks.WOOL, 13, new ResourceLocation("blocks/wool_colored_green"));
            beddingReg.registerMaterial(Blocks.WOOL, 14, new ResourceLocation("blocks/wool_colored_red"));
            beddingReg.registerMaterial(Blocks.WOOL, 15, new ResourceLocation("blocks/wool_colored_black"));
        }
    }
}
