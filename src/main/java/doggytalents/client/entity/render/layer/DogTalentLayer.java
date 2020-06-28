package doggytalents.client.entity.render.layer;

import java.util.Map;

import com.mojang.blaze3d.matrix.MatrixStack;

import doggytalents.api.client.render.ITalentRenderer;
import doggytalents.api.registry.Talent;
import doggytalents.client.entity.model.DogModel;
import doggytalents.client.entity.render.CollarRenderManager;
import doggytalents.common.entity.DogEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;

public class DogTalentLayer extends LayerRenderer<DogEntity, DogModel<DogEntity>> {

    public DogTalentLayer(IEntityRenderer<DogEntity, DogModel<DogEntity>> rendererIn) {
        super(rendererIn);
    }

    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, DogEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        Map<Talent, Integer> collar = entitylivingbaseIn.getTalentMap();

        collar.entrySet().forEach((entry) -> {
            if (entry.getValue() > 0 && entry.getKey().hasRenderer()) {
                ITalentRenderer renderer = CollarRenderManager.getRendererFor(entry.getKey());

                if (renderer != null) {
                    renderer.render(this, matrixStackIn, bufferIn, packedLightIn, entitylivingbaseIn, entry.getValue(), limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
                }
            }
        });
    }
}