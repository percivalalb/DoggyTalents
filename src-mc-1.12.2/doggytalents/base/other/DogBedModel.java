package doggytalents.base.other;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import doggytalents.api.registry.DogBedRegistry;
import doggytalents.base.VersionControl.VersionConfig;
import doggytalents.block.BlockDogBed;
import doggytalents.client.model.block.IStateParticleModel;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.client.model.IRetexturableModel;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.common.property.IExtendedBlockState;

@VersionConfig({"1.9.4", "1.10.2", "1.11.2"})
public class DogBedModel implements IPerspectiveAwareModel, IStateParticleModel {
	
    public static DogBedItemOverride ITEM_OVERIDE = new DogBedItemOverride();
	
    private IPerspectiveAwareModel variantModel;
    private IRetexturableModel baseModel;

    private final Function<ResourceLocation, TextureAtlasSprite> textureGetter;
    private final VertexFormat format;
    private final Map<Map<String, EnumFacing>, IBakedModel> cache = Maps.newHashMap();

    public DogBedModel(IPerspectiveAwareModel modelDefault, IRetexturableModel modelWooden, VertexFormat format) {
        this.variantModel = modelDefault;
        this.baseModel = modelWooden;
        this.textureGetter = location -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString());
        this.format = format;
    }

	public IBakedModel getCustomModelFromState(IBlockState state) {
		String casing = "minecraft:blocks/planks_oak";
		String bedding = "minecraft:blocks/wool_colored_white";
        EnumFacing facing = EnumFacing.NORTH;
        
		if(state instanceof IExtendedBlockState) {
            IExtendedBlockState extendedState = (IExtendedBlockState)state;
 
            if(extendedState.getUnlistedNames().contains(BlockDogBed.CASING))
            	casing = DogBedRegistry.CASINGS.getTexture(extendedState.getValue(BlockDogBed.CASING));

            if(extendedState.getUnlistedNames().contains(BlockDogBed.BEDDING))
            	bedding = DogBedRegistry.BEDDINGS.getTexture(extendedState.getValue(BlockDogBed.BEDDING));
            
            facing = extendedState.getValue(BlockDogBed.FACING);
        }
		
		return this.getCustomModel(casing, bedding, facing);
	}
    
    public IBakedModel getCustomModel(String casingResource, String beddingResource, EnumFacing facing) {
        IBakedModel customModel = this.variantModel;
        if(casingResource == null) casingResource = "nomissing";
        if(beddingResource == null) beddingResource = "nomissing";

        Map<String, EnumFacing> cacheKey = Maps.newHashMap();
        cacheKey.put(beddingResource + casingResource, facing);

        if(this.cache.containsKey(cacheKey))
            customModel = this.cache.get(cacheKey);
        else if(this.baseModel != null) {
            ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
            builder.put("bedding", beddingResource);
            builder.put("casing", casingResource);
            builder.put("particle", casingResource);
            IModel retexturedModel = this.baseModel.retexture(builder.build());

            //Likely inventory render
            if(facing == null) facing = EnumFacing.NORTH;

            customModel = retexturedModel.bake(new TRSRTransformation(TRSRTransformation.getMatrix(facing)), this.format, this.textureGetter::apply);

            this.cache.put(cacheKey, customModel);
        }

        return customModel;
    }

    @Override
    public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
    	return this.getCustomModelFromState(state).getQuads(state, side, rand);
    }

    @Override
    public boolean isAmbientOcclusion() {
        return this.variantModel.isAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return this.variantModel.isGui3d();
    }

    @Override
    public boolean isBuiltInRenderer() {
        return this.variantModel.isBuiltInRenderer();
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return this.variantModel.getParticleTexture();
    }
    
	@Override
	public TextureAtlasSprite getParticleTexture(IBlockState state) {
		return this.getCustomModelFromState(state).getParticleTexture();
	}

    @Override
    public ItemCameraTransforms getItemCameraTransforms() {
        return this.variantModel.getItemCameraTransforms();
    }

    @Override
    public ItemOverrideList getOverrides() {
        return ITEM_OVERIDE;
    }

    @Override
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType) {
        return Pair.of(this, this.variantModel.handlePerspective(cameraTransformType).getRight());
    }
}