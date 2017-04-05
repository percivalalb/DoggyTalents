package doggytalents.talent;

import org.lwjgl.opengl.GL11;

import doggytalents.entity.EntityDog;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author ProPercivalalb
 */
public class BedFinderHandler {

	@SubscribeEvent
	public void onWorldRenderLast(RenderWorldLastEvent event) {
		EntityPlayer player = FMLClientHandler.instance().getClientPlayerEntity();
		
		for(Entity passenger : player.getPassengers()) {
			if(passenger instanceof EntityDog) {
				EntityDog dog = (EntityDog)passenger;
				FMLLog.info("Bed coords");
				if(dog.coords.hasBedPos()) {
					FMLLog.info("Bed coords");
					int level = dog.talents.getLevel("bedfinder");
					double distance = (level * 200D) - Math.sqrt(dog.getDistanceSq(dog.coords.getBedPos()));
				    if(level == 5 || distance >= 0.0D) {
						
						GL11.glPushMatrix();
						
						AxisAlignedBB boundingBox = new AxisAlignedBB(dog.coords.getBedPos()).expandXyz(0.5D);
						//func_189697_a(iblockstate.getSelectedBoundingBox(this.theWorld, blockpos).expandXyz(0.0020000000949949026D).offset(-d0, -d1, -d2), 0.0F, 0.0F, 0.0F, 0.4F);
						this.drawSelectionBox(player, event.getPartialTicks(), boundingBox);
					    GL11.glPopMatrix();
				    }
				}
			}
		}
	}
	public void drawSelectionBox(EntityPlayer player, float particleTicks, AxisAlignedBB boundingBox) {
		GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_LIGHTING); //Make the line see thought blocks
        GL11.glDepthMask(false);
        GL11.glDisable(GL11.GL_DEPTH_TEST); //Make the line see thought blocks
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1.0F, 0.0F, 0.0F, 0.7F);
        //TODO Used when drawing outline of bounding box 
        GL11.glLineWidth(2.0F);
        
        GL11.glDisable(GL11.GL_TEXTURE_2D);
    	double d0 = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double)particleTicks;
    	double d1 = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double)particleTicks;
    	double d2 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double)particleTicks;

    	RenderGlobal.drawOutlinedBoundingBox(boundingBox.offset(-d0, -d1, -d2), 255, 255, 0, 255);
        GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.3F);
        GL11.glEnable(GL11.GL_DEPTH_TEST); //Make the line see thought blocks
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LIGHTING); //Make the line see thought blocks
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
    }
}
