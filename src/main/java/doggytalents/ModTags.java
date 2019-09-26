package doggytalents;

import doggytalents.lib.Reference;
import net.minecraft.item.Item;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;

public class ModTags {

    public static Tag<Item> BEG_ITEMS_TAMED = tag("beg_items_tamed");
    public static Tag<Item> BEG_ITEMS_UNTAMED = tag("beg_items_untamed");
    public static Tag<Item> BREEDING_ITEMS = tag("breeding_items");
    public static Tag<Item> PACK_PUPPY_BLACKLIST = tag("pack_puppy_blacklist");
    
    private static Tag<Item> tag(String name) {
        return new ItemTags.Wrapper(new ResourceLocation(Reference.MOD_ID, name));
    }
}
