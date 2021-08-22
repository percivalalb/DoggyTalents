package doggytalents.client.entity.render.layer;

import com.mojang.blaze3d.vertex.PoseStack;

import doggytalents.api.client.render.ITalentRenderer;
import doggytalents.api.registry.TalentInstance;
import doggytalents.client.entity.model.DogBackpackModel;
import doggytalents.common.entity.DogEntity;
import doggytalents.common.lib.Resources;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.model.EntityModel;

public class PackPuppyRenderer implements ITalentRenderer<DogEntity> {

    private final EntityModel<DogEntity> model;

    public PackPuppyRenderer() {
        this.model = new DogBackpackModel(0.0F);
    }

    @Override
    public void render(RenderLayer<DogEntity, EntityModel<DogEntity>> layer, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, DogEntity dogIn, TalentInstance inst, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!dogIn.isInvisible()) {
            layer.getParentModel().copyPropertiesTo(this.model);
            this.model.prepareMobModel(dogIn, limbSwing, limbSwingAmount, partialTicks);
            this.model.setupAnim(dogIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

            RenderLayer.renderColoredCutoutModel(this.model, Resources.TALENT_CHEST, matrixStackIn, bufferIn, packedLightIn, dogIn, 1.0f, 1.0f, 1.0f);
        }
    }
}
