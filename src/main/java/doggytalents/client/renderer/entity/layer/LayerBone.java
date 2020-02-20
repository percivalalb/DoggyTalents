package doggytalents.client.renderer.entity.layer;

import com.mojang.blaze3d.platform.GlStateManager;

import doggytalents.api.inferface.IThrowableItem;
import doggytalents.client.model.entity.ModelDog;
import doggytalents.client.renderer.entity.RenderDog;
import doggytalents.entity.EntityDog;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LayerBone extends LayerRenderer<EntityDog, ModelDog> {

    public LayerBone(RenderDog dogRendererIn) {
        super(dogRendererIn);
    }

    @Override
    public void render(EntityDog dog, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if(dog.hasBone()) {

            GlStateManager.pushMatrix();

            if(this.getEntityModel().isChild) {
                // derived from AgeableModel head offset
                GlStateManager.translatef(0.0F, 5.0F / 16.0F, 2.0F / 16.0F);
            }

            this.getEntityModel().head.postRender(0.0625F);

            GlStateManager.translated(-0.025F, 0.125F, -0.32F);
            GlStateManager.rotatef(45.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotatef(90.0F, 1.0F, 0.0F, 0.0F);

            IThrowableItem throwableItem = dog.getThrowableItem();
            Minecraft.getInstance().getFirstPersonRenderer().renderItemSide(dog, throwableItem != null ? throwableItem.getRenderStack(dog.getBoneVariant()) : dog.getBoneVariant(), ItemCameraTransforms.TransformType.GROUND, false);
            GlStateManager.popMatrix();
        }
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}