package doggytalents.data;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import doggytalents.ModBlocks;
import doggytalents.lib.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.client.model.generators.ModelBuilder.ElementBuilder;
import net.minecraftforge.client.model.generators.ModelBuilder.ElementBuilder.FaceBuilder;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class DTBlockstateProvider extends BlockStateProvider {

    public DTBlockstateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, Reference.MOD_ID, exFileHelper);
    }

    public ExistingFileHelper getExistingHelper() {
        return this.existingFileHelper;
    }

    @Override
    public String getName() {
        return "DoggyTalents Blockstates/Block Models";
    }

    @Override
    protected void registerStatesAndModels() {
        dogBath(ModBlocks.DOG_BATH.delegate);
        dogBed(ModBlocks.DOG_BED.delegate);
        createFromShape(ModBlocks.FOOD_BOWL.delegate, new AxisAlignedBB(1.0D, 0.0D, 1.0D, 15.0D, 8.0D, 15.0D));
    }

    // Applies texture to all faces and for the input face culls that direction
    private static BiFunction<String, Direction, BiConsumer<Direction, ModelBuilder<BlockModelBuilder>.ElementBuilder.FaceBuilder>> cullFaceFactory = (texture, input) -> (d, b) -> b.texture(texture).cullface(d == input ? d : null);

    protected void createFromShape(Supplier<? extends Block> blockIn, AxisAlignedBB bb) {
        BlockModelBuilder model = this
                .getBuilder(name(blockIn))
                .parent(this.getExistingFile(mcLoc(ModelProvider.BLOCK_FOLDER + "/block")))
                .texture("particle", extend(blockTexture(blockIn), "_bottom"))
                .texture("bottom", extend(blockTexture(blockIn), "_bottom"))
                .texture("top", extend(blockTexture(blockIn), "_top"))
                .texture("side", extend(blockTexture(blockIn), "_side"));

        model.element()
          .from((float) bb.minX, (float) bb.minY, (float) bb.minZ)
            .to((float) bb.maxX, (float) bb.maxY, (float) bb.maxZ)
            .allFaces((d, f) -> f.cullface(d == Direction.DOWN ? d : null).texture(d.getAxis().isHorizontal() ? "#side" : d == Direction.DOWN ? "#bottom" : "#top"));

        this.simpleBlock(blockIn.get(), model);
    }


    protected void dogBed(Supplier<? extends Block> blockIn) {
        BlockModelBuilder model = this
                .getBuilder(name(blockIn))
                .parent(this.getExistingFile(mcLoc(ModelProvider.BLOCK_FOLDER + "/block")))
                .texture("particle", blockTexture(Blocks.OAK_PLANKS.delegate))
                .texture("bedding", blockTexture(Blocks.WHITE_WOOL.delegate))
                .texture("casing", blockTexture(Blocks.OAK_PLANKS.delegate))
                .ao(false);

        model.element()
          .from(1.6F, 3.2F, 1.6F)
          .to(14.4F, 6.4F, 14.4F)
          .face(Direction.UP).texture("#bedding").end()
          .face(Direction.NORTH).texture("#bedding");

        model.element() //base
          .from(0, 0, 0)
          .to(16, 3.2F, 16)
          .allFaces(cullFaceFactory.apply("#casing", Direction.DOWN));

        model.element()
          .from(11.2F, 3.2F, 0)
          .to(16, 9.6F, 1.6F)
          .allFaces(cullFaceFactory.apply("#casing", Direction.NORTH));

        model.element()
          .from(0, 3.2F, 0)
          .to(4.8F, 9.6F, 1.6F)
          .allFaces(cullFaceFactory.apply("#casing", Direction.NORTH));

        model.element()
          .from(14.4F, 3.2F, 0)
          .to(16, 9.6F, 16)
          .allFaces(cullFaceFactory.apply("#casing", Direction.EAST));

        model.element()
          .from(0, 3.2F, 14.4F)
          .to(16, 9.6F, 16)
          .allFaces(cullFaceFactory.apply("#casing", Direction.SOUTH));

        model.element()
          .from(0, 3.2F, 0)
          .to(1.6F, 9.6F, 16)
          .allFaces(cullFaceFactory.apply("#casing", Direction.WEST));

        this.simpleBlock(blockIn.get(), model);
    }

    protected void dogBath(Supplier<? extends Block> blockIn) {
        BlockModelBuilder model = this
                .getBuilder(name(blockIn))
                .parent(this.getExistingFile(mcLoc(ModelProvider.BLOCK_FOLDER + "/block")))
                .texture("particle", blockTexture(Blocks.IRON_BLOCK.delegate))
                .texture("water", extend(blockTexture(Blocks.WATER.delegate), "_still"))
                .texture("side", blockTexture(Blocks.IRON_BLOCK.delegate))
                .texture("bottom", blockTexture(Blocks.IRON_BLOCK.delegate))
                .ao(false);

        model.element()
          .from(1, 0, 1)
          .to(15, 6, 15)
          .face(Direction.UP).texture("#water").tintindex(0);

        model.element()
          .from(1, 0, 1)
          .to(15, 6, 15)
          .face(Direction.DOWN).texture("#bottom");

        model.element()
          .from(0, 0, 0)
          .to(16, 8, 1)
          .allFaces(cullFaceFactory.apply("#side", Direction.NORTH));

        model.element()
          .from(15, 0, 0)
          .to(16, 8, 16)
          .allFaces(cullFaceFactory.apply("#side", Direction.EAST));

        model.element()
          .from(0, 0, 15)
          .to(16, 8, 16)
          .allFaces(cullFaceFactory.apply("#side", Direction.SOUTH));

        model.element()
          .from(0, 0, 0)
          .to(1, 8, 16)
          .allFaces(cullFaceFactory.apply("#side", Direction.WEST));

        this.simpleBlock(blockIn.get(), model);
    }



    private String name(Supplier<? extends IForgeRegistryEntry<?>> block) {
        return block.get().getRegistryName().getPath();
    }

    private ResourceLocation blockTexture(Supplier<? extends Block> block) {
        ResourceLocation base = block.get().getRegistryName();
        return prextend(base, ModelProvider.BLOCK_FOLDER + "/");
    }

    public ModelFile cross(Supplier<? extends Block> block) {
        return this.cross(name(block), blockTexture(block));
    }


    protected void makeSimple(Supplier<? extends Block> blockIn) {
        this.simpleBlock(blockIn.get());
    }

    private ResourceLocation prextend(ResourceLocation rl, String prefix) {
        return new ResourceLocation(rl.getNamespace(), prefix + rl.getPath());
    }

    private ResourceLocation extend(ResourceLocation rl, String suffix) {
        return new ResourceLocation(rl.getNamespace(), rl.getPath() + suffix);
    }
}
