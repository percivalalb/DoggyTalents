package doggytalents.client.entity.render.layer.accessory;

import com.mojang.blaze3d.vertex.PoseStack;

import doggytalents.api.client.render.IAccessoryRenderer;
import doggytalents.api.registry.AccessoryInstance;
import doggytalents.common.entity.DogEntity;
import doggytalents.common.entity.accessory.DyeableAccessory.DyeableAccessoryInstance;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.resources.ResourceLocation;

public class DyeableAccessoryRenderer implements IAccessoryRenderer<DogEntity> {

    private ResourceLocation texture;

    public DyeableAccessoryRenderer(ResourceLocation textureIn) {
        this.texture = textureIn;
    }

    @Override
    public void render(RenderLayer<DogEntity, EntityModel<DogEntity>> layer, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, DogEntity dog, AccessoryInstance data, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (dog.isTame() && !dog.isInvisible()) {
            float[] color = data.cast(DyeableAccessoryInstance.class).getFloatArray();

            RenderLayer.renderColoredCutoutModel(layer.getParentModel(), this.getTexture(dog, data), matrixStackIn, bufferIn, packedLightIn, dog, color[0], color[1], color[2]);
        }
    }

    public ResourceLocation getTexture(DogEntity dog, AccessoryInstance data) {
        return this.texture;
    }
}
