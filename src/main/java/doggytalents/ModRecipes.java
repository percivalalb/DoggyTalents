package doggytalents;

import doggytalents.api.lib.Reference;
import doggytalents.inventory.recipe.RecipeDogBed;
import doggytalents.inventory.recipe.RecipeDogCape;
import doggytalents.inventory.recipe.RecipeDogCollar;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * 1.12 Code
 */
@EventBusSubscriber(modid = Reference.MOD_ID)
public class ModRecipes {
    
    //All of the default crafting recipes now are contained in json files
    
    @SubscribeEvent
    public static void onRegister(RegistryEvent.Register<IRecipe> event) {
        event.getRegistry().register(new RecipeDogBed().setRegistryName(Reference.MOD_ID + ":dog_bed"));
        event.getRegistry().register(new RecipeDogCollar().setRegistryName(Reference.MOD_ID + ":collar_colouring"));
        event.getRegistry().register(new RecipeDogCape().setRegistryName(Reference.MOD_ID + ":cape_colouring"));
    }
}
