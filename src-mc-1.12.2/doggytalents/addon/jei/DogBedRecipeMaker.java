package doggytalents.addon.jei;

import java.util.ArrayList;
import java.util.List;

import doggytalents.api.inferface.IBedMaterial;
import doggytalents.block.DogBedRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.util.NonNullList;

public final class DogBedRecipeMaker {

    public static List<ShapedRecipes> createDogBedRecipes() {
        List<ShapedRecipes> recipes = new ArrayList<>();
        String group = "doggytalents.dogbed";
        for(IBedMaterial beddingId : DogBedRegistry.BEDDINGS.getKeys()) {
            for(IBedMaterial casingId : DogBedRegistry.CASINGS.getKeys()) {

                Ingredient beddingIngredient = beddingId.getIngredient();
                Ingredient casingIngredient = casingId.getIngredient();
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