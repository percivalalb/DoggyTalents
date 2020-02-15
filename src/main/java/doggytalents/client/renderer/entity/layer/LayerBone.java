package doggytalents.client.renderer.entity.layer;

import com.mojang.blaze3d.matrix.MatrixStack;

import doggytalents.api.inferface.IThrowableItem;
import doggytalents.client.model.entity.ModelDog;
import doggytalents.client.renderer.entity.RenderDog;
import doggytalents.entity.EntityDog;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LayerBone extends LayerRenderer<EntityDog, ModelDog> {

    public LayerBone(RenderDog dogRendererIn) {
        super(dogRendererIn);
    }

    @Override
    public void render(MatrixStack matrixStack, IRenderTypeBuffer bufferSource, int packedLight, EntityDog dog, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if(dog.hasBone()) {

            matrixStack.push();
            ModelDog model = this.getEntityModel();
            if(model.isChild) {
                // derived from AgeableModel head offset
                matrixStack.translate(0.0F, 5.0F / 16.0F, 2.0F / 16.0F);
            }

            ModelRenderer head = model.head;
            matrixStack.translate(head.rotationPointX / 16.0F, head.rotationPointY / 16.0F, head.rotationPointZ / 16.0F);
            matrixStack.rotate(Vector3f.ZP.rotation(head.rotateAngleZ));
            matrixStack.rotate(Vector3f.YP.rotation(head.rotateAngleY));
            matrixStack.rotate(Vector3f.XP.rotation(head.rotateAngleX));

            matrixStack.translate(-0.025F, 0.125F, -0.32F);
            matrixStack.rotate(Vector3f.YP.rotationDegrees(45.0F));
            matrixStack.rotate(Vector3f.XP.rotationDegrees(90.0F));

            IThrowableItem throwableItem = dog.getThrowableItem();
            Minecraft.getInstance().getFirstPersonRenderer().renderItemSide(dog, throwableItem != null ? throwableItem.getRenderStack(dog.getBoneVariant()) : dog.getBoneVariant(), ItemCameraTransforms.TransformType.GROUND, false, matrixStack, bufferSource, packedLight);
            matrixStack.pop();
        }
    }
}