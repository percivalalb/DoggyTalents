package doggytalents.client.model.block;

import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import doggytalents.ModBeddings;
import doggytalents.api.inferface.IBedMaterial;
import doggytalents.block.BlockDogBed;
import doggytalents.tileentity.TileEntityDogBed;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.BlockModel;
import net.minecraft.client.renderer.model.BlockPart;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IEnviromentBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.common.model.TRSRTransformation;

@OnlyIn(Dist.CLIENT)
public class DogBedModel implements IBakedModel, IStateParticleModel {
    
    public static DogBedItemOverride ITEM_OVERIDE = new DogBedItemOverride();

    private ModelLoader modelLoader;
    private BlockModel model;
    private IBakedModel bakedModel;
    
    private final VertexFormat format;
    private final Map<Triple<String, String, Direction>, IBakedModel> cache = Maps.newHashMap();

    public DogBedModel(ModelLoader modelLoader, BlockModel model, IBakedModel bakedModel, VertexFormat format) {
        this.modelLoader = modelLoader;
        this.model = model;
        this.bakedModel = bakedModel;
        this.format = format;
    }
    
    public IBakedModel getCustomModel(IBedMaterial casing, IBedMaterial bedding, Direction facing) {
        String casingTex = casing.getTexture();
        String beddingTex = bedding.getTexture();
        
        return this.getCustomModel(casingTex, beddingTex, facing);
    }
    
    @Override
    public List<BakedQuad> getQuads(BlockState state, Direction side, Random rand) {
        return this.getCustomModel(ModBeddings.OAK, ModBeddings.WHITE, Direction.NORTH).getQuads(state, side, rand);
    }
    
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData) {
        IBedMaterial casing = extraData.getData(TileEntityDogBed.CASING);
        IBedMaterial bedding = extraData.getData(TileEntityDogBed.BEDDING);
        Direction facing = extraData.getData(TileEntityDogBed.FACING);
        return this.getCustomModel(casing, bedding, facing).getQuads(state, side, rand);
    }

    @Override
    public IModelData getModelData(@Nonnull IEnviromentBlockReader world, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull IModelData tileData) {
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
    
    public IBakedModel getCustomModel(@Nonnull String casingResource, @Nonnull String beddingResource, @Nonnull Direction facing) {
        IBakedModel customModel = this.bakedModel;

        Triple<String, String, Direction> key = ImmutableTriple.of(casingResource, beddingResource, facing);

        IBakedModel possibleModel = this.cache.get(key);
        
        if(possibleModel != null) {
            customModel = possibleModel;
        } else if(this.model != null) {
            List<BlockPart> elements = Lists.newArrayList(); //We have to duplicate this so we can edit it below.
            for (BlockPart part : this.model.getElements()) {
                elements.add(new BlockPart(part.positionFrom, part.positionTo, Maps.newHashMap(part.mapFaces), part.partRotation, part.shade));
            }

            BlockModel newModel = new BlockModel(this.model.getParentLocation(), elements,
                Maps.newHashMap(this.model.textures), this.model.isAmbientOcclusion(), this.model.isGui3d(), //New Textures man VERY IMPORTANT
                this.model.getAllTransforms(), Lists.newArrayList(this.model.getOverrides()));
            newModel.name = this.model.name;
            newModel.parent = this.model.parent;

            newModel.textures.put("bedding", beddingResource);
            newModel.textures.put("casing", casingResource);
            newModel.textures.put("particle", casingResource);
            
            customModel = newModel.bake(this.modelLoader, ModelLoader.defaultTextureGetter(), TRSRTransformation.getRotation(facing), this.format);
            this.cache.put(key, customModel);
        }

        return customModel;
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
    public TextureAtlasSprite getParticleTexture(World worldIn, BlockPos pos, BlockState state, Direction side) {
        IBedMaterial casing = ModBeddings.OAK;
        IBedMaterial bedding = ModBeddings.WHITE;
        Direction facing = Direction.NORTH;
        
        TileEntity tile = worldIn.getTileEntity(pos);
        if(tile instanceof TileEntityDogBed) {
            casing = ((TileEntityDogBed)tile).getCasingId();
            bedding = ((TileEntityDogBed)tile).getBeddingId();
        }
        
        if(state.has(BlockDogBed.FACING)) {
            facing = state.get(BlockDogBed.FACING);
        }
        
        return this.getCustomModel(casing, bedding, facing).getParticleTexture();
    }

    @Override
    public ItemOverrideList getOverrides() {
        return ITEM_OVERIDE;
    }

    @Override
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType) {
        return Pair.of(this, this.bakedModel.handlePerspective(cameraTransformType).getRight());
    }
}