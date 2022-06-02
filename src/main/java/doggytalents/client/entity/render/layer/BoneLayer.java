package doggytalents.client.entity.render.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import doggytalents.api.inferface.IThrowableItem;
import doggytalents.client.entity.model.DogModel;
import doggytalents.client.entity.render.DogRenderer;
import doggytalents.common.entity.Dog;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.layers.RenderLayer;

public class BoneLayer extends RenderLayer<Dog, DogModel<Dog>> {

    public BoneLayer(DogRenderer dogRendererIn) {
        super(dogRendererIn);
    }

    @Override
    public void render(PoseStack matrixStack, MultiBufferSource bufferSource, int packedLight, Dog dog, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (dog.hasBone()) {

            matrixStack.pushPose();
            DogModel<Dog> model = this.getParentModel();
            if (model.young) {
                // derived from AgeableModel head offset
                matrixStack.translate(0.0F, 5.0F / 16.0F, 2.0F / 16.0F);
            }

            model.head.translateAndRotate(matrixStack);

            matrixStack.translate(-0.025F, 0.125F, -0.32F);
            matrixStack.mulPose(Vector3f.YP.rotationDegrees(45.0F));
            matrixStack.mulPose(Vector3f.XP.rotationDegrees(90.0F));

            IThrowableItem throwableItem = dog.getThrowableItem();
            Minecraft.getInstance().getItemInHandRenderer().renderItem(dog, throwableItem != null ? throwableItem.getRenderStack(dog.getBoneVariant()) : dog.getBoneVariant(), ItemTransforms.TransformType.GROUND, false, matrixStack, bufferSource, packedLight);
            matrixStack.popPose();
        }
    }
}
