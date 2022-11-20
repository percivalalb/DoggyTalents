package doggytalents;

import doggytalents.common.util.Util;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class DoggyTags {

    public static TagKey<Item> BEG_ITEMS_TAMED = tag("beg_items_tamed");
    public static TagKey<Item> BEG_ITEMS_UNTAMED = tag("beg_items_untamed");
    public static TagKey<Item> BREEDING_ITEMS = tag("breeding_items");
    public static TagKey<Item> PACK_PUPPY_BLACKLIST = tag("pack_puppy_blacklist");
    public static TagKey<Item> TREATS = tag("treats");

    private static TagKey<Item> tag(String name) {
        return ItemTags.create(Util.getResource(name));
    }
}
