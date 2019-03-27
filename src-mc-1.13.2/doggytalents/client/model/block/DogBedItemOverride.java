package doggytalents.client.model.block;

import doggytalents.DoggyTalentsMod;
import doggytalents.api.registry.DogBedRegistry;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class DogBedItemOverride extends ItemOverrideList {

    public DogBedItemOverride() {
    }

    @Override
    public IBakedModel getModelWithOverrides(IBakedModel modelOriginal, ItemStack stack, World world, EntityLivingBase entity) {
    	DoggyTalentsMod.LOGGER.info("dawd");
        if (modelOriginal instanceof DogBedModel) {
        	if(stack.hasTag() && stack.getTag().hasKey("doggytalents")) {
    			NBTTagCompound tag = stack.getTag().getCompound("doggytalents");
    		    
    			String casingId = DogBedRegistry.CASINGS.getTexture(tag.getString("casingId"));
    			String beddingId = DogBedRegistry.BEDDINGS.getTexture(tag.getString("beddingId"));
    			return ((DogBedModel)modelOriginal).getCustomModel(casingId, beddingId, null);
        	}
        }

        return modelOriginal;
    }
}