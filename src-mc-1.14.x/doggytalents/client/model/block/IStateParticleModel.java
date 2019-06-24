package doggytalents.client.model.block;

import doggytalents.api.inferface.IBedMaterial;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Direction;

public interface IStateParticleModel {

    public TextureAtlasSprite getParticleTexture(IBedMaterial casing, IBedMaterial bedding, Direction facing);
    
}
