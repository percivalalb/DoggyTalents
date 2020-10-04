package doggytalents.client.block.model;

import javax.annotation.Nullable;

import org.apache.commons.lang3.tuple.Pair;

import doggytalents.api.registry.IBeddingMaterial;
import doggytalents.api.registry.ICasingMaterial;
import doggytalents.common.util.DogBedUtil;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class DogBedItemOverride extends ItemOverrideList {

    @Override
    public IBakedModel getOverrideModel(IBakedModel modelOriginal, ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity livingEntity) {
        if (modelOriginal instanceof DogBedModel) {
            Pair<ICasingMaterial, IBeddingMaterial> materials = DogBedUtil.getMaterials(stack);
            return ((DogBedModel) modelOriginal).getModelVariant(materials.getLeft(), materials.getRight(), Direction.NORTH);
        }

        return modelOriginal;
    }
}