package doggytalents.api.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;

import doggytalents.client.entity.model.DogModel;
import doggytalents.common.entity.DogEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;

public interface ITalentRenderer {

    default void render(LayerRenderer<DogEntity, DogModel> layer, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, DogEntity dog, int level, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {

    }
}
