package doggytalents.client.model.block;

import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public interface IStateParticleModel {

	public TextureAtlasSprite getParticleTexture(BlockState state);
	
}
