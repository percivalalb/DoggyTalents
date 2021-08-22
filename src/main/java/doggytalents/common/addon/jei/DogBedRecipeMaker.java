package doggytalents.common.addon.jei;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import doggytalents.api.DoggyTalentsAPI;
import doggytalents.api.registry.IBeddingMaterial;
import doggytalents.api.registry.ICasingMaterial;
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
        Collection<IBeddingMaterial> beddingMaterials = DoggyTalentsAPI.BEDDING_MATERIAL.getValues();
        Collection<ICasingMaterial>  casingMaterials  = DoggyTalentsAPI.CASING_MATERIAL.getValues();

        List<IShapedRecipe<? extends IInventory>> recipes = new ArrayList<>(beddingMaterials.size() * casingMaterials.size());
        String group = "doggytalents.dogbed";
        for (IBeddingMaterial beddingId : DoggyTalentsAPI.BEDDING_MATERIAL.getValues()) {
            for (ICasingMaterial casingId : DoggyTalentsAPI.CASING_MATERIAL.getValues()) {

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