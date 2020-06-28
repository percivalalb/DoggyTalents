package doggytalents.api.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;

import doggytalents.api.inferface.AbstractDogEntity;
import doggytalents.api.registry.AccessoryInstance;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;

public interface IAccessoryRenderer<T extends AbstractDogEntity> {

    default void render(LayerRenderer<T, EntityModel<T>> layer, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, T dog, AccessoryInstance data, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {

    }
}
