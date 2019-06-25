package doggytalents.client.model.block;

import javax.annotation.Nonnull;

import doggytalents.api.inferface.IBedMaterial;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Direction;

public interface IStateParticleModel {

    public TextureAtlasSprite getParticleTexture(@Nonnull IBedMaterial casing, @Nonnull IBedMaterial bedding, @Nonnull Direction facing);
    
}
