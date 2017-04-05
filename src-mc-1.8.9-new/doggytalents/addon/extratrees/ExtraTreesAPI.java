package doggytalents.addon.extratrees;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.fml.common.Loader;

/**
 * @author ProPercivalalb
 */
public class ExtraTreesAPI {
	
	public ExtraTreesAPI(String modId) {
		if(!Loader.isModLoaded(modId))
			return;
		
		try {
			Class<?> c = Class.forName("binnie.extratrees.block.PlankType.ExtraTreePlanks");
			if(c.isEnum()) {

	
				Field[] flds = c.getDeclaredFields();
				List<Field> cst = new ArrayList<Field>();
				List<Field> mbr = new ArrayList<Field>();
			    for (Field f : flds) {
			    	if (f.isEnumConstant())
			    		cst.add(f);
			    	else
			    		mbr.add(f);
					
			    }
	
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
