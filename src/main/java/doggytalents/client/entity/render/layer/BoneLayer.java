package doggytalents.client.entity.render.layer;

import com.mojang.blaze3d.matrix.MatrixStack;

import doggytalents.api.inferface.IThrowableItem;
import doggytalents.client.entity.model.DogModel;
import doggytalents.client.entity.render.DogRenderer;
import doggytalents.common.entity.DogEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.util.math.vector.Vector3f;

public class BoneLayer extends LayerRenderer<DogEntity, DogModel<DogEntity>> {

    public BoneLayer(DogRenderer dogRendererIn) {
        super(dogRendererIn);
    }

    @Override
    public void render(MatrixStack matrixStack, IRenderTypeBuffer bufferSource, int packedLight, DogEntity dog, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (dog.hasBone()) {

            matrixStack.pushPose();
            DogModel<DogEntity> model = this.getParentModel();
            if (model.young) {
                // derived from AgeableModel head offset
                matrixStack.translate(0.0F, 5.0F / 16.0F, 2.0F / 16.0F);
            }

            model.head.translateAndRotate(matrixStack);

            matrixStack.translate(-0.025F, 0.125F, -0.32F);
            matrixStack.mulPose(Vector3f.YP.rotationDegrees(45.0F));
            matrixStack.mulPose(Vector3f.XP.rotationDegrees(90.0F));

            IThrowableItem throwableItem = dog.getThrowableItem();
            Minecraft.getInstance().getItemInHandRenderer().renderItem(dog, throwableItem != null ? throwableItem.getRenderStack(dog.getBoneVariant()) : dog.getBoneVariant(), ItemCameraTransforms.TransformType.GROUND, false, matrixStack, bufferSource, packedLight);
            matrixStack.popPose();
        }
    }
}