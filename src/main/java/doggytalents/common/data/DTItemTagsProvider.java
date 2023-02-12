package doggytalents.common.data;

import doggytalents.DoggyItems;
import doggytalents.DoggyTags;
import doggytalents.common.lib.Constants;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class DTItemTagsProvider extends ItemTagsProvider {

    public DTItemTagsProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> providers, TagsProvider<Block> tagProvider, ExistingFileHelper existingFileHelper) {
        super(packOutput, providers, tagProvider, Constants.MOD_ID, existingFileHelper);
    }

    @Override
    public String getName() {
        return "DoggyTalents Item Tags";
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
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
