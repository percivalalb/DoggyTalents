package doggytalents.api.registry;

import java.util.ArrayList;
import java.util.List;

import doggytalents.DoggyTalentsMod;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * @author ProPercivalalb
 */
public class ItemList {

	private List<Item> itemlist = new ArrayList<Item>();
	
	public void registerItem(Item item) { this.registerItem(item); }
	public void registerItem(Item item, int meta) {
		if(this.itemlist.contains(item))
			DoggyTalentsMod.LOGGER.warn("The item {} meta {} is already registered in this item list", item.getRegistryName(), meta);
		else {
			this.itemlist.add(item);
			DoggyTalentsMod.LOGGER.info("The item {} meta {} was register to an item list", item.getRegistryName(), meta);
		}
	}
	
	public boolean containsItem(ItemStack stack) { return this.containsItem(stack.getItem()); }
	public boolean containsItem(Item item) {
		return this.itemlist.contains(item);
	}
}
