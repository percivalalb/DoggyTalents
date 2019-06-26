package doggytalents.block;

import javax.annotation.Nullable;

import doggytalents.api.inferface.IBedMaterial;
import net.minecraft.block.Block;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.registries.ForgeRegistries;

public class BedMaterial implements IBedMaterial {

    @Nullable
    private String translationKey;
    
    public ResourceLocation key;
    public String textureLoc;
    public Ingredient ingredients;
    public String regName;
    
    public BedMaterial(ResourceLocation key, ResourceLocation texture, Ingredient ingredients) {
        this.key = key;
        this.textureLoc = texture.toString();
        this.ingredients = ingredients;
    }
    
    public BedMaterial(Block block, ResourceLocation texture, Ingredient ingredients) {
        this(ForgeRegistries.BLOCKS.getKey(block), texture, ingredients);
    }

    @Override
    public String getTexture() {
        return this.textureLoc;
    }
    
    @Override
    public Ingredient getIngredient() {
        return this.ingredients;
    }
    
    @Override
    public ITextComponent getTooltip() {
        if(this.translationKey == null) {
            this.translationKey = Util.makeTranslationKey("dogbed." + this.regName, this.key);
        }
        return new TranslationTextComponent(this.translationKey);
    }
    
    @Override
    public String getSaveId() {
        return this.key.toString();
    }
    
    @Override
    public boolean equals(Object o) {
        if(this == o) {
            return true;
        } else if(!(o instanceof BedMaterial)) {
            return false;
        }
        
        BedMaterial other = (BedMaterial)o;
        return other.key.equals(this.key);
    }
    
    @Override
    public int hashCode() {
        return this.key.hashCode();
    }
    
    @Override
    public BedMaterial setRegName(String regName) {
        this.regName = regName;
        return this;
    }
}
