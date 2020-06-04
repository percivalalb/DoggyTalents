package doggytalents.client.entity.render;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Matrix4f;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.text.ITextComponent;

public class RenderUtil {

    public static <T extends Entity> void renderLabelWithScale(T entity, EntityRenderer<T> renderer, ITextComponent text, MatrixStack stack, IRenderTypeBuffer buffer, int packedLightIn, float scale, float yChange) {
        RenderUtil.renderLabelWithScale(entity, renderer, text.getFormattedText(), stack, buffer, packedLightIn, scale, yChange);
    }

    public static <T extends Entity> void renderLabelWithScale(T entity, EntityRenderer<T> renderer, String text, MatrixStack stack, IRenderTypeBuffer buffer, int packedLightIn, float scale, float yChange) {
        renderLabelWithScale(!entity.isDiscrete(), renderer.getRenderManager(), text, stack, buffer, packedLightIn, scale, yChange + entity.getHeight() + 0.5F);
    }

    public static void renderLabelWithScale(boolean flag, EntityRendererManager renderManager, ITextComponent text, MatrixStack stack, IRenderTypeBuffer buffer, int packedLightIn, float scale, float yOffset) {
        renderLabelWithScale(flag, renderManager, text.getFormattedText(), stack, buffer, packedLightIn, scale, yOffset);
    }

    public static void renderLabelWithScale(boolean flag, EntityRendererManager renderManager, String text, MatrixStack stack, IRenderTypeBuffer buffer, int packedLightIn, float scale, float yOffset) {
        stack.push();
        stack.translate(0.0D, yOffset, 0.0D);
        stack.rotate(renderManager.getCameraOrientation());
        stack.scale(-scale, -scale, scale);
        Matrix4f matrix4f = stack.getLast().getMatrix();
        float f1 = Minecraft.getInstance().gameSettings.getTextBackgroundOpacity(0.25F);
        int j = (int)(f1 * 255.0F) << 24;
        FontRenderer fontrenderer = renderManager.getFontRenderer();
        float f2 = -fontrenderer.getStringWidth(text) / 2F;
        fontrenderer.renderString(text, f2, 0, 553648127, false, matrix4f, buffer, flag, j, packedLightIn);
        if (flag) {
            fontrenderer.renderString(text, f2, 0, -1, false, matrix4f, buffer, false, 0, packedLightIn);
        }

        stack.pop();
    }
}
