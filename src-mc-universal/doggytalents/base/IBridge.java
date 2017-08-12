package doggytalents.base;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/** 
 * Interface that provides a bridge between 1.8.9 and 1.9.4+ 
 * Any functions in here should work for 1.9.4+
*/
public interface IBridge {

	public Object createBlockPos(int x, int y, int z);
	
	public TileEntity getTileEntity(World world, int x, int y, int z);
	
	
	public String translateToLocal(String key);
	public String translateToLocalFormatted(String key, Object... format);
	
	//Math functions
	
	public int clamp(int num, int min, int max);
	public float clamp(float num, float min, float max);
	public float cos(float value);
	public float sin(float value);
	public int floor(double value);
	public int ceil(double value);
	public float sqrt(double value);
	public double atan2(double x, double z);
	public float wrapDegrees(float value);

}
