package doggytalents;

import doggytalents.lib.BlockNames;
import doggytalents.lib.Reference;
import doggytalents.tileentity.TileEntityDogBed;
import doggytalents.tileentity.TileEntityFoodBowl;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(Reference.MOD_ID)
public class ModTileEntities {
	
	public static final TileEntityType<TileEntityDogBed> DOG_BED = null;
	public static final TileEntityType<TileEntityFoodBowl> FOOD_BOWL = null;
	
	@Mod.EventBusSubscriber(modid = Reference.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class Registration {
	    
	    @SubscribeEvent
	    public static void registerTileEntities(final RegistryEvent.Register<TileEntityType<?>> event) {
	    	IForgeRegistry<TileEntityType<?>> tileRegistry = event.getRegistry();
	    	
	    	DoggyTalentsMod.LOGGER.debug("Registering TileEnities");
	        tileRegistry.register(TileEntityType.Builder.create(TileEntityDogBed::new, ModBlocks.DOG_BED).build(null).setRegistryName(BlockNames.DOG_BED));
	        tileRegistry.register(TileEntityType.Builder.create(TileEntityFoodBowl::new, ModBlocks.FOOD_BOWL).build(null).setRegistryName(BlockNames.FOOD_BOWL));
	        DoggyTalentsMod.LOGGER.debug("Finished Registering TileEnities");
	    }
    }
}