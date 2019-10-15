package doggytalents.api.inferface;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;

public interface IDogBedRegistry {

    /**
     * Creates and registers an IBedMaterial instance
     * @param block Used for the material id and crafting item
     * @param meta Used for the material id and crafting item
     * @param textureLocation The resource location of the texture to be used
     * @return The created IBedMaterial instance
     */
    public IBedMaterial registerMaterial(@Nonnull Block block, int meta, ResourceLocation textureLocation);

    /**
     * Registers an IBedMaterial instance
     */
    public IBedMaterial registerMaterial(@Nonnull IBedMaterial material);
}
