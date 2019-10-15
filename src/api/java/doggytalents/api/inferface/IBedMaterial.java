package doggytalents.api.inferface;

import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

public interface IBedMaterial {

    /**
     * Texture location that for material, eg 'minecraft:block/white_wool'
     */
    public String getTexture();

    /**
     * The translation key using for the tooltip
     */
    public ITextComponent getTooltip();

    /**
     * The string saved to the item NBT and tileentity NBT, used for material lookup.
     * This should be unique and is usually a block registry name, e.g 'minecraft:white_wool'
     */
    public String getSaveId();

    /**
     * The ingredient used in the crafting recipe of the bed
     */
    default Ingredient getIngredient() {
        return Ingredient.EMPTY;
    }

    /**
     * Receives the registry name, in default implementation this is used for tooltip translation key
     */
    default IBedMaterial setRegName(String regName) {
        return this;
    }

    public static IBedMaterial NULL = new IBedMaterial() {
        @Override
        public String getTexture() { return "doggytalents:missing_dog_bed"; }

        @Override
        public ITextComponent getTooltip() {
            return new TextComponentTranslation("dogbed.null").setStyle((new Style()).setColor(TextFormatting.RED));
        }

        @Override
        public String getSaveId() { return "missing"; }
    };

    public static IBedMaterial getHolder(String id) {
        return new IBedMaterial() {
            @Override
            public String getTexture() { return "doggytalents:missing_dog_bed"; }

            @Override
            public ITextComponent getTooltip() {
                return new TextComponentTranslation("dogbed.missing", id).setStyle((new Style()).setColor(TextFormatting.RED));
            }

            @Override
            public String getSaveId() { return id; }
        };
    }
}
