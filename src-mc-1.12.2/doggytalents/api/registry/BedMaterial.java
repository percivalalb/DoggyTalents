package doggytalents.api.registry;

import javax.annotation.Nullable;

import doggytalents.api.inferface.IBedMaterial;
import net.minecraft.block.Block;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class BedMaterial implements IBedMaterial {

    @Nullable
    private String translationKey;
    
    public ResourceLocation key;
    public int meta;
    public ResourceLocation texture;
    public Ingredient ingredients;
    public String regName;
    
    public BedMaterial(ResourceLocation key, int meta, ResourceLocation texture, Ingredient ingredients) {
        this.key = key;
        this.meta = meta;
        this.texture = texture;
        this.ingredients = ingredients;
    }
    
    public BedMaterial(Block block, int meta, ResourceLocation texture, Ingredient ingredients) {
        this(ForgeRegistries.BLOCKS.getKey(block), meta, texture, ingredients);
    }

    @Override
    public ResourceLocation getTexture() {
        return this.texture;
    }
    
    @Override
    public Ingredient getIngredients() {
        return this.ingredients;
    }
    
    @Override
    public String getTranslationKey() {
        if(this.translationKey == null) {
            this.translationKey = BedMaterial.makeTranslationKey("dogbed." + this.regName, this.key) + "." + this.meta;
        }
        return this.translationKey;
    }

    // 1.13 function Util.make(String, ResourceLocation)
     public static String makeTranslationKey(String type, @Nullable ResourceLocation id) {
         return id == null ? type + ".unregistered_sadface" : type + '.' + id.getNamespace() + '.' + id.getPath().replace('/', '.');
     }
    
    @Override
    public String getSaveId() {
        return this.key.toString() + "." + this.meta;
    }
    
    @Override
    public boolean equals(Object o) {
        if(!(o instanceof BedMaterial))
            return false;
        BedMaterial other = (BedMaterial)o;
        return other.getSaveId().equals(this.getSaveId());
    }
    
    @Override
    public int hashCode() {
        return this.getSaveId().hashCode();
    }
    
    public BedMaterial setRegName(String regName) {
        this.regName = regName;
        return this;
    }
}
