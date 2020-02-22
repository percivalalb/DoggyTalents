package doggytalents.client.renderer.entity.layer;

import doggytalents.api.inferface.IThrowableItem;
import doggytalents.client.model.entity.ModelDog;
import doggytalents.client.renderer.entity.RenderDog;
import doggytalents.entity.EntityDog;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LayerBone implements LayerRenderer<EntityDog> {

    protected final RenderDog dogRenderer;
    public LayerBone(RenderDog dogRendererIn) {
        this.dogRenderer = dogRendererIn;
    }

    @Override
    public void doRenderLayer(EntityDog dog, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if(dog.hasBone()) {

            GlStateManager.pushMatrix();

            if (this.dogRenderer.getMainModel().isChild) {
                // derived from AgeableModel head offset
                GlStateManager.translate(0.0F, 5.0F / 16.0F, 2.0F / 16.0F);
            }

            ((ModelDog)this.dogRenderer.getMainModel()).wolfHeadMain.postRender(0.0625F);

            GlStateManager.translate(-0.025F, 0.125F, -0.32F);
            GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);

            IThrowableItem throwableItem = dog.getThrowableItem();
            Minecraft.getMinecraft().getItemRenderer().renderItemSide(dog, throwableItem != null ? throwableItem.getRenderStack(dog.getBoneVariant()) : dog.getBoneVariant(), ItemCameraTransforms.TransformType.GROUND, false);
            GlStateManager.popMatrix();
        }
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}