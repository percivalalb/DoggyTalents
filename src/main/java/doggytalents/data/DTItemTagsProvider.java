package doggytalents.data;

import java.util.Arrays;
import java.util.function.Supplier;

import doggytalents.ModItems;
import doggytalents.ModTags;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.tags.Tag;
import net.minecraft.util.IItemProvider;

public class DTItemTagsProvider extends ItemTagsProvider {

    public DTItemTagsProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    public String getName() {
        return "DoggyTalents Item Tags";
    }

    @Override
    public void registerTags() {
        createTag(ModTags.BEG_ITEMS_TAMED, ModItems.BREEDING_BONE, ModItems.THROW_STICK, ModItems.THROW_BONE, Items.BONE.delegate);
        appendToTag(ModTags.TREATS);
        createTag(ModTags.BEG_ITEMS_UNTAMED, ModItems.TRAINING_TREAT, Items.BONE.delegate);
        createTag(ModTags.BREEDING_ITEMS, ModItems.BREEDING_BONE);
        createTag(ModTags.PACK_PUPPY_BLACKLIST, ModItems.THROW_BONE, ModItems.THROW_BONE_WET, ModItems.THROW_STICK, ModItems.THROW_STICK_WET);
        createTag(ModTags.TREATS, ModItems.TRAINING_TREAT, ModItems.SUPER_TREAT, ModItems.MASTER_TREAT, ModItems.DIRE_TREAT);
    }

    @SafeVarargs
    private final void createTag(Tag<Item> tag, Supplier<? extends IItemProvider>... items) {
        getBuilder(tag).add(Arrays.stream(items).map(Supplier::get).map(IItemProvider::asItem).toArray(Item[]::new));
    }

    @SafeVarargs
    private final void appendToTag(Tag<Item> tag, Tag<Item>... toAppend) {
        getBuilder(tag).add(toAppend);
    }
}