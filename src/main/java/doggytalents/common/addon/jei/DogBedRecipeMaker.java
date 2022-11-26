package doggytalents.common.addon.jei;

import doggytalents.api.DoggyTalentsAPI;
import doggytalents.api.registry.IBeddingMaterial;
import doggytalents.api.registry.ICasingMaterial;
import doggytalents.common.util.DogBedUtil;
import doggytalents.common.util.Util;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class DogBedRecipeMaker {

    public static List<CraftingRecipe> createDogBedRecipes() {
        Collection<IBeddingMaterial> beddingMaterials = DoggyTalentsAPI.BEDDING_MATERIAL.get().getValues();
        Collection<ICasingMaterial>  casingMaterials  = DoggyTalentsAPI.CASING_MATERIAL.get().getValues();

        List<CraftingRecipe> recipes = new ArrayList<>(beddingMaterials.size() * casingMaterials.size());
        String group = "doggytalents.dogbed";
        for (IBeddingMaterial beddingId : DoggyTalentsAPI.BEDDING_MATERIAL.get().getValues()) {
            for (ICasingMaterial casingId : DoggyTalentsAPI.CASING_MATERIAL.get().getValues()) {

                Ingredient beddingIngredient = beddingId.getIngredient();
                Ingredient casingIngredient = casingId.getIngredient();
                NonNullList<Ingredient> inputs = NonNullList.of(Ingredient.EMPTY,
                    casingIngredient, beddingIngredient, casingIngredient,
                    casingIngredient, beddingIngredient, casingIngredient,
                    casingIngredient, casingIngredient, casingIngredient
                );
                ItemStack output = DogBedUtil.createItemStack(casingId, beddingId);

                ResourceLocation id = Util.getResource("" + output.getDescriptionId()); //TODO update resource location
                ShapedRecipe recipe = new ShapedRecipe(id, group, 3, 3, inputs, output);
                recipes.add(recipe);
            }
        }
        return recipes;
    }
}
