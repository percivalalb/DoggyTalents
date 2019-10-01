package doggytalents.data;

import doggytalents.ModItems;
import doggytalents.ModTags;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.item.Items;

public class DTItemTagsProvider extends ItemTagsProvider {

	public DTItemTagsProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    public void registerTags() {
        getBuilder(ModTags.BEG_ITEMS_TAMED).add(ModItems.TRAINING_TREAT, ModItems.SUPER_TREAT, ModItems.MASTER_TREAT, ModItems.DIRE_TREAT, ModItems.BREEDING_BONE, ModItems.THROW_STICK, ModItems.THROW_BONE, Items.BONE);
        getBuilder(ModTags.BEG_ITEMS_UNTAMED).add(ModItems.TRAINING_TREAT, Items.BONE);
        getBuilder(ModTags.BREEDING_ITEMS).add(ModItems.BREEDING_BONE);
        getBuilder(ModTags.PACK_PUPPY_BLACKLIST).add(ModItems.THROW_BONE, ModItems.THROW_BONE_WET, ModItems.THROW_STICK, ModItems.THROW_STICK_WET);
    }

    @Override
    public String getName() {
        return "DoggyTalents Item Tags";
    }
}