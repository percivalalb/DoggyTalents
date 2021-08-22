package doggytalents.api.client.render;

import com.mojang.blaze3d.vertex.PoseStack;

import doggytalents.api.inferface.AbstractDogEntity;
import doggytalents.api.registry.TalentInstance;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.model.EntityModel;

public interface ITalentRenderer <T extends AbstractDogEntity> {

    default void render(RenderLayer<T, EntityModel<T>> layer, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, T dog, TalentInstance inst, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {

    }
}
