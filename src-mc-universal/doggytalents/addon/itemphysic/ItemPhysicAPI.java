package doggytalents.addon.itemphysic;

import java.lang.reflect.Method;
import java.util.ArrayList;

import doggytalents.DoggyTalents;
import doggytalents.helper.ReflectionUtil;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;

/**
 * @author ProPercivalalb
 */
public class ItemPhysicAPI {
	
	public Class<?> serverPhysicClass;
	public Class<?> sortingListClass;
	public Method addSortingBlock;
	public Method addSortingItem;
	
	public ArrayList addSwimItem;
	public ArrayList addBurnItem;
	
	public ItemPhysicAPI(String modId) {
		if(!Loader.isModLoaded(modId))
			return;
		
		this.serverPhysicClass = ReflectionUtil.getClass(ItemPhysicLib.SERVER_PHYSIC_CLASS);
		if(MinecraftForge.MC_VERSION.equals("1.9.4")) {
			this.addSwimItem = ReflectionUtil.getField(this.serverPhysicClass, ArrayList.class, null, ItemPhysicLib.SWIMMING_ITEMS_FIELD_OLD);
			this.addBurnItem = ReflectionUtil.getField(this.serverPhysicClass, ArrayList.class, null, ItemPhysicLib.BURNING_ITEMS_FIELD_OLD);
		}
		else {
			this.sortingListClass = ReflectionUtil.getClass(ItemPhysicLib.SORTING_LIST_CLASS);
			this.addSortingBlock = ReflectionUtil.getMethod(sortingListClass, ItemPhysicLib.ADD_SORTING_BLOCK, new Class[] {Block.class});
			this.addSortingItem = ReflectionUtil.getMethod(sortingListClass, ItemPhysicLib.ADD_SORTING_ITEM, new Class[] {Item.class});
		}
	}
	
	public void addSortingBlocks(String sortingListName, Block... blocks) throws Exception {
		Object sortingList = ReflectionUtil.getField(this.serverPhysicClass, sortingListName).get(null);
		for(Block block : blocks) {
			this.addSortingBlock.invoke(sortingList, block);
			DoggyTalents.LOGGER.info("Successefully registered %s in %s list", block, sortingListName);
		}
	}
	
	public void addSortingItems(String sortingListName, Item... items) throws Exception {
		Object sortingList = ReflectionUtil.getField(this.serverPhysicClass, sortingListName).get(null);
		for(Item item : items) {
			this.addSortingItem.invoke(sortingList, item);
			DoggyTalents.LOGGER.info("Successefully registered %s in %s list", item, sortingListName);
		}
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
