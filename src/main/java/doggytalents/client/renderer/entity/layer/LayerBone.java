package doggytalents.client.renderer.entity.layer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import doggytalents.api.inferface.IThrowableItem;
import doggytalents.client.model.entity.ModelDog;
import doggytalents.client.renderer.entity.RenderDog;
import doggytalents.entity.EntityDog;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
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
    public void render(MatrixStack p_225628_1_, IRenderTypeBuffer p_225628_2_, int p_225628_3_, EntityDog dog, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if(dog.hasBone()) {

            RenderSystem.pushMatrix();
            RenderSystem.enableLighting();
            if(this.getEntityModel().isChild) { //isChild
                float f = 0.5F;
                RenderSystem.translatef(0.0F, 0.75F, -0.25F);
                RenderSystem.scalef(0.5F, 0.5F, 0.5F);
            }

            if(dog.isShiftKeyDown())
                RenderSystem.translatef(0.0F, 0.2F, 0.0F);

            //this.getEntityModel().head.postRender(0.0625F);
            RenderSystem.rotatef(90.0F, 0.0F, 1.0F, 0.0F);
            RenderSystem.rotatef(90.0F, 1.0F, 0.0F, 0.0F);
            RenderSystem.rotatef(45.0F, 0.0F, 0.0F, 1.0F);

            RenderSystem.translated(0.20, -0.10, -0.10);
            IThrowableItem throwableItem = dog.getThrowableItem();
            Minecraft.getInstance().getItemRenderer().renderItem(throwableItem != null ? throwableItem.getRenderStack(dog.getBoneVariant()) : dog.getBoneVariant(), ItemCameraTransforms.TransformType.NONE, 15728880, OverlayTexture.DEFAULT_LIGHT, p_225628_1_, p_225628_2_);
            RenderSystem.popMatrix();
        }
    }
}