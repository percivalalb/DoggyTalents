package doggytalents.client.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Matrix4f;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderUtil {

    public static <T extends Entity> void renderLabelWithScale(T entity, EntityRenderer<T> renderer, String text, MatrixStack stack, IRenderTypeBuffer buffer, int packedLightIn, float scale, int yChange) {
       boolean flag = !entity.isDiscrete();
       float f = entity.getHeight() + 0.5F;
       stack.push();
       stack.translate(0.0D, f, 0.0D);
       stack.rotate(renderer.getRenderManager().getCameraOrientation());
       stack.scale(-scale, -scale, scale);
       Matrix4f matrix4f = stack.getLast().getMatrix();
       float f1 = Minecraft.getInstance().gameSettings.getTextBackgroundOpacity(0.25F);
       int j = (int)(f1 * 255.0F) << 24;
       FontRenderer fontrenderer = renderer.getFontRendererFromRenderManager();
       float f2 = -fontrenderer.getStringWidth(text) / 2;
       fontrenderer.renderString(text, f2, yChange, 553648127, false, matrix4f, buffer, flag, j, packedLightIn);
       if (flag) {
          fontrenderer.renderString(text, f2, yChange, -1, false, matrix4f, buffer, false, 0, packedLightIn);
       }

       stack.pop();
     }
}
