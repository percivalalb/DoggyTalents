package doggytalents;

import java.util.function.Function;
import java.util.function.Supplier;

import doggytalents.inventory.recipe.RecipeDogBed;
import doggytalents.inventory.recipe.RecipeDogCape;
import doggytalents.inventory.recipe.RecipeDogCollar;
import doggytalents.lib.Reference;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModRecipes {

    public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZERS = new DeferredRegister<>(ForgeRegistries.RECIPE_SERIALIZERS, Reference.MOD_ID);

    public static final RegistryObject<SpecialRecipeSerializer<RecipeDogBed>> DOG_BED = register("dog_bed", RecipeDogBed::new);
    public static final RegistryObject<SpecialRecipeSerializer<RecipeDogCollar>> COLLAR_COLOURING = register("collar_colouring", RecipeDogCollar::new);
    public static final RegistryObject<SpecialRecipeSerializer<RecipeDogCape>> CAPE_COLOURING = register("cape_colouring", RecipeDogCape::new);

    private static <R extends IRecipe<?>, T extends IRecipeSerializer<R>> RegistryObject<SpecialRecipeSerializer<R>> register(final String name, Function<ResourceLocation, R> factory) {
        return register(name, () -> new SpecialRecipeSerializer<>(factory));
    }

    private static <T extends IRecipeSerializer<?>> RegistryObject<T> register(final String name, final Supplier<T> sup) {
        return RECIPE_SERIALIZERS.register(name, sup);
    }
}

