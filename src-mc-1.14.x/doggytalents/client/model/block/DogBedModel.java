package doggytalents.client.model.block;

import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import doggytalents.api.registry.DogBedRegistry;
import doggytalents.block.BlockDogBed;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.BlockModel;
import net.minecraft.client.renderer.model.BlockPart;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.Direction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.model.TRSRTransformation;

@OnlyIn(Dist.CLIENT)
public class DogBedModel implements IBakedModel, IStateParticleModel {
	
    public static DogBedItemOverride ITEM_OVERIDE = new DogBedItemOverride();

    private ModelLoader modelLoader;
    private BlockModel model;
    private IBakedModel bakedModel;
    
    private final VertexFormat format;
    private final Map<Map<String, Direction>, IBakedModel> cache = Maps.newHashMap();

    public DogBedModel(ModelLoader modelLoader, BlockModel model, IBakedModel bakedModel, VertexFormat format) {
        this.modelLoader = modelLoader;
    	this.model = model;
        this.bakedModel = bakedModel;
        this.format = format;
    }

    public IBakedModel getCustomModelFromState(BlockState state) {
		String casing = "minecraft:block/oak_planks";
		String bedding = "minecraft:block/white_wool";
        Direction facing = Direction.NORTH;
        
		if(state != null) {
	        facing = state.get(BlockDogBed.FACING);
	        casing = DogBedRegistry.CASINGS.getTexture(state, BlockDogBed.CASING);
	        bedding = DogBedRegistry.BEDDINGS.getTexture(state, BlockDogBed.BEDDING);
		}
		
		return this.getCustomModel(casing, bedding, facing);
	}
    
    public IBakedModel getCustomModel(String casingResource, String beddingResource, Direction facing) {
        IBakedModel customModel = this.bakedModel;
        if(casingResource == null) casingResource = "nomissing";
        if(beddingResource == null) beddingResource = "nomissing";
        if(facing == null) facing = Direction.NORTH;
        

        Map<String, Direction> cacheKey = Maps.newHashMap();
        cacheKey.put(beddingResource + casingResource, facing);

        if(this.cache.containsKey(cacheKey))
            customModel = this.cache.get(cacheKey);
        else if(this.model != null) {
            ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
            builder.put("bedding", beddingResource);
            builder.put("casing", casingResource);
            builder.put("particle", casingResource);
            ImmutableMap<String, String> textures = builder.build();
            
            
            List<BlockPart> elements = Lists.newArrayList(); //We have to duplicate this so we can edit it below.
            for (BlockPart part : this.model.getElements()) {
                elements.add(new BlockPart(part.positionFrom, part.positionTo, Maps.newHashMap(part.mapFaces), part.partRotation, part.shade));
            }

            BlockModel newModel = new BlockModel(this.model.getParentLocation(), elements,
                Maps.newHashMap(this.model.textures), this.model.isAmbientOcclusion(), this.model.isGui3d(), //New Textures man VERY IMPORTANT
                this.model.getAllTransforms(), Lists.newArrayList(model.getOverrides()));
            newModel.name = this.model.name;
            newModel.parent = this.model.parent;

            newModel.textures.put("bedding", beddingResource);
            newModel.textures.put("casing", casingResource);
            newModel.textures.put("particle", casingResource);
            

           // customModel = retexturedModel.bake(new TRSRTransformation(TRSRTransformation.getMatrix(facing)), this.format, this.textureGetter::apply);
            customModel = newModel.bake(this.modelLoader, ModelLoader.defaultTextureGetter(), TRSRTransformation.getRotation(facing), this.format);
            this.cache.put(cacheKey, customModel);
        }

        return customModel;
    }

    @Override
    public List<BakedQuad> getQuads(BlockState state, Direction side, Random rand) {
    	return this.getCustomModelFromState(state).getQuads(state, side, rand);
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
	public TextureAtlasSprite getParticleTexture(BlockState state) {
		return this.getCustomModelFromState(state).getParticleTexture();
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