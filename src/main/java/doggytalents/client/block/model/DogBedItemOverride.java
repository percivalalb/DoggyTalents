package doggytalents.client.block.model;

import org.apache.commons.lang3.tuple.Pair;

import doggytalents.api.registry.IBeddingMaterial;
import doggytalents.api.registry.ICasingMaterial;
import doggytalents.common.util.DogBedUtil;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class DogBedItemOverride extends ItemOverrideList {

    @Override
    public IBakedModel getModelWithOverrides(IBakedModel modelOriginal, ItemStack stack, World world, LivingEntity entity) {
        if(modelOriginal instanceof DogBedModel) {
            Pair<ICasingMaterial, IBeddingMaterial> materials = DogBedUtil.getMaterials(stack);
            return ((DogBedModel) modelOriginal).getModelVariant(materials.getLeft(), materials.getRight(), Direction.NORTH);
        }

        return modelOriginal;
    }
}