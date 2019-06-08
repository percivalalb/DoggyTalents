package doggytalents.client.model.block;

import doggytalents.api.registry.DogBedRegistry;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class DogBedItemOverride extends ItemOverrideList {

    @Override
    public IBakedModel getModelWithOverrides(IBakedModel modelOriginal, ItemStack stack, World world, LivingEntity entity) {
        if(modelOriginal instanceof DogBedModel) {
        	if(stack.hasTag() && stack.getTag().contains("doggytalents")) {
    			CompoundNBT tag = stack.getTag().getCompound("doggytalents");
    		    
    			String casingId = DogBedRegistry.CASINGS.getTexture(tag, "casingId");
    			String beddingId = DogBedRegistry.BEDDINGS.getTexture(tag, "beddingId");
    			return ((DogBedModel)modelOriginal).getCustomModel(casingId, beddingId, Direction.NORTH);
        	}
        }

        return modelOriginal;
    }
}