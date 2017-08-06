package doggytalents.api.registry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import doggytalents.DoggyTalents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

/**
 * @author ProPercivalalb
 */
public class ItemList {

	private List<List<Object>> itemlist = new ArrayList<List<Object>>();
	
	public void registerItem(Item item) { this.registerItem(item, OreDictionary.WILDCARD_VALUE); }
	public void registerItem(Item item, int meta) {
		List array = Arrays.asList(new Object[] {item, meta});
		if(this.itemlist.contains(array))
			DoggyTalents.LOGGER.warn("The item {} meta {} is already registered in this item list", item, meta);
		else {
			this.itemlist.add(array);
			DoggyTalents.LOGGER.info("The item {} meta {} was register to an item list", item, meta);
		}
	}
	
	public boolean containsItem(Item item) { return this.containsItem(item, OreDictionary.WILDCARD_VALUE); }
	public boolean containsItem(ItemStack stack) { return this.containsItem(stack.getItem(), stack.getItemDamage()); }
	public boolean containsItem(Item item, int meta) {
		List array = Arrays.asList(new Object[] {item, meta});
		List array_any = Arrays.asList(new Object[] {item, OreDictionary.WILDCARD_VALUE});
		return this.itemlist.contains(array) || this.itemlist.contains(array_any);
	}
}
