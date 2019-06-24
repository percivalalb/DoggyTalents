package doggytalents.addon.jei;

import java.util.ArrayList;
import java.util.List;

import doggytalents.api.registry.BedMaterial;
import doggytalents.api.registry.DogBedRegistry;
import doggytalents.lib.Reference;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.IShapedRecipe;

public final class DogBedRecipeMaker {

	public static List<IShapedRecipe<? extends IInventory>> createDogBedRecipes() {
		List<IShapedRecipe<? extends IInventory>> recipes = new ArrayList<>();
		String group = "doggytalents.dogbed";
		for(BedMaterial beddingId : DogBedRegistry.BEDDINGS.getKeys()) {
			for(BedMaterial casingId : DogBedRegistry.CASINGS.getKeys()) {
			    
				Ingredient beddingIngredient = beddingId.getIngredients();
				Ingredient casingIngredient = casingId.getIngredients();
				NonNullList<Ingredient> inputs = NonNullList.from(Ingredient.EMPTY,
					casingIngredient, beddingIngredient, casingIngredient,
					casingIngredient, beddingIngredient, casingIngredient,
					casingIngredient, casingIngredient, casingIngredient
				);
				ItemStack output = DogBedRegistry.createItemStack(casingId, beddingId);
				
				ResourceLocation id = new ResourceLocation(Reference.MOD_ID, "doggytalents.dogbed" + output.getTranslationKey());
				ShapedRecipe recipe = new ShapedRecipe(id, group, 3, 3, inputs, output);
				recipes.add(recipe);
			}
		}
		return recipes;
	}
}