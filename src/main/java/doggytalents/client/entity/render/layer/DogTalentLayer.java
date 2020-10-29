package doggytalents.client.entity.render.layer;

import java.util.List;

import com.mojang.blaze3d.matrix.MatrixStack;

import doggytalents.api.client.render.ITalentRenderer;
import doggytalents.api.registry.TalentInstance;
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
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, DogEntity dogIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
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