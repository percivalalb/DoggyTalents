package doggytalents.base;

/** 
 * Interface that provides a bridge between 1.8.9 and 1.9.4+ 
 * Any functions in here should work for 1.9.4+
*/
public interface IBridge {

	public Object createBlockPos(int x, int y, int z);

	
}
