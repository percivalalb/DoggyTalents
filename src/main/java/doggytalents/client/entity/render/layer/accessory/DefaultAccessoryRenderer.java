package doggytalents.client.entity.render.layer.accessory;

import com.mojang.blaze3d.matrix.MatrixStack;

import doggytalents.api.client.render.IAccessoryRenderer;
import doggytalents.api.inferface.AbstractDogEntity;
import doggytalents.api.registry.AccessoryInstance;
import doggytalents.common.entity.DogEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.util.ResourceLocation;

public class DefaultAccessoryRenderer implements IAccessoryRenderer<DogEntity> {

    private ResourceLocation texture;

    public DefaultAccessoryRenderer(ResourceLocation textureIn) {
        this.texture = textureIn;
    }

    @Override
    public void render(LayerRenderer<DogEntity, EntityModel<DogEntity>> layer, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, DogEntity dog, AccessoryInstance data, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if(dog.isTamed() && !dog.isInvisible()) {
            LayerRenderer.renderCutoutModel(layer.getEntityModel(), this.getTexture(dog, data), matrixStackIn, bufferIn, packedLightIn, dog, 1.0f, 1.0f, 1.0f);
        }
    }

    public <T extends AbstractDogEntity> ResourceLocation getTexture(T dog, AccessoryInstance data) {
        return this.texture;
    }
}
