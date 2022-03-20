package doggytalents;

import doggytalents.common.inventory.recipe.DogBedRecipe;
import doggytalents.common.lib.Constants;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Function;
import java.util.function.Supplier;

public class DoggyRecipeSerializers {

    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.Keys.RECIPE_SERIALIZERS, Constants.MOD_ID);

    public static final RegistryObject<SimpleRecipeSerializer<DogBedRecipe>> DOG_BED = register("dog_bed", DogBedRecipe::new);
//    public static final RegistryObject<SpecialRecipeSerializer<DogCollarRecipe>> COLLAR_COLOURING = register("collar_colouring", DogCollarRecipe::new);
//    public static final RegistryObject<SpecialRecipeSerializer<DogCapeRecipe>> CAPE_COLOURING = register("cape_colouring", DogCapeRecipe::new);

    private static <R extends Recipe<?>, T extends RecipeSerializer<R>> RegistryObject<SimpleRecipeSerializer<R>> register(final String name, Function<ResourceLocation, R> factory) {
        return register(name, () -> new SimpleRecipeSerializer<>(factory));
    }

    private static <T extends RecipeSerializer<?>> RegistryObject<T> register(final String name, final Supplier<T> sup) {
        return RECIPE_SERIALIZERS.register(name, sup);
    }
}

