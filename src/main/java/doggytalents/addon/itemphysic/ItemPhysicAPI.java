package doggytalents.addon.itemphysic;

import java.lang.reflect.Method;

import doggytalents.DoggyTalents;
import doggytalents.helper.ReflectionUtil;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

/**
 * @author ProPercivalalb
 */
public class ItemPhysicAPI {
	
	public Class apiClass;
	public Method apiMethod;
	
	public Class<?> serverPhysicClass;
	public Class<?> sortingListClass;
	public Method addSortingObjects;
	
	public ItemPhysicAPI() {
		this.apiClass = ReflectionUtil.getClass(ItemPhysicLib.API_CLASS);
		
		if (this.apiClass != null) {
			this.apiMethod = ReflectionUtil.getMethod(apiClass, ItemPhysicLib.API_METHOD, String.class, Object[].class);
		} else {
			this.serverPhysicClass = ReflectionUtil.getClass(ItemPhysicLib.SERVER_PHYSIC_CLASS);
			
			// Tries to find newest class from CreativeCore if not looks in old location
			this.sortingListClass = ReflectionUtil.getClass(ItemPhysicLib.SORTING_LIST_CLASS);
			if (this.sortingListClass == null)
				this.sortingListClass = ReflectionUtil.getClass(ItemPhysicLib.SORTING_LIST_CLASS_OLD);
			
			this.addSortingObjects = ReflectionUtil.getMethod(this.sortingListClass, ItemPhysicLib.API_METHOD, Object[].class);
		}
	}
	
	public void addSorting(String sortingListName, Object... objects) throws Exception {
		if (apiClass != null)
			apiMethod.invoke(null, sortingListName, objects);
		else {
			Object sortingList = ReflectionUtil.getField(this.serverPhysicClass, sortingListName).get(null);
			addSortingObjects.invoke(sortingList, objects);
			DoggyTalents.LOGGER.debug("Successefully registered {} in {} list", Arrays.toString(objects), sortingListName);
		}
	}
}
