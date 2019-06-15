package doggytalents;

import doggytalents.lib.Reference;
import doggytalents.lib.ResourceLib;
import doggytalents.tileentity.TileEntityDogBed;
import doggytalents.tileentity.TileEntityFoodBowl;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

@ObjectHolder(Reference.MOD_ID)
public class ModTileEntities {

    public static class Registration {
	    
	    public static void registerTileEntities() {
	    	DoggyTalents.LOGGER.info("Registering TileEnities");
	    	GameRegistry.registerTileEntity(TileEntityDogBed.class, ResourceLib.get("dog_bed").toString());
			GameRegistry.registerTileEntity(TileEntityFoodBowl.class, ResourceLib.get("dog_bowl").toString());
	        DoggyTalents.LOGGER.info("Finished Registering TileEnities");
	    }
    }
}