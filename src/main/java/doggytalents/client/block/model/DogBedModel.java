package doggytalents.client.block.model;

import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Either;

import doggytalents.DoggyBedMaterials;
import doggytalents.api.registry.IBeddingMaterial;
import doggytalents.api.registry.ICasingMaterial;
import doggytalents.common.block.DogBedBlock;
import doggytalents.common.block.tileentity.DogBedTileEntity;
import doggytalents.common.lib.Constants;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.BlockModel;
import net.minecraft.client.renderer.model.BlockPart;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.model.ModelRotation;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockDisplayReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.client.model.data.IModelData;

@OnlyIn(Dist.CLIENT)
public class DogBedModel implements IBakedModel {

    public static DogBedItemOverride ITEM_OVERIDE = new DogBedItemOverride();

    private ModelLoader modelLoader;
    private BlockModel model;
    private IBakedModel bakedModel;

    private final Map<Triple<ICasingMaterial, IBeddingMaterial, Direction>, IBakedModel> cache = Maps.newHashMap();

    public DogBedModel(ModelLoader modelLoader, BlockModel model, IBakedModel bakedModel) {
        this.modelLoader = modelLoader;
        this.model = model;
        this.bakedModel = bakedModel;
    }

    public IBakedModel getModelVariant(@Nonnull IModelData data) {
        return this.getModelVariant(data.getData(DogBedTileEntity.CASING), data.getData(DogBedTileEntity.BEDDING), data.getData(DogBedTileEntity.FACING));
    }

    public IBakedModel getModelVariant(ICasingMaterial casing, IBeddingMaterial bedding, Direction facing) {
        if (casing == null) { casing = DoggyBedMaterials.OAK_PLANKS.get(); }
        if (bedding == null) { bedding = DoggyBedMaterials.WHITE_WOOL.get(); }
        if (facing == null) { facing = Direction.NORTH; }

        return this.cache.computeIfAbsent(ImmutableTriple.of(casing, bedding, facing), (k) -> bakeModelVariant(k.getLeft(), k.getMiddle(), k.getRight()));
    }

    @Override
    public List<BakedQuad> getQuads(BlockState state, Direction side, Random rand) {
        return this.getModelVariant(DoggyBedMaterials.OAK_PLANKS.get(), DoggyBedMaterials.WHITE_WOOL.get(), Direction.NORTH).getQuads(state, side, rand, EmptyModelData.INSTANCE);
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData data) {
        return this.getModelVariant(data).getQuads(state, side, rand, data);
    }

    @Override
    public TextureAtlasSprite getParticleTexture(@Nonnull IModelData data) {
        return this.getModelVariant(data).getParticleTexture(data);
    }

    @Override
    public IModelData getModelData(@Nonnull IBlockDisplayReader world, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull IModelData tileData) {
        ICasingMaterial casing = null;
        IBeddingMaterial bedding = null;
        Direction facing = null;

        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof DogBedTileEntity) {
            casing = ((DogBedTileEntity) tile).getCasing();
            bedding = ((DogBedTileEntity) tile).getBedding();
        }

        if (state.hasProperty(DogBedBlock.FACING)) {
            facing = state.get(DogBedBlock.FACING);
        }

        tileData.setData(DogBedTileEntity.CASING, casing);
        tileData.setData(DogBedTileEntity.BEDDING, bedding);
        tileData.setData(DogBedTileEntity.FACING, facing);

        return tileData;
    }

    public IBakedModel bakeModelVariant(@Nonnull ICasingMaterial casingResource, @Nonnull IBeddingMaterial beddingResource, @Nonnull Direction facing) {
        List<BlockPart> elements = Lists.newArrayList(); //We have to duplicate this so we can edit it below.
        for (BlockPart part : this.model.getElements()) {
            elements.add(new BlockPart(part.positionFrom, part.positionTo, Maps.newHashMap(part.mapFaces), part.partRotation, part.shade));
        }

        BlockModel newModel = new BlockModel(this.model.getParentLocation(), elements,
            Maps.newHashMap(this.model.textures), this.model.isAmbientOcclusion(), this.model.getGuiLight(),
            this.model.getAllTransforms(), Lists.newArrayList(this.model.getOverrides()));
        newModel.name = this.model.name;
        newModel.parent = this.model.parent;


        Either<RenderMaterial, String> casingTexture = findTexture(casingResource.getTexture());
        newModel.textures.put("bedding", findTexture(beddingResource.getTexture()));
        newModel.textures.put("casing", casingTexture);
        newModel.textures.put("particle", casingTexture);

        return newModel.bakeModel(this.modelLoader, newModel, ModelLoader.defaultTextureGetter(), getModelRotation(facing), createResourceVariant(casingResource, beddingResource, facing), true);
    }

    private ResourceLocation createResourceVariant(@Nonnull ICasingMaterial casingResource, @Nonnull IBeddingMaterial beddingResource, @Nonnull Direction facing) {
        return new ModelResourceLocation(Constants.MOD_ID, "block/dog_bed#bedding=" + beddingResource.getRegistryName().toString().replace(':', '.') + ",casing=" + casingResource.getRegistryName().toString().replace(':', '.') + ",facing=" + facing.getName2());
    }

    private Either<RenderMaterial, String> findTexture(ResourceLocation resource) {
        return Either.left(new RenderMaterial(PlayerContainer.LOCATION_BLOCKS_TEXTURE, resource));
    }

    private ModelRotation getModelRotation(Direction dir) {
        switch(dir) {
        default:    return ModelRotation.X0_Y0; // North
        case EAST:  return ModelRotation.X0_Y90;
        case SOUTH: return ModelRotation.X0_Y180;
        case WEST:  return ModelRotation.X0_Y270;
        }
    }

    @Override
    public boolean isAmbientOcclusion() {
        return this.bakedModel.isAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return this.bakedModel.isGui3d();
    }

    @Override
    public boolean isSideLit() {
        return this.bakedModel.isSideLit();
    }

    @Override
    public boolean isBuiltInRenderer() {
        return this.bakedModel.isBuiltInRenderer();
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return this.bakedModel.getParticleTexture();
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms() {
        return this.bakedModel.getItemCameraTransforms();
    }

    @Override
    public ItemOverrideList getOverrides() {
        return ITEM_OVERIDE;
    }
}