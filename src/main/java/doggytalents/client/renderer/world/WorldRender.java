package doggytalents.client.renderer.world;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

import doggytalents.ModTalents;
import doggytalents.entity.EntityDog;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import org.lwjgl.opengl.GL11;

/**
 * @author ProPercivalalb
 */
public class WorldRender {

    public static void onWorldRenderLast(RenderWorldLastEvent event) {
        PlayerEntity player = Minecraft.getInstance().player;
        for(Entity passenger : player.getPassengers()) {
            if(passenger instanceof EntityDog) {
                EntityDog dog = (EntityDog)passenger;
                if(dog.COORDS.hasBedPos()) {
                    int level = dog.TALENTS.getLevel(ModTalents.BED_FINDER);
                    double distance = (level * 200D) - Math.sqrt(dog.COORDS.getBedPos().distanceSq(dog.getPosition()));
                    if(level == 5 || distance >= 0.0D) {

                        MatrixStack stack = event.getMatrixStack();

                        AxisAlignedBB boundingBox = new AxisAlignedBB(dog.COORDS.getBedPos()).grow(0.5D);
                        drawSelectionBox(stack, boundingBox);
                    }
                }
            }
        }
    }

    public static void drawSelectionBox(MatrixStack stack, AxisAlignedBB boundingBox) {
        RenderSystem.disableAlphaTest();
        RenderSystem.disableLighting(); //Make the line see thought blocks
        RenderSystem.depthMask(false);
        RenderSystem.disableDepthTest(); //Make the line see thought blocks
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.color4f(1.0F, 0.0F, 0.0F, 0.7F);
        //TODO Used when drawing outline of bounding box
        RenderSystem.lineWidth(2.0F);

        RenderSystem.disableTexture();
        Vec3d vec3d = Minecraft.getInstance().gameRenderer.getActiveRenderInfo().getProjectedView();
        double d0 = vec3d.getX();
        double d1 = vec3d.getY();
        double d2 = vec3d.getZ();

        BufferBuilder buf = Tessellator.getInstance().getBuffer();
        buf.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);
        WorldRenderer.drawBoundingBox(stack, buf, boundingBox.offset(-d0, -d1, -d2), 1F, 1F, 0, 1F); // 1.14 drawSelectionBoundingBox
        Tessellator.getInstance().draw();
        RenderSystem.color4f(0.0F, 0.0F, 0.0F, 0.3F);
        RenderSystem.enableDepthTest(); //Make the line see thought blocks
        RenderSystem.depthMask(true);
        RenderSystem.enableTexture();
        RenderSystem.enableLighting(); //Make the line see thought blocks
        RenderSystem.disableBlend();
        RenderSystem.enableAlphaTest();
    }
}
