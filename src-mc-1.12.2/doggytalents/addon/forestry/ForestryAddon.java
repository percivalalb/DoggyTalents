package doggytalents.addon.forestry;

import doggytalents.addon.AddonEvent;
import doggytalents.addon.AddonManager;
import doggytalents.api.registry.DogBedRegistry;
import doggytalents.lib.ConfigValues;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author ProPercivalalb
 */
public class ForestryAddon {

    @SubscribeEvent
    public void onPost(AddonEvent.Post event) throws Exception {
        if(!AddonManager.areModsLoaded(ForestryLib.MOD_NAME) || !ConfigValues.MOD_BED_STUFF)
            return;
        
        Block planks1 = Block.getBlockFromName(ForestryLib.ITEM_NAME_1);
        Block planks2 = Block.getBlockFromName(ForestryLib.ITEM_NAME_2);
        
        for(int i = 0; i < ForestryLib.PLANKS_1_TEXURE.length; i++)
            DogBedRegistry.CASINGS.registerMaterial(planks1, i, new ResourceLocation(ForestryLib.TEXTURE_LOCATION + ForestryLib.PLANKS_1_TEXURE[i]));
        
        for(int i = 0; i < ForestryLib.PLANKS_2_TEXURE.length; i++)
            DogBedRegistry.CASINGS.registerMaterial(planks2, i, new ResourceLocation(ForestryLib.TEXTURE_LOCATION + ForestryLib.PLANKS_2_TEXURE[i]));
        
    }
}
