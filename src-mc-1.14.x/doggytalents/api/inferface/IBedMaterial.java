package doggytalents.api.inferface;

import net.minecraft.item.crafting.Ingredient;

public interface IBedMaterial {

    public static IBedMaterial NULL = new IBedMaterial() {
        @Override
        public String getTexture() { return "doggytalents:missing_dog_bed"; }

        @Override
        public String getTranslationKey() { return "dogbed.null"; }

        @Override
        public String getSaveId() { return "missing"; } 
    };
    
    public static IBedMaterial getHolder(String id) {
        return new IBedMaterial() {
            @Override
            public String getTexture() { return "doggytalents:missing_dog_bed"; }
            
            @Override
            public String getTranslationKey() { return "dogbed.missing"; }

            @Override
            public String getSaveId() { return id; }
            
            @Override
            public boolean isValid() { return false; }
        };
    }
    
    public String getTexture();
    public String getTranslationKey();
    public String getSaveId();
    
    default Ingredient getIngredients() {
        return Ingredient.EMPTY;
    }
    
    default boolean isValid() {
        return this != NULL;
    }
    
    default IBedMaterial setRegName(String regName) {
        return this;
    }
}
