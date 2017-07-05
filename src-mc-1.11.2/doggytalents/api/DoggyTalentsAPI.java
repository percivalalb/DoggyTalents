package doggytalents.api;

import doggytalents.api.registry.ItemList;
import doggytalents.creativetab.CreativeTabDogBed;
import doggytalents.creativetab.CreativeTabDoggyTalents;
import net.minecraft.creativetab.CreativeTabs;

/**
 * @author ProPercivalalb
 */
public class DoggyTalentsAPI {
	
	public static CreativeTabs CREATIVE_TAB = new CreativeTabDoggyTalents();
	public static CreativeTabs CREATIVE_TAB_BED = new CreativeTabDogBed();
	public static ItemList PACKPUPPY_BLACKLIST = new ItemList();
	
	public static ItemList BEG_WHITELIST = new ItemList();
	public static ItemList BREED_WHITELIST = new ItemList();
}
