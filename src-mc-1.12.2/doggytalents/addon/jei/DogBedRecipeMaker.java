package doggytalents.addon.jei;

import java.util.ArrayList;
import java.util.List;

import doggytalents.api.registry.DogBedRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.util.NonNullList;

public final class DogBedRecipeMaker {

	public static List<ShapedRecipes> createDogBedRecipes() {
		List<ShapedRecipes> recipes = new ArrayList<>();
		String group = "doggytalents.dogbed";
		for(String beddingId : DogBedRegistry.BEDDINGS.getKeys()) {
			for(String casingId : DogBedRegistry.CASINGS.getKeys()) {

				ItemStack beddingStack = DogBedRegistry.BEDDINGS.getCraftingItemFromId(beddingId).getStack();
				ItemStack casingStack = DogBedRegistry.CASINGS.getCraftingItemFromId(casingId).getStack();

				Ingredient beddingIngredient = Ingredient.fromStacks(beddingStack);
				Ingredient casingIngredient = Ingredient.fromStacks(casingStack);
				NonNullList<Ingredient> inputs = NonNullList.from(Ingredient.EMPTY,
					casingIngredient, beddingIngredient, casingIngredient,
					casingIngredient, beddingIngredient, casingIngredient,
					casingIngredient, casingIngredient, casingIngredient
				);
				ItemStack output = DogBedRegistry.createItemStack(casingId, beddingId);
				
				ShapedRecipes recipe = new ShapedRecipes(group, 3, 3, inputs, output);
				recipes.add(recipe);
			}
		}
		return recipes;
	}
}