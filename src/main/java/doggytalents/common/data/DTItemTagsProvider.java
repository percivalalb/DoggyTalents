package doggytalents.common.data;

import java.util.Arrays;
import java.util.function.Supplier;

import doggytalents.DoggyItems;
import doggytalents.DoggyTags;
import doggytalents.common.lib.Constants;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.tags.ITag;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

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
        createTag(DoggyTags.BEG_ITEMS_TAMED, DoggyItems.BREEDING_BONE, DoggyItems.THROW_STICK, DoggyItems.THROW_BONE, Items.BONE.delegate);
        appendToTag(DoggyTags.TREATS);
        createTag(DoggyTags.BEG_ITEMS_UNTAMED, DoggyItems.TRAINING_TREAT, Items.BONE.delegate);
        createTag(DoggyTags.BREEDING_ITEMS, DoggyItems.BREEDING_BONE);
        createTag(DoggyTags.PACK_PUPPY_BLACKLIST, DoggyItems.THROW_BONE, DoggyItems.THROW_BONE_WET, DoggyItems.THROW_STICK, DoggyItems.THROW_STICK_WET);
        createTag(DoggyTags.TREATS, DoggyItems.TRAINING_TREAT, DoggyItems.SUPER_TREAT, DoggyItems.MASTER_TREAT, DoggyItems.DIRE_TREAT);
    }

    @SafeVarargs
    private final void createTag(ITag.INamedTag<Item> tag, Supplier<? extends IItemProvider>... items) {
        tag(tag).add(Arrays.stream(items).map(Supplier::get).map(IItemProvider::asItem).toArray(Item[]::new));
    }

    @SafeVarargs
    private final void appendToTag(ITag.INamedTag<Item> tag, ITag.INamedTag<Item>... toAppend) {
        tag(tag).addTags(toAppend);
    }
}