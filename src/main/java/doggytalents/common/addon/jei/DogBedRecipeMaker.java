package doggytalents.common.addon.jei;

import java.util.List;

import com.google.common.collect.Lists;

import doggytalents.api.DoggyTalentsAPI;
import doggytalents.api.registry.BeddingMaterial;
import doggytalents.api.registry.CasingMaterial;
import doggytalents.common.util.DogBedUtil;
import doggytalents.common.util.Util;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.IShapedRecipe;

public final class DogBedRecipeMaker {

    public static List<IShapedRecipe<? extends IInventory>> createDogBedRecipes() {
        List<IShapedRecipe<? extends IInventory>> recipes = Lists.newArrayList();
        String group = "doggytalents.dogbed";
        for(BeddingMaterial beddingId : DoggyTalentsAPI.BEDDING_MATERIAL.getValues()) {
            for(CasingMaterial casingId : DoggyTalentsAPI.CASING_MATERIAL.getValues()) {

                Ingredient beddingIngredient = beddingId.getIngredient();
                Ingredient casingIngredient = casingId.getIngredient();
                NonNullList<Ingredient> inputs = NonNullList.from(Ingredient.EMPTY,
                    casingIngredient, beddingIngredient, casingIngredient,
                    casingIngredient, beddingIngredient, casingIngredient,
                    casingIngredient, casingIngredient, casingIngredient
                );
                ItemStack output = DogBedUtil.createItemStack(casingId, beddingId);

                ResourceLocation id = Util.getResource("" + output.getTranslationKey()); //TODO update resource location
                ShapedRecipe recipe = new ShapedRecipe(id, group, 3, 3, inputs, output);
                recipes.add(recipe);
            }
        }
        return recipes;
    }
}