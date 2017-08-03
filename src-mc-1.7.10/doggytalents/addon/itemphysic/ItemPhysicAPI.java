package doggytalents.addon.itemphysic;

import java.lang.reflect.Method;
import java.util.ArrayList;

import doggytalents.DoggyTalents;
import doggytalents.helper.ReflectionUtil;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;

/**
 * @author ProPercivalalb
 */
public class ItemPhysicAPI {
	
	public Class<?> serverPhysicClass;
	
	public ArrayList addSwimItem;
	public ArrayList addBurnItem;
	
	public ItemPhysicAPI() {
		this.serverPhysicClass = ReflectionUtil.getClass(ItemPhysicLib.SERVER_PHYSIC_CLASS);
		
		
		this.addSwimItem = ReflectionUtil.getField(this.serverPhysicClass, ArrayList.class, null, ItemPhysicLib.SWIMMING_ITEMS_FIELD_OLD);
		this.addBurnItem = ReflectionUtil.getField(this.serverPhysicClass, ArrayList.class, null, ItemPhysicLib.BURNING_ITEMS_FIELD_OLD);
		
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
