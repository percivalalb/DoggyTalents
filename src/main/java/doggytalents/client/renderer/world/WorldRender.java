package doggytalents.client.renderer.world;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import doggytalents.ModTalents;
import doggytalents.entity.EntityDog;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.client.event.RenderWorldLastEvent;

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

                        RenderSystem.pushMatrix();

                        AxisAlignedBB boundingBox = new AxisAlignedBB(dog.COORDS.getBedPos()).grow(0.5D);
                        IRenderTypeBuffer.Impl irendertypebuffer$impl = Minecraft.getInstance().func_228019_au_().func_228487_b_();
                        drawSelectionBox(event.getMatrixStack(), irendertypebuffer$impl.getBuffer(RenderType.lines()), player, event.getPartialTicks(), boundingBox);
                        RenderSystem.popMatrix();
                    }
                }
            }
        }
    }

    public static void drawSelectionBox(MatrixStack p_228430_0_, IVertexBuilder p_228430_1_, PlayerEntity player, float particleTicks, AxisAlignedBB boundingBox) {
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
        double d0 = player.lastTickPosX + (player.getPosX() - player.lastTickPosX) * particleTicks;
        double d1 = player.lastTickPosY + (player.getPosY() - player.lastTickPosY) * particleTicks;
        double d2 = player.lastTickPosZ + (player.getPosZ() - player.lastTickPosZ) * particleTicks;

        WorldRenderer.func_228430_a_(p_228430_0_, p_228430_1_, boundingBox.offset(-d0, -d1, -d2), 1F, 1F, 0, 1F); // 1.14 drawSelectionBoundingBox
        RenderSystem.color4f(0.0F, 0.0F, 0.0F, 0.3F);
        RenderSystem.enableDepthTest(); //Make the line see thought blocks
        RenderSystem.depthMask(true);
        RenderSystem.enableTexture();
        RenderSystem.enableLighting(); //Make the line see thought blocks
        RenderSystem.disableBlend();
        RenderSystem.enableAlphaTest();
    }
}
