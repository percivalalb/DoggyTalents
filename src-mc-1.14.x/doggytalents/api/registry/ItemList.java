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
	
	public void registerItem(Item... items) { for(Item item : items) this.registerItem(item); }
	public void registerItem(Item item) {
		if(this.itemlist.contains(item))
			DoggyTalentsMod.LOGGER.warn("The item {} is already registered in this item list", item.getRegistryName());
		else {
			this.itemlist.add(item);
			DoggyTalentsMod.LOGGER.info("The item {} was register to an item list", item.getRegistryName());
		}
	}
	
	public boolean containsItem(ItemStack stack) { return this.containsItem(stack.getItem()); }
	public boolean containsItem(Item item) {
		return this.itemlist.contains(item);
	}
}
