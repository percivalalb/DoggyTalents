package doggytalents.base.f;

import doggytalents.lib.Reference;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * 1.12 Code
 */
@EventBusSubscriber(modid = Reference.MOD_ID)
public class CraftingRecipes {
	
	//All of the default crafting recipes now are contained in json files
	
	@SubscribeEvent
	public static void onRegister(RegistryEvent.Register<IRecipe> event) {
		event.getRegistry().register(new RecipeDogBed().setRegistryName(Reference.MOD_ID + ":dogbed"));
		event.getRegistry().register(new RecipeDogCollar().setRegistryName(Reference.MOD_ID + ":dogcollar"));
		event.getRegistry().register(new RecipeDogCape().setRegistryName(Reference.MOD_ID + ":dogcape"));
	}
}
