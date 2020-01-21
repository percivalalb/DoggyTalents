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

    public static <T extends Entity> void renderLabelWithScale(T entity, EntityRenderer<T> renderer, String text, MatrixStack stack, IRenderTypeBuffer buffer, int p_225629_5_, float scale, int yChange) {
       boolean flag = !entity.func_226273_bm_();
       float f = entity.getHeight() + 0.5F;
       stack.func_227860_a_();
       stack.func_227861_a_(0.0D, f, 0.0D);
       stack.func_227863_a_(renderer.getRenderManager().func_229098_b_());
       stack.func_227862_a_(-scale, -scale, scale);
       Matrix4f matrix4f = stack.func_227866_c_().func_227870_a_();
       float f1 = Minecraft.getInstance().gameSettings.func_216840_a(0.25F);
       int j = (int)(f1 * 255.0F) << 24;
       FontRenderer fontrenderer = renderer.getFontRendererFromRenderManager();
       float f2 = -fontrenderer.getStringWidth(text) / 2;
       fontrenderer.func_228079_a_(text, f2, yChange, 553648127, false, matrix4f, buffer, flag, j, p_225629_5_);
       if (flag) {
          fontrenderer.func_228079_a_(text, f2, yChange, -1, false, matrix4f, buffer, false, 0, p_225629_5_);
       }

       stack.func_227865_b_();
     }
}
