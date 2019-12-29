package doggytalents.client.renderer;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderUtil {

    public static void renderLabelWithScale(FontRenderer fontRendererIn, String str, float x, float y, float z, int verticalShift, float viewerYaw, float viewerPitch, boolean isThirdPersonFrontal, boolean isSneaking, float scale) {
        RenderSystem.pushMatrix();
        RenderSystem.translatef(x, y, z);
        RenderSystem.normal3f(0.0F, 1.0F, 0.0F);
        RenderSystem.rotatef(-viewerYaw, 0.0F, 1.0F, 0.0F);
        RenderSystem.rotatef((isThirdPersonFrontal ? -1 : 1) * viewerPitch, 1.0F, 0.0F, 0.0F);
        RenderSystem.scalef(-scale, -scale, scale);
        RenderSystem.disableLighting();
        RenderSystem.depthMask(false);

        if (!isSneaking)
            RenderSystem.disableDepthTest();

        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        int i = fontRendererIn.getStringWidth(str) / 2;
        RenderSystem.disableTexture();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferBuilder.func_225582_a_(-i - 1, -1 + verticalShift, 0.0D).func_227885_a_(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        bufferBuilder.func_225582_a_(-i - 1, 8 + verticalShift, 0.0D).func_227885_a_(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        bufferBuilder.func_225582_a_(i + 1, 8 + verticalShift, 0.0D).func_227885_a_(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        bufferBuilder.func_225582_a_(i + 1, -1 + verticalShift, 0.0D).func_227885_a_(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        tessellator.draw();
        RenderSystem.enableTexture();

        if (!isSneaking) {
            fontRendererIn.drawString(str, -fontRendererIn.getStringWidth(str) / 2, verticalShift, 553648127);
            RenderSystem.enableDepthTest();
        }

        RenderSystem.depthMask(true);
        fontRendererIn.drawString(str, -fontRendererIn.getStringWidth(str) / 2, verticalShift, isSneaking ? 553648127 : -1);
        RenderSystem.enableLighting();
        RenderSystem.disableBlend();
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.popMatrix();
    }
}
