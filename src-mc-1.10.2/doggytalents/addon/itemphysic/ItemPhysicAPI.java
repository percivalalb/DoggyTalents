package doggytalents.addon.itemphysic;

import java.lang.reflect.Method;

import doggytalents.DoggyTalentsMod;
import doggytalents.helper.ReflectionHelper;
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
		
		this.serverPhysicClass = ReflectionHelper.getClass(ItemPhysicLib.SERVER_PHYSIC_CLASS);
		this.sortingListClass = ReflectionHelper.getClass(ItemPhysicLib.SORTING_LIST_CLASS);
		this.addSortingBlock = ReflectionHelper.getMethod(sortingListClass, ItemPhysicLib.ADD_SORTING_BLOCK, new Class[] {Block.class});
		this.addSortingItem = ReflectionHelper.getMethod(sortingListClass, ItemPhysicLib.ADD_SORTING_ITEM, new Class[] {Item.class});
	}
	
	public void addSortingBlocks(String sortingListName, Block... blocks) throws Exception {
		Object sortingList = ReflectionHelper.getField(this.serverPhysicClass, sortingListName).get(null);
		for(Block block : blocks) {
			this.addSortingBlock.invoke(sortingList, block);
			DoggyTalentsMod.logger.info("Successefully registered %s in %s list", block, sortingListName);
		}
	}
	
	public void addSortingItems(String sortingListName, Item... items) throws Exception {
		Object sortingList = ReflectionHelper.getField(this.serverPhysicClass, sortingListName).get(null);
		for(Item item : items) {
			this.addSortingItem.invoke(sortingList, item);
			DoggyTalentsMod.logger.info("Successefully registered %s in %s list", item, sortingListName);
		}
	}
}
