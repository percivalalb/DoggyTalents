package doggytalents.common.data;

import doggytalents.DoggyItems;
import doggytalents.DoggyTags;
import doggytalents.common.lib.Constants;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.Arrays;
import java.util.function.Supplier;

public class DTItemTagsProvider extends ItemTagsProvider {

    public DTItemTagsProvider(DataGenerator generatorIn, BlockTagsProvider blockTagProvider, ExistingFileHelper existingFileHelper) {
        super(generatorIn, blockTagProvider, Constants.MOD_ID, existingFileHelper);
    }

    @Override
    public String getName() {
        return "DoggyTalents Item Tags";
    }

    @Override
    public void addTags() {
        createTag(DoggyTags.BEG_ITEMS_TAMED, DoggyItems.BREEDING_BONE, DoggyItems.THROW_STICK, DoggyItems.THROW_BONE, () -> Items.BONE);
        appendToTag(DoggyTags.TREATS);
        createTag(DoggyTags.BEG_ITEMS_UNTAMED, DoggyItems.TRAINING_TREAT, () -> Items.BONE);
        createTag(DoggyTags.BREEDING_ITEMS, DoggyItems.BREEDING_BONE);
        createTag(DoggyTags.PACK_PUPPY_BLACKLIST, DoggyItems.THROW_BONE, DoggyItems.THROW_BONE_WET, DoggyItems.THROW_STICK, DoggyItems.THROW_STICK_WET);
        createTag(DoggyTags.TREATS, DoggyItems.TRAINING_TREAT, DoggyItems.SUPER_TREAT, DoggyItems.MASTER_TREAT, DoggyItems.DIRE_TREAT);
    }

    @SafeVarargs
    private final void createTag(TagKey<Item> tag, Supplier<? extends ItemLike>... items) {
        tag(tag).add(Arrays.stream(items).map(Supplier::get).map(ItemLike::asItem).toArray(Item[]::new));
    }

    @SafeVarargs
    private final void appendToTag(TagKey<Item> tag, TagKey<Item>... toAppend) {
        tag(tag).addTags(toAppend);
    }
}
