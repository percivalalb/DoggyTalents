package doggytalents;

import doggytalents.api.lib.Reference;
import doggytalents.lib.ResourceLib;
import doggytalents.tileentity.TileEntityDogBed;
import doggytalents.tileentity.TileEntityFoodBowl;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

@ObjectHolder(Reference.MOD_ID)
public class ModTileEntities {

    public static class Registration {
        
        public static void registerTileEntities() {
            DoggyTalents.LOGGER.debug("Registering TileEnities");
            GameRegistry.registerTileEntity(TileEntityDogBed.class, ResourceLib.get("dog_bed"));
            GameRegistry.registerTileEntity(TileEntityFoodBowl.class, ResourceLib.get("dog_bowl"));
            DoggyTalents.LOGGER.debug("Finished Registering TileEnities");
        }
    }
}