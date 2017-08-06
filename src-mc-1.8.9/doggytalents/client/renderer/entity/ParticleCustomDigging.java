package doggytalents.client.renderer.entity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.EntityDiggingFX;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class ParticleCustomDigging extends EntityDiggingFX {

	public ParticleCustomDigging(World worldIn, double xIn, double yIn, double zIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, IBlockState state, BlockPos pos, TextureAtlasSprite sprite) {
		super(worldIn, xIn, yIn, zIn, xSpeedIn, ySpeedIn, zSpeedIn, state);
		this.setBlockPos(pos);
		this.setParticleIcon(sprite);
	}
}