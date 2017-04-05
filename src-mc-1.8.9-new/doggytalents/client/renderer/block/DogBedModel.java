package doggytalents.client.renderer.block;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import javax.vecmath.Matrix4f;
import org.apache.commons.lang3.tuple.Pair;

import doggytalents.api.registry.DogBedRegistry;
import doggytalents.block.BlockDogBed;
import jline.internal.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.IFlexibleBakedModel;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.client.model.IRetexturableModel;
import net.minecraftforge.client.model.ISmartBlockModel;
import net.minecraftforge.client.model.ISmartItemModel;
import net.minecraftforge.client.model.TRSRTransformation;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.fml.common.FMLLog;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class DogBedModel implements IPerspectiveAwareModel, ISmartBlockModel, ISmartItemModel {
	
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

    public IBakedModel getCustomModel(String casingResource, String beddingResource, @Nullable EnumFacing facing) {
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
	public List<BakedQuad> getFaceQuads(EnumFacing side) {
		return null;
	}

	@Override
	public List<BakedQuad> getGeneralQuads() {
		return null;
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
    public ItemCameraTransforms getItemCameraTransforms() {
        return this.variantModel.getItemCameraTransforms();
    }
    
	@Override
	public VertexFormat getFormat() {
		return this.format;
	}

    @Override
    public Pair<? extends IFlexibleBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType) {
        return Pair.of(this, this.variantModel.handlePerspective(cameraTransformType).getRight());
    }
    
    @Override
    public IBakedModel handleBlockState(IBlockState blockstate) {
		String casing = "minecraft:blocks/planks_oak";
        String bedding = "minecraft:blocks/wool_colored_white";
        EnumFacing facing = EnumFacing.NORTH;
        
        if(blockstate instanceof IExtendedBlockState) {
            IExtendedBlockState extendedState = (IExtendedBlockState)blockstate;
 
            if(extendedState.getUnlistedNames().contains(BlockDogBed.CASING))
            	casing = DogBedRegistry.CASINGS.getTexture(extendedState.getValue(BlockDogBed.CASING));

            if(extendedState.getUnlistedNames().contains(BlockDogBed.BEDDING))
            	bedding = DogBedRegistry.BEDDINGS.getTexture(extendedState.getValue(BlockDogBed.BEDDING));
            
            facing = extendedState.getValue(BlockDogBed.FACING);
        }
    	
    	return this.getCustomModel(casing, bedding, facing);
    }

    @Override
    public IBakedModel handleItemState(ItemStack stack) {
    	if(stack.hasTagCompound() && stack.getTagCompound().hasKey("doggytalents")) {
			NBTTagCompound tag = stack.getTagCompound().getCompoundTag("doggytalents");
		    
			String casingId = DogBedRegistry.CASINGS.getTexture(tag.getString("casingId"));
			String beddingId = DogBedRegistry.BEDDINGS.getTexture(tag.getString("beddingId"));
		    return this.getCustomModel(casingId, beddingId, null);
    	}
    	
    	return this;
    }
}