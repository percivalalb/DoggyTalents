package doggytalents;

import doggytalents.block.BlockDogBath;
import doggytalents.block.BlockDogBed;
import doggytalents.block.BlockFoodBowl;
import doggytalents.lib.BlockNames;
import doggytalents.lib.Reference;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.world.biome.BiomeColors;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(Reference.MOD_ID)
public class ModBlocks {

    public static final Block DOG_BED = null;
    public static final Block DOG_BATH = null;
    public static final Block FOOD_BOWL = null;
    
    public static void registerBlocks(final RegistryEvent.Register<Block> event) {
        IForgeRegistry<Block> blockRegistry = event.getRegistry();
        DoggyTalentsMod.LOGGER.debug("Registering Blocks");
        blockRegistry.register(new BlockDogBed().setRegistryName(BlockNames.DOG_BED));
        blockRegistry.register(new BlockDogBath().setRegistryName(BlockNames.DOG_BATH));
        blockRegistry.register(new BlockFoodBowl().setRegistryName(BlockNames.FOOD_BOWL));
        DoggyTalentsMod.LOGGER.debug("Finished Registering Blocks");
    }

    public static void registerItemBlocks(final RegistryEvent.Register<Item> event) {
        DoggyTalentsMod.LOGGER.debug("Registering ItemBlocks");
        event.getRegistry().register(makeItemBlock(DOG_BED, ModCreativeTabs.DOG_BED));
        event.getRegistry().register(makeItemBlock(DOG_BATH));
        event.getRegistry().register(makeItemBlock(FOOD_BOWL));
        DoggyTalentsMod.LOGGER.debug("Finished Registering ItemBlocks");
    }
    
    public static void registerBlockColours(final ColorHandlerEvent.Block event) {
        BlockColors blockColors = event.getBlockColors();
        
        blockColors.register((state, world, pos, tintIndex) -> {
            return world != null && pos != null ? BiomeColors.getWaterColor(world, pos) : -1;
         }, ModBlocks.DOG_BATH);
    }
    
    private static BlockItem makeItemBlock(Block block) {
        return makeItemBlock(block, ModCreativeTabs.GENERAL);
    }
    
    private static BlockItem makeItemBlock(Block block, ItemGroup group) {
        return (BlockItem)new BlockItem(block, new Item.Properties().group(group)).setRegistryName(block.getRegistryName());
    }
}