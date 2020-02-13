package doggytalents.client.renderer.entity.layer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import doggytalents.api.inferface.IThrowableItem;
import doggytalents.client.model.entity.ModelDog;
import doggytalents.client.renderer.entity.RenderDog;
import doggytalents.entity.EntityDog;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LayerBone extends LayerRenderer<EntityDog, ModelDog> {

    public LayerBone(RenderDog dogRendererIn) {
        super(dogRendererIn);
    }

    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, EntityDog dog, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if(dog.hasBone()) {

            matrixStackIn.push();
            RenderSystem.enableLighting();
            if(this.getEntityModel().isChild) { //isChild
                float f = 0.5F;
                matrixStackIn.translate(0.0F, 0.75F, -0.25F);
                matrixStackIn.scale(0.5F, 0.5F, 0.5F);
            }

            if(dog.isShiftKeyDown())
                matrixStackIn.translate(0.0F, 0.2F, 0.0F);
           // matrixStackIn.rotate(Vector3f.ZP.rotation((float) (Math.PI / 4)));
            this.getEntityModel().head.translateRotate(matrixStackIn);
           // matrixStackIn.rotate(new Quaternion(90.0F, 0.0F, 1.0F, 0.0F));
           /// matrixStackIn.rotate(new Quaternion(90.0F, 1.0F, 0.0F, 0.0F));
           // matrixStackIn.rotate(new Quaternion(45.0F, 0.0F, 0.0F, 1.0F));

            matrixStackIn.translate(0.00, 0.0, -0.3);
            IThrowableItem throwableItem = dog.getThrowableItem();
            Minecraft.getInstance().getItemRenderer().renderItem(throwableItem != null ? throwableItem.getRenderStack(dog.getBoneVariant()) : dog.getBoneVariant(), ItemCameraTransforms.TransformType.GROUND, packedLightIn, OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn);
            matrixStackIn.pop();
        }
    }
}