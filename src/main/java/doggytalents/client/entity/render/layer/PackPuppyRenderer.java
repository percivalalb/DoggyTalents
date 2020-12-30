package doggytalents.client.entity.render.layer;

import com.mojang.blaze3d.matrix.MatrixStack;

import doggytalents.api.client.render.ITalentRenderer;
import doggytalents.api.registry.TalentInstance;
import doggytalents.client.entity.model.DogBackpackModel;
import doggytalents.common.entity.DogEntity;
import doggytalents.common.lib.Resources;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;

public class PackPuppyRenderer implements ITalentRenderer<DogEntity> {

    private final EntityModel<DogEntity> model;

    public PackPuppyRenderer() {
        this.model = new DogBackpackModel(0.0F);
    }

    @Override
    public void render(LayerRenderer<DogEntity, EntityModel<DogEntity>> layer, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, DogEntity dogIn, TalentInstance inst, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!dogIn.isInvisible()) {
            layer.getEntityModel().copyModelAttributesTo(this.model);
            this.model.setLivingAnimations(dogIn, limbSwing, limbSwingAmount, partialTicks);
            this.model.setRotationAngles(dogIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

            LayerRenderer.renderCutoutModel(this.model, Resources.TALENT_CHEST, matrixStackIn, bufferIn, packedLightIn, dogIn, 1.0f, 1.0f, 1.0f);
        }
    }
}
