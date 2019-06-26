package doggytalents.client.model.block;


import javax.annotation.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IStateParticleModel {

    @Nullable
    public TextureAtlasSprite getParticleTexture(World worldIn, BlockPos pos, BlockState state, @Nullable Direction side);
    
}
