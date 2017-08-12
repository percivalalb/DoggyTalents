package doggytalents.base.b;

import java.util.List;

import doggytalents.base.IBridge;
import doggytalents.entity.EntityDog;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class Bridge implements IBridge {

	@Override
	public Object createBlockPos(int x, int y, int z) {
		return null;
	}

	@Override
	public TileEntity getTileEntity(World world, int x, int y, int z) {
		return world.getTileEntity(new BlockPos(x, y, z));
	}
	
	@Override
	public <T extends Entity> List<T> getEntitiesWithinAABB(World world, Class<? extends T> classEntity, double x, double y, double z, int xG, int yG, int zG) {
		return world.getEntitiesWithinAABB(classEntity, AxisAlignedBB.fromBounds(x, y, z, x + 1.0D, y + 1.0D, z + 1.0D).grow(xG, yG, zG));
	}
	
	@Override
	public String translateToLocal(String key) {
		return StatCollector.translateToLocal(key);
	}

	@Override
	public String translateToLocalFormatted(String key, Object... format) {
		return StatCollector.translateToLocalFormatted(key, format);
	}

	@Override
	public int clamp(int num, int min, int max) {
		return MathHelper.clamp_int(num, min, max);
	}
	
	@Override
	public float clamp(float num, float min, float max) {
		return MathHelper.clamp_float(num, min, max);
	}
	
	@Override
	public float cos(float value) {
		return MathHelper.cos(value);
	}
	
	@Override
	public float sin(float value) {
		return MathHelper.sin(value);
	}
	
	@Override
	public int floor(double value) {
		return MathHelper.floor_double(value);
	}
	
	@Override
	public int ceil(double value) {
		return MathHelper.ceiling_double_int(value);
	}
	
	@Override
	public float sqrt(double value) {
		return MathHelper.sqrt_double(value);
	}

	@Override
	public double atan2(double x, double z) {
		return MathHelper.atan2(x, z);
	}
	
	@Override
	public float wrapDegrees(float value) {
		return MathHelper.wrapAngleTo180_float(value);
	}
}
