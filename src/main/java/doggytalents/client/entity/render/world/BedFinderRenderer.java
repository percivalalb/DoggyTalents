package doggytalents.client.entity.render.world;

import java.util.Optional;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

import doggytalents.DoggyTalents;
import doggytalents.common.entity.DogEntity;
import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.Tesselator;
import net.minecraft.client.renderer.LevelRenderer;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderWorldLastEvent;

public class BedFinderRenderer {

    public static void onWorldRenderLast(RenderWorldLastEvent event) {
        Player player = Minecraft.getInstance().player;
        for (Entity passenger : player.getPassengers()) {
            if (passenger instanceof DogEntity) {
                DogEntity dog = (DogEntity) passenger;
                Optional<BlockPos> bedPosOpt = dog.getBedPos();

                if (bedPosOpt.isPresent()) {
                    BlockPos bedPos = bedPosOpt.get();
                    int level = dog.getLevel(DoggyTalents.BED_FINDER);
                    double distance = (level * 200D) - Math.sqrt(bedPos.distSqr(dog.blockPosition()));
                    if (level == 5 || distance >= 0.0D) {
                        PoseStack stack = event.getMatrixStack();

                        AABB boundingBox = new AABB(bedPos).inflate(0.5D);
                        drawSelectionBox(stack, boundingBox);
                    }
                }
            }
        }
    }

    public static void drawSelectionBox(PoseStack stack, AABB boundingBox) {
        RenderSystem.disableAlphaTest();
        RenderSystem.disableLighting(); //Make the line see thought blocks
        RenderSystem.depthMask(false);
        RenderSystem.disableDepthTest(); //Make the line see thought blocks
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        //TODO Used when drawing outline of bounding box
        RenderSystem.lineWidth(2.0F);

        RenderSystem.disableTexture();
        Vec3 vec3d = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
        double d0 = vec3d.x();
        double d1 = vec3d.y();
        double d2 = vec3d.z();

        BufferBuilder buf = Tesselator.getInstance().getBuilder();
        buf.begin(GL11.GL_LINES, DefaultVertexFormat.POSITION_COLOR);
        LevelRenderer.renderLineBox(stack, buf, boundingBox.move(-d0, -d1, -d2), 1F, 1F, 0, 1F);
        Tesselator.getInstance().end();
        RenderSystem.color4f(0.0F, 0.0F, 0.0F, 0.3F);
        RenderSystem.enableDepthTest(); //Make the line see thought blocks
        RenderSystem.depthMask(true);
        RenderSystem.enableTexture();
        RenderSystem.enableLighting(); //Make the line see thought blocks
        RenderSystem.disableBlend();
        RenderSystem.enableAlphaTest();
    }
}
