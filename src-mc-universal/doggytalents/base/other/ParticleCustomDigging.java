package doggytalents.base.other;

import doggytalents.base.VersionControl.VersionConfig;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.ParticleDigging;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(value = Side.CLIENT)
@VersionConfig({"1.9.4", "1.10.2", "1.11.2", "1.12", "1.12.1"})
public class ParticleCustomDigging extends ParticleDigging {

	public ParticleCustomDigging(World worldIn, double xIn, double yIn, double zIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, IBlockState state, BlockPos pos, TextureAtlasSprite sprite) {
		super(worldIn, xIn, yIn, zIn, xSpeedIn, ySpeedIn, zSpeedIn, state);
		this.setBlockPos(pos);
		this.setParticleTexture(sprite);
	}
}