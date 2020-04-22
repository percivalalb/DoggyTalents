package doggytalents.data;

import java.util.function.Supplier;

import doggytalents.ModBlocks;
import doggytalents.ModItems;
import doggytalents.lib.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.FenceBlock;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelBuilder.Perspective;
import net.minecraftforge.client.model.generators.ModelFile.UncheckedModelFile;
import net.minecraftforge.client.model.generators.ModelProvider;

public class DTItemModelProvider extends ItemModelProvider {

    public DTItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, Reference.MOD_ID, existingFileHelper);
    }

    @Override
    public String getName() {
        return "DoggyTalents Item Models";
    }

    @Override
    protected void registerModels() {
        handheld(ModItems.TINY_BONE.delegate);
        handheld(ModItems.BIG_BONE.delegate);

        handheld(ModItems.TRAINING_TREAT.delegate);
        handheld(ModItems.SUPER_TREAT.delegate);
        handheld(ModItems.MASTER_TREAT.delegate);
        handheld(ModItems.DIRE_TREAT.delegate);
        handheld(ModItems.BREEDING_BONE.delegate);
        handheld(ModItems.CHEW_STICK.delegate);

        radar(ModItems.CREATIVE_RADAR.delegate);
        radar(ModItems.RADAR.delegate);

        generated(ModItems.CAPE.delegate);
        generated(ModItems.CAPE_COLOURED.delegate);
        generated(ModItems.COLLAR_SHEARS.delegate);
        generated(ModItems.CREATIVE_COLLAR.delegate);
        generated(ModItems.DOGGY_CHARM.delegate);
        generated(ModItems.LEATHER_JACKET.delegate);
        generated(ModItems.MULTICOLOURED_COLLAR.delegate);
        generated(ModItems.OWNER_CHANGE.delegate);
        generated(ModItems.RADIO_COLLAR.delegate);
        generated(ModItems.SPOTTED_COLLAR.delegate);
        generated(ModItems.SUNGLASSES.delegate);
        generated(ModItems.THROW_BONE.delegate);
        generated(ModItems.THROW_BONE_WET.delegate);
        generated(ModItems.THROW_STICK.delegate);
        generated(ModItems.THROW_STICK_WET.delegate);
        generated(ModItems.TREAT_BAG.delegate);
        generated(ModItems.WHISTLE.delegate);
        generated(ModItems.WOOL_COLLAR.delegate);

        blockItem(ModBlocks.DOG_BATH.delegate);
        blockItem(ModBlocks.DOG_BED.delegate);
        blockItem(ModBlocks.FOOD_BOWL.delegate);
    }

    private ResourceLocation itemTexture(Supplier<? extends IItemProvider> item) {
        return modLoc(ModelProvider.ITEM_FOLDER + "/" + name(item));
    }

    private String name(Supplier<? extends IItemProvider> item) {
        return item.get().asItem().getRegistryName().getPath();
    }

    private ItemModelBuilder blockItem(Supplier<? extends Block> block) {
        return blockItem(block, "");
    }

    private ItemModelBuilder radar(Supplier<? extends IItemProvider> item) {
        return radar(item, itemTexture(item));
    }

    private ItemModelBuilder radar(Supplier<? extends IItemProvider> item, ResourceLocation texture) {
        ItemModelBuilder builder = generated(item, texture);
        builder.transforms().transform(Perspective.THIRDPERSON_RIGHT).rotation(0, 0, 55F).translation(0, 4F, 0.5F).scale(0.85F);
        builder.transforms().transform(Perspective.THIRDPERSON_LEFT).rotation(0, 0, -55F).translation(0, 4F, 0.5F).scale(0.85F);
        builder.transforms().transform(Perspective.FIRSTPERSON_RIGHT).translation(-3.13F, 3.2F, 1.13F).scale(0.8F);
        builder.transforms().transform(Perspective.FIRSTPERSON_LEFT).translation(-3.13F, 3.2F, 1.13F).scale(0.8F);
        return builder;
    }

    private ItemModelBuilder generated(Supplier<? extends IItemProvider> item) {
        return generated(item, itemTexture(item));
    }

    private ItemModelBuilder generated(Supplier<? extends IItemProvider> item, ResourceLocation texture) {
        return getBuilder(name(item)).parent(new UncheckedModelFile(ModelProvider.ITEM_FOLDER + "/generated")).texture("layer0", texture);
    }

    private ItemModelBuilder handheld(Supplier<? extends IItemProvider> item) {
        return handheld(item, itemTexture(item));
    }

    private ItemModelBuilder handheld(Supplier<? extends IItemProvider> item, ResourceLocation texture) {
        return getBuilder(name(item)).parent(new UncheckedModelFile(ModelProvider.ITEM_FOLDER + "/handheld")).texture("layer0", texture);
    }

    private ItemModelBuilder blockItem(Supplier<? extends Block> block, String suffix) {
        return withExistingParent(name(block), modLoc(ModelProvider.BLOCK_FOLDER + "/" + name(block) + suffix));
    }
}
