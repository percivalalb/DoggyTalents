package doggytalents.addon.itemphysic;

import java.lang.reflect.Method;

import doggytalents.DoggyTalents;
import doggytalents.helper.ReflectionUtil;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.Loader;

/**
 * @author ProPercivalalb
 */
public class ItemPhysicAPI {
	
	public Class<?> serverPhysicClass;
	public Class<?> sortingListClass;
	public Method addSortingBlock;
	public Method addSortingItem;
	
	public ItemPhysicAPI(String modId) {
		if(!Loader.isModLoaded(modId))
			return;
		
		this.serverPhysicClass = ReflectionUtil.getClass(ItemPhysicLib.SERVER_PHYSIC_CLASS);
		this.sortingListClass = ReflectionUtil.getClass(ItemPhysicLib.SORTING_LIST_CLASS);
		this.addSortingBlock = ReflectionUtil.getMethod(sortingListClass, ItemPhysicLib.ADD_SORTING_BLOCK, new Class[] {Block.class});
		this.addSortingItem = ReflectionUtil.getMethod(sortingListClass, ItemPhysicLib.ADD_SORTING_ITEM, new Class[] {Item.class});
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
}
