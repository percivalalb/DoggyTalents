package doggytalents;

import doggytalents.api.BeddingRegistryEvent;
import doggytalents.block.BlockDogBath;
import doggytalents.block.BlockDogBed;
import doggytalents.block.BlockFoodBowl;
import doggytalents.client.model.ModelHelper;
import doggytalents.lib.BlockNames;
import doggytalents.lib.Reference;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

@ObjectHolder(Reference.MOD_ID)
public class ModBlocks {

    public static final Block DOG_BED = null;
    public static final Block DOG_BATH = null;
    //public static final Block WATER_BOWL = null;
    public static final Block FOOD_BOWL = null;

    @Mod.EventBusSubscriber(modid = Reference.MOD_ID)
    public static class Registration {

        @SubscribeEvent
        public static void registerBlocks(final RegistryEvent.Register<Block> event) {
            IForgeRegistry<Block> blockRegistry = event.getRegistry();
            MinecraftForge.EVENT_BUS.post(new BeddingRegistryEvent());
               
               
            DoggyTalents.LOGGER.debug("Registering Blocks");
            blockRegistry.register(new BlockDogBed().setTranslationKey(BlockNames.DOG_BED.replace(":", ".")).setRegistryName(BlockNames.DOG_BED));
            blockRegistry.register(new BlockDogBath().setTranslationKey(BlockNames.DOG_BATH.replace(":", ".")).setRegistryName(BlockNames.DOG_BATH));
            //blockRegistry.register(new BlockWaterBowl().setTranslationKey(BlockNames.WATER_BOWL.replace(":", ".")).setRegistryName(BlockNames.WATER_BOWL));
            blockRegistry.register(new BlockFoodBowl().setTranslationKey(BlockNames.FOOD_BOWL.replace(":", ".")).setRegistryName(BlockNames.FOOD_BOWL));
            DoggyTalents.LOGGER.debug("Finished Registering Blocks");
            
            
            ModTileEntities.Registration.registerTileEntities();
        }
        
        @SubscribeEvent
        public static void onRegisterItem(RegistryEvent.Register<Item> registry) {
            DoggyTalents.LOGGER.debug("Registering ItemBlocks");
            registry.getRegistry().register(makeItemBlock(DOG_BED).setHasSubtypes(true));
            registry.getRegistry().register(makeItemBlock(DOG_BATH));
            //registry.getRegistry().register(makeItemBlock(WATER_BOWL));
            registry.getRegistry().register(makeItemBlock(FOOD_BOWL));
            DoggyTalents.LOGGER.debug("Finished Registering ItemBlocks");
        }
        
        
        @SubscribeEvent
        @SideOnly(Side.CLIENT)
        public static void setItemModels(ModelRegistryEvent event) {
            ModelHelper.setDefaultModel(DOG_BATH);
            ModelHelper.setDefaultModel(DOG_BED);
            //ModelHelper.setDefaultModel(WATER_BOWL);
            ModelHelper.setDefaultModel(FOOD_BOWL);
        }
    }
    
    
    private static ItemBlock makeItemBlock(Block block) {
        return (ItemBlock)new ItemBlock(block).setRegistryName(block.getRegistryName());
    }
}
