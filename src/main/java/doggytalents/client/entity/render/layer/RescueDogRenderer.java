package doggytalents.client.entity.render.layer;

import com.mojang.blaze3d.matrix.MatrixStack;

import doggytalents.api.client.render.ITalentRenderer;
import doggytalents.client.entity.model.DogRescueModel;
import doggytalents.common.entity.DogEntity;
import doggytalents.common.lib.Resources;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;

public class RescueDogRenderer implements ITalentRenderer<DogEntity> {

    private final EntityModel<DogEntity> model;

    public RescueDogRenderer() {
        this.model = new DogRescueModel();
    }

    @Override
    public void render(LayerRenderer<DogEntity, EntityModel<DogEntity>> layer, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, DogEntity dog, int level, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {

        if(!dog.isInvisible() && level >= 5) {
            layer.getEntityModel().copyModelAttributesTo(this.model);
            this.model.setLivingAnimations(dog, limbSwing, limbSwingAmount, partialTicks);
            this.model.setRotationAngles(dog, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

            LayerRenderer.renderCutoutModel(this.model, Resources.TALENT_RESCUE, matrixStackIn, bufferIn, packedLightIn, dog, 1.0F, 1.0F, 1.0F);
        }
    }
}
