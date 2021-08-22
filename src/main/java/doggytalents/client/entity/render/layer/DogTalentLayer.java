package doggytalents.client.entity.render.layer;

import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;

import doggytalents.api.client.render.ITalentRenderer;
import doggytalents.api.registry.TalentInstance;
import doggytalents.client.entity.model.DogModel;
import doggytalents.client.entity.render.CollarRenderManager;
import doggytalents.common.entity.DogEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;

public class DogTalentLayer extends RenderLayer<DogEntity, DogModel<DogEntity>> {

    public DogTalentLayer(RenderLayerParent<DogEntity, DogModel<DogEntity>> rendererIn) {
        super(rendererIn);
    }

    @Override
    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, DogEntity dogIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        List<TalentInstance> talents = dogIn.getTalentMap();

        for (TalentInstance inst : talents) {
            if (inst.level() > 0 && inst.hasRenderer()) {
                ITalentRenderer renderer = CollarRenderManager.getRendererFor(inst.getTalent());

                if (renderer != null) {
                    renderer.render(this, matrixStackIn, bufferIn, packedLightIn, dogIn, inst, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
                }
            }
        };
    }
}