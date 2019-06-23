package doggytalents.api.inferface;

import doggytalents.lib.Reference;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;

public interface IBedMaterial {

    public static IBedMaterial MISSING = new IBedMaterial() {

        @Override
        public ResourceLocation getTexture() {
            return new ResourceLocation(Reference.MOD_ID, "missing_dog_bed");
        }

        @Override
        public Ingredient getIngredients() {
            return Ingredient.EMPTY;
        }

        @Override
        public String getTranslationKey() {
            return "dogbed.missing";
        }

        @Override
        public String getSaveId() {
            return "missing";
        }
        
    };
    
    public ResourceLocation getTexture();
    public Ingredient getIngredients();
    public String getTranslationKey();
    public String getSaveId();
    default boolean isValid() {
        return this != MISSING;
    }
    
}
