package doggytalents;

import doggytalents.lib.Reference;
import net.minecraft.item.Item;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;

public class ModTags {

	public static ResourceLocation BEG_ITEMS_TAMED = new ResourceLocation(Reference.MOD_ID, "beg_items_tamed");
	public static ResourceLocation BEG_ITEMS_UNTAMED = new ResourceLocation(Reference.MOD_ID, "beg_items_untamed");
	public static ResourceLocation BREEDING_ITEMS = new ResourceLocation(Reference.MOD_ID, "breeding_items");
	public static ResourceLocation PACK_PUPPY_BLACKLIST = new ResourceLocation(Reference.MOD_ID, "pack_puppy_blacklist");
	
	public static Tag<Item> getTag(ResourceLocation name) {
        return ItemTags.getCollection().getOrCreate(name);
    }
}
