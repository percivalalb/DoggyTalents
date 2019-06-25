package doggytalents.api.inferface;

import doggytalents.lib.Reference;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

public interface IBedMaterial {

    public static IBedMaterial NULL = new IBedMaterial() {
        @Override
        public ResourceLocation getTexture() { return new ResourceLocation(Reference.MOD_ID, "missing_dog_bed"); }

        @Override
        public String getTranslationKey() { return "dogbed.null"; }

        @Override
        public String getSaveId() { return "missing"; } 
    };
    
    public static IBedMaterial getHolder(String id) {
        return new IBedMaterial() {
            @Override
            public ResourceLocation getTexture() { return new ResourceLocation(Reference.MOD_ID, "missing_dog_bed"); }
            
            @Override
            public String getTranslationKey() { return "dogbed.missing"; }

            @Override
            public String getSaveId() { return id; }
            
            @Override
            public boolean isValid() { return false; }
        };
    }
    
    public ResourceLocation getTexture();
    default Ingredient getIngredients() {
        return Ingredient.EMPTY;
    }
    public String getTranslationKey();
    public String getSaveId();
    default boolean isValid() {
        return this != NULL;
    }
}
