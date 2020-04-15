package doggytalents.client.renderer.world;

import doggytalents.DoggyTalents;
import doggytalents.ModTalents;
import doggytalents.api.lib.Reference;
import doggytalents.entity.EntityDog;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ProPercivalalb
 */
@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(modid = Reference.MOD_ID, value = Side.CLIENT)
public class WorldRender {

    @SubscribeEvent
    public static void onWorldRenderLast(RenderWorldLastEvent event) {
        EntityPlayer player = DoggyTalents.PROXY.getPlayerEntity();
        for(Entity passenger : player.getPassengers()) {
            if(passenger instanceof EntityDog) {
                EntityDog dog = (EntityDog)passenger;
                if(dog.COORDS.hasBedPos()) {
                    int level = dog.TALENTS.getLevel(ModTalents.BED_FINDER);
                    double distance = (level * 200D) - Math.sqrt(dog.getDistanceSq(dog.COORDS.getBedPos()));
                    if(level == 5 || distance >= 0.0D) {
                        
                        GlStateManager.pushMatrix();
                        
                        AxisAlignedBB boundingBox = new AxisAlignedBB(dog.COORDS.getBedPos()).grow(0.5D);
                        drawSelectionBox(player, event.getPartialTicks(), boundingBox);
                        GlStateManager.popMatrix();
                    }
                }
            }
        }
    }
    
    public static void drawSelectionBox(EntityPlayer player, float particleTicks, AxisAlignedBB boundingBox) {
        GlStateManager.disableAlpha();
        GlStateManager.disableLighting(); //Make the line see thought blocks
        GlStateManager.depthMask(false);
        GlStateManager.disableDepth(); //Make the line see thought blocks
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.color(1.0F, 0.0F, 0.0F, 0.7F);
        //TODO Used when drawing outline of bounding box 
        GlStateManager.glLineWidth(2.0F);
        
        GlStateManager.disableTexture2D();
        double d0 = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double)particleTicks;
        double d1 = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double)particleTicks;
        double d2 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double)particleTicks;

        RenderGlobal.drawSelectionBoundingBox(boundingBox.offset(-d0, -d1, -d2), 1F, 1F, 0, 1F);
        GlStateManager.color(0.0F, 0.0F, 0.0F, 0.3F);
        GlStateManager.enableDepth(); //Make the line see thought blocks
        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        GlStateManager.enableLighting(); //Make the line see thought blocks
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
    }
}
