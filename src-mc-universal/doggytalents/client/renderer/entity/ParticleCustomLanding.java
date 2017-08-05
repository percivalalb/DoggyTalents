package doggytalents.client.renderer.entity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ParticleCustomLanding extends ParticleCustomDigging {
   
	public ParticleCustomLanding(World worldIn, double xIn, double yIn, double zIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, IBlockState state, BlockPos pos, TextureAtlasSprite sprite) {
        super(worldIn, xIn, yIn, zIn, xSpeedIn, ySpeedIn, zSpeedIn, state, pos, sprite);
        this.motionX = xSpeedIn;
        this.motionY = ySpeedIn;
        this.motionZ = zSpeedIn;
    }
}