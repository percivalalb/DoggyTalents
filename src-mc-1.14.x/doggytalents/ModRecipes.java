package doggytalents;

import doggytalents.inventory.recipe.RecipeDogBed;
import doggytalents.inventory.recipe.RecipeDogCape;
import doggytalents.inventory.recipe.RecipeDogCollar;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipeSerializer;

public class ModRecipes {
	
	public static IRecipeSerializer<RecipeDogBed> DOG_BED;
	public static IRecipeSerializer<RecipeDogCollar> DOG_COLLAR;
	public static IRecipeSerializer<RecipeDogCape> DOG_CAPE;

    public static class Registration {
		public static void registerRecipes() {
			DOG_BED = IRecipeSerializer.func_222156_a("doggytalents:dogbed", new SpecialRecipeSerializer<>(RecipeDogBed::new));
			DOG_COLLAR = IRecipeSerializer.func_222156_a("doggytalents:dogcollar", new SpecialRecipeSerializer<>(RecipeDogCollar::new));
			DOG_CAPE = IRecipeSerializer.func_222156_a("doggytalents:dogcape", new SpecialRecipeSerializer<>(RecipeDogCape::new));
			
		}
    }

}
