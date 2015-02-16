package doggytalents.client.renderer.block;

import java.util.Iterator;
import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BreakingFour;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ModelBlock;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.SimpleBakedModel;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.IFlexibleBakedModel;
import net.minecraftforge.client.model.ISmartBlockModel;
import net.minecraftforge.client.model.ISmartItemModel;
import net.minecraftforge.common.property.IExtendedBlockState;

import com.google.common.collect.Lists;

import doggytalents.api.registry.DogBedRegistry;
import doggytalents.block.BlockDogBed;

public class DogBedModelTexture extends SimpleBakedModel implements ISmartBlockModel, ISmartItemModel {
	
	
	public DogBedModelTexture(List p_i46077_1_, List p_i46077_2_, boolean p_i46077_3_, boolean p_i46077_4_, TextureAtlasSprite particleTexture, ItemCameraTransforms p_i46077_6_) {
		super(p_i46077_1_, p_i46077_2_, p_i46077_3_, p_i46077_4_, particleTexture, p_i46077_6_);
	}
	
	@Override
    public IBakedModel handleBlockState(IBlockState blockstate) {
    	if(blockstate instanceof IExtendedBlockState) {
    		IExtendedBlockState exState = (IExtendedBlockState)blockstate;
    		TextureAtlasSprite casingTexture = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(DogBedRegistry.CASINGS.getTexture((String)exState.getValue(BlockDogBed.CASING)));
  		    TextureAtlasSprite beddingTexture = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(DogBedRegistry.BEDDINGS.getTexture((String)exState.getValue(BlockDogBed.BEDDING)));
	    	return new DogBedModelTexture.Builder(this, casingTexture, beddingTexture).makeBakedModel();
    	}
	    return this;
    }

    @Override
    public IBakedModel handleItemState(ItemStack stack) {
    	if(stack.hasTagCompound() && stack.getTagCompound().hasKey("doggytalents")) {
			NBTTagCompound tag = stack.getTagCompound().getCompoundTag("doggytalents");
		    
		    TextureAtlasSprite casingTexture = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(DogBedRegistry.CASINGS.getTexture(tag.getString("casingId")));
		    TextureAtlasSprite beddingTexture = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(DogBedRegistry.BEDDINGS.getTexture(tag.getString("beddingId")));
		    return new DogBedModelTexture.Builder(this, casingTexture, beddingTexture).makeBakedModel();
    	}
    	
    	return this;
    }
    
	public static class Builder {
        private final List builderGeneralQuads;
        private final List builderFaceQuads;
        private final boolean builderAmbientOcclusion;
        private TextureAtlasSprite builderTexture;
        private boolean builderGui3d;
        private ItemCameraTransforms builderCameraTransforms;

        public Builder(IBakedModel p_i46075_1_, TextureAtlasSprite casingTexture, TextureAtlasSprite beddingTexture) {
            this(p_i46075_1_.isAmbientOcclusion(), p_i46075_1_.isGui3d(), p_i46075_1_.getItemCameraTransforms());
            this.builderTexture = casingTexture;
            EnumFacing[] aenumfacing = EnumFacing.values();
            int i = aenumfacing.length;

            for (int j = 0; j < i; ++j)
            {
                EnumFacing enumfacing = aenumfacing[j];
                this.addFaceBreakingFours(p_i46075_1_, casingTexture, beddingTexture, enumfacing);
            }

            this.addGeneralBreakingFours(p_i46075_1_, casingTexture, beddingTexture);
        }

        private void addFaceBreakingFours(IBakedModel p_177649_1_, TextureAtlasSprite casingTexture, TextureAtlasSprite beddingTexture, EnumFacing p_177649_3_)
        {
            Iterator iterator = p_177649_1_.getFaceQuads(p_177649_3_).iterator();

            int i = 0;
            while (iterator.hasNext())
            {
                BakedQuad bakedquad = (BakedQuad)iterator.next();
                TextureAtlasSprite sprite = casingTexture;
                //if(i == 1)
                //	sprite = beddingTexture;
                this.addFaceQuad(p_177649_3_, new BreakingFour(bakedquad, sprite));
                i += 1;
            }
        }

        private void addGeneralBreakingFours(IBakedModel p_177647_1_, TextureAtlasSprite casingTexture, TextureAtlasSprite beddingTexture)
        {
            Iterator iterator = p_177647_1_.getGeneralQuads().iterator();

            int i = 0;
            while (iterator.hasNext())
            {
                BakedQuad bakedquad = (BakedQuad)iterator.next();
                TextureAtlasSprite sprite = casingTexture;
                if(i == 1 || i == 2 || i == 3 || i == 4)
                	sprite = beddingTexture;
                this.addGeneralQuad(new BreakingFour(bakedquad, sprite));
                i += 1;
            }
        }

        private Builder(boolean p_i46076_1_, boolean p_i46076_2_, ItemCameraTransforms p_i46076_3_)
        {
            this.builderGeneralQuads = Lists.newArrayList();
            this.builderFaceQuads = Lists.newArrayListWithCapacity(6);
            EnumFacing[] aenumfacing = EnumFacing.values();
            int i = aenumfacing.length;

            for (int j = 0; j < i; ++j)
            {
                EnumFacing enumfacing = aenumfacing[j];
                this.builderFaceQuads.add(Lists.newArrayList());
            }

            this.builderAmbientOcclusion = p_i46076_1_;
            this.builderGui3d = p_i46076_2_;
            this.builderCameraTransforms = p_i46076_3_;
        }

        public DogBedModelTexture.Builder addFaceQuad(EnumFacing p_177650_1_, BakedQuad p_177650_2_)
        {
            ((List)this.builderFaceQuads.get(p_177650_1_.ordinal())).add(p_177650_2_);
            return this;
        }

        public DogBedModelTexture.Builder addGeneralQuad(BakedQuad p_177648_1_)
        {
            this.builderGeneralQuads.add(p_177648_1_);
            return this;
        }

        public DogBedModelTexture.Builder setTexture(TextureAtlasSprite p_177646_1_)
        {
            this.builderTexture = p_177646_1_;
            return this;
        }

        public IBakedModel makeBakedModel()
        {
            if (this.builderTexture == null)
            {
                throw new RuntimeException("Missing particle!");
            }
            else
            {
                return new DogBedModelTexture(this.builderGeneralQuads, this.builderFaceQuads, this.builderAmbientOcclusion, this.builderGui3d, this.builderTexture, this.builderCameraTransforms);
            }
        }
    }
}
