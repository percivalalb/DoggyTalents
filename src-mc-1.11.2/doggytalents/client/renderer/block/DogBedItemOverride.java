package doggytalents.client.renderer.block;

import com.google.common.collect.Lists;

import doggytalents.api.registry.DogBedRegistry;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverride;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class DogBedItemOverride extends ItemOverrideList {

    public DogBedItemOverride() {
        super(Lists.<ItemOverride>newArrayList());
    }

    @Override
    public IBakedModel handleItemState(IBakedModel modelOriginal, ItemStack stack, World world, EntityLivingBase entity) {
        if (modelOriginal instanceof DogBedModel) {
        	if(stack.hasTagCompound() && stack.getTagCompound().hasKey("doggytalents")) {
    			NBTTagCompound tag = stack.getTagCompound().getCompoundTag("doggytalents");
    		    
    			String casingId = DogBedRegistry.CASINGS.getTexture(tag.getString("casingId"));
    			String beddingId = DogBedRegistry.BEDDINGS.getTexture(tag.getString("beddingId"));
    			return ((DogBedModel)modelOriginal).getCustomModel(casingId, beddingId, null);
        	}
        }

        return modelOriginal;
    }
}