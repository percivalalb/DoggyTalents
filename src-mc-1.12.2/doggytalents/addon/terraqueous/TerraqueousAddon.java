package doggytalents.addon.terraqueous;

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
public class TerraqueousAddon {

    @SubscribeEvent
    public void onPost(AddonEvent.Post event) throws Exception {
        if(!AddonManager.areModsLoaded(TerraqueousLib.MOD_NAME) || !ConfigValues.MOD_BED_STUFF)
            return;
        
        Block planks1 = Block.getBlockFromName(TerraqueousLib.ITEM_NAME);
        
        for(int i = 0; i < TerraqueousLib.PLANKS_TEXURE.length; i++)
            DogBedRegistry.CASINGS.registerMaterial(planks1, i, new ResourceLocation(TerraqueousLib.TEXTURE_LOCATION + TerraqueousLib.PLANKS_TEXURE[i]));
    }
}
