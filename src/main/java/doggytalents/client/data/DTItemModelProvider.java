package doggytalents.client.data;

import doggytalents.DoggyBlocks;
import doggytalents.DoggyItems;
import doggytalents.common.lib.Constants;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelBuilder.Perspective;
import net.minecraftforge.client.model.generators.ModelFile.UncheckedModelFile;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.function.Supplier;

public class DTItemModelProvider extends ItemModelProvider {

    public DTItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, Constants.MOD_ID, existingFileHelper);
    }

    @Override
    public String getName() {
        return "DoggyTalents Item Models";
    }

    @Override
    protected void registerModels() {
        handheld(DoggyItems.TINY_BONE);
        handheld(DoggyItems.BIG_BONE);

        handheld(DoggyItems.TRAINING_TREAT);
        handheld(DoggyItems.SUPER_TREAT);
        handheld(DoggyItems.MASTER_TREAT);
        handheld(DoggyItems.DIRE_TREAT);
        handheld(DoggyItems.BREEDING_BONE);
        handheld(DoggyItems.CHEW_STICK);

        radar(DoggyItems.CREATIVE_RADAR);
        radar(DoggyItems.RADAR);

        generated(DoggyItems.CAPE);
        generated(DoggyItems.CAPE_COLOURED);
        generated(DoggyItems.COLLAR_SHEARS);
        generated(DoggyItems.CREATIVE_COLLAR);
        generated(DoggyItems.DOGGY_CHARM);
        generated(DoggyItems.GUARD_SUIT);
        generated(DoggyItems.LEATHER_JACKET);
        generated(DoggyItems.MULTICOLOURED_COLLAR);
        generated(DoggyItems.OWNER_CHANGE);
        generated(DoggyItems.RADIO_COLLAR);
        generated(DoggyItems.SPOTTED_COLLAR);
        generated(DoggyItems.SUNGLASSES);
        generated(DoggyItems.THROW_BONE);
        generated(DoggyItems.THROW_BONE_WET);
        generated(DoggyItems.THROW_STICK);
        generated(DoggyItems.THROW_STICK_WET);
        generated(DoggyItems.TREAT_BAG);
        generated(DoggyItems.WHISTLE);
        generated(DoggyItems.WOOL_COLLAR);

        blockItem(DoggyBlocks.DOG_BATH);
        blockItem(DoggyBlocks.DOG_BED);
        blockItem(DoggyBlocks.FOOD_BOWL);
    }

    private ResourceLocation itemTexture(Supplier<? extends ItemLike> item) {
        return modLoc(ModelProvider.ITEM_FOLDER + "/" + name(item));
    }

    private String name(Supplier<? extends ItemLike> item) {
        return item.get().asItem().getRegistryName().getPath();
    }

    private ItemModelBuilder blockItem(Supplier<? extends Block> block) {
        return blockItem(block, "");
    }

    private ItemModelBuilder radar(Supplier<? extends ItemLike> item) {
        return radar(item, itemTexture(item));
    }

    private ItemModelBuilder radar(Supplier<? extends ItemLike> item, ResourceLocation texture) {
        ItemModelBuilder builder = generated(item, texture);
        builder.transforms().transform(Perspective.THIRDPERSON_RIGHT).rotation(0, 0, 55F).translation(0, 4F, 0.5F).scale(0.85F);
        builder.transforms().transform(Perspective.THIRDPERSON_LEFT).rotation(0, 0, -55F).translation(0, 4F, 0.5F).scale(0.85F);
        builder.transforms().transform(Perspective.FIRSTPERSON_RIGHT).translation(-3.13F, 3.2F, 1.13F).scale(0.8F);
        builder.transforms().transform(Perspective.FIRSTPERSON_LEFT).translation(-3.13F, 3.2F, 1.13F).scale(0.8F);
        return builder;
    }

    private ItemModelBuilder generated(Supplier<? extends ItemLike> item) {
        return generated(item, itemTexture(item));
    }

    private ItemModelBuilder generated(Supplier<? extends ItemLike> item, ResourceLocation texture) {
        return getBuilder(name(item)).parent(new UncheckedModelFile(ModelProvider.ITEM_FOLDER + "/generated")).texture("layer0", texture);
    }

    private ItemModelBuilder handheld(Supplier<? extends ItemLike> item) {
        return handheld(item, itemTexture(item));
    }

    private ItemModelBuilder handheld(Supplier<? extends ItemLike> item, ResourceLocation texture) {
        return getBuilder(name(item)).parent(new UncheckedModelFile(ModelProvider.ITEM_FOLDER + "/handheld")).texture("layer0", texture);
    }

    private ItemModelBuilder blockItem(Supplier<? extends Block> block, String suffix) {
        return withExistingParent(name(block), modLoc(ModelProvider.BLOCK_FOLDER + "/" + name(block) + suffix));
    }
}
