package doggytalents.client.entity.render.layer.accessory;

import com.mojang.blaze3d.vertex.PoseStack;
import doggytalents.api.inferface.IColoredObject;
import doggytalents.api.registry.AccessoryInstance;
import doggytalents.client.entity.model.DogModel;
import doggytalents.common.entity.Dog;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;

public class DefaultAccessoryRenderer extends RenderLayer<Dog, DogModel<Dog>> {

    public DefaultAccessoryRenderer(RenderLayerParent parentRenderer, EntityRendererProvider.Context ctx) {
        super(parentRenderer);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, Dog dog, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        // Only show armour if dog is tamed or visible
        if (!dog.isTame() || dog.isInvisible()) {
            return;
        }

        for (AccessoryInstance accessoryInst : dog.getAccessories()) {
            if (accessoryInst.usesRenderer(this.getClass())) {
                if (accessoryInst instanceof IColoredObject coloredObject) {
                    float[] color = coloredObject.getColor();
                    this.renderColoredCutoutModel(this.getParentModel(), accessoryInst.getModelTexture(dog), poseStack, buffer, packedLight, dog, color[0], color[1], color[2]);
                } else {
                    RenderLayer.renderColoredCutoutModel(this.getParentModel(), accessoryInst.getModelTexture(dog), poseStack, buffer, packedLight, dog, 1.0F, 1.0F, 1.0F);
                }
            }
        }
    }
}
