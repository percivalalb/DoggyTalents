package doggytalents.base.b;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

import doggytalents.api.registry.DogBedRegistry;
import doggytalents.block.BlockDogBed;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BreakingFour;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.SimpleBakedModel;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.ISmartBlockModel;
import net.minecraftforge.client.model.ISmartItemModel;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(value = Side.CLIENT)
public class DogBedModel extends SimpleBakedModel implements ISmartBlockModel, ISmartItemModel {
	
	public DogBedModel(List<BakedQuad> generalQuadsIn, List<List<BakedQuad>> faceQuadsIn, boolean ambientOcclusionIn, boolean gui3dIn, TextureAtlasSprite particleTexture, ItemCameraTransforms cameraTransformsIn) {
		super(generalQuadsIn, faceQuadsIn, ambientOcclusionIn, gui3dIn, particleTexture, cameraTransformsIn);
	}
	
	@Override
    public IBakedModel handleBlockState(IBlockState blockstate) {
    	if(blockstate instanceof IExtendedBlockState) {
    		IExtendedBlockState exState = (IExtendedBlockState)blockstate;
    		String casingId = (String)exState.getValue(BlockDogBed.CASING);
  		    String beddingId = (String)exState.getValue(BlockDogBed.BEDDING);
	    	return this.getCachedModel(casingId, beddingId);
    	}
	    return this;
    }

    @Override
    public IBakedModel handleItemState(ItemStack stack) {
    	if(stack.hasTagCompound() && stack.getTagCompound().hasKey("doggytalents")) {
			NBTTagCompound tag = stack.getTagCompound().getCompoundTag("doggytalents");
		    
			String casingId = tag.getString("casingId");
			String beddingId = tag.getString("beddingId");
		    return this.getCachedModel(casingId, beddingId);
    	}
    	
    	return this;
    }
    
    private final Map<List<String>, IBakedModel> cache = new HashMap<List<String>, IBakedModel>();

    public IBakedModel getCachedModel(String casingId, String beddingId) {
    	
    	List<String> key = Arrays.asList(casingId, beddingId);
  
        if(!this.cache.containsKey(key)) {
        	TextureAtlasSprite casingTexture = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(DogBedRegistry.CASINGS.getTexture(casingId));
   		    TextureAtlasSprite beddingTexture = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(DogBedRegistry.BEDDINGS.getTexture(beddingId));
            this.cache.put(key, new DogBedModel.Builder(this, casingTexture, beddingTexture).makeBakedModel());
        }
        
        return this.cache.get(key);
    }
    
	public static class Builder {
		
        private final List<BakedQuad> builderGeneralQuads;
        private final List<List<BakedQuad>> builderFaceQuads;
        private final boolean builderAmbientOcclusion;
        private TextureAtlasSprite builderTexture;
        private boolean builderGui3d;
        private ItemCameraTransforms builderCameraTransforms;

        public Builder(IBakedModel orignalModel, TextureAtlasSprite casingTexture, TextureAtlasSprite beddingTexture) {
            this(orignalModel.isAmbientOcclusion(), orignalModel.isGui3d(), orignalModel.getItemCameraTransforms());
            this.builderTexture = casingTexture;
            
            for(EnumFacing facing : EnumFacing.VALUES)
                this.addFaceBreakingFours(orignalModel, casingTexture, beddingTexture, facing);

            this.addGeneralBreakingFours(orignalModel, casingTexture, beddingTexture);
        }
        
        private Builder(boolean ambientOcclusionIn, boolean gui3dIn, ItemCameraTransforms cameraTransformsIn) {
            this.builderGeneralQuads = Lists.newArrayList();
            this.builderFaceQuads = Lists.newArrayListWithCapacity(6);
            
            for(EnumFacing facing : EnumFacing.VALUES)
                this.builderFaceQuads.add(Lists.newArrayList());

            this.builderAmbientOcclusion = ambientOcclusionIn;
            this.builderGui3d = gui3dIn;
            this.builderCameraTransforms = cameraTransformsIn;
        }

        private void addFaceBreakingFours(IBakedModel orignalModel, TextureAtlasSprite casingTexture, TextureAtlasSprite beddingTexture, EnumFacing facing) {
            Iterator iterator = orignalModel.getFaceQuads(facing).iterator();

            int i = 0;
            while (iterator.hasNext()) {
                BakedQuad bakedquad = (BakedQuad)iterator.next();
                TextureAtlasSprite sprite = casingTexture;
                //if(i == 1)
                //	sprite = beddingTexture;
                this.addFaceQuad(facing, new BreakingFour(bakedquad, sprite));
                i += 1;
            }
        }

        private void addGeneralBreakingFours(IBakedModel orignalModel, TextureAtlasSprite casingTexture, TextureAtlasSprite beddingTexture) {
            Iterator iterator = orignalModel.getGeneralQuads().iterator();

            int i = 0;
            while(iterator.hasNext()) {
                BakedQuad bakedquad = (BakedQuad)iterator.next();
                TextureAtlasSprite sprite = casingTexture;
                if(i == 1 || i == 2 || i == 3 || i == 4)
                	sprite = beddingTexture;
                this.addGeneralQuad(new BreakingFour(bakedquad, sprite));
                i += 1;
            }
        }

        public DogBedModel.Builder addFaceQuad(EnumFacing facing, BakedQuad bakedQuad) {
            ((List)this.builderFaceQuads.get(facing.ordinal())).add(bakedQuad);
            return this;
        }

        public DogBedModel.Builder addGeneralQuad(BakedQuad bakedQuad) {
            this.builderGeneralQuads.add(bakedQuad);
            return this;
        }

        public DogBedModel.Builder setTexture(TextureAtlasSprite textureIn) {
            this.builderTexture = textureIn;
            return this;
        }

        public IBakedModel makeBakedModel() {
            if (this.builderTexture == null)
                throw new RuntimeException("Missing particle!");
            else
                return new DogBedModel(this.builderGeneralQuads, this.builderFaceQuads, this.builderAmbientOcclusion, this.builderGui3d, this.builderTexture, this.builderCameraTransforms);
        }
    }
}
