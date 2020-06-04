package doggytalents;

import doggytalents.api.BeddingRegistryEvent;
import doggytalents.api.inferface.IBedMaterial;
import doggytalents.api.inferface.IDogBedRegistry;
import net.minecraft.block.Blocks;

public class DoggyBedMaterials {

    public static IBedMaterial OAK;
    public static IBedMaterial WHITE;

    public static void registerBeddingMaterial(BeddingRegistryEvent event) {
        IDogBedRegistry beddingReg = event.getBeddingRegistry();
        IDogBedRegistry casingReg = event.getCasingRegistry();

        OAK = casingReg.registerMaterial(Blocks.OAK_PLANKS);
        casingReg.registerMaterial(Blocks.SPRUCE_PLANKS);
        casingReg.registerMaterial(Blocks.BIRCH_PLANKS);
        casingReg.registerMaterial(Blocks.JUNGLE_PLANKS);
        casingReg.registerMaterial(Blocks.ACACIA_PLANKS);
        casingReg.registerMaterial(Blocks.DARK_OAK_PLANKS);

        WHITE = beddingReg.registerMaterial(Blocks.WHITE_WOOL);
        beddingReg.registerMaterial(Blocks.ORANGE_WOOL);
        beddingReg.registerMaterial(Blocks.MAGENTA_WOOL);
        beddingReg.registerMaterial(Blocks.LIGHT_BLUE_WOOL);
        beddingReg.registerMaterial(Blocks.YELLOW_WOOL);
        beddingReg.registerMaterial(Blocks.LIME_WOOL);
        beddingReg.registerMaterial(Blocks.PINK_WOOL);
        beddingReg.registerMaterial(Blocks.GRAY_WOOL);
        beddingReg.registerMaterial(Blocks.LIGHT_GRAY_WOOL);
        beddingReg.registerMaterial(Blocks.CYAN_WOOL);
        beddingReg.registerMaterial(Blocks.PURPLE_WOOL);
        beddingReg.registerMaterial(Blocks.BLUE_WOOL);
        beddingReg.registerMaterial(Blocks.BROWN_WOOL);
        beddingReg.registerMaterial(Blocks.GREEN_WOOL);
        beddingReg.registerMaterial(Blocks.RED_WOOL);
        beddingReg.registerMaterial(Blocks.BLACK_WOOL);
    }
}
