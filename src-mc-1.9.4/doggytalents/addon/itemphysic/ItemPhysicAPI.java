package doggytalents.addon.itemphysic;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

import doggytalents.helper.ReflectionHelper;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.Loader;

/**
 * @author ProPercivalalb
 */
public class ItemPhysicAPI {
	
	public Class<?> serverPhysicClass;
	public ArrayList addSwimItem;
	public ArrayList addBurnItem;
	
	public ItemPhysicAPI(String modId) {
		if(!Loader.isModLoaded(modId))
			return;
		
		this.serverPhysicClass = ReflectionHelper.getClass(ItemPhysicLib.SERVER_PHYSIC_CLASS);
		this.addSwimItem = ReflectionHelper.getField(this.serverPhysicClass, ArrayList.class, null, ItemPhysicLib.SWIMMING_ITEMS_FIELD);
		this.addBurnItem = ReflectionHelper.getField(this.serverPhysicClass, ArrayList.class, null, ItemPhysicLib.BURNING_ITEMS_FIELD);
	}
	
	public void addSwimObjects(Object... objs) throws Exception {
		for(Object obj : objs)
			this.addSwimItem.add(obj);
	}
	
	public void addBurnObjects(Object... objs) throws Exception {
		for(Object obj : objs)
			this.addBurnItem.add(obj);
	}
}
