package doggytalents.api.impl;

import doggytalents.api.registry.IBeddingMaterial;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;

public class MissingBeddingMaterial extends IBeddingMaterial {

    public static final IBeddingMaterial NULL = new MissingBeddingMaterial();
    private static final ResourceLocation MISSING_TEXTURE = new ResourceLocation("missingno");

    @Override
    public ResourceLocation getTexture() {
        return MissingBeddingMaterial.MISSING_TEXTURE;
    }

    @Override
    public Component getTooltip() {
        return Component.translatable("dogbed.bedding.missing", "//TODO");
    }

    @Override
    public Ingredient getIngredient() {
        return Ingredient.EMPTY;
    }
}
