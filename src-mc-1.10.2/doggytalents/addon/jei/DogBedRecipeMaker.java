package doggytalents.addon.jei;

import java.util.ArrayList;
import java.util.List;

import doggytalents.api.registry.DogBedRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapedRecipes;

public final class DogBedRecipeMaker {

	public static List<ShapedRecipes> createDogBedRecipes() {
		List<ShapedRecipes> recipes = new ArrayList<>();
		String group = "doggytalents.dogbed";
		for(String beddingId : DogBedRegistry.BEDDINGS.getKeys()) {
			for(String casingId : DogBedRegistry.CASINGS.getKeys()) {

				ItemStack beddingStack = DogBedRegistry.BEDDINGS.getCraftingItemFromId(beddingId).getStack();
				ItemStack casingStack = DogBedRegistry.CASINGS.getCraftingItemFromId(casingId).getStack();

				ItemStack[] inputs = new ItemStack[] {
						casingStack, beddingStack, casingStack,
						casingStack, beddingStack, casingStack,
						casingStack, casingStack, casingStack
				};
				ItemStack output = DogBedRegistry.createItemStack(casingId, beddingId);
				
				ShapedRecipes recipe = new ShapedRecipes(3, 3, inputs, output);
				recipes.add(recipe);
			}
		}
		return recipes;
	}
}