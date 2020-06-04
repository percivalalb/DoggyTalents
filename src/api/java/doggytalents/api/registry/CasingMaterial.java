package doggytalents.api.registry;

import java.util.function.Supplier;

import javax.annotation.Nullable;

import doggytalents.api.DoggyTalentsAPI;
import net.minecraft.block.Block;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class CasingMaterial extends ForgeRegistryEntry<CasingMaterial> {

    private final Supplier<Block> block;
    private ResourceLocation texture;

    @Nullable
    private String translationKey;

    public CasingMaterial(Supplier<Block> blockIn) {
        this.block = blockIn;
    }

    /**
     * Texture location that for material, eg 'minecraft:block/white_wool'
     */
    public ResourceLocation getTexture() {
        if (this.texture == null) {
            ResourceLocation loc = this.block.get().getRegistryName();
            this.texture = new ResourceLocation(loc.getNamespace(), "block/" + loc.getPath());
        }

        return this.texture;
    }

    /**
     * The translation key using for the tooltip
     */
    public ITextComponent getTooltip() {
        if (this.translationKey == null) {
            this.translationKey = Util.makeTranslationKey("dogbed.casing", DoggyTalentsAPI.CASING_MATERIAL.getKey(this));
        }

        return new TranslationTextComponent(this.translationKey);
    }

    /**
     * The ingredient used in the crafting recipe of the bed
     */
    public Ingredient getIngredient() {
        return Ingredient.fromItems(this.block.get());
    }
}
