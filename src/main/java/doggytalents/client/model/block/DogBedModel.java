package doggytalents.client.model.block;

import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.datafixers.util.Either;

import doggytalents.ModBeddings;
import doggytalents.api.inferface.IBedMaterial;
import doggytalents.block.BlockDogBed;
import doggytalents.lib.Reference;
import doggytalents.tileentity.TileEntityDogBed;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.BlockModel;
import net.minecraft.client.renderer.model.BlockPart;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.model.Material;
import net.minecraft.client.renderer.model.ModelRotation;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ILightReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.data.IModelData;

@OnlyIn(Dist.CLIENT)
public class DogBedModel implements IBakedModel {

    public static DogBedItemOverride ITEM_OVERIDE = new DogBedItemOverride();

    private ModelLoader modelLoader;
    private BlockModel model;
    private IBakedModel bakedModel;

    private final Map<Triple<String, String, Direction>, IBakedModel> cache = Maps.newHashMap();

    public DogBedModel(ModelLoader modelLoader, BlockModel model, IBakedModel bakedModel) {
        this.modelLoader = modelLoader;
        this.model = model;
        this.bakedModel = bakedModel;
    }

    public IBakedModel getModelVariant(IBedMaterial casing, IBedMaterial bedding, Direction facing) {
        // Hotfix for possible optifine bug
        if (casing == null) { casing = ModBeddings.OAK; }
        if (bedding == null) { bedding = ModBeddings.WHITE; }
        if (facing == null) { facing = Direction.NORTH; }

        String casingTex = casing.getTexture();
        String beddingTex = bedding.getTexture();
        Triple<String, String, Direction> key = ImmutableTriple.of(casingTex, beddingTex, facing);
        return this.cache.computeIfAbsent(key, k -> bakeModelVariant(k.getLeft(), k.getMiddle(), k.getRight()));
    }

    @Override
    public List<BakedQuad> getQuads(BlockState state, Direction side, Random rand) {
        return this.getModelVariant(ModBeddings.OAK, ModBeddings.WHITE, Direction.NORTH).getQuads(state, side, rand);
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData data) {
        IBedMaterial casing = data.getData(TileEntityDogBed.CASING);
        IBedMaterial bedding = data.getData(TileEntityDogBed.BEDDING);
        Direction facing = data.getData(TileEntityDogBed.FACING);
        return this.getModelVariant(casing, bedding, facing).getQuads(state, side, rand);
    }

    @Override
    public TextureAtlasSprite getParticleTexture(@Nonnull IModelData data) {
        IBedMaterial casing = data.getData(TileEntityDogBed.CASING);
        IBedMaterial bedding = data.getData(TileEntityDogBed.BEDDING);
        Direction facing = data.getData(TileEntityDogBed.FACING);
        return this.getModelVariant(casing, bedding, facing).getParticleTexture();
    }

    @Override
    public IModelData getModelData(@Nonnull ILightReader world, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull IModelData tileData) {
        IBedMaterial casing = ModBeddings.OAK;
        IBedMaterial bedding = ModBeddings.WHITE;
        Direction facing = Direction.NORTH;

        TileEntity tile = world.getTileEntity(pos);
        if(tile instanceof TileEntityDogBed) {
            casing = ((TileEntityDogBed)tile).getCasingId();
            bedding = ((TileEntityDogBed)tile).getBeddingId();
        }

        if(state.has(BlockDogBed.FACING)) {
            facing = state.get(BlockDogBed.FACING);
        }

        tileData.setData(TileEntityDogBed.CASING, casing);
        tileData.setData(TileEntityDogBed.BEDDING, bedding);
        tileData.setData(TileEntityDogBed.FACING, facing);
        return tileData;
    }

    public IBakedModel bakeModelVariant(@Nonnull String casingResource, @Nonnull String beddingResource, @Nonnull Direction facing) {
        List<BlockPart> elements = Lists.newArrayList(); //We have to duplicate this so we can edit it below.
        for (BlockPart part : this.model.getElements()) {
            elements.add(new BlockPart(part.positionFrom, part.positionTo, Maps.newHashMap(part.mapFaces), part.partRotation, part.shade));
        }

        BlockModel newModel = new BlockModel(this.model.getParentLocation(), elements,
            Maps.newHashMap(this.model.textures), this.model.isAmbientOcclusion(), this.model.func_230176_c_(),
            this.model.getAllTransforms(), Lists.newArrayList(this.model.getOverrides()));
        newModel.name = this.model.name;
        newModel.parent = this.model.parent;

        newModel.textures.put("bedding", findTexture(ResourceLocation.tryCreate(beddingResource)));
        newModel.textures.put("casing", findTexture(ResourceLocation.tryCreate(casingResource)));
        newModel.textures.put("particle", findTexture(ResourceLocation.tryCreate(casingResource)));

        return newModel.bakeModel(this.modelLoader, newModel, ModelLoader.defaultTextureGetter(), getModelRotation(facing), createResourceVariant(casingResource, beddingResource, facing), true);
    }

    private ResourceLocation createResourceVariant(@Nonnull String casingResource, @Nonnull String beddingResource, @Nonnull Direction facing) {
        return new ResourceLocation(Reference.MOD_ID, "block/dog_bed"); // #bedding=" + beddingResource + ",casing=" + casingResource + ",facing=" + facing.getName()
    }

    private Either<Material, String> findTexture(ResourceLocation resource) {
        return Either.left(new Material(PlayerContainer.LOCATION_BLOCKS_TEXTURE, resource));
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
    public boolean func_230044_c_() {
        return this.bakedModel.func_230044_c_();
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

    @Override
    public IBakedModel handlePerspective(ItemCameraTransforms.TransformType cameraTransformType, MatrixStack mat) {
        return this;
    }
}