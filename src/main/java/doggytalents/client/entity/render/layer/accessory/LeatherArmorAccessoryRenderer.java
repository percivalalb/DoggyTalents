package doggytalents.client.entity.render.layer.accessory;

import com.mojang.blaze3d.vertex.PoseStack;

import doggytalents.api.client.render.IAccessoryRenderer;
import doggytalents.api.registry.AccessoryInstance;
import doggytalents.client.entity.model.DogModel;
import doggytalents.common.entity.DogEntity;
import doggytalents.common.entity.accessory.DyeableAccessory.DyeableAccessoryInstance;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.resources.ResourceLocation;

public class LeatherArmorAccessoryRenderer implements IAccessoryRenderer<DogEntity> {

    private final EntityModel<DogEntity> model;
    private ResourceLocation texture;

    public LeatherArmorAccessoryRenderer(ResourceLocation textureIn) {
        this.model = new DogModel<>(0.4F);
        this.texture = textureIn;
    }

    @Override
    public void render(RenderLayer<DogEntity, EntityModel<DogEntity>> layer, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, DogEntity dog, AccessoryInstance data, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (dog.isTame() && !dog.isInvisible()) {
            float[] color = data.cast(DyeableAccessoryInstance.class).getFloatArray();

            layer.getParentModel().copyPropertiesTo(this.model);
            this.model.prepareMobModel(dog, limbSwing, limbSwingAmount, partialTicks);
            this.model.setupAnim(dog, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

            RenderLayer.renderColoredCutoutModel(this.model, this.getTexture(dog, data), matrixStackIn, bufferIn, packedLightIn, dog, color[0], color[1], color[2]);
        }
    }

    public ResourceLocation getTexture(DogEntity dog, AccessoryInstance data) {
        return this.texture;
    }
}
