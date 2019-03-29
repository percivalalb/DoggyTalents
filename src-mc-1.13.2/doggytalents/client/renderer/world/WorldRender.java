package doggytalents.client.renderer.world;

import doggytalents.DoggyTalentsMod;
import doggytalents.entity.EntityDog;
import doggytalents.lib.Reference;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * @author ProPercivalalb
 */
@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = Reference.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class WorldRender {

	@SubscribeEvent
	public static void onWorldRenderLast(RenderWorldLastEvent event) {
		EntityPlayer player = DoggyTalentsMod.PROXY.getPlayerEntity();
		for(Entity passenger : player.getPassengers()) {
			if(passenger instanceof EntityDog) {
				EntityDog dog = (EntityDog)passenger;
				if(dog.COORDS.hasBedPos()) {
					int level = dog.TALENTS.getLevel("bedfinder");
					double distance = (level * 200D) - Math.sqrt(dog.getDistanceSq(dog.COORDS.getBedX(), dog.COORDS.getBedY(), dog.COORDS.getBedZ()));
				    if(level == 5 || distance >= 0.0D) {
						
				    	GlStateManager.pushMatrix();
						
						AxisAlignedBB boundingBox = new AxisAlignedBB(dog.COORDS.getBedX(), dog.COORDS.getBedY(), dog.COORDS.getBedZ(), dog.COORDS.getBedX() + 1, dog.COORDS.getBedY() + 1, dog.COORDS.getBedZ() + 1).grow(0.5D);
						drawSelectionBox(player, event.getPartialTicks(), boundingBox);
						GlStateManager.popMatrix();
				    }
				}
			}
		}
	}
	
	public static void drawSelectionBox(EntityPlayer player, float particleTicks, AxisAlignedBB boundingBox) {
		GlStateManager.disableAlphaTest();
		GlStateManager.disableLighting(); //Make the line see thought blocks
		GlStateManager.depthMask(false);
		GlStateManager.disableDepthTest(); //Make the line see thought blocks
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.color4f(1.0F, 0.0F, 0.0F, 0.7F);
        //TODO Used when drawing outline of bounding box 
        GlStateManager.lineWidth(2.0F);
        
        GlStateManager.disableTexture2D();
    	double d0 = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double)particleTicks;
    	double d1 = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double)particleTicks;
    	double d2 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double)particleTicks;

    	WorldRenderer.drawSelectionBoundingBox(boundingBox.offset(-d0, -d1, -d2), 1F, 1F, 0, 1F);
    	GlStateManager.color4f(0.0F, 0.0F, 0.0F, 0.3F);
    	GlStateManager.enableDepthTest(); //Make the line see thought blocks
        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        GlStateManager.enableLighting(); //Make the line see thought blocks
        GlStateManager.disableBlend();
        GlStateManager.enableAlphaTest();
    }
}
