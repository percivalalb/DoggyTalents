package doggytalents.client.model.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public interface IStateParticleModel {

    public TextureAtlasSprite getParticleTexture(IBlockState state);
    
}
