package doggytalents.client.entity.render.layer;

import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;

import doggytalents.api.client.render.IAccessoryRenderer;
import doggytalents.api.registry.AccessoryInstance;
import doggytalents.client.entity.model.DogModel;
import doggytalents.client.entity.render.CollarRenderManager;
import doggytalents.common.entity.DogEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;

public class DogAccessoryLayer extends RenderLayer<DogEntity, DogModel<DogEntity>> {

    public DogAccessoryLayer(RenderLayerParent<DogEntity, DogModel<DogEntity>> rendererIn) {
        super(rendererIn);
    }

    @Override
    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, DogEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        List<AccessoryInstance> accessories = entitylivingbaseIn.getAccessories();

        for (AccessoryInstance inst : accessories) {
            IAccessoryRenderer renderer = CollarRenderManager.getRendererFor(inst.getAccessory());

            if (renderer != null) {
                renderer.render(this, matrixStackIn, bufferIn, packedLightIn, entitylivingbaseIn, inst, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
            }
        };
    }
}