package doggytalents.client.entity.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import org.joml.Matrix4f;

public class RenderUtil {

    public static <T extends Entity> void renderLabelWithScale(T entity, EntityRenderer<T> renderer, EntityRenderDispatcher entityRenderDispatcher, Component text, PoseStack stack, MultiBufferSource buffer, int packedLightIn, float scale, float yChange) {
        RenderUtil.renderLabelWithScale(entity, renderer, entityRenderDispatcher, text.getString(), stack, buffer, packedLightIn, scale, yChange);
    }

    public static <T extends Entity> void renderLabelWithScale(T entity, EntityRenderer<T> renderer, EntityRenderDispatcher entityRenderDispatcher, String text, PoseStack stack, MultiBufferSource buffer, int packedLightIn, float scale, float yChange) {
        renderLabelWithScale(!entity.isDiscrete(), renderer, entityRenderDispatcher, text, stack, buffer, packedLightIn, scale, yChange + entity.getBbHeight() + 0.5F);
    }

    public static void renderLabelWithScale(boolean flag, EntityRenderer renderer, EntityRenderDispatcher entityRenderDispatcher, Component text, PoseStack stack, MultiBufferSource buffer, int packedLightIn, float scale, float yOffset) {
        renderLabelWithScale(flag, renderer, entityRenderDispatcher, text.getString(), stack, buffer, packedLightIn, scale, yOffset);
    }

    public static void renderLabelWithScale(boolean flag, EntityRenderer renderer, EntityRenderDispatcher entityRenderDispatcher, String text, PoseStack stack, MultiBufferSource buffer, int packedLightIn, float scale, float yOffset) {
        stack.pushPose();
        stack.translate(0.0D, yOffset, 0.0D);
        stack.mulPose(entityRenderDispatcher.cameraOrientation());
        stack.scale(-scale, -scale, scale);
        Matrix4f matrix4f = stack.last().pose();
        float f1 = Minecraft.getInstance().options.getBackgroundOpacity(0.25F);
        int j = (int) (f1 * 255.0F) << 24;
        Font fontrenderer = renderer.getFont();
        float f2 = -fontrenderer.width(text) / 2F;
        fontrenderer.drawInBatch(text, f2, 0, 553648127, false, matrix4f, buffer, flag, j, packedLightIn);
        if (flag) {
            fontrenderer.drawInBatch(text, f2, 0, -1, false, matrix4f, buffer, false, 0, packedLightIn);
        }

        stack.popPose();
    }

    // From net.minecraft.client.gui.AbstractGui
    public static void blit(int x, int y, int zLevel, int width, int height, TextureAtlasSprite sprite) {
        innerBlit(x, x + width, y, y + height, zLevel, sprite.getU0(), sprite.getU1(), sprite.getV0(), sprite.getV1());
    }

    public static void blit(int x, int y, int width, int height, int textureX, int textureY) {
        blit(x, y, 0, width, height, textureX, textureY, 256, 256);
    }

    public static void blit(int p_blit_0_, int p_blit_1_, int p_blit_2_, float p_blit_3_, float p_blit_4_, int p_blit_5_, int p_blit_6_, int p_blit_7_, int p_blit_8_) {
        innerBlit(p_blit_0_, p_blit_0_ + p_blit_5_, p_blit_1_, p_blit_1_ + p_blit_6_, p_blit_2_, p_blit_5_, p_blit_6_, p_blit_3_, p_blit_4_, p_blit_8_, p_blit_7_);
    }

    public static void blit(int p_blit_0_, int p_blit_1_, int p_blit_2_, int p_blit_3_, float p_blit_4_, float p_blit_5_, int p_blit_6_, int p_blit_7_, int p_blit_8_, int p_blit_9_) {
        innerBlit(p_blit_0_, p_blit_0_ + p_blit_2_, p_blit_1_, p_blit_1_ + p_blit_3_, 0, p_blit_6_, p_blit_7_, p_blit_4_, p_blit_5_, p_blit_8_, p_blit_9_);
    }

    public static void blit(int p_blit_0_, int p_blit_1_, float p_blit_2_, float p_blit_3_, int p_blit_4_, int p_blit_5_, int p_blit_6_, int p_blit_7_) {
        blit(p_blit_0_, p_blit_1_, p_blit_4_, p_blit_5_, p_blit_2_, p_blit_3_, p_blit_4_, p_blit_5_, p_blit_6_, p_blit_7_);
    }

    public static void innerBlit(int minX, int maxX, int yMin, int yMax, int zLevel, int textureXMin, int textureXMax, float textureYMin, float textureYMax, int textureXScale, int textureYScale) {
        innerBlit(minX, maxX, yMin, yMax, zLevel, textureYMin / textureXScale, (textureYMin + textureXMin) / textureXScale, textureYMax / textureYScale, (textureYMax + textureXMax) / textureYScale);
    }

    public static void innerBlit(int minX, int maxX, int yMin, int yMax, int zLevel, float textureXMin, float textureXMax, float textureYMin, float textureYMax) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferbuilder.vertex(minX, yMax, zLevel).uv(textureXMin, textureYMax).endVertex();
        bufferbuilder.vertex(maxX, yMax, zLevel).uv(textureXMax, textureYMax).endVertex();
        bufferbuilder.vertex(maxX, yMin, zLevel).uv(textureXMax, textureYMin).endVertex();
        bufferbuilder.vertex(minX, yMin, zLevel).uv(textureXMin, textureYMin).endVertex();
        BufferUploader.drawWithShader(bufferbuilder.end());
    }
}
