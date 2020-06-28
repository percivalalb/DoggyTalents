package doggytalents;

import doggytalents.api.impl.BeddingMaterial;
import doggytalents.api.impl.CasingMaterial;
import doggytalents.api.registry.IBeddingMaterial;
import doggytalents.api.registry.ICasingMaterial;
import doggytalents.common.lib.Constants;
import net.minecraft.block.Blocks;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(Constants.VANILLA_ID)
public class DoggyBedMaterials {

    public static final ICasingMaterial OAK_PLANKS = null;
    public static final ICasingMaterial SPRUCE_PLANKS = null;
    public static final ICasingMaterial BIRCH_PLANKS = null;
    public static final ICasingMaterial JUNGLE_PLANKS = null;
    public static final ICasingMaterial ACACIA_PLANKS = null;
    public static final ICasingMaterial DARK_OAK_PLANKS = null;

    public static final IBeddingMaterial WHITE_WOOL = null;
    public static final IBeddingMaterial ORANGE_WOOL = null;
    public static final IBeddingMaterial MAGENTA_WOOL = null;
    public static final IBeddingMaterial LIGHT_BLUE_WOOL = null;
    public static final IBeddingMaterial YELLOW_WOOL = null;
    public static final IBeddingMaterial LIME_WOOL = null;
    public static final IBeddingMaterial PINK_WOOL = null;
    public static final IBeddingMaterial GRAY_WOOL = null;
    public static final IBeddingMaterial LIGHT_GRAY_WOOL = null;
    public static final IBeddingMaterial CYAN_WOOL = null;
    public static final IBeddingMaterial PURPLE_WOOL = null;
    public static final IBeddingMaterial BLUE_WOOL = null;
    public static final IBeddingMaterial BROWN_WOOL = null;
    public static final IBeddingMaterial GREEN_WOOL = null;
    public static final IBeddingMaterial RED_WOOL = null;
    public static final IBeddingMaterial BLACK_WOOL = null;

    public static final void registerCasings(final RegistryEvent.Register<ICasingMaterial> event) {
        IForgeRegistry<ICasingMaterial> casingRegistry = event.getRegistry();
        casingRegistry.register(new CasingMaterial(Blocks.OAK_PLANKS.delegate).setRegistryName(Blocks.OAK_PLANKS.getRegistryName()));
        casingRegistry.register(new CasingMaterial(Blocks.SPRUCE_PLANKS.delegate).setRegistryName(Blocks.SPRUCE_PLANKS.getRegistryName()));
        casingRegistry.register(new CasingMaterial(Blocks.BIRCH_PLANKS.delegate).setRegistryName(Blocks.BIRCH_PLANKS.getRegistryName()));
        casingRegistry.register(new CasingMaterial(Blocks.JUNGLE_PLANKS.delegate).setRegistryName(Blocks.JUNGLE_PLANKS.getRegistryName()));
        casingRegistry.register(new CasingMaterial(Blocks.ACACIA_PLANKS.delegate).setRegistryName(Blocks.ACACIA_PLANKS.getRegistryName()));
        casingRegistry.register(new CasingMaterial(Blocks.DARK_OAK_PLANKS.delegate).setRegistryName(Blocks.DARK_OAK_PLANKS.getRegistryName()));
    }

    public static final void registerBeddings(final RegistryEvent.Register<IBeddingMaterial> event) {
        IForgeRegistry<IBeddingMaterial> beddingRegistry = event.getRegistry();
        beddingRegistry.register(new BeddingMaterial(Blocks.WHITE_WOOL.delegate).setRegistryName(Blocks.WHITE_WOOL.getRegistryName()));
        beddingRegistry.register(new BeddingMaterial(Blocks.ORANGE_WOOL.delegate).setRegistryName(Blocks.ORANGE_WOOL.getRegistryName()));
        beddingRegistry.register(new BeddingMaterial(Blocks.MAGENTA_WOOL.delegate).setRegistryName(Blocks.MAGENTA_WOOL.getRegistryName()));
        beddingRegistry.register(new BeddingMaterial(Blocks.LIGHT_BLUE_WOOL.delegate).setRegistryName(Blocks.LIGHT_BLUE_WOOL.getRegistryName()));
        beddingRegistry.register(new BeddingMaterial(Blocks.YELLOW_WOOL.delegate).setRegistryName(Blocks.YELLOW_WOOL.getRegistryName()));
        beddingRegistry.register(new BeddingMaterial(Blocks.LIME_WOOL.delegate).setRegistryName(Blocks.LIME_WOOL.getRegistryName()));
        beddingRegistry.register(new BeddingMaterial(Blocks.PINK_WOOL.delegate).setRegistryName(Blocks.PINK_WOOL.getRegistryName()));
        beddingRegistry.register(new BeddingMaterial(Blocks.GRAY_WOOL.delegate).setRegistryName(Blocks.GRAY_WOOL.getRegistryName()));
        beddingRegistry.register(new BeddingMaterial(Blocks.LIGHT_GRAY_WOOL.delegate).setRegistryName(Blocks.LIGHT_GRAY_WOOL.getRegistryName()));
        beddingRegistry.register(new BeddingMaterial(Blocks.CYAN_WOOL.delegate).setRegistryName(Blocks.CYAN_WOOL.getRegistryName()));
        beddingRegistry.register(new BeddingMaterial(Blocks.PURPLE_WOOL.delegate).setRegistryName(Blocks.PURPLE_WOOL.getRegistryName()));
        beddingRegistry.register(new BeddingMaterial(Blocks.BLUE_WOOL.delegate).setRegistryName(Blocks.BLUE_WOOL.getRegistryName()));
        beddingRegistry.register(new BeddingMaterial(Blocks.BROWN_WOOL.delegate).setRegistryName(Blocks.BROWN_WOOL.getRegistryName()));
        beddingRegistry.register(new BeddingMaterial(Blocks.GREEN_WOOL.delegate).setRegistryName(Blocks.GREEN_WOOL.getRegistryName()));
        beddingRegistry.register(new BeddingMaterial(Blocks.RED_WOOL.delegate).setRegistryName(Blocks.RED_WOOL.getRegistryName()));
        beddingRegistry.register(new BeddingMaterial(Blocks.BLACK_WOOL.delegate).setRegistryName(Blocks.BLACK_WOOL.getRegistryName()));
    }
}
