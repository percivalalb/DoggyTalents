package doggytalents.client.renderer.particle;

import net.minecraft.block.BlockState;
import net.minecraft.client.particle.DiggingParticle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ParticleCustomDigging extends DiggingParticle {

	public ParticleCustomDigging(World worldIn, double xIn, double yIn, double zIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, BlockState state, BlockPos pos, TextureAtlasSprite sprite) {
		super(worldIn, xIn, yIn, zIn, xSpeedIn, ySpeedIn, zSpeedIn, state);
		this.setBlockPos(pos);
		this.setSprite(sprite);
	}
}