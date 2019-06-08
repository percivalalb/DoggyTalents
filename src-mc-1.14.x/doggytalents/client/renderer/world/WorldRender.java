package doggytalents.client.renderer.world;

import com.mojang.blaze3d.platform.GlStateManager;

import doggytalents.DoggyTalentsMod;
import doggytalents.ModTalents;
import doggytalents.entity.EntityDog;
import doggytalents.lib.Reference;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
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
@Mod.EventBusSubscriber(modid = Reference.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class WorldRender {

	@SubscribeEvent
	public static void onWorldRenderLast(RenderWorldLastEvent event) {
		PlayerEntity player = DoggyTalentsMod.PROXY.getPlayerEntity();
		for(Entity passenger : player.getPassengers()) {
			if(passenger instanceof EntityDog) {
				EntityDog dog = (EntityDog)passenger;
				if(dog.COORDS.hasBedPos()) {
					int level = dog.TALENTS.getLevel(ModTalents.BED_FINDER);
					double distance = (level * 200D) - Math.sqrt(dog.COORDS.getBedPos().distanceSq(dog.getPosition()));
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
	
	public static void drawSelectionBox(PlayerEntity player, float particleTicks, AxisAlignedBB boundingBox) {
		GlStateManager.disableAlphaTest();
		GlStateManager.disableLighting(); //Make the line see thought blocks
		GlStateManager.depthMask(false);
		GlStateManager.disableDepthTest(); //Make the line see thought blocks
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.color4f(1.0F, 0.0F, 0.0F, 0.7F);
        //TODO Used when drawing outline of bounding box 
        GlStateManager.lineWidth(2.0F);
        
        GlStateManager.disableTexture();
    	double d0 = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double)particleTicks;
    	double d1 = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double)particleTicks;
    	double d2 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double)particleTicks;

    	WorldRenderer.drawSelectionBoundingBox(boundingBox.offset(-d0, -d1, -d2), 1F, 1F, 0, 1F);
    	GlStateManager.color4f(0.0F, 0.0F, 0.0F, 0.3F);
    	GlStateManager.enableDepthTest(); //Make the line see thought blocks
        GlStateManager.depthMask(true);
        GlStateManager.enableTexture();
        GlStateManager.enableLighting(); //Make the line see thought blocks
        GlStateManager.disableBlend();
        GlStateManager.enableAlphaTest();
    }
}
