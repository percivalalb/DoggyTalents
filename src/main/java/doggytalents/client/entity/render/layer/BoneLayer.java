package doggytalents.client.entity.render.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import doggytalents.api.inferface.IThrowableItem;
import doggytalents.client.entity.model.DogModel;
import doggytalents.client.entity.render.DogRenderer;
import doggytalents.common.entity.DogEntity;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.layers.RenderLayer;

public class BoneLayer extends RenderLayer<DogEntity, DogModel<DogEntity>> {

    private ItemInHandRenderer itemInHandRenderer;
    public BoneLayer(DogRenderer dogRendererIn, ItemInHandRenderer itemInHandRendererIn) {
        super(dogRendererIn);
        this.itemInHandRenderer = itemInHandRendererIn;
    }

    @Override
    public void render(PoseStack matrixStack, MultiBufferSource bufferSource, int packedLight, DogEntity dog, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (dog.hasBone()) {

            matrixStack.pushPose();
            DogModel<DogEntity> model = this.getParentModel();
            if (model.young) {
                // derived from AgeableModel head offset
                matrixStack.translate(0.0F, 5.0F / 16.0F, 2.0F / 16.0F);
            }

            model.head.translateAndRotate(matrixStack);

            matrixStack.translate(-0.025F, 0.125F, -0.32F);
            matrixStack.mulPose(Axis.YP.rotationDegrees(45.0F));
            matrixStack.mulPose(Axis.XP.rotationDegrees(90.0F));

            IThrowableItem throwableItem = dog.getThrowableItem();
            this.itemInHandRenderer.renderItem(dog, throwableItem != null ? throwableItem.getRenderStack(dog.getBoneVariant()) : dog.getBoneVariant(), ItemTransforms.TransformType.GROUND, false, matrixStack, bufferSource, packedLight);
            matrixStack.popPose();
        }
    }
}
