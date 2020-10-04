package doggytalents;

import doggytalents.common.util.Util;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;

public class DoggyTags {

    public static ITag.INamedTag<Item> BEG_ITEMS_TAMED = tag("beg_items_tamed");
    public static ITag.INamedTag<Item> BEG_ITEMS_UNTAMED = tag("beg_items_untamed");
    public static ITag.INamedTag<Item> BREEDING_ITEMS = tag("breeding_items");
    public static ITag.INamedTag<Item> PACK_PUPPY_BLACKLIST = tag("pack_puppy_blacklist");
    public static ITag.INamedTag<Item> TREATS = tag("treats");

    private static ITag.INamedTag<Item> tag(String name) {
        return ItemTags.makeWrapperTag(Util.getResourcePath(name));
    }
}
