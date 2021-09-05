package doggytalents.client.block.model;

import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Either;
import doggytalents.DoggyTalents2;
import doggytalents.api.registry.IBeddingMaterial;
import doggytalents.api.registry.ICasingMaterial;
import doggytalents.common.block.DogBedBlock;
import doggytalents.common.block.tileentity.DogBedTileEntity;
import doggytalents.common.lib.Constants;
import net.minecraft.client.particle.TerrainParticle;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.registries.IRegistryDelegate;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

@OnlyIn(Dist.CLIENT)
public class DogBedModel implements BakedModel {

    public static DogBedItemOverride ITEM_OVERIDE = new DogBedItemOverride();
    private static final ResourceLocation MISSING_TEXTURE = new ResourceLocation("missingno");

    private ModelLoader modelLoader;
    private BlockModel model;
    private BakedModel bakedModel;

    private final Map<Triple<IRegistryDelegate<ICasingMaterial>, IRegistryDelegate<IBeddingMaterial>, Direction>, BakedModel> cache = Maps.newHashMap();

    public DogBedModel(ModelLoader modelLoader, BlockModel model, BakedModel bakedModel) {
        this.modelLoader = modelLoader;
        this.model = model;
        this.bakedModel = bakedModel;
    }

    public BakedModel getModelVariant(@Nonnull IModelData data) {
        return this.getModelVariant(data.getData(DogBedTileEntity.CASING), data.getData(DogBedTileEntity.BEDDING), data.getData(DogBedTileEntity.FACING));
    }

    public BakedModel getModelVariant(ICasingMaterial casing, IBeddingMaterial bedding, Direction facing) {
        Triple<IRegistryDelegate<ICasingMaterial>, IRegistryDelegate<IBeddingMaterial>, Direction> key =
                ImmutableTriple.of(casing != null ? casing.delegate : null, bedding != null ? bedding.delegate : null, facing != null ? facing : Direction.NORTH);

        return this.cache.computeIfAbsent(key, (k) -> bakeModelVariant(k.getLeft(), k.getMiddle(), k.getRight()));
    }

    @Override
    public List<BakedQuad> getQuads(BlockState state, Direction side, Random rand) {
        return this.getModelVariant(null, null, Direction.NORTH).getQuads(state, side, rand, EmptyModelData.INSTANCE);
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData data) {
        return this.getModelVariant(data).getQuads(state, side, rand, data);
    }

    @Override
    public TextureAtlasSprite getParticleIcon(@Nonnull IModelData data) {
        return this.getModelVariant(data).getParticleIcon(data);
    }

    @Override
    public IModelData getModelData(@Nonnull BlockAndTintGetter world, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull IModelData tileData) {
        ICasingMaterial casing = null;
        IBeddingMaterial bedding = null;
        Direction facing = Direction.NORTH;

        BlockEntity tile = world.getBlockEntity(pos);
        if (tile instanceof DogBedTileEntity) {
            casing = ((DogBedTileEntity) tile).getCasing();
            bedding = ((DogBedTileEntity) tile).getBedding();
        }

        if (state.hasProperty(DogBedBlock.FACING)) {
            facing = state.getValue(DogBedBlock.FACING);
        }

        tileData.setData(DogBedTileEntity.CASING, casing);
        tileData.setData(DogBedTileEntity.BEDDING, bedding);
        tileData.setData(DogBedTileEntity.FACING, facing);

        return tileData;
    }

    public BakedModel bakeModelVariant(@Nullable IRegistryDelegate<ICasingMaterial> casingResource, @Nullable IRegistryDelegate<IBeddingMaterial> beddingResource, @Nonnull Direction facing) {
        List<BlockElement> parts = this.model.getElements();
        List<BlockElement> elements = new ArrayList<>(parts.size()); //We have to duplicate this so we can edit it below.
        for (BlockElement part : parts) {
            elements.add(new BlockElement(part.from, part.to, Maps.newHashMap(part.faces), part.rotation, part.shade));
        }

        BlockModel newModel = new BlockModel(this.model.getParentLocation(), elements,
            Maps.newHashMap(this.model.textureMap), this.model.hasAmbientOcclusion(), this.model.getGuiLight(),
            this.model.getTransforms(), new ArrayList<>(this.model.getOverrides()));
        newModel.name = this.model.name;
        newModel.parent = this.model.parent;


        Either<Material, String> casingTexture = findCasingTexture(casingResource);
        newModel.textureMap.put("bedding", findBeddingTexture(beddingResource));
        newModel.textureMap.put("casing", casingTexture);
        newModel.textureMap.put("particle", casingTexture);

        return newModel.bake(this.modelLoader, newModel, ModelLoader.defaultTextureGetter(), getModelRotation(facing), createResourceVariant(casingResource, beddingResource, facing), true);
    }

    private ResourceLocation createResourceVariant(@Nonnull IRegistryDelegate<ICasingMaterial> casingResource, @Nonnull IRegistryDelegate<IBeddingMaterial> beddingResource, @Nonnull Direction facing) {
        String beddingKey = beddingResource != null
                ? beddingResource.name().toString().replace(':', '.')
                : "doggytalents.dogbed.bedding.missing";
        String casingKey = beddingResource != null
                ? casingResource.name().toString().replace(':', '.')
                : "doggytalents.dogbed.casing.missing";
        return new ModelResourceLocation(Constants.MOD_ID, "block/dog_bed#bedding=" + beddingKey + ",casing=" + casingKey + ",facing=" + facing.getName());
    }

    private Either<Material, String> findCasingTexture(@Nullable IRegistryDelegate<ICasingMaterial> resource) {
        return findTexture(resource != null ? resource.get().getTexture() : null);
    }

    private Either<Material, String> findBeddingTexture(@Nullable IRegistryDelegate<IBeddingMaterial> resource) {
        return findTexture(resource != null ? resource.get().getTexture() : null);
    }

    private Either<Material, String> findTexture(@Nullable ResourceLocation resource) {
        if (resource == null) {
            resource = MISSING_TEXTURE;
        }

        return Either.left(new Material(InventoryMenu.BLOCK_ATLAS, resource));
    }

    private BlockModelRotation getModelRotation(@Nonnull Direction dir) {
        switch (dir) {
        default:    return BlockModelRotation.X0_Y0; // North
        case EAST:  return BlockModelRotation.X0_Y90;
        case SOUTH: return BlockModelRotation.X0_Y180;
        case WEST:  return BlockModelRotation.X0_Y270;
        }
    }

    @Override
    public boolean useAmbientOcclusion() {
        return this.bakedModel.useAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return this.bakedModel.isGui3d();
    }

    @Override
    public boolean usesBlockLight() {
        return this.bakedModel.usesBlockLight();
    }

    @Override
    public boolean isCustomRenderer() {
        return this.bakedModel.isCustomRenderer();
    }

    @Override
    public TextureAtlasSprite getParticleIcon() {
        return this.bakedModel.getParticleIcon();
    }

    @Override
    public ItemTransforms getTransforms() {
        return this.bakedModel.getTransforms();
    }

    @Override
    public ItemOverrides getOverrides() {
        return ITEM_OVERIDE;
    }
}
