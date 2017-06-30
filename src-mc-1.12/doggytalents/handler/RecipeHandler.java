package doggytalents.handler;

import doggytalents.ModItems;
import doggytalents.inventory.RecipeDogBed;
import doggytalents.lib.Reference;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.GameData;

@EventBusSubscriber(modid = Reference.MOD_ID)
public class RecipeHandler {

	@SubscribeEvent
	public static void onRegister(RegistryEvent.Register<IRecipe> event) {
		event.getRegistry().register(new RecipeDogBed().setRegistryName(Reference.MOD_ID + ":dogbed"));
	}
	
}
