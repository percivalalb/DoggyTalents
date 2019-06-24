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
            CompoundNBT tag = stack.getChildTag("doggytalents");
            if(tag != null) {
                String casingId = DogBedRegistry.CASINGS.get(tag.getString("casingId")).getTexture().toString();
                String beddingId = DogBedRegistry.BEDDINGS.get(tag.getString("beddingId")).getTexture().toString();
                return ((DogBedModel)modelOriginal).getCustomModel(casingId, beddingId, Direction.NORTH);
            }
        }

        return modelOriginal;
    }
}