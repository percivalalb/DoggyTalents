package doggytalents.addon.itemphysic;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import doggytalents.ModBlocks;
import doggytalents.helper.ReflectionHelper;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.Loader;

/**
 * @author ProPercivalalb
 */
public class ItemPhysicAPI {
	
	public Class<?> serverPhysicClass = ReflectionHelper.getClass(ItemPhysicLib.SERVER_PHYSIC_CLASS);
	public Class<?> sortingListClass = ReflectionHelper.getClass(ItemPhysicLib.SORTING_LIST_CLASS);
	public Method addSortingBlock = ReflectionHelper.getMethod(sortingListClass, ItemPhysicLib.ADD_SORTING_BLOCK, new Class[] {Block.class});
	public Method addSortingItem = ReflectionHelper.getMethod(sortingListClass, ItemPhysicLib.ADD_SORTING_ITEM, new Class[] {Item.class});
	
	public ItemPhysicAPI(String modId) {
		if(!Loader.isModLoaded(modId))
			return;
		
	}
	
	public void addSortingBlocks(String sortingListName, Block... blocks) throws Exception {
		Object sortingList = ReflectionHelper.getField(this.serverPhysicClass, sortingListName).get(null);
		for(Block block : blocks)
			this.addSortingBlock.invoke(sortingList, block);
	}
	
	public void addSortingItems(String sortingListName, Item... items) throws Exception {
		Object sortingList = ReflectionHelper.getField(this.serverPhysicClass, sortingListName).get(null);
		for(Item item : items)
			this.addSortingItem.invoke(sortingList, item);
	}
}
