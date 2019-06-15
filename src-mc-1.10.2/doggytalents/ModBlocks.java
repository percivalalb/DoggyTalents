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
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModBlocks {

	public static Block DOG_BED = null;
	public static Block DOG_BATH = null;
    public static Block FOOD_BOWL = null;

    @Mod.EventBusSubscriber
    public static class Registration {

	    @SubscribeEvent
	    public static void registerBlocks(final RegistryEvent.Register<Block> event) {
	       	IForgeRegistry<Block> blockRegistry = event.getRegistry();
	       	MinecraftForge.EVENT_BUS.post(new BeddingRegistryEvent());
	       	
	       	
			DoggyTalents.LOGGER.info("Registering Blocks");
			blockRegistry.register(DOG_BED = new BlockDogBed().setUnlocalizedName(BlockNames.DOG_BED.replace(":", ".")).setRegistryName(BlockNames.DOG_BED));
		    blockRegistry.register(DOG_BATH = new BlockDogBath().setUnlocalizedName(BlockNames.DOG_BATH.replace(":", ".")).setRegistryName(BlockNames.DOG_BATH));
		    blockRegistry.register(FOOD_BOWL = new BlockFoodBowl().setUnlocalizedName(BlockNames.FOOD_BOWL.replace(":", ".")).setRegistryName(BlockNames.FOOD_BOWL));
			DoggyTalents.LOGGER.info("Finished Registering Blocks");
		    
		    
			ModTileEntities.Registration.registerTileEntities();
		}
	    
	    @SubscribeEvent
		public static void onRegisterItem(RegistryEvent.Register<Item> registry) {
			DoggyTalents.LOGGER.info("Registering ItemBlocks");
			registry.getRegistry().register(makeItemBlock(DOG_BED));
			registry.getRegistry().register(makeItemBlock(DOG_BATH));
			registry.getRegistry().register(makeItemBlock(FOOD_BOWL));
			DoggyTalents.LOGGER.info("Finished Registering ItemBlocks");
		}
		
		
		@SubscribeEvent
		@SideOnly(Side.CLIENT)
		public static void setItemModels(ModelRegistryEvent event) {
			ModelHelper.setDefaultModel(DOG_BATH);
			ModelHelper.setDefaultModel(DOG_BED);
			ModelHelper.setDefaultModel(FOOD_BOWL);
		}
    }
	
	
	private static ItemBlock makeItemBlock(Block block) {
        return (ItemBlock)new ItemBlock(block).setRegistryName(block.getRegistryName());
    }
}
