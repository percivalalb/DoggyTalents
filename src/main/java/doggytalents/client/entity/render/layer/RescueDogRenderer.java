package doggytalents.client.entity.render.layer;

import com.mojang.blaze3d.vertex.PoseStack;

import doggytalents.api.client.render.ITalentRenderer;
import doggytalents.api.registry.TalentInstance;
import doggytalents.client.entity.model.DogRescueModel;
import doggytalents.common.entity.DogEntity;
import doggytalents.common.lib.Resources;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.model.EntityModel;

public class RescueDogRenderer implements ITalentRenderer<DogEntity> {

    private final EntityModel<DogEntity> model;

    public RescueDogRenderer() {
        this.model = new DogRescueModel();
    }

    @Override
    public void render(RenderLayer<DogEntity, EntityModel<DogEntity>> layer, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, DogEntity dogIn, TalentInstance inst, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {

        if (!dogIn.isInvisible() && inst.level() >= 5) {
            layer.getParentModel().copyPropertiesTo(this.model);
            this.model.prepareMobModel(dogIn, limbSwing, limbSwingAmount, partialTicks);
            this.model.setupAnim(dogIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

            RenderLayer.renderColoredCutoutModel(this.model, Resources.TALENT_RESCUE, matrixStackIn, bufferIn, packedLightIn, dogIn, 1.0F, 1.0F, 1.0F);
        }
    }
}
