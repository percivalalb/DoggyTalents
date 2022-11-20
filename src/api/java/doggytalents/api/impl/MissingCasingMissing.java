package doggytalents.api.impl;

import doggytalents.api.registry.ICasingMaterial;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;

public class MissingCasingMissing extends ICasingMaterial {

    public static final ICasingMaterial NULL = new MissingCasingMissing();
    private static final ResourceLocation MISSING_TEXTURE = new ResourceLocation("missingno");

    @Override
    public ResourceLocation getTexture() {
        return MissingCasingMissing.MISSING_TEXTURE;
    }

    @Override
    public Component getTooltip() {
        return Component.translatable("dogbed.casing.missing", "//TODO");
    }

    @Override
    public Ingredient getIngredient() {
        return Ingredient.EMPTY;
    }
}
